package com.safelogisitics.gestionentreprisesusers.service;

import java.security.SecureRandom;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import com.safelogisitics.gestionentreprisesusers.dao.CompteDao;
import com.safelogisitics.gestionentreprisesusers.dao.InfosPersoDao;
import com.safelogisitics.gestionentreprisesusers.dao.UserDao;
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
import com.safelogisitics.gestionentreprisesusers.security.services.UserDetailsImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
	PasswordEncoder encoder;

  @Override
  public InfosPerso getUserInfos() {
    UserDetailsImpl currentUser = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    return infosPersoDao.findById(currentUser.getInfosPerso().getId()).get();
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
      .map(compte -> compte.getInfosPersoId())
      .collect(Collectors.toList());

    return infosPersoDao.findByIdIn(ids, pageable);
  }

  @Override
  public InfosPerso createInfosPerso(InfosPersoRequest infosPersoRequest) {
    Optional<InfosPerso> _infosPerso = infosPersoDao.findByEmailOrTelephone(infosPersoRequest.getEmail(), infosPersoRequest.getTelephone());

    InfosPerso infosPerso = _infosPerso.isPresent() ? updateInfosPerso(_infosPerso.get().getId(), infosPersoRequest) : new InfosPerso(
      infosPersoRequest.getPrenom(),
      infosPersoRequest.getNom(),
      infosPersoRequest.getEmail(),
      infosPersoRequest.getTelephone(),
      infosPersoRequest.getAdresse()
    );

    infosPersoDao.save(infosPerso);

    return infosPerso;
  }

  @Override
  public InfosPerso updateInfosPerso(String id, InfosPersoRequest infosPersoRequest) {
    Optional<InfosPerso> _infosPerso = infosPersoDao.findById(id);

    if (!_infosPerso.isPresent()) {
      throw new IllegalArgumentException("InfosPerso with that id does not exists!");
    }

    InfosPerso infosPerso = _infosPerso.get();

    infosPerso.setPrenom(infosPersoRequest.getPrenom());
    infosPerso.setNom(infosPersoRequest.getNom());
    infosPerso.setEmail(infosPersoRequest.getEmail());
    infosPerso.setTelephone(infosPersoRequest.getTelephone());
    infosPerso.setAdresse(infosPersoRequest.getAdresse());

    infosPersoDao.save(infosPerso);

    return infosPerso;
  }

  @Override
  public InfosPerso createOrUpdateCompteAdministrateur(InfosPersoAvecCompteRequest request) {
    Optional<User> accessExist = userDao.findByUsername(request.getEmail());

    if (accessExist.isPresent()) {
      throw new IllegalArgumentException("Email déjà utilisé!");
    }

    InfosPersoRequest infosPersoRequest = request;

    InfosPerso infosPerso = createInfosPerso(infosPersoRequest);

    Optional<Role> _role = roleService.getRoleById(request.getRoleId());

    if (!_role.isPresent() || !_role.get().getType().equals(ECompteType.COMPTE_ADMINISTRATEUR)) {
      throw new IllegalArgumentException("Role invalide!");
    }

    Role role = _role.get();

    compteDao.findByInfosPersoIdAndType(infosPerso.getId(), ECompteType.COMPTE_ADMINISTRATEUR).ifPresentOrElse(compte -> {
      compte.setDeleted(false);
      compte.setRole(role);
      compte.setStatut(request.getStatut());
      compteDao.save(compte);
      infosPerso.updateCompte(compte);
      infosPersoDao.save(infosPerso);
    }, () -> {
      Compte compte = new Compte(ECompteType.COMPTE_ADMINISTRATEUR, infosPerso.getId(), role, request.getStatut());
      compteDao.save(compte);
      infosPerso.addCompte(compte);
      infosPersoDao.save(infosPerso);
    });

    userDao.findByInfosPerso(infosPerso).ifPresentOrElse(user -> {
      user.setStatut(1);
      userDao.save(user);
    }, () -> {
      String password = alphaNumericString(1, 8);
      User user = new User(infosPerso, infosPerso.getEmail(), encoder.encode(password), 1);
      userDao.save(user);
      System.out.println("MOT DE PASSE =====> "+password);
    });

    return infosPerso;
  }

  @Override
  public void deleteCompteAdministrateur(String infosPersoId) {
    InfosPerso infosPerso = infosPersoDao.findById(infosPersoId).get();

    compteDao.findByInfosPersoIdAndType(infosPerso.getId(), ECompteType.COMPTE_ADMINISTRATEUR).ifPresent(compte -> {
      compte.setDeleted(true);
      compteDao.save(compte);
      infosPerso.updateCompte(compte);
      infosPersoDao.save(infosPerso);
    });
  }

  @Override
  public InfosPerso createOrUpdateCompteAgent(InfosPersoAvecCompteRequest request) {
    Optional<User> accessExist = userDao.findByUsername(request.getEmail());

    if (accessExist.isPresent()) {
      throw new IllegalArgumentException("Email déjà utilisé!");
    }

    InfosPersoRequest infosPersoRequest = request;

    InfosPerso infosPerso = createInfosPerso(infosPersoRequest);

    compteDao.findByInfosPersoIdAndType(infosPerso.getId(), ECompteType.COMPTE_COURSIER).ifPresentOrElse(compte -> {
      compte.setDeleted(false);
      compte.setStatut(request.getStatut());
      compteDao.save(compte);
      infosPerso.updateCompte(compte);
      infosPersoDao.save(infosPerso);
    }, () -> {
      Compte compte = new Compte(ECompteType.COMPTE_COURSIER, infosPerso.getId(), request.getStatut());
      compteDao.save(compte);
      infosPerso.addCompte(compte);
      infosPersoDao.save(infosPerso);
    });

    userDao.findByInfosPerso(infosPerso).ifPresentOrElse(user -> {
      user.setStatut(1);
      userDao.save(user);
    }, () -> {
      String password = alphaNumericString(1, 8);
      User user = new User(infosPerso, infosPerso.getEmail(), encoder.encode(password), 1);
      userDao.save(user);
      System.out.println("MOT DE PASSE =====> "+password);
    });

    return infosPerso;
  }

  @Override
  public void deleteCompteAgent(String infosPersoId) {
    InfosPerso infosPerso = infosPersoDao.findById(infosPersoId).get();

    compteDao.findByInfosPersoIdAndType(infosPerso.getId(), ECompteType.COMPTE_PRESTATAIRE).ifPresent(compte -> {
      compte.setDeleted(true);
      compteDao.save(compte);
      infosPerso.updateCompte(compte);
      infosPersoDao.save(infosPerso);
    });
  }

  @Override
  public InfosPerso createOrUpdateComptePrestataire(InfosPersoAvecCompteRequest request) {
    Optional<User> accessExist = userDao.findByUsername(request.getEmail());

    if (accessExist.isPresent()) {
      throw new IllegalArgumentException("Email déjà utilisé!");
    }

    InfosPersoRequest infosPersoRequest = request;

    InfosPerso infosPerso = createInfosPerso(infosPersoRequest);

    compteDao.findByInfosPersoIdAndType(infosPerso.getId(), ECompteType.COMPTE_PRESTATAIRE).ifPresentOrElse(compte -> {
      compte.setDeleted(false);
      compte.setStatut(request.getStatut());
      compteDao.save(compte);
      infosPerso.updateCompte(compte);
      infosPersoDao.save(infosPerso);
    }, () -> {
      Compte compte = new Compte(ECompteType.COMPTE_PRESTATAIRE, infosPerso.getId(), request.getStatut());
      compteDao.save(compte);
      infosPerso.addCompte(compte);
      infosPersoDao.save(infosPerso);
    });

    userDao.findByInfosPerso(infosPerso).ifPresentOrElse(user -> {
      user.setStatut(1);
      userDao.save(user);
    }, () -> {
      String password = alphaNumericString(1, 8);
      User user = new User(infosPerso, infosPerso.getEmail(), encoder.encode(password), 1);
      userDao.save(user);
      System.out.println("MOT DE PASSE =====> "+password);
    });

    return infosPerso;
  }

  @Override
  public void deleteComptePrestataire(String infosPersoId) {
    InfosPerso infosPerso = infosPersoDao.findById(infosPersoId).get();

    compteDao.findByInfosPersoIdAndType(infosPerso.getId(), ECompteType.COMPTE_COURSIER).ifPresent(compte -> {
      compte.setDeleted(true);
      compteDao.save(compte);
      infosPerso.updateCompte(compte);
      infosPersoDao.save(infosPerso);
    });
  }

  @Override
  public InfosPerso createCompteClient(RegisterRequest request) {
    Optional<User> accessExist = userDao.findByUsername(request.getUsername());

    if (accessExist.isPresent() &&
      (!accessExist.get().getInfosPerso().getEmail().equals(request.getEmail()) || !accessExist.get().getInfosPerso().getTelephone().equals(request.getTelephone()))
    ) {
      throw new IllegalArgumentException("Nom d'utilisateur déjà utilisé!");
    }

    InfosPersoRequest infosPersoRequest = request;

    InfosPerso infosPerso = createInfosPerso(infosPersoRequest);

    compteDao.findByInfosPersoIdAndType(infosPerso.getId(), ECompteType.COMPTE_PARTICULIER).ifPresentOrElse(compte -> {
      if (!compte.isDeleted()) {
        throw new IllegalArgumentException("User is already register!");
      }
      compte.setDeleted(false);
      compte.setStatut(1);
      compteDao.save(compte);
      infosPerso.updateCompte(compte);
      infosPersoDao.save(infosPerso);
    }, () -> {
      Compte compte = new Compte(ECompteType.COMPTE_PARTICULIER, infosPerso.getId(), 1);
      compteDao.save(compte);
      infosPerso.addCompte(compte);
      infosPersoDao.save(infosPerso);
    });

    userDao.findByInfosPerso(infosPerso).ifPresentOrElse(user -> {
      user.setStatut(1);
      user.setUsername(request.getUsername());
      user.setPassword(encoder.encode(request.getPassword()));
      userDao.save(user);
    }, () -> {
      User user = new User(infosPerso, request.getUsername(), encoder.encode(request.getPassword()), 1);
      userDao.save(user);
    });

    return infosPerso;
  }

  @Override
  public InfosPerso updateCompteClient(String id, UpdateInfosPersoRequest request) {
    Optional<User> accessExist = userDao.findByUsername(request.getUsername());

    if (accessExist.isPresent() && !accessExist.get().getInfosPerso().getId().equals(id)) {
      throw new IllegalArgumentException("Nom d'utilisateur déjà utilisé!");
    }

    InfosPerso infosPerso = updateInfosPerso(id, request);

    userDao.findByInfosPerso(infosPerso).ifPresent(user -> {
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
    InfosPerso infosPerso = infosPersoDao.findById(infosPersoId).get();

    compteDao.findByInfosPersoIdAndType(infosPerso.getId(), ECompteType.COMPTE_PARTICULIER).ifPresent(compte -> {
      compte.setDeleted(true);
      compteDao.save(compte);
      infosPerso.updateCompte(compte);
      infosPersoDao.save(infosPerso);
    });
  }

  @Override
  public JwtResponse clientRegistration(RegisterRequest request) {

    createCompteClient(request);

    return userService.authenticate(new LoginRequest(request.getUsername(), request.getPassword()));
  }

  @Override
  public InfosPerso updateUserInfos(UpdateInfosPersoRequest request) {
    UserDetailsImpl currentUser = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    
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
}
