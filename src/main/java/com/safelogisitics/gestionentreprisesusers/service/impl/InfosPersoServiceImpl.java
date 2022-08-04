package com.safelogisitics.gestionentreprisesusers.service.impl;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.safelogisitics.gestionentreprisesusers.data.dao.AbonnementDao;
import com.safelogisitics.gestionentreprisesusers.data.dao.CompteDao;
import com.safelogisitics.gestionentreprisesusers.data.dao.EntrepriseDao;
import com.safelogisitics.gestionentreprisesusers.data.dao.EquipementDao;
import com.safelogisitics.gestionentreprisesusers.data.dao.MoyenTransportDao;
import com.safelogisitics.gestionentreprisesusers.data.dao.NumeroCarteDao;
import com.safelogisitics.gestionentreprisesusers.data.dao.TypeAbonnementDao;
import com.safelogisitics.gestionentreprisesusers.data.dao.UserDao;
import com.safelogisitics.gestionentreprisesusers.data.dto.request.AbonnementRequest;
import com.safelogisitics.gestionentreprisesusers.data.dto.request.CompteAggregationDto;
import com.safelogisitics.gestionentreprisesusers.data.dto.request.EnrollmentRequest;
import com.safelogisitics.gestionentreprisesusers.data.dto.request.InfosPersoAvecCompteRequest;
import com.safelogisitics.gestionentreprisesusers.data.dto.request.InfosPersoRequest;
import com.safelogisitics.gestionentreprisesusers.data.dto.request.LoginRequest;
import com.safelogisitics.gestionentreprisesusers.data.dto.request.RechargementTransactionRequest;
import com.safelogisitics.gestionentreprisesusers.data.dto.request.RegisterRequest;
import com.safelogisitics.gestionentreprisesusers.data.dto.request.SendSmsRequest;
import com.safelogisitics.gestionentreprisesusers.data.dto.request.UpdateInfosPersoRequest;
import com.safelogisitics.gestionentreprisesusers.data.dto.response.EntrepriseInfosResponse;
import com.safelogisitics.gestionentreprisesusers.data.dto.response.JwtResponse;
import com.safelogisitics.gestionentreprisesusers.data.dto.response.UserInfosResponse;
import com.safelogisitics.gestionentreprisesusers.data.enums.ECompteType;
import com.safelogisitics.gestionentreprisesusers.data.enums.EServiceConciergeType;
import com.safelogisitics.gestionentreprisesusers.data.enums.EServiceType;
import com.safelogisitics.gestionentreprisesusers.data.enums.ETransactionType;
import com.safelogisitics.gestionentreprisesusers.data.model.*;
import com.safelogisitics.gestionentreprisesusers.data.repository.InfosPersoRepository;
import com.safelogisitics.gestionentreprisesusers.service.AbonnementService;
import com.safelogisitics.gestionentreprisesusers.service.EmailService;
import com.safelogisitics.gestionentreprisesusers.service.InfosPersoService;
import com.safelogisitics.gestionentreprisesusers.service.RoleService;
import com.safelogisitics.gestionentreprisesusers.service.SMSService;
import com.safelogisitics.gestionentreprisesusers.service.TransactionService;
import com.safelogisitics.gestionentreprisesusers.service.UserService;
import com.safelogisitics.gestionentreprisesusers.util.ClientNumberGeneratorUtils;
import com.safelogisitics.gestionentreprisesusers.web.security.services.UserDetailsImpl;

