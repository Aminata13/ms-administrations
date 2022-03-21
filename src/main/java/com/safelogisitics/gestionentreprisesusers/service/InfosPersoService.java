package com.safelogisitics.gestionentreprisesusers.service;

import com.safelogisitics.gestionentreprisesusers.data.dto.request.*;
import com.safelogisitics.gestionentreprisesusers.data.dto.response.EntrepriseInfosResponse;
import com.safelogisitics.gestionentreprisesusers.data.dto.response.JwtResponse;
import com.safelogisitics.gestionentreprisesusers.data.dto.response.UserInfosResponse;
import com.safelogisitics.gestionentreprisesusers.data.enums.ECompteType;
import com.safelogisitics.gestionentreprisesusers.data.enums.EServiceConciergeType;
import com.safelogisitics.gestionentreprisesusers.data.model.AffectationEquipement;
import com.safelogisitics.gestionentreprisesusers.data.model.Compte;
import com.safelogisitics.gestionentreprisesusers.data.model.InfosPersoModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface InfosPersoService {

  public UserInfosResponse getUserInfos();

  // @TODO move to entreprise service
  public EntrepriseInfosResponse getEntrepriseInfos();

  public UserInfosResponse getUserInfos(String id);

  public Optional<InfosPersoModel> findInfosPersoById(String id);

  public Map<String, String> verifierAbonnement(String telephone);

  public Object findInfosPersoByCompteId(String id);

  public Collection<Object> findAllInfosPersoByCompteIds(Set<String> ids);

  public Optional<InfosPersoModel> findByCompteId(String id);

  public Optional<InfosPersoModel> findByEmailOrTelephone(String emailOrTelephone, Boolean prestation);

  public Collection<UserInfosResponse> findByCustomSearch(String prenom, String nom, String email, String telephone, String numeroCarte, ECompteType compteType, EServiceConciergeType serviceConciergeType);

  public Page<InfosPersoModel> getInfosPersos(Pageable pageable); // list infosPerso sans compte

  public Page<UserInfosResponse> getInfosPersos(ECompteType type, Pageable pageable); // list infosPerso avec compte

  public Page<UserInfosResponse> getInfosPersos(ECompteType type, Boolean isAbonnee, Pageable pageable); // list infosPerso avec compte
  
  public InfosPersoModel createInfosPerso(InfosPersoRequest infosPersoRequest); // Ajout d'un infos perso sans compte

  public InfosPersoModel updateInfosPerso(String id, InfosPersoRequest infosPersoRequest); // Ajout d'un infos perso sans compte

  public InfosPersoModel createOrUpdateCompteAdministrateur(String id, InfosPersoAvecCompteRequest request); // Création ou Modification d'un infosPerso avec compte administrateur

  public void deleteCompteAdministrateur(String infosPersoId); // Suppression d'un admnistrateur (Soft delete)

  public InfosPersoModel createOrUpdateCompteAgent(String id, InfosPersoAvecCompteRequest request); // Création et modification d'un infosPerso avec compte agent (Coursier)

  public InfosPersoModel equiperAgent(String id, Set<AffectationEquipement> affectationEquipements);

  public InfosPersoModel affecterMoyenTransportAgent(String id, String moyenTransportId);

  public void deleteCompteAgent(String infosPersoId); // Suppression d'un agent (coursier) (Soft delete)

  public InfosPersoModel createOrUpdateComptePrestataire(String id, InfosPersoAvecCompteRequest request); // Création et modification d'un infosPerso avec compte agent (Coursier)

  public void deleteComptePrestataire(String infosPersoId); // Suppression d'un agent (coursier) (Soft delete)

  public Page<UserInfosResponse> getEntrepriseUsers(String entrepriseId, Map<String, String> requestParams, Pageable pageable);

  public InfosPersoModel createOrUpdateCompteEntreprise(String id, InfosPersoAvecCompteRequest request);

  public void deleteCompteEntreprise(String infosPersoId);

  public UserInfosResponse createCompteClient(RegisterRequest request); // Création d'un infosPerso avec compte particulier (Client)

  public UserInfosResponse getCompteClient(String id); // get infosPerso avec compte

  public UserInfosResponse updateCompteClient(String id, UpdateInfosPersoRequest request); // Modification d'un infosPerso avec compte particulier (Client)

  public void deleteCompteClient(String infosPersoId); // Suppression d'un agent (coursier) (Soft delete)

  public JwtResponse clientRegistration(RegisterRequest request); // Inscription en tant que client (particulier)

  public InfosPersoModel newEnrollment(EnrollmentRequest enrollmentRequest);

  public Page<?> getMyEnrollments(Pageable pageable);

  public UserInfosResponse updateUserInfos(UpdateInfosPersoRequest request); // Inscription en tant que client (particulier)

  public UserInfosResponse updateUserInfos(UpdateInfosPersoRequest request, String id);

  public Collection<Compte> getInfosPersoComptes(String id);

  // public void sendEmailRegistration(User user); // Suppression d'un agent (coursier) (Soft delete)
}
