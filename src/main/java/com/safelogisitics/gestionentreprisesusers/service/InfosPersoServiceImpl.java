package com.safelogisitics.gestionentreprisesusers.service;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.safelogisitics.gestionentreprisesusers.dao.AbonnementDao;
import com.safelogisitics.gestionentreprisesusers.dao.CompteDao;
import com.safelogisitics.gestionentreprisesusers.dao.InfosPersoDao;
import com.safelogisitics.gestionentreprisesusers.dao.NumeroCarteDao;
import com.safelogisitics.gestionentreprisesusers.dao.TypeAbonnementDao;
import com.safelogisitics.gestionentreprisesusers.dao.UserDao;
import com.safelogisitics.gestionentreprisesusers.model.Abonnement;
import com.safelogisitics.gestionentreprisesusers.model.Compte;
import com.safelogisitics.gestionentreprisesusers.model.InfosPerso;
import com.safelogisitics.gestionentreprisesusers.model.NumeroCarte;
import com.safelogisitics.gestionentreprisesusers.model.Role;
import com.safelogisitics.gestionentreprisesusers.model.User;
import com.safelogisitics.gestionentreprisesusers.model.enums.ECompteType;
import com.safelogisitics.gestionentreprisesusers.payload.request.AbonnementRequest;
import com.safelogisitics.gestionentreprisesusers.payload.request.EnrollmentRequest;
import com.safelogisitics.gestionentreprisesusers.payload.request.InfosPersoAvecCompteRequest;
import com.safelogisitics.gestionentreprisesusers.payload.request.InfosPersoRequest;
import com.safelogisitics.gestionentreprisesusers.payload.request.LoginRequest;
import com.safelogisitics.gestionentreprisesusers.payload.request.RechargementTransactionRequest;
import com.safelogisitics.gestionentreprisesusers.payload.request.RegisterRequest;
import com.safelogisitics.gestionentreprisesusers.payload.request.UpdateInfosPersoRequest;
import com.safelogisitics.gestionentreprisesusers.payload.response.JwtResponse;
import com.safelogisitics.gestionentreprisesusers.payload.response.UserInfosResponse;
import com.safelogisitics.gestionentreprisesusers.security.services.UserDetailsImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class InfosPersoServiceImpl implements InfosPersoService {

  @Autowired
  private InfosPersoDao infosPersoDao;

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
  private MongoTemplate mongoTemplate;

  @Autowired
	private PasswordEncoder encoder;

  @Override
  public UserInfosResponse getUserInfos() {
    UserDetailsImpl currentUser = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    InfosPerso infosPerso = infosPersoDao.findById(currentUser.getInfosPerso().getId()).get();
    User user = userDao.findByInfosPersoId(currentUser.getInfosPerso().getId()).get();
    Abonnement abonnement = null;
    Optional<Abonnement> _abonnement = abonnementService.getByCompteClientInfosPersoId(currentUser.getInfosPerso().getId());
    if (_abonnement.isPresent()) {
      abonnement = _abonnement.get();
    }

    return new UserInfosResponse(infosPerso, abonnement, user);
  }

  @Override
  public Object findInfosPersoByCompteId(String id) {
    Optional<Compte> compteExist = compteDao.findById(id);
    if (!compteExist.isPresent() || compteExist.get().isDeleted()) {
      return null;
    }

    InfosPerso infosPerso = infosPersoDao.findById(compteExist.get().getInfosPersoId()).get();

    return infosPerso.getDefaultFields();
  }

  @Override
  public Optional<InfosPerso> findByCompteId(String id) {
    Optional<Compte> compteExist = compteDao.findById(id);
    if (!compteExist.isPresent() || compteExist.get().isDeleted()) {
      return null;
    }

    return infosPersoDao.findById(compteExist.get().getInfosPersoId());
  }

  @Override
  public Optional<InfosPerso> findByEmailOrTelephone(String email, String telephone) {
    Optional<InfosPerso> infosPerso = infosPersoDao.findByEmailOrTelephone(email, telephone);

    if (infosPerso.isPresent() && !compteDao.existsByInfosPersoIdAndDeletedIsFalse(infosPerso.get().getId())) {
      return null;
    }
    return infosPerso;
  }

  @Override
  public Optional<InfosPerso> findByCustomSearch(String prenom, String nom, String email, String telephone, String numeroCarte, ECompteType compteType) {
    final Query query = new Query();

    final List<Criteria> criteria = new ArrayList<>();

    if (prenom != null && !prenom.isEmpty())
      criteria.add(Criteria.where("prenom").regex(".*"+prenom.trim()+".*","i"));

    if (nom != null && !nom.isEmpty())
      criteria.add(Criteria.where("nom").regex(".*"+nom.trim()+".*","i"));

    if (email != null && !email.isEmpty())
      criteria.add(Criteria.where("email").regex(".*"+email.trim()+".*","i"));

    if (telephone != null && !telephone.isEmpty())
      criteria.add(Criteria.where("telephone").regex(".*"+telephone.trim()+".*","i"));

    if (criteria.isEmpty())
      return null;

    query.addCriteria(new Criteria().andOperator(criteria.toArray(new Criteria[criteria.size()])));

    query.limit(1);

    return Optional.ofNullable(mongoTemplate.findOne(query, InfosPerso.class));
  }

  @Override
  public Optional<InfosPerso> findInfosPersoById(String id) {
    return infosPersoDao.findById(id);
  }

  @Override
  public Page<InfosPerso> getInfosPersos(Pageable pageable) {
    return infosPersoDao.findByComptesIsNull(pageable);
  }

  @Override
  public Page<InfosPerso> getInfosPersos(ECompteType type, Pageable pageable) {
    List<String> ids = compteDao.findByTypeAndDeletedIsFalse(type)
      .stream()
      .filter(compte -> !compte.isDeleted() && (compte.getRole() == null || (compte.getRole() != null && compte.getRole().isEditable())))
      .map(compte -> compte.getInfosPersoId())
      .collect(Collectors.toList());

    return infosPersoDao.findByIdIn(ids, pageable);
  }

  @Override
  public InfosPerso createInfosPerso(InfosPersoRequest infosPersoRequest) {
    InfosPerso _infosPerso = findInfosPerso(infosPersoRequest.getEmail(), infosPersoRequest.getTelephone(), infosPersoRequest.getNumeroPermis(), infosPersoRequest.getNumeroPiece());

    if (_infosPerso != null &&_infosPerso.getId() != null)
      throw new ResponseStatusException(HttpStatus.CONFLICT, "Ces informations existe déjà!");

    InfosPerso infosPerso = new InfosPerso(
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

    infosPersoDao.save(infosPerso);

    return infosPerso;
  }

  @Override
  public InfosPerso updateInfosPerso(String id, InfosPersoRequest infosPersoRequest) {
    Optional<InfosPerso> _infosPerso = infosPersoDao.findById(id);
    InfosPerso _infosPersoExist = findInfosPerso(infosPersoRequest.getEmail(), infosPersoRequest.getTelephone(), infosPersoRequest.getNumeroPermis(), infosPersoRequest.getNumeroPiece());

    if (!_infosPerso.isPresent()) {
      throw new IllegalArgumentException("InfosPerso with that id does not exists!");
    }

    if (!_infosPerso.get().getId().equals(_infosPersoExist.getId())) {
      throw new IllegalArgumentException("Ces informations existent déjà!");
    }

    InfosPerso infosPerso = _infosPerso.get();

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

    infosPersoDao.save(infosPerso);

    return infosPerso;
  }

  @Override
  public InfosPerso createOrUpdateCompteAdministrateur(String id, InfosPersoAvecCompteRequest request) {
    if (request.getRoleId() == null || request.getRoleId().isEmpty())
      throw new IllegalArgumentException("Role est requis!");

    Optional<Role> _role = roleService.getRoleById(request.getRoleId());

    if (!_role.isPresent() || !_role.get().getType().equals(ECompteType.COMPTE_ADMINISTRATEUR))
      throw new IllegalArgumentException("Role invalide!");

    InfosPerso infosPerso = null;

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
    infosPersoDao.save(infosPerso);

    return infosPerso;
  }

  @Override
  public void deleteCompteAdministrateur(String infosPersoId) {
    Optional<InfosPerso> infosPerso = infosPersoDao.findById(infosPersoId);

    if (infosPerso.isPresent()) {
      deleteCompte(infosPersoId, ECompteType.COMPTE_ADMINISTRATEUR);
    }
  }

  @Override
  public InfosPerso createOrUpdateCompteAgent(String id, InfosPersoAvecCompteRequest request) {
    if (!request.valideFieldsCompteAgent())
      throw new IllegalArgumentException("Informations agent manquant!");

    final Query query = new Query();
    final List<Criteria> listCriteria = new ArrayList<>();

    listCriteria.add(Criteria.where("numeroEmei").is(request.getNumeroEmei()));
    listCriteria.add(Criteria.where("numeroReference").is(request.getNumeroReference()));
    query.addCriteria(new Criteria().orOperator(listCriteria.toArray(new Criteria[listCriteria.size()])));
    query.addCriteria(Criteria.where("deleted").is(false));

    // Validation de l'unicité des infos de la compte agent
    Collection<Compte> _comptes = mongoTemplate.find(query, Compte.class);
    if (_comptes != null && _comptes.size() > 0 && (_comptes.size() > 1 || (id == null && _comptes.size() == 1) || (id != null && !_comptes.iterator().next().getInfosPersoId().equals(id))))
      throw new IllegalArgumentException("Numéro emei ou référence déjà utilisé!");

    InfosPerso infosPerso = null;

    if (id == null) {
      infosPerso = createCompte(request, ECompteType.COMPTE_COURSIER, null, null);
      id = infosPerso.getId();
    } else {
      infosPerso = updateCompte(id, request, ECompteType.COMPTE_COURSIER);
    }

    Compte compte = compteDao.findByInfosPersoIdAndType(infosPerso.getId(), ECompteType.COMPTE_COURSIER).get();

    compte.setNumeroEmei(request.getNumeroEmei());
    compte.setNumeroReference(request.getNumeroReference());
    compte.setStatut(request.getStatut());
    compteDao.save(compte);
    infosPerso.updateCompte(compte);
    infosPersoDao.save(infosPerso);

    return infosPerso;
  }

  @Override
  public void deleteCompteAgent(String infosPersoId) {
    Optional<InfosPerso> infosPerso = infosPersoDao.findById(infosPersoId);

    if (infosPerso.isPresent()) {
      deleteCompte(infosPersoId, ECompteType.COMPTE_COURSIER);
    }
  }

  @Override
  public InfosPerso createOrUpdateComptePrestataire(String id, InfosPersoAvecCompteRequest request) {
    InfosPerso infosPerso = null;

    if (id == null) {
      infosPerso = createCompte(request, ECompteType.COMPTE_PRESTATAIRE, null, null);
      id = infosPerso.getId();
    } else {
      infosPerso = updateCompte(id, request, ECompteType.COMPTE_PRESTATAIRE);
    }

    Compte compte = compteDao.findByInfosPersoIdAndType(infosPerso.getId(), ECompteType.COMPTE_PRESTATAIRE).get();

    compte.setStatut(request.getStatut());
    compteDao.save(compte);
    infosPerso.updateCompte(compte);
    infosPersoDao.save(infosPerso);

    return infosPerso;
  }

  @Override
  public void deleteComptePrestataire(String infosPersoId) {
    Optional<InfosPerso> infosPerso = infosPersoDao.findById(infosPersoId);

    if (infosPerso.isPresent()) {
      deleteCompte(infosPersoId, ECompteType.COMPTE_PRESTATAIRE);
    }
  }

  @Override
  public UserInfosResponse createCompteClient(RegisterRequest request) {
    InfosPerso infosPerso = createCompte(request, ECompteType.COMPTE_PARTICULIER, request.getUsername(), request.getPassword());

    Compte compte = compteDao.findByInfosPersoIdAndType(infosPerso.getId(), ECompteType.COMPTE_PARTICULIER).get();

    compte.setStatut(1);
    compteDao.save(compte);
    infosPerso.updateCompte(compte);
    infosPersoDao.save(infosPerso);

    User user = userDao.findByInfosPersoId(infosPerso.getId()).get();

    return new UserInfosResponse(infosPerso, null, user);
  }

  @Override
  public UserInfosResponse getCompteClient(String id) {
    Optional<Compte> compteExist = compteDao.findByInfosPersoIdAndType(id, ECompteType.COMPTE_PARTICULIER);

    if (!compteExist.isPresent() && !compteExist.get().isDeleted())
      throw new IllegalArgumentException("Cet client n'existe pas!");

    InfosPerso infosPerso = infosPersoDao.findById(id).get();
    User user = userDao.findByInfosPersoId(id).get();

    return new UserInfosResponse(infosPerso, null, user);
  }

  @Override
  public UserInfosResponse updateCompteClient(String id, UpdateInfosPersoRequest request) {
    Optional<Compte> compteExist = compteDao.findByInfosPersoIdAndType(id, ECompteType.COMPTE_PARTICULIER);

    if (!compteExist.isPresent() && !compteExist.get().isDeleted())
      throw new IllegalArgumentException("Cet client n'existe pas!");

    Optional<User> accessExist = userDao.findByUsername(request.getUsername());

    if (accessExist.isPresent() && !accessExist.get().getInfosPerso().getId().equals(id)) {
      throw new IllegalArgumentException("Nom d'utilisateur déjà utilisé!");
    }

    InfosPerso infosPerso = updateInfosPerso(id, request);

    User user = userDao.findByInfosPersoId(infosPerso.getId()).get();

    if (request.getUsername() != null && !request.getUsername().isEmpty())
      user.setUsername(request.getUsername());

    if (request.getPassword() != null && !request.getPassword().isEmpty()) {
      user.setPassword(encoder.encode(request.getPassword())); 
    }
    
    userDao.save(user);

    return new UserInfosResponse(infosPerso, null, user);
  }

  @Override
  public void deleteCompteClient(String infosPersoId) {
    Optional<InfosPerso> infosPerso = infosPersoDao.findById(infosPersoId);

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

    if (request.getPassword() != null && !request.getPassword().isEmpty()) {
      Optional<User> userExist = userDao.findByInfosPersoId(currentUser.getInfosPerso().getId());

      if (!userExist.isPresent() || !encoder.matches(request.getOldPassword(), userExist.get().getPassword()))
        throw new IllegalArgumentException("Encien mot de passe invalide!");
    }

    UserInfosResponse infosPerso = updateCompteClient(currentUser.getInfosPerso().getId(), request);

    return infosPerso;
  }

  @Override
  public InfosPerso newEnrollment(EnrollmentRequest enrollmentRequest) {
    Optional<NumeroCarte> numeroCarteExist = numeroCarteDao.findByNumero(enrollmentRequest.getNumeroCarte());

    if (!numeroCarteExist.isPresent())
      throw new IllegalArgumentException("Cette carte n'existe pas!");

    if (numeroCarteExist.get().isActive())
      throw new IllegalArgumentException("Cette carte est déjà activé!");

    InfosPerso infosPerso = findInfosPerso(enrollmentRequest.getEmail(), enrollmentRequest.getTelephone(), null, enrollmentRequest.getNumeroPiece());

    if (infosPerso != null && abonnementDao.existsByCompteClientInfosPersoId(infosPerso.getId()))
      throw new IllegalArgumentException("Cet utilisateur est déjà abonné!");

    BigDecimal prixCarte = typeAbonnementDao.findById(numeroCarteExist.get().getTypeAbonnementId()).get().getPrix();

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
        infosPerso = infosPersoDao.findById(_infosPerso.getId()).get();
    }

    UserDetailsImpl currentUser = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    ECompteType compteType = compteDao.existsByInfosPersoIdAndType(currentUser.getInfosPerso().getId(), ECompteType.COMPTE_COURSIER) ? ECompteType.COMPTE_COURSIER : ECompteType.COMPTE_ADMINISTRATEUR;

    if (!abonnementDao.existsByCompteClientInfosPersoId(infosPerso.getId())) {
      // On creée un abonnement s'il n'a pas d'abonnement
      abonnementService.createAbonnement(
        new AbonnementRequest(carte.getTypeAbonnementId(), infosPerso.getId(), 1, carte.getNumero(), false),
        compteType
      );
    }

    compte = compteDao.findByInfosPersoIdAndType(infosPerso.getId(), ECompteType.COMPTE_PARTICULIER).get();

    abonnement = abonnementService.getAbonnementByCompteClient(compte).get();

    // On soustrait deux mille comme prix de la carte
    enrollmentRequest.setMontant(enrollmentRequest.getMontant().subtract(prixCarte));

    abonnement.setDepotInitial(enrollmentRequest.getMontant());

    abonnementDao.save(abonnement);

    if (abonnement.getDepotInitial().compareTo(BigDecimal.valueOf(5)) != -1) {
      transactionService.createRechargementTransaction(
        new RechargementTransactionRequest(abonnement.getNumeroCarte(), abonnement.getDepotInitial()),
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
      throw new ResponseStatusException(HttpStatus.FORBIDDEN , "Accès non autorisé!");
    }

    Page<Abonnement> abonnements = abonnementService.getAbonnementByCompteCreateur(compteExist.get(), pageable);

    if (abonnements.getContent().size() <= 0) {
      return abonnements;
    }

    Page<UserInfosResponse> customData = abonnements.map(new Function<Abonnement, UserInfosResponse>() {
      @Override
      public UserInfosResponse apply(Abonnement abonnement) {
        String infosPersoId = abonnement.getCompteClient().getInfosPersoId();

        InfosPerso infosPerso = infosPersoDao.findById(infosPersoId).get();
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

  private InfosPerso findInfosPerso(String email, String telephone, String numeroPermis, String numeroPiece) {
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

    Collection<InfosPerso> results = mongoTemplate.find(query, InfosPerso.class);

    if (results.size() == 0)
      return null;

    if (results.size() > 1)
      throw new ResponseStatusException(HttpStatus.CONFLICT, "L'email ou le numero téléphone ou le numéro permis ou le numéro pièce existe déjà!");

    return results.iterator().next();
  }

  private InfosPerso createCompte(InfosPersoRequest request, ECompteType compteType, String _username, String _password) {

    InfosPerso infosPerso = findInfosPerso(request.getEmail(), request.getTelephone(), request.getNumeroPermis(), request.getNumeroPiece());

    Compte compte;

    if (infosPerso != null) {
      Optional<Compte> _compte = compteDao.findByInfosPersoIdAndType(infosPerso.getId(), compteType);

      if (_compte.isPresent() && !_compte.get().isDeleted())
        throw new ResponseStatusException(HttpStatus.CONFLICT, "Cette utilisateur a déjà un compte administrateur!");

      compte = _compte.get();
      compte.setDeleted(false);
      compteDao.save(compte);
      infosPerso.updateCompte(compte);
      infosPersoDao.save(infosPerso);
    } else {
      if (userDao.existsByUsername(request.getEmail()))
        throw new IllegalArgumentException("Email déjà utilisé!");

      infosPerso = createInfosPerso(request);

      compte = new Compte(compteType, infosPerso.getId());
      compteDao.save(compte);
      infosPerso.addCompte(compte);
      infosPersoDao.save(infosPerso);
    }

    if (!userDao.existsByInfosPersoId(infosPerso.getId())) {
      String username = _username != null ? _username : infosPerso.getEmail();
      String password = _password != null ? _password : alphaNumericString(1, 8);
      User user = new User(infosPerso, username, encoder.encode(password), 1);
      userDao.save(user);

      emailService.sendSimpleMessage(infosPerso.getEmail(),"Support Safe Logistics",
      String.format("Bonjour %s %s, \nBienvenue dans Safe Logistics. Vous êtes ajouté en tant que membre. \nVotre login est %s votre mot de passe est %s. Une fois connecté veuillez changer votre mot de passe.",
      infosPerso.getPrenom(), infosPerso.getNom(), user.getUsername(), password));
    }

    return infosPerso;
  }

  private InfosPerso updateCompte(String id, InfosPersoAvecCompteRequest request, ECompteType compteType) {
    Optional<InfosPerso> infosPersoExist = infosPersoDao.findById(id);
    if (!infosPersoExist.isPresent())
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cette utilisateur n'existe pas!");

    Optional<Compte> _compte = compteDao.findByInfosPersoIdAndType(id, compteType);

    if (!_compte.isPresent() || _compte.get().isDeleted())
      throw new ResponseStatusException(HttpStatus.CONFLICT, "Cette utilisateur n'a pas de compte administrateur!");

    InfosPerso _infosPerso = findInfosPerso(request.getEmail(), request.getTelephone(), request.getNumeroPermis(), request.getNumeroPiece());

    if (_infosPerso != null && !infosPersoExist.get().getId().equals(_infosPerso.getId()))
      throw new ResponseStatusException(HttpStatus.CONFLICT, "Ces informations existent déjà!");

    InfosPerso infosPerso = infosPersoExist.get();

    infosPerso = updateInfosPerso(id, request);

    Compte compte = _compte.get();
    compte.setStatut(request.getStatut());
    compteDao.save(compte);
    infosPerso.updateCompte(compte);
    infosPersoDao.save(infosPerso);

    return infosPerso;
  }

  private void deleteCompte(String infosPersoId, ECompteType compteType) {
    InfosPerso infosPerso = infosPersoDao.findById(infosPersoId).get();

    Optional<Compte> _compte = compteDao.findByInfosPersoIdAndType(infosPerso.getId(), compteType);

    if (!_compte.isPresent())
      return;

    Compte compte = _compte.get();
    compte.setDeleted(true);
    compteDao.save(compte);
    infosPerso.updateCompte(compte);
    infosPersoDao.save(infosPerso);

    if (compteDao.countByInfosPersoIdAndDeletedIsFalse(infosPersoId) == 0) {
      userDao.deleteByInfosPersoId(infosPersoId);
    }
  }
}
