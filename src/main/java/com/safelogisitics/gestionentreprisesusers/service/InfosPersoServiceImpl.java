package com.safelogisitics.gestionentreprisesusers.service;

import java.security.SecureRandom;
import java.util.Collection;
import java.util.Optional;
import java.util.Random;

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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
	PasswordEncoder encoder;

  public Optional<InfosPerso> findInfosPersoById(String id) {
    return infosPersoDao.findById(id);
  }

  @Override
  public Page<InfosPerso> getInfosPersos(Pageable pageable) {
    return infosPersoDao.findByComptesIsNull(pageable);
  }

  @Override
  public Page<InfosPerso> getInfosPersos(ECompteType type, Pageable pageable) {
    return infosPersoDao.findByComptesType(type, pageable);
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

    return infosPersoDao.save(infosPerso);
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

    return infosPersoDao.save(infosPerso);
  }

  @Override
  public InfosPerso createOrUpdateCompteAdministrateur(InfosPersoAvecCompteRequest request) {
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
    }, () -> {
      Compte compte = new Compte(ECompteType.COMPTE_ADMINISTRATEUR, infosPerso.getId(), role, request.getStatut());
      compteDao.save(compte);
      infosPerso.addCompte(compte);
    });

    userDao.findByInfosPerso(infosPerso).ifPresentOrElse(user -> {
      user.setStatut(1);
      userDao.save(user);
    }, () -> {
      // Génération du mot de passe
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
    });

    userDao.findByInfosPerso(infosPerso).ifPresent(user -> {
      user.setStatut(0);
      userDao.save(user);
    });
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