import org.bson.Document;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.SkipOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class InfosPersoServiceImpl implements InfosPersoService {

  @Autowired
  private InfosPersoRepository infosPersoRepository; 

  @Autowired
  private CompteDao compteDao;

  @Autowired
  private UserDao userDao;

  @Autowired
  private NumeroCarteDao numeroCarteDao;

  @Autowired
  private AbonnementDao abonnementDao;

  @Autowired
  private TypeAbonnementDao typeAbonnementDao;

  @Autowired
  private EquipementDao equipementDao;

  @Autowired
  private RoleService roleService;

  @Autowired
  private UserService userService;

  @Autowired
	private EmailService emailService;

  @Autowired
  private AbonnementService abonnementService;

  @Autowired
  private TransactionService transactionService;

  @Autowired
  private MoyenTransportDao moyenTransportDao;

  @Autowired
  private EntrepriseDao entrepriseDao;

  @Autowired
  @Qualifier(value = "mongoTemplate")
  private MongoTemplate mongoTemplate;

  @Autowired
	private PasswordEncoder encoder;

  @Autowired
  private SMSService smsService;

  @Override
  public UserInfosResponse getUserInfos() {
    UserDetailsImpl currentUser = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    InfosPersoModel infosPerso = infosPersoRepository.findById(currentUser.getInfosPerso().getId()).get();
    User user = userDao.findByInfosPersoId(currentUser.getInfosPerso().getId()).get();
    Abonnement abonnement = null;
    Optional<Abonnement> _abonnement = abonnementService.getByCompteClientInfosPersoId(currentUser.getInfosPerso().getId());
    if (_abonnement.isPresent()) {
      abonnement = _abonnement.get();
    }

    return new UserInfosResponse(infosPerso, abonnement, user);
  }

  @Override
  public EntrepriseInfosResponse getEntrepriseInfos() {
    UserDetailsImpl currentUser = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    Compte compte = compteDao.findByInfosPersoIdAndType(currentUser.getInfosPerso().getId(), ECompteType.COMPTE_ENTREPRISE).get();
    if (compte.isDeleted() || !compte.isEntrepriseUser())
      return null;

    Entreprise entreprise = entrepriseDao.findById(compte.getEntrepriseId()).get();
    InfosPersoModel gerant = null; 
    Abonnement abonnement = null;

    Optional<Compte> _gerant = compteDao.findById(entreprise.getGerantId());
    if (_gerant.isPresent()) {
      gerant = infosPersoRepository.findById(_gerant.get().getInfosPersoId()).get();
    }
    Optional<Abonnement> _abonnement = abonnementDao.findByEntrepriseId(entreprise.getId());
    if (_abonnement.isPresent()) {
      abonnement = _abonnement.get();
    }

    return new EntrepriseInfosResponse(entreprise, gerant, abonnement);
  }

  @Override
  public UserInfosResponse getUserInfos(String id) {
    InfosPersoModel infosPerso = infosPersoRepository.findById(id).get();
    User user = userDao.findByInfosPersoId(id).get();
    Abonnement abonnement = null;
    Optional<Abonnement> _abonnement = abonnementService.getByCompteClientInfosPersoId(id);
    if (_abonnement.isPresent())
      abonnement = _abonnement.get();

    return new UserInfosResponse(infosPerso, abonnement, user);
  }

  @Override
  public Object findInfosPersoByCompteId(String id) {
    Optional<Compte> compteExist = compteDao.findById(id);
    if (!compteExist.isPresent()) {
      return null;
    }

    InfosPersoModel infosPerso = infosPersoRepository.findById(compteExist.get().getInfosPersoId()).get();
    
    Optional<Abonnement> abonnement = abonnementDao.findByCompteClientId(id);

    if (abonnement.isPresent() && !abonnement.get().isDeleted() && !compteExist.get().isDeleted()) {
      return infosPerso.getDefaultFields(abonnement.get().getTypeAbonnement().getLibelle());
    }

    return infosPerso.getDefaultFields();
  }

  @Override
  public Map<String, String> verifierAbonnement(String telephone) {
    final List<AggregationOperation> listAggregations = new ArrayList<AggregationOperation>();
    final List<Criteria> listCritarias = new ArrayList<Criteria>(Arrays.asList(
      Criteria.where("type").is(ECompteType.COMPTE_PARTICULIER),
      Criteria.where("userInfos.telephone").regex(".*"+telephone.trim()+".*","xi"),
      Criteria.where("deleted").is(false),
      Criteria.where("statut").ne(-1)
    ));

    listAggregations.add(l -> new Document("$addFields", new Document("infosPersoObjectId", new Document("$toObjectId", "$infosPersoId"))));
    listAggregations.add(Aggregation.lookup("infosPersos", "infosPersoObjectId", "_id", "userInfos"));
    listAggregations.add(Aggregation.lookup("abonnements", "infosPersoId", "compteClient.infosPersoId", "abonnement"));
    listAggregations.add(Aggregation.unwind("userInfos"));
    listAggregations.add(Aggregation.unwind("abonnement"));
    listAggregations.add(Aggregation.match(new Criteria().andOperator(listCritarias.toArray(new Criteria[listCritarias.size()]))));

    Aggregation aggregation = Aggregation.newAggregation(listAggregations);

    List<Compte> listUsers = mongoTemplate.aggregate(aggregation, Compte.class, Compte.class).getMappedResults();

    if (listUsers.size() <= 0 || listUsers.get(0).getAbonnement() == null
      || listUsers.get(0).getAbonnement().isDeleted() || listUsers.get(0).getAbonnement().isCarteBloquer()) {
      return null;
    }

    Map<String, String> response = new HashMap<>();
    response.put("id", listUsers.get(0).getAbonnement().getCompteClient().getId());
    response.put("abonnement", listUsers.get(0).getAbonnement().getTypeAbonnement().getLibelle());

    return response;
  }

  @Override
  public Collection<Object> findAllInfosPersoByCompteIds(Set<String> ids) {
    return StreamSupport.stream(compteDao.findAllById(ids).spliterator(), false)
      .filter(compte -> !compte.isDeleted())
      .map(compte -> infosPersoRepository.findById(compte.getInfosPersoId()).get())
      .collect(Collectors.toList());
  }

  @Override
  public Optional<InfosPersoModel> findByCompteId(String id) {
    Optional<Compte> compteExist = compteDao.findById(id);
    if (!compteExist.isPresent() || compteExist.get().isDeleted()) {
      return null;
    }

    return infosPersoRepository.findById(compteExist.get().getInfosPersoId());
  }

  @Override
  public Optional<InfosPersoModel> findByEmailOrTelephone(String emailOrTelephone, Boolean prestation) {
    final Query query = new Query();
    final List<Criteria> listCritarias = new ArrayList<Criteria>();
    emailOrTelephone = emailOrTelephone.replaceAll(" ", "");

    listCritarias.add(Criteria.where("email").regex(".*"+emailOrTelephone.trim()+".*","i"));
    listCritarias.add(Criteria.where("telephone").regex(".*"+emailOrTelephone.trim()+".*","xi"));

    query.addCriteria(new Criteria().orOperator(listCritarias.toArray(new Criteria[listCritarias.size()])));

    Collection<InfosPersoModel> results = mongoTemplate.find(query, InfosPersoModel.class);

    if (results == null || results.size() <= 0)
      return Optional.ofNullable(null);

    if (prestation != null) {
      InfosPersoModel infosPersoModel = results.stream().findFirst().get();
      Optional<Compte> compteExist = infosPersoModel.getComptes().stream().filter(c -> c.getType().equals(ECompteType.COMPTE_PARTICULIER)).findFirst();

      if (compteExist.isPresent()) {
        Compte compte = compteExist.get();
        if(!compte.getServices().contains(EServiceType.PRESTATION)) {
          throw new IllegalArgumentException("Ce client n'a pas accès à ce service.");
        }
      }
    }

    return Optional.ofNullable(results.iterator().next());
  }

  @Override
  public Collection<UserInfosResponse> findByCustomSearch(String prenom, String nom, String email, String telephone, String numeroCarte, ECompteType compteType, EServiceConciergeType serviceConciergeType) {
    final List<AggregationOperation> listAggregations = new ArrayList<AggregationOperation>();
    final List<Criteria> listCritarias = new ArrayList<Criteria>();

    if (compteType != null)
      listCritarias.add(Criteria.where("type").is(compteType));

    if (serviceConciergeType != null)
      listCritarias.add(Criteria.where("serviceConciergerie").is(serviceConciergeType));

    if (prenom != null && !prenom.isEmpty())
      listCritarias.add(Criteria.where("userInfos.prenom").regex(".*"+prenom.trim()+".*","i"));

    if (nom != null && !nom.isEmpty())
      listCritarias.add(Criteria.where("userInfos.nom").regex(".*"+nom.trim()+".*","i"));

    if (email != null && !email.isEmpty())
      listCritarias.add(Criteria.where("userInfos.email").regex(".*"+email.trim()+".*","i"));

    if (telephone != null && !telephone.isEmpty())
      listCritarias.add(Criteria.where("userInfos.telephone").regex(".*"+telephone.trim()+".*","xi"));

    if (numeroCarte != null && !numeroCarte.isEmpty())
      listCritarias.add(Criteria.where("abonnement.numeroCarte").regex(".*"+numeroCarte.trim()+".*","xi"));

    if (listCritarias.isEmpty())
      return new ArrayList<UserInfosResponse>();

    listCritarias.add(Criteria.where("deleted").is(false));
    listCritarias.add(Criteria.where("statut").ne(-1));

    listAggregations.add(l -> new Document("$addFields", new Document("infosPersoObjectId", new Document("$toObjectId", "$infosPersoId"))));
    listAggregations.add(Aggregation.lookup("infosPersos", "infosPersoObjectId", "_id", "userInfos"));
    listAggregations.add(Aggregation.unwind("userInfos"));
    if (numeroCarte != null && !numeroCarte.isEmpty()) {
      listAggregations.add(Aggregation.lookup("abonnements", "infosPersoId", "compteClient.infosPersoId", "abonnement"));
      listAggregations.add(Aggregation.unwind("abonnement"));
    }
    listAggregations.add(Aggregation.match(new Criteria().andOperator(listCritarias.toArray(new Criteria[listCritarias.size()]))));

    Aggregation aggregation = Aggregation.newAggregation(listAggregations);

    AggregationResults<Compte> listUsers = mongoTemplate.aggregate(aggregation, Compte.class, Compte.class);

    return listUsers.getMappedResults().stream().map(compte -> {
      User user = userDao.findByInfosPersoId(compte.getInfosPersoId()).get();
        return new UserInfosResponse(compte.getUserInfos(), compte.getAbonnement(), user);
    }).collect(Collectors.toList());
  }

  @Override
  public Optional<InfosPersoModel> findInfosPersoById(String id) {
    return infosPersoRepository.findById(id);
  }

  @Override
  public Page<InfosPersoModel> getInfosPersos(Pageable pageable) {
    return infosPersoRepository.findByComptesIsNull(pageable);
  }

  @Override
  public Page<UserInfosResponse> getInfosPersos(ECompteType type, Pageable pageable) {
    List<String> ids = compteDao.findByTypeAndDeletedIsFalse(type)
      .stream()
      .filter(compte -> !compte.isDeleted() && (compte.getRole() == null || (compte.getRole() != null && compte.getRole().isEditable())))
      .map(compte -> compte.getInfosPersoId())
      .collect(Collectors.toList());

    return infosPersoRepository.findByIdInOrderByDateCreationDesc(ids, pageable).map(new Function<InfosPersoModel, UserInfosResponse>() {
      @Override
      public UserInfosResponse apply(InfosPersoModel infosPerso) {
        User user = userDao.findByInfosPersoId(infosPerso.getId()).get();
        Abonnement abonnement = null;
        Optional<Abonnement> _abonnement = abonnementService.getByCompteClientInfosPersoId(infosPerso.getId());
        if (_abonnement.isPresent()) {
          abonnement = _abonnement.get();
        }
        return new UserInfosResponse(infosPerso, abonnement, user);
      }
    });
  }

  @Override
  public Page<UserInfosResponse> getInfosPersos(ECompteType type, Boolean isAbonnee, Pageable pageable) {
    List<String> ids = compteDao.findByTypeAndDeletedIsFalse(type)
      .stream()
      .filter(compte -> {
        boolean validCompte = !compte.isDeleted() && (compte.getRole() == null || (compte.getRole() != null && compte.getRole().isEditable()));
        if (isAbonnee != null)
          validCompte = validCompte && isAbonnee.booleanValue() == abonnementDao.existsByCompteClientInfosPersoId(compte.getInfosPersoId());
        return validCompte;
      })
      .map(compte -> compte.getInfosPersoId())
      .collect(Collectors.toList());

    return infosPersoRepository.findByIdInOrderByDateCreationDesc(ids, pageable).map(new Function<InfosPersoModel, UserInfosResponse>() {
      @Override
      public UserInfosResponse apply(InfosPersoModel infosPerso) {
        User user = userDao.findByInfosPersoId(infosPerso.getId()).get();
        Abonnement abonnement = null;
        Optional<Abonnement> _abonnement = abonnementService.getByCompteClientInfosPersoId(infosPerso.getId());
        if (_abonnement.isPresent()) {
          abonnement = _abonnement.get();
        }
        return new UserInfosResponse(infosPerso, abonnement, user);
      }
    });
  }

  @Override
  public InfosPersoModel createInfosPerso(InfosPersoRequest infosPersoRequest) {
    InfosPersoModel _infosPerso = findInfosPerso(infosPersoRequest.getEmail(), infosPersoRequest.getTelephone(), infosPersoRequest.getNumeroPermis(), infosPersoRequest.getNumeroPiece());

    if (_infosPerso != null &&_infosPerso.getId() != null)
      throw new IllegalArgumentException("Ces informations existe déjà!");

    InfosPersoModel infosPerso = new InfosPersoModel(
      infosPersoRequest.getPrenom(),
      infosPersoRequest.getNom(),
      infosPersoRequest.getEmail(),
      infosPersoRequest.getTelephone(),
      infosPersoRequest.getAdresse(),
      infosPersoRequest.getDateNaissance(),
      infosPersoRequest.getNumeroPermis(),
      infosPersoRequest.getNumeroPiece(),
      infosPersoRequest.getPhotoProfil()
    );

    infosPerso.setAnneeNaissance(infosPersoRequest.getAnneeNaissance());

    infosPersoRepository.save(infosPerso);

    return infosPerso;
  }

  @Override
  public InfosPersoModel updateInfosPerso(String id, InfosPersoRequest infosPersoRequest) {
    Optional<InfosPersoModel> _infosPerso = infosPersoRepository.findById(id);
    InfosPersoModel _infosPersoExist = findInfosPerso(infosPersoRequest.getEmail(), infosPersoRequest.getTelephone(), infosPersoRequest.getNumeroPermis(), infosPersoRequest.getNumeroPiece());

    if (!_infosPerso.isPresent()) {
      throw new IllegalArgumentException("InfosPerso with that id does not exists!");
    }

    if (!_infosPerso.get().getId().equals(_infosPersoExist.getId())) {
      throw new IllegalArgumentException("Ces informations existent déjà!");
    }

    InfosPersoModel infosPerso = _infosPerso.get();

    infosPerso.setPrenom(infosPersoRequest.getPrenom());
    infosPerso.setNom(infosPersoRequest.getNom());
    infosPerso.setPhotoProfil(infosPersoRequest.getPhotoProfil());
    infosPerso.setEmail(infosPersoRequest.getEmail());
    infosPerso.setTelephone(infosPersoRequest.getTelephone());
    infosPerso.setAdresse(infosPersoRequest.getAdresse());
    infosPerso.setDateNaissance(infosPersoRequest.getDateNaissance());
    infosPerso.setAnneeNaissance(infosPersoRequest.getAnneeNaissance());
    infosPerso.setNumeroPermis(infosPersoRequest.getNumeroPermis());
    infosPerso.setNumeroPiece(infosPersoRequest.getNumeroPiece());

    infosPersoRepository.save(infosPerso);

    return infosPerso;
  }

  @Override
  public InfosPersoModel createOrUpdateCompteAdministrateur(String id, InfosPersoAvecCompteRequest request) { // chnager

    if (request.getRoleId() == null || request.getRoleId().isEmpty())
      throw new IllegalArgumentException("Role est requis!");

    Optional<Role> _role = roleService.getRoleById(request.getRoleId());

    if (!_role.isPresent() || !_role.get().getType().equals(ECompteType.COMPTE_ADMINISTRATEUR))
      throw new IllegalArgumentException("Role invalide!");

    InfosPersoModel infosPerso = null;

    if (id == null) {
      infosPerso = createCompte(request, ECompteType.COMPTE_ADMINISTRATEUR, null, null);
      id = infosPerso.getId();
    } else {
      infosPerso = updateCompte(id, request, ECompteType.COMPTE_ADMINISTRATEUR);
    }

    Compte compte = compteDao.findByInfosPersoIdAndType(infosPerso.getId(), ECompteType.COMPTE_ADMINISTRATEUR).get();

    Role role = _role.get();

    compte.setRole(role);
    compte.setStatut(request.getStatut());
    compteDao.save(compte);
    infosPerso.updateCompte(compte);
    infosPersoRepository.save(infosPerso);

    return infosPerso;
  }

  @Override
  public void deleteCompteAdministrateur(String infosPersoId) {
    Optional<InfosPersoModel> infosPerso = infosPersoRepository.findById(infosPersoId);

    if (infosPerso.isPresent()) {
      deleteCompte(infosPersoId, ECompteType.COMPTE_ADMINISTRATEUR);
    }
  }

  @Override
  public InfosPersoModel createOrUpdateCompteAgent(String id, InfosPersoAvecCompteRequest request) {
    if (!request.valideFieldsCompteAgent())
      throw new IllegalArgumentException("Informations agent manquant!");

    final Query query = new Query();
    final List<Criteria> listCriteria = new ArrayList<>();

    listCriteria.add(Criteria.where("numeroEmei").is(request.getNumeroEmei()));
    query.addCriteria(new Criteria().orOperator(listCriteria.toArray(new Criteria[listCriteria.size()])));
    query.addCriteria(Criteria.where("deleted").is(false));

    // Validation de l'unicité des infos du compte de l'agent
    Collection<Compte> _comptes = mongoTemplate.find(query, Compte.class);
    if (_comptes != null && _comptes.size() > 0 && (_comptes.size() > 1 || (id == null && _comptes.size() == 1) || (id != null && !_comptes.iterator().next().getInfosPersoId().equals(id))))
      throw new IllegalArgumentException("Numéro emei déjà utilisé!");

    InfosPersoModel infosPerso = null;

    if (id == null) {
      infosPerso = createCompte(request, ECompteType.COMPTE_COURSIER, null, null);
      id = infosPerso.getId();
    } else {
      infosPerso = updateCompte(id, request, ECompteType.COMPTE_COURSIER);
    }

    Compte compte = compteDao.findByInfosPersoIdAndType(infosPerso.getId(), ECompteType.COMPTE_COURSIER).get();

    compte.setNumeroEmei(request.getNumeroEmei());
    compte.setStatut(request.getStatut());
    compteDao.save(compte);
    infosPerso.updateCompte(compte);
    infosPersoRepository.save(infosPerso);

    return infosPerso;
  }

  @Override
  public InfosPersoModel equiperAgent(String id, Set<AffectationEquipement> affectationEquipements) {
    Optional<Compte> _compte = compteDao.findByIdAndType(id, ECompteType.COMPTE_COURSIER);
    StringBuilder errors = new StringBuilder();

    if (!_compte.isPresent() || _compte.get().isDeleted())
      throw new IllegalArgumentException("Cet utilisateur n'a pas de compte agent.");

    Compte compte = _compte.get();
    InfosPersoModel infosPerso = infosPersoRepository.findById(compte.getInfosPersoId()).get();

    for (AffectationEquipement affectationEquipement : affectationEquipements) {
      Optional<Equipement> _equipement = equipementDao.findById(affectationEquipement.getIdEquipement());

      if (!_equipement.isPresent() || _equipement.get().getStock() <= 0) {
        if (_equipement.get().getStock() == 0) errors.append("Le stock de ").append(_equipement.get().getLibelle().toLowerCase()).append(" est insuffisant.");
        continue;
      }

      Equipement equipement = _equipement.get();
      Double quantiteAffecter = equipement.getQuantiteAffecter();
      Double stock = equipement.getStock();

      for (Iterator<AffectationEquipement> iterate = compte.getEquipements().iterator(); iterate.hasNext(); ) {
        AffectationEquipement _affectationEquipement = iterate.next();
        if (_affectationEquipement.equals(affectationEquipement)) {
          quantiteAffecter = quantiteAffecter - _affectationEquipement.getQuantite();
          stock = stock + _affectationEquipement.getQuantite();
          break;
        }
      }

      if (affectationEquipement.getQuantite() == 0 && compte.getEquipements().contains(affectationEquipement)) {
        compte.removeEquipement(affectationEquipement);
      }

      if (affectationEquipement.getQuantite() > 0) {
        if (affectationEquipement.getQuantite() > stock) {
          errors.append("Le stock de ").append(_equipement.get().getLibelle().toLowerCase()).append(" est insuffisant.");
          continue;
        }
        compte.addEquipement(affectationEquipement);
        quantiteAffecter = quantiteAffecter + affectationEquipement.getQuantite();
        stock = stock - affectationEquipement.getQuantite();
      }

      equipement.setQuantiteAffecter(quantiteAffecter);
      equipement.setStock(stock);


      equipementDao.save(equipement);

      compteDao.save(compte);
    }

    infosPerso.updateCompte(compte);

    infosPersoRepository.save(infosPerso);

    if (!errors.toString().equals("")) {
      throw new IllegalArgumentException(errors.toString());
    }

    return infosPerso;
  }

  @Override
  public InfosPersoModel affecterMoyenTransportAgent(String id, String moyenTransportId) {
    Optional<Compte> _compte = compteDao.findByIdAndType(id, ECompteType.COMPTE_COURSIER);

    if (!_compte.isPresent() || _compte.get().isDeleted())
      throw new IllegalArgumentException("Cet utilisateur n'a pas de compte agent.");

    Compte compte = _compte.get();
    InfosPersoModel infosPerso = infosPersoRepository.findById(compte.getInfosPersoId()).get();

    Optional<MoyenTransport> _moyenTransport = moyenTransportDao.findById(moyenTransportId);

    if (!_moyenTransport.isPresent())
      throw new IllegalArgumentException("Ce moyen de transport n'existe pas.");

    MoyenTransport moyenTransport = _moyenTransport.get();

    if (compte.getMoyenTransportId() != null && moyenTransportDao.existsById(compte.getMoyenTransportId())) {
      MoyenTransport olMoyenTransport = moyenTransportDao.findById(compte.getMoyenTransportId()).get();
      olMoyenTransport.setEnService(false);
      moyenTransportDao.save(olMoyenTransport);
    }

    moyenTransport.setEnService(true);
    moyenTransportDao.save(moyenTransport);

    compte.setMoyenTransportId(moyenTransportId);
    compteDao.save(compte);

    infosPerso.updateCompte(compte);

    infosPersoRepository.save(infosPerso);

    return infosPerso;
  }

  @Override
  public void deleteCompteAgent(String infosPersoId) {
    Optional<InfosPersoModel> infosPerso = infosPersoRepository.findById(infosPersoId);

    if (infosPerso.isPresent()) {
      deleteCompte(infosPersoId, ECompteType.COMPTE_COURSIER);
    }
  }

  @Override
  public InfosPersoModel createOrUpdateComptePrestataire(String id, InfosPersoAvecCompteRequest request) {

    if (!request.valideFieldsComptePrestataire())
      throw new IllegalArgumentException("Informations prestataire manquant!");

    InfosPersoModel infosPerso = null;

    if (id == null) {
      infosPerso = createCompte(request, ECompteType.COMPTE_PRESTATAIRE, null, null);
      id = infosPerso.getId();
    } else {
      infosPerso = updateCompte(id, request, ECompteType.COMPTE_PRESTATAIRE);
    }

    Compte compte = compteDao.findByInfosPersoIdAndType(infosPerso.getId(), ECompteType.COMPTE_PRESTATAIRE).get();

    compte.setStatut(request.getStatut());
    compte.setServiceConciergeries(request.getServiceConciergeries());
    compteDao.save(compte);
    infosPerso.updateCompte(compte);
    infosPersoRepository.save(infosPerso);

    return infosPerso;
  }

  @Override
  public void deleteComptePrestataire(String infosPersoId) {
    Optional<InfosPersoModel> infosPerso = infosPersoRepository.findById(infosPersoId);

    if (infosPerso.isPresent()) {
      deleteCompte(infosPersoId, ECompteType.COMPTE_PRESTATAIRE);
    }
  }

  @Override
  public Page<UserInfosResponse> getEntrepriseUsers(String entrepriseId, Map<String, String> requestParams, Pageable pageable) {
    final List<AggregationOperation> listAggregations = new ArrayList<AggregationOperation>();
    final List<Criteria> listCritarias = new ArrayList<Criteria>();

    listCritarias.add(Criteria.where("type").is(ECompteType.COMPTE_ENTREPRISE));
    listCritarias.add(Criteria.where("entrepriseId").is(entrepriseId));
    listCritarias.add(Criteria.where("entrepriseUser").is(true));

    listCritarias.add(Criteria.where("deleted").is(false));
    listCritarias.add(Criteria.where("statut").ne(-1));

    listAggregations.add(l -> new Document("$addFields", new Document("infosPersoObjectId", new Document("$toObjectId", "$infosPersoId"))));
    listAggregations.add(Aggregation.lookup("infosPersos", "infosPersoObjectId", "_id", "userInfos"));
    listAggregations.add(Aggregation.unwind("userInfos"));
    listAggregations.add(Aggregation.match(new Criteria().andOperator(listCritarias.toArray(new Criteria[listCritarias.size()]))));

    listAggregations.add(new SkipOperation(pageable.getPageNumber() * pageable.getPageSize()));
    listAggregations.add(Aggregation.limit(pageable.getPageSize()));

    listAggregations.add(Aggregation.group().addToSet(Aggregation.ROOT).as("comptes").count().as("count"));

    Aggregation aggregation = Aggregation.newAggregation(listAggregations);

    CompteAggregationDto aggregationResult = mongoTemplate.aggregate(aggregation, Compte.class, CompteAggregationDto.class).getUniqueMappedResult();

    if (aggregationResult == null)
      return new PageImpl<>(new ArrayList<>(), pageable, 0);

    List<UserInfosResponse> listUsers = aggregationResult.getComptes().stream().map(compte -> {
      User user = userDao.findByInfosPersoId(compte.getInfosPersoId()).get();
      return new UserInfosResponse(compte.getUserInfos(), null, user);
    }).collect(Collectors.toList());

    return new PageImpl<>(listUsers, pageable, aggregationResult.getCount());
  }

  @Override
  public InfosPersoModel createOrUpdateCompteEntreprise(String id, InfosPersoAvecCompteRequest request) {
    Optional<Entreprise> _entreprise = entrepriseDao.findById(request.getEntrepriseId());

    if (!_entreprise.isPresent() || _entreprise.get().isDeleted())
      throw new IllegalArgumentException("Cette entreprise n'existe pas ou a été supprimé!");

    Entreprise entreprise = _entreprise.get();

    InfosPersoModel infosPerso = null;

    if (id == null) {
      infosPerso = createCompte(request, ECompteType.COMPTE_ENTREPRISE, null, null);
      id = infosPerso.getId();
    } else {
      infosPerso = updateCompte(id, request, ECompteType.COMPTE_ENTREPRISE);
    }

    Compte compte = compteDao.findByInfosPersoIdAndType(infosPerso.getId(), ECompteType.COMPTE_ENTREPRISE).get();

    if (id == null) compte.setNumeroReference(ClientNumberGeneratorUtils.generateReference(entreprise.getNumeroCarte(), null));

    compte.setStatut(request.getStatut());
    compte.setEntreprise(entreprise);
    compte.setEntrepriseUser(true);
    compteDao.save(compte);

    infosPerso.updateCompte(compte);
    infosPersoRepository.save(infosPerso);

    return infosPerso;
  }

  @Override
  public void deleteCompteEntreprise(String infosPersoId) {
    Optional<InfosPersoModel> infosPerso = infosPersoRepository.findById(infosPersoId);

    if (infosPerso.isPresent()) {
      deleteCompte(infosPersoId, ECompteType.COMPTE_ENTREPRISE);
    }
  }

  @Override
  public UserInfosResponse createCompteClient(RegisterRequest request) {
    InfosPersoModel infosPerso = createCompte(request, ECompteType.COMPTE_PARTICULIER, request.getUsername(), request.getPassword());

    Compte compte = compteDao.findByInfosPersoIdAndType(infosPerso.getId(), ECompteType.COMPTE_PARTICULIER).get();

    compte.setStatut(1);
    compteDao.save(compte);

    infosPerso.updateCompte(compte);
    infosPersoRepository.save(infosPerso);

    User user = userDao.findByInfosPersoId(infosPerso.getId()).get();

    String smsText  = String.format("Cher(e) client(e) %s\nBienvenue chez Safelogistics.\nVotre inscription est maintenant validée.\nVotre login est: %s.\nVotre mot de passe par défaut est: %s\nPour plus d’informations, rendez-vous sur notre site :safelogistics-senegal.com/\nSafelogistics vous remercie.\nService commercial : 78 306 45 45",
    infosPerso.getNomComplet(), request.getUsername(), request.getPassword());

    SendSmsRequest sms = new SendSmsRequest("SFLOGISTICS", "Message d'inscription", smsText, Arrays.asList(infosPerso.getTelephone()));

    smsService.sendSms(sms);

    return new UserInfosResponse(infosPerso, null, user);
  }

  @Override
  public UserInfosResponse getCompteClient(String id) {
    Optional<Compte> compteExist = compteDao.findByInfosPersoIdAndType(id, ECompteType.COMPTE_PARTICULIER);

    if (!compteExist.isPresent() && !compteExist.get().isDeleted())
      throw new IllegalArgumentException("Ce client n'existe pas!");

    InfosPersoModel infosPerso = infosPersoRepository.findById(id).get();
    User user = userDao.findByInfosPersoId(id).get();

    return new UserInfosResponse(infosPerso, null, user);
  }

  @Override
  public UserInfosResponse updateCompteClient(String id, UpdateInfosPersoRequest request) {
    Optional<Compte> compteExist = compteDao.findByInfosPersoIdAndType(id, ECompteType.COMPTE_PARTICULIER);

    if (!compteExist.isPresent() && !compteExist.get().isDeleted())
      throw new IllegalArgumentException("Ce client n'existe pas!");

    return updateUserInfos(request, id);
  }

  @Override
  public void deleteCompteClient(String infosPersoId) {
    Optional<InfosPersoModel> infosPerso = infosPersoRepository.findById(infosPersoId);

    if (infosPerso.isPresent()) {
      abonnementService.deleteAbonnement(infosPersoId);
      deleteCompte(infosPersoId, ECompteType.COMPTE_PARTICULIER);
    }
  }

  @Override
  public JwtResponse clientRegistration(RegisterRequest request) {

    createCompteClient(request);

    return userService.authenticate(new LoginRequest(request.getUsername(), request.getPassword()));
  }

  @Override
  public UserInfosResponse updateUserInfos(UpdateInfosPersoRequest request) {
    UserDetailsImpl currentUser = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    return updateUserInfos(request, currentUser.getInfosPerso().getId());
  }

  @Override
  public UserInfosResponse updateUserInfos(UpdateInfosPersoRequest request, String id) {
    InfosPersoModel infosPerso = updateInfosPerso(id, request);

    Optional<User> _user = userDao.findByInfosPersoId(id);

    if (!_user.isPresent())
      throw new IllegalArgumentException("Utilisateur non retrouvé!");

    User user = _user.get();

    if (request.getUsername() != null && !request.getUsername().isEmpty()) {
      Optional<User> _userWithUsername = userDao.findByUsername(request.getUsername().trim());

      if (_userWithUsername.isPresent() && !_userWithUsername.get().getId().equals(user.getId()))
        throw new IllegalArgumentException("Ce nom d'utilisateur existe déjà!");
      
      user.setUsername(request.getUsername());
    }

    if (request.getPassword() != null && !request.getPassword().isEmpty()) {
      if (!encoder.matches(request.getOldPassword(), user.getPassword()))
        throw new IllegalArgumentException("Ancien mot de passe invalide!");

      user.setPassword(encoder.encode(request.getPassword())); 
    }

    userDao.save(user);

    return new UserInfosResponse(infosPerso, null, user);
  }

  @Override
  public InfosPersoModel newEnrollment(EnrollmentRequest enrollmentRequest) {
    Optional<NumeroCarte> numeroCarteExist = numeroCarteDao.findByNumero(enrollmentRequest.getNumeroCarte());

    if (!numeroCarteExist.isPresent())
      throw new IllegalArgumentException("Cette carte n'existe pas!");

    if (numeroCarteExist.get().isActive())
      throw new IllegalArgumentException("Cette carte est déjà activé!");

    InfosPersoModel infosPerso = findInfosPerso(enrollmentRequest.getEmail(), enrollmentRequest.getTelephone(), null, enrollmentRequest.getNumeroPiece());

    if (infosPerso != null && abonnementDao.existsByCompteClientInfosPersoId(infosPerso.getId()))
      throw new IllegalArgumentException("Cet utilisateur est déjà abonné!");

    TypeAbonnement typeAbonnement = typeAbonnementDao.findById(numeroCarteExist.get().getTypeAbonnementId()).get();
    BigDecimal prixCarte = typeAbonnement.getPrix();

    if (enrollmentRequest.getMontant() == null || enrollmentRequest.getMontant().compareTo(prixCarte) == -1)
      throw new IllegalArgumentException("Le montant à recharger est insuffisant!");

    NumeroCarte carte = numeroCarteExist.get();

    Compte compte = null;

    Abonnement abonnement = null;

    if (infosPerso == null && !enrollmentRequest.isRegistrationDataValid())
      return null;

    if (infosPerso == null || !compteDao.existsByInfosPersoIdAndTypeAndDeletedIsFalse(infosPerso.getId(), ECompteType.COMPTE_PARTICULIER)) {
      // On crée un compte client en assurant de ne pas modifier ses accès s'il est déjà membre
      RegisterRequest registerRequest = new RegisterRequest(
        enrollmentRequest.getPrenom(),
        enrollmentRequest.getNom(),
        enrollmentRequest.getEmail(),
        enrollmentRequest.getTelephone(),
        enrollmentRequest.getUsername(),
        enrollmentRequest.getPassword(),
        enrollmentRequest.getAdresse(),
        enrollmentRequest.getDateNaissance(),
        null,
        enrollmentRequest.getNumeroPiece(),
        null);

        UserInfosResponse _infosPerso = createCompteClient(registerRequest);
        infosPerso = infosPersoRepository.findById(_infosPerso.getId()).get();
    }

    UserDetailsImpl currentUser = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    ECompteType compteType = compteDao.existsByInfosPersoIdAndType(currentUser.getInfosPerso().getId(), ECompteType.COMPTE_COURSIER) ? ECompteType.COMPTE_COURSIER : ECompteType.COMPTE_ADMINISTRATEUR;

    if (!abonnementDao.existsByCompteClientInfosPersoId(infosPerso.getId())) {
      // On creée un abonnement s'il n'a pas d'abonnement
      abonnementService.createAbonnement(
        new AbonnementRequest(carte.getTypeAbonnementId(), infosPerso.getId(), null, 1, carte.getNumero(), false),
        compteType
      );
    }

    compte = compteDao.findByInfosPersoIdAndType(infosPerso.getId(), ECompteType.COMPTE_PARTICULIER).get();
    compte.setServices(typeAbonnement.getServices());

    abonnement = abonnementService.getAbonnementByCompteClient(compte).get();

    // On soustrait deux mille comme prix de la carte
    enrollmentRequest.setMontant(enrollmentRequest.getMontant().subtract(prixCarte));

    abonnement.setDepotInitial(enrollmentRequest.getMontant());

    abonnementDao.save(abonnement);

    if (abonnement.getDepotInitial().compareTo(BigDecimal.valueOf(5)) != -1) {
      transactionService.createRechargementTransaction(
        new RechargementTransactionRequest(abonnement.getNumeroCarte(), abonnement.getDepotInitial()),
        ETransactionType.SOLDE_COMPTE,
        compteType
      );
    }

    return infosPerso;
  }

  @Override
  public Page<?> getMyEnrollments(Pageable pageable) {
    UserDetailsImpl currentUser = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    ECompteType compteType = compteDao.existsByInfosPersoIdAndType(currentUser.getInfosPerso().getId(), ECompteType.COMPTE_COURSIER) ? ECompteType.COMPTE_COURSIER : ECompteType.COMPTE_ADMINISTRATEUR;

    Optional<Compte> compteExist = compteDao.findByInfosPersoIdAndType(currentUser.getInfosPerso().getId(), compteType);

    if (!compteExist.isPresent()) {
      throw new IllegalArgumentException( "Accès non autorisé!");
    }

    Page<Abonnement> abonnements = abonnementService.getAbonnementByCompteCreateur(compteExist.get(), pageable);

    if (abonnements.getContent().size() <= 0) {
      return abonnements;
    }

    Page<UserInfosResponse> customData = abonnements.map(new Function<Abonnement, UserInfosResponse>() {
      @Override
      public UserInfosResponse apply(Abonnement abonnement) {
        String infosPersoId = abonnement.getCompteClient().getInfosPersoId();

        InfosPersoModel infosPerso = infosPersoRepository.findById(infosPersoId).get();
        User user = userDao.findByInfosPersoId(infosPersoId).get();

        return new UserInfosResponse(infosPerso, abonnement, user);
      }
    });

    return customData;
  }

  @Override
  public Collection<Compte> getInfosPersoComptes(String id) {
    return compteDao.findByInfosPersoId(id);
  }

  public static String alphaNumericString(int type, int len) {
    String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    if (type == 2) {
      ALPHABET = "0123456789";
    }

    Random RANDOM = new SecureRandom();

    StringBuilder returnValue = new StringBuilder(len);
    for (int i = 0; i < len; i++) {
      returnValue.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
    }
    return new String(returnValue);
  }

  private InfosPersoModel findInfosPerso(String email, String telephone, String numeroPermis, String numeroPiece) {
    final Query query = new Query();
    final List<Criteria> listCriteria = new ArrayList<>();

    if (email != null && !email.isEmpty())
      listCriteria.add(Criteria.where("email").is(email));

    if (telephone != null && !telephone.isEmpty())
      listCriteria.add(Criteria.where("telephone").is(telephone));

    if (numeroPermis != null && !numeroPermis.isEmpty())
      listCriteria.add(Criteria.where("numeroPermis").is(numeroPermis));

    if (numeroPiece != null && !numeroPiece.isEmpty())
      listCriteria.add(Criteria.where("numeroPiece").is(numeroPiece));

    if (listCriteria.isEmpty())
      throw new IllegalArgumentException("Il faut au moins l'email ou le numero de téléphone ou le numéro de piece ou le numéro de permis!");

    query.addCriteria(new Criteria().orOperator(listCriteria.toArray(new Criteria[listCriteria.size()])));

    Collection<InfosPersoModel> results = mongoTemplate.find(query, InfosPersoModel.class);

    if (results.size() == 0)
      return null;

    if (results.size() > 1)
      throw new IllegalArgumentException("L'email ou le numero téléphone ou le numéro permis ou le numéro pièce existe déjà!");

    return results.iterator().next();
  }

  private InfosPersoModel createCompte(InfosPersoRequest request, ECompteType compteType, String _username, String _password) {
    InfosPersoModel infosPerso = findInfosPerso(request.getEmail(), request.getTelephone(), request.getNumeroPermis(), request.getNumeroPiece());

    Compte compte;

    if (infosPerso != null && compteDao.existsByInfosPersoIdAndTypeAndDeletedIsFalse(infosPerso.getId(), compteType)) {
      throw new IllegalArgumentException("Cet utilisateur a déjà un compte.");
    }

    String username = _username != null ? _username : infosPerso != null ? infosPerso.getEmail() : request.getEmail();
    Optional<User> _user = userDao.findByUsername(username);

    if (_user.isPresent() && (infosPerso == null || !_user.get().getInfosPerso().getId().equals(infosPerso.getId()))) {
      throw new IllegalArgumentException("Ce nom d'utilisateur existe déjà!");
    }

    if (infosPerso == null) {
      infosPerso = createInfosPerso(request);
    }

    Optional<Compte> _compte = compteDao.findByInfosPersoIdAndType(infosPerso.getId(), compteType);

    if (!_compte.isPresent()) {
      compte = new Compte(compteType, infosPerso.getId());
      compteDao.save(compte);
      infosPerso.addCompte(compte);
    } else {
      compte = _compte.get();
    }

    compte.setDeleted(false);
    compte.setNumeroReference(ClientNumberGeneratorUtils.generateReference(null,  compteType));
    compteDao.save(compte);
    infosPerso.updateCompte(compte);
    infosPersoRepository.save(infosPerso);

    if (!userDao.existsByInfosPersoId(infosPerso.getId())) {
      String password = _password != null ? _password : alphaNumericString(1, 8);
      User user = new User(infosPerso, username, encoder.encode(password), 1);
      userDao.save(user);

      emailService.sendSimpleMessage(infosPerso.getEmail(),"Support Safe Logistics",
      String.format("Bonjour %s %s, \nBienvenue dans Safe Logistics. Vous êtes ajouté en tant que membre. \nVotre login est %s votre mot de passe est %s. Une fois connecté veuillez changer votre mot de passe.",
      infosPerso.getPrenom(), infosPerso.getNom(), user.getUsername(), password));
    }

    return infosPerso;
  }

  private InfosPersoModel updateCompte(String id, InfosPersoAvecCompteRequest request, ECompteType compteType) {
    Optional<InfosPersoModel> infosPersoExist = infosPersoRepository.findById(id);
    if (!infosPersoExist.isPresent())
      throw new IllegalArgumentException("Cet utilisateur n'existe pas!");

    Optional<Compte> _compte = compteDao.findByInfosPersoIdAndType(id, compteType);

    if (!_compte.isPresent() || _compte.get().isDeleted())
      throw new IllegalArgumentException("Cet utilisateur n'a pas de compte " + compteType.name());

    InfosPersoModel _infosPerso = findInfosPerso(request.getEmail(), request.getTelephone(), request.getNumeroPermis(), request.getNumeroPiece());

    if (_infosPerso != null && !infosPersoExist.get().getId().equals(_infosPerso.getId()))
      throw new IllegalArgumentException("Ces informations existent déjà!");

    InfosPersoModel infosPerso = infosPersoExist.get();

    infosPerso = updateInfosPerso(id, request);

    Compte compte = _compte.get();
    compte.setStatut(request.getStatut());
    compteDao.save(compte);
    infosPerso.updateCompte(compte);
    infosPersoRepository.save(infosPerso);

    return infosPerso;
  }

  private void deleteCompte(String infosPersoId, ECompteType compteType) {
    InfosPersoModel infosPerso = infosPersoRepository.findById(infosPersoId).get();

    Optional<Compte> _compte = compteDao.findByInfosPersoIdAndType(infosPerso.getId(), compteType);

    if (!_compte.isPresent())
      return;

    Compte compte = _compte.get();
    compte.setDeleted(true);
    compte.setNumeroEmei(null);
    compte.setNumeroReference(null);
    compteDao.save(compte);
    infosPerso.updateCompte(compte);
    infosPersoRepository.save(infosPerso);

    if (compteDao.countByInfosPersoIdAndDeletedIsFalse(infosPersoId) == 0) {
      userDao.deleteByInfosPersoId(infosPersoId);
    }
  }
}
