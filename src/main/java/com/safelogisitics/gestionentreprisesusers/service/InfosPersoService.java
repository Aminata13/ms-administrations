package com.safelogisitics.gestionentreprisesusers.service;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.safelogisitics.gestionentreprisesusers.data.dto.request.EnrollmentRequest;
import com.safelogisitics.gestionentreprisesusers.data.dto.request.InfosPersoAvecCompteRequest;
import com.safelogisitics.gestionentreprisesusers.data.dto.request.InfosPersoRequest;
import com.safelogisitics.gestionentreprisesusers.data.dto.request.RegisterRequest;
import com.safelogisitics.gestionentreprisesusers.data.dto.request.UpdateInfosPersoRequest;
import com.safelogisitics.gestionentreprisesusers.data.dto.response.EntrepriseInfosResponse;
import com.safelogisitics.gestionentreprisesusers.data.dto.response.JwtResponse;
import com.safelogisitics.gestionentreprisesusers.data.dto.response.UserInfosResponse;
import com.safelogisitics.gestionentreprisesusers.data.model.AffectationEquipement;
import com.safelogisitics.gestionentreprisesusers.data.model.Compte;
import com.safelogisitics.gestionentreprisesusers.data.model.InfosPerso;
import com.safelogisitics.gestionentreprisesusers.data.model.enums.ECompteType;
import com.safelogisitics.gestionentreprisesusers.data.model.enums.EServiceConciergeType;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface InfosPersoService {

  public UserInfosResponse getUserInfos();

  public EntrepriseInfosResponse getEntrepriseInfos();

  public UserInfosResponse getUserInfos(String id);

  public Optional<InfosPerso> findInfosPersoById(String id);

  public Map<String, String> verifierAbonnement(String telephone);

  public Object findInfosPersoByCompteId(String id);

  public Collection<Object> findAllInfosPersoByCompteIds(Set<String> ids);

  public Optional<InfosPerso> findByCompteId(String id);

  public Optional<InfosPerso> findByEmailOrTelephone(String emailOrTelephone);

  public Collection<UserInfosResponse> findByCustomSearch(String prenom, String nom, String email, String telephone, String numeroCarte, ECompteType compteType, EServiceConciergeType serviceConciergeType);

  public Page<InfosPerso> getInfosPersos(Pageable pageable); // list infosPerso sans compte

  public Page<UserInfosResponse> getInfosPersos(ECompteType type, Pageable pageable); // list infosPerso avec compte

  public Page<UserInfosResponse> getInfosPersos(ECompteType type, Boolean isAbonnee, Pageable pageable); // list infosPerso avec compte
  
  public InfosPerso createInfosPerso(InfosPersoRequest infosPersoRequest); // Ajout d'un infos perso sans compte

  public InfosPerso updateInfosPerso(String id, InfosPersoRequest infosPersoRequest); // Ajout d'un infos perso sans compte

  public InfosPerso createOrUpdateCompteAdministrateur(String id, InfosPersoAvecCompteRequest request); // Création ou Modification d'un infosPerso avec compte administrateur

  public void deleteCompteAdministrateur(String infosPersoId); // Suppression d'un admnistrateur (Soft delete)

  public InfosPerso createOrUpdateCompteAgent(String id, InfosPersoAvecCompteRequest request); // Création et modification d'un infosPerso avec compte agent (Coursier)

  public InfosPerso equiperAgent(String id, Set<AffectationEquipement> affectationEquipements);

  public InfosPerso affecterMoyenTransportAgent(String id, String moyenTransportId);

  public void deleteCompteAgent(String infosPersoId); // Suppression d'un agent (coursier) (Soft delete)

  public InfosPerso createOrUpdateComptePrestataire(String id, InfosPersoAvecCompteRequest request); // Création et modification d'un infosPerso avec compte agent (Coursier)

  public void deleteComptePrestataire(String infosPersoId); // Suppression d'un agent (coursier) (Soft delete)

  public Page<UserInfosResponse> getEntrepriseUsers(String entrepriseId, Map<String, String> requestParams, Pageable pageable);

  public InfosPerso createOrUpdateCompteEntreprise(String id, InfosPersoAvecCompteRequest request);

  public void deleteCompteEntreprise(String infosPersoId);

  public UserInfosResponse createCompteClient(RegisterRequest request); // Création d'un infosPerso avec compte particulier (Client)

  public UserInfosResponse getCompteClient(String id); // get infosPerso avec compte

  public UserInfosResponse updateCompteClient(String id, UpdateInfosPersoRequest request); // Modification d'un infosPerso avec compte particulier (Client)

  public void deleteCompteClient(String infosPersoId); // Suppression d'un agent (coursier) (Soft delete)

  public JwtResponse clientRegistration(RegisterRequest request); // Inscription en tant que client (particulier)

  public InfosPerso newEnrollment(EnrollmentRequest enrollmentRequest);

  public Page<?> getMyEnrollments(Pageable pageable);

  public UserInfosResponse updateUserInfos(UpdateInfosPersoRequest request); // Inscription en tant que client (particulier)

  public UserInfosResponse updateUserInfos(UpdateInfosPersoRequest request, String id);

  public Collection<Compte> getInfosPersoComptes(String id);

  // public void sendEmailRegistration(User user); // Suppression d'un agent (coursier) (Soft delete)
}
