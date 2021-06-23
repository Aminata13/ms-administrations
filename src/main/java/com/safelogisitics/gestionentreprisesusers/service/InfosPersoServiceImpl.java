package com.safelogisitics.gestionentreprisesusers.service;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import com.safelogisitics.gestionentreprisesusers.dao.CompteDao;
import com.safelogisitics.gestionentreprisesusers.dao.InfosPersoDao;
import com.safelogisitics.gestionentreprisesusers.dao.UserDao;
import com.safelogisitics.gestionentreprisesusers.model.Abonnement;
import com.safelogisitics.gestionentreprisesusers.model.Compte;
import com.safelogisitics.gestionentreprisesusers.model.InfosPerso;
import com.safelogisitics.gestionentreprisesusers.model.Role;
import com.safelogisitics.gestionentreprisesusers.model.User;
import com.safelogisitics.gestionentreprisesusers.model.enums.ECompteType;
import com.safelogisitics.gestionentreprisesusers.payload.request.InfosPersoAvecCompteRequest;
import com.safelogisitics.gestionentreprisesusers.payload.request.InfosPersoRequest;
import com.safelogisitics.gestionentreprisesusers.payload.request.LoginRequest;
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
  InfosPersoDao infosPersoDao;

  @Autowired
  CompteDao compteDao;

  @Autowired
  UserDao userDao;

  @Autowired
  RoleService roleService;

  @Autowired
  UserService userService;

  @Autowired
	EmailService emailService;

  @Autowired
	PasswordEncoder encoder;

  @Autowired
  MongoTemplate mongoTemplate;

  @Autowired
  AbonnementService abonnementService;

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
  public Optional<InfosPerso> findByEmailOrTelephone(String email, String telephone) {
    return infosPersoDao.findByEmailOrTelephone(email, telephone);
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
    InfosPerso _infosPerso = findInfosPerso(infosPersoRequest);

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

    infosPersoDao.save(infosPerso);

    return infosPerso;
  }

  @Override
  public InfosPerso updateInfosPerso(String id, InfosPersoRequest infosPersoRequest) {
    Optional<InfosPerso> _infosPerso = infosPersoDao.findById(id);
    InfosPerso _infosPersoExist = findInfosPerso(infosPersoRequest);

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

    Compte compte = compteDao.findByInfosPersoIdAndType(infosPerso.getId(), ECompteType.COMPTE_COURSIER).get();

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
  public InfosPerso createCompteClient(RegisterRequest request) {
    InfosPerso infosPerso = createCompte(request, ECompteType.COMPTE_PARTICULIER, request.getUsername(), request.getPassword());

    Compte compte = compteDao.findByInfosPersoIdAndType(infosPerso.getId(), ECompteType.COMPTE_COURSIER).get();

    compte.setStatut(1);
    compteDao.save(compte);
    infosPerso.updateCompte(compte);
    infosPersoDao.save(infosPerso);

    return infosPerso;
  }

  @Override
  public InfosPerso updateCompteClient(String id, UpdateInfosPersoRequest request) {
    Optional<User> accessExist = userDao.findByUsername(request.getUsername());

    if (accessExist.isPresent() && !accessExist.get().getInfosPerso().getId().equals(id)) {
      throw new IllegalArgumentException("Nom d'utilisateur déjà utilisé!");
    }

    InfosPerso infosPerso = updateInfosPerso(id, request);

    userDao.findByInfosPersoId(infosPerso.getId()).ifPresent(user -> {
      user.setUsername(request.getUsername());
      if (request.getPassword() != null) {
        user.setPassword(encoder.encode(request.getPassword())); 
      }
      userDao.save(user);
    });

    return infosPerso;
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
  public InfosPerso updateUserInfos(UpdateInfosPersoRequest request) {
    UserDetailsImpl currentUser = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    Optional<User> userExist = userDao.findByInfosPersoId(currentUser.getInfosPerso().getId());

    if (!userExist.isPresent() || !encoder.matches(request.getOldPassword(), userExist.get().getPassword()))
      throw new IllegalArgumentException("Encien mot de passe invalide!");

    InfosPerso infosPerso = updateCompteClient(currentUser.getInfosPerso().getId(), request);

    return infosPerso;
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

  private InfosPerso findInfosPerso(InfosPersoRequest infosPersoRequest) {
    final Query query = new Query();
    final List<Criteria> listCriteria = new ArrayList<>();

    if (infosPersoRequest.getEmail() != null && !infosPersoRequest.getEmail().isEmpty())
      listCriteria.add(Criteria.where("email").is(infosPersoRequest.getEmail()));

    if (infosPersoRequest.getTelephone() != null && !infosPersoRequest.getTelephone().isEmpty())
      listCriteria.add(Criteria.where("telephone").is(infosPersoRequest.getTelephone()));

    if (infosPersoRequest.getNumeroPermis() != null && !infosPersoRequest.getNumeroPermis().isEmpty())
      listCriteria.add(Criteria.where("numeroPermis").is(infosPersoRequest.getNumeroPermis()));

    if (infosPersoRequest.getNumeroPiece() != null && !infosPersoRequest.getNumeroPiece().isEmpty())
      listCriteria.add(Criteria.where("numeroPiece").is(infosPersoRequest.getNumeroPiece()));

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

    InfosPerso infosPerso = findInfosPerso(request);

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

    InfosPersoRequest infosPersoRequest = request;

    InfosPerso _infosPerso = findInfosPerso(infosPersoRequest);

    if (_infosPerso != null && !infosPersoExist.get().getId().equals(_infosPerso.getId()))
      throw new ResponseStatusException(HttpStatus.CONFLICT, "Ces informations existent déjà!");

    InfosPerso infosPerso = infosPersoExist.get();

    infosPerso = updateInfosPerso(id, infosPersoRequest);

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
