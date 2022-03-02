package com.safelogisitics.gestionentreprisesusers.data.fixture;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.safelogisitics.gestionentreprisesusers.data.dao.CompteDao;
import com.safelogisitics.gestionentreprisesusers.data.dao.InfosPersoDao;
import com.safelogisitics.gestionentreprisesusers.data.dao.RoleDao;
import com.safelogisitics.gestionentreprisesusers.data.dao.UserDao;
import com.safelogisitics.gestionentreprisesusers.data.enums.ECompteType;
import com.safelogisitics.gestionentreprisesusers.data.model.Compte;
import com.safelogisitics.gestionentreprisesusers.data.model.InfosPersoModel;
import com.safelogisitics.gestionentreprisesusers.data.model.Role;
import com.safelogisitics.gestionentreprisesusers.data.model.User;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdministrateurFixture implements CommandLineRunner {

  private InfosPersoDao infosPersoDao;

  private CompteDao compteDao;

  private RoleDao roleDao;

  private UserDao userDao;

	private PasswordEncoder encoder;

  private ObjectMapper objectMapper;

  public AdministrateurFixture(
    InfosPersoDao infosPersoDao,
    CompteDao compteDao,
    RoleDao roleDao,
    UserDao userDao,
    PasswordEncoder encoder,
    ObjectMapper objectMapper
  ) {
    this.infosPersoDao = infosPersoDao;
    this.compteDao = compteDao;
    this.roleDao = roleDao;
    this.userDao = userDao;
    this.encoder = encoder;
    this.objectMapper = objectMapper;
  }

  @Override
  public void run(String... args) throws Exception {
    InfosPersoModel[] newInfosPersos = objectMapper.readValue(getClass()
      .getResourceAsStream("/data/infosPerso.json"), InfosPersoModel[].class);
    
    Collection<Map<String, Object>>  administrateurPrivileges = objectMapper.readValue(getClass()
      .getResourceAsStream("/data/administrateur-privileges.json"), new TypeReference<Collection<Map<String, Object>>>(){});

    Role role = this.handleRoleData(administrateurPrivileges);
    
    for (InfosPersoModel newInfosPerso : newInfosPersos) {
      InfosPersoModel infosPerso = this.getInfosPerso(newInfosPerso);

      Compte compte = this.handleCompteData(infosPerso.getId(), role);

      infosPerso.addCompte(compte);

      infosPersoDao.save(infosPerso);

      Optional<User> _user = userDao.findByInfosPersoId(infosPerso.getId());

      if (!_user.isPresent()) {
        String username = infosPerso.getEmail();
        int index = 0;
        while (userDao.existsByUsername(username)) {
          String[] domains = username.split("@");
          index++;

          username = String.format("%s%s@%s", domains[0], index, domains[1]);
        }
        User user = new User(infosPerso, username, encoder.encode("azerty"), 1);
        userDao.save(user);
      }
    }
  }

  private Compte handleCompteData(String infosPersoId, Role role) {
    Compte compte = null;
    Optional<Compte> _compte = compteDao.findByInfosPersoIdAndType(infosPersoId, ECompteType.COMPTE_ADMINISTRATEUR);

    if (_compte.isPresent()) {
      compte = _compte.get();
    } else {
      compte = new Compte();
    }
    compte.setType(ECompteType.COMPTE_ADMINISTRATEUR);
    compte.setInfosPersoId(infosPersoId);
    compte.setRole(role);
    compte.setStatut(1);

    compteDao.save(compte);

    return compte;
  }

  private InfosPersoModel getInfosPerso(InfosPersoModel newInfosPerso) {
    InfosPersoModel infosPerso = null;
    Optional<InfosPersoModel> _infosPerso = infosPersoDao.findByEmailOrTelephone(newInfosPerso.getEmail(), newInfosPerso.getTelephone());

    if (!_infosPerso.isPresent()) {
      infosPersoDao.save(newInfosPerso);
      infosPerso = newInfosPerso;
    } else {
      infosPerso = _infosPerso.get();
    }

    return infosPerso;
  }

  private Role handleRoleData(Collection<Map<String, Object>>  administrateurPrivileges) {
    Role role = new Role();

    Optional<Role> _role = roleDao.findByLibelle("Super Administrateur");
    if (_role.isPresent()) {
      role = _role.get();
    }

    role.setLibelle("Super Administrateur");
    role.setEditable(false);
    role.setType(ECompteType.COMPTE_ADMINISTRATEUR);
    for (Map<String,Object> privilegeActions : administrateurPrivileges) {
      role.getPrivilegesActions().put(
        String.valueOf(privilegeActions.get("valeur")),
        new HashSet<>(objectMapper.convertValue(privilegeActions.get("actions"), new TypeReference<Collection<String>>(){}))
      );
    }

    roleDao.save(role);

    return role;
  }
}
