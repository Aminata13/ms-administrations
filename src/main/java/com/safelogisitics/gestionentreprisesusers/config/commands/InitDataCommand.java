package com.safelogisitics.gestionentreprisesusers.config.commands;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safelogisitics.gestionentreprisesusers.dao.CompteDao;
import com.safelogisitics.gestionentreprisesusers.dao.InfosPersoDao;
import com.safelogisitics.gestionentreprisesusers.dao.PrivilegeDao;
import com.safelogisitics.gestionentreprisesusers.dao.RoleDao;
import com.safelogisitics.gestionentreprisesusers.dao.TypeAbonnementDao;
import com.safelogisitics.gestionentreprisesusers.dao.UserDao;
import com.safelogisitics.gestionentreprisesusers.model.Compte;
import com.safelogisitics.gestionentreprisesusers.model.InfosPerso;
import com.safelogisitics.gestionentreprisesusers.model.Privilege;
import com.safelogisitics.gestionentreprisesusers.model.Role;
import com.safelogisitics.gestionentreprisesusers.model.TypeAbonnement;
import com.safelogisitics.gestionentreprisesusers.model.User;
import com.safelogisitics.gestionentreprisesusers.model.enums.ECompteType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * 1. Act as main class for spring boot application
 * 2. Also implements CommandLineRunner, so that code within run method
 * is executed before application startup but after all beans are effectively created
 * @author hemant
 *
 */
@Component
public class InitDataCommand implements CommandLineRunner {

	private static Logger LOG = LoggerFactory.getLogger(InitDataCommand.class);

  @Autowired
  private PrivilegeDao privilegeDao;

  @Autowired
  private RoleDao roleDao;

  @Autowired
  private UserDao userDao;

  @Autowired
  private InfosPersoDao infosPersoDao;

  @Autowired
  private CompteDao compteDao;

  @Autowired
  private TypeAbonnementDao typeAbonnementDao;

  @Autowired
	private PasswordEncoder encoder;

	/**
	 * This method will be executed after the application context is loaded and
	 * right before the Spring Application main method is completed.
	 */
	@Override
	public void run(String... args) throws Exception, IOException {
    
    ObjectMapper objectMapper = new ObjectMapper();

    InfosPerso[] _newInfosPerso = objectMapper.readValue(getClass().getResourceAsStream("/data/infosPerso.json"), InfosPerso[].class);

    InfosPerso newInfosPerso = _newInfosPerso[0];

    if (!infosPersoDao.existsByEmailOrTelephone(newInfosPerso.getEmail(), newInfosPerso.getTelephone())) {
      newInfosPerso.setDateCreation(LocalDateTime.now());
      infosPersoDao.save(newInfosPerso);
    }

    InfosPerso infosPerso = infosPersoDao.findByEmail(newInfosPerso.getEmail()).get();

    Privilege[] newPrivileges = objectMapper.readValue(getClass().getResourceAsStream("/data/privileges.json"), Privilege[].class);

    for (Privilege privilege : newPrivileges) {
      if (!privilegeDao.existsByTypeAndValeur(privilege.getType(), privilege.getValeur())) {
        privilegeDao.save(privilege);
        LOG.info("Nouveau privilège ajouté avec succès: {}", privilege.getLibelle());
      }
    }

    Role role = new Role();

    Optional<Role> _role = roleDao.findByLibelle("Super Administrateur");
    if (_role.isPresent()) {
      role = _role.get();
    }

    role.setLibelle("Super Administrateur");
    role.setEditable(false);
    role.setType(ECompteType.COMPTE_ADMINISTRATEUR);
    role.setPrivileges(new HashSet<Privilege>(privilegeDao.findByType(ECompteType.COMPTE_ADMINISTRATEUR)));
    roleDao.save(role);

    Compte compte;
    Optional<Compte> _compte = compteDao.findByInfosPersoIdAndType(infosPerso.getId(), ECompteType.COMPTE_ADMINISTRATEUR);

    if (_compte.isPresent()) {
      compte = _compte.get();
    } else {
      compte = new Compte();
    }

    compte.setType(ECompteType.COMPTE_ADMINISTRATEUR);
    compte.setInfosPersoId(infosPerso.getId());
    compte.setRole(role);
    compte.setStatut(1);

    compteDao.save(compte);

    infosPerso.addCompte(compte);

    infosPersoDao.save(infosPerso);

    Optional<User> _user = userDao.findByUsername(infosPerso.getEmail());

    if (!_user.isPresent()) {
      User user = new User(infosPerso, infosPerso.getEmail(), encoder.encode("azerty"), 1);
      userDao.save(user);
    }

    TypeAbonnement[] newTypeAbonnements = objectMapper.readValue(getClass().getResourceAsStream("/data/typeAbonnements.json"), TypeAbonnement[].class);

    for (TypeAbonnement _typeAbonnement : newTypeAbonnements) {
      typeAbonnementDao.findByLibelle(_typeAbonnement.getLibelle()).ifPresentOrElse((typeAbonnement) -> {
        typeAbonnement.setLibelle(_typeAbonnement.getLibelle());
        typeAbonnement.setIcon(_typeAbonnement.getIcon());
        typeAbonnement.setReduction(_typeAbonnement.getReduction());
        typeAbonnement.setPrix(_typeAbonnement.getPrix());
        typeAbonnement.setStatut(_typeAbonnement.getStatut());
        typeAbonnementDao.save(typeAbonnement);
      }, () -> {
        TypeAbonnement typeAbonnement = new TypeAbonnement(_typeAbonnement.getLibelle(), _typeAbonnement.getReduction(), _typeAbonnement.getStatut());
        typeAbonnement.setPrix(_typeAbonnement.getPrix());
        typeAbonnementDao.save(typeAbonnement);
      });
    }
	}
}
