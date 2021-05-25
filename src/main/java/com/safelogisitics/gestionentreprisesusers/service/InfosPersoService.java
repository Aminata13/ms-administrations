package com.safelogisitics.gestionentreprisesusers.service;

import java.util.Collection;
import java.util.Optional;

import com.safelogisitics.gestionentreprisesusers.model.Compte;
import com.safelogisitics.gestionentreprisesusers.model.InfosPerso;
import com.safelogisitics.gestionentreprisesusers.model.enums.ECompteType;
import com.safelogisitics.gestionentreprisesusers.payload.request.InfosPersoAvecCompteRequest;
import com.safelogisitics.gestionentreprisesusers.payload.request.InfosPersoRequest;
import com.safelogisitics.gestionentreprisesusers.payload.request.RegisterRequest;
import com.safelogisitics.gestionentreprisesusers.payload.response.JwtResponse;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface InfosPersoService {

  public Optional<InfosPerso> findInfosPersoById(String id); 

  public Page<InfosPerso> getInfosPersos(Pageable pageable); // list infosPerso sans compte

  public Page<InfosPerso> getInfosPersos(ECompteType type, Pageable pageable); // list infosPerso avec compte
  
  public InfosPerso createInfosPerso(InfosPersoRequest infosPersoRequest); // Ajout d'un infos perso sans compte

  public InfosPerso updateInfosPerso(String id, InfosPersoRequest infosPersoRequest); // Ajout d'un infos perso sans compte

  public InfosPerso createOrUpdateCompteAdministrateur(InfosPersoAvecCompteRequest request); // Création et modification d'un infosPerso avec compte administrateur

  public void deleteCompteAdministrateur(String infosPersoId); // 

  public JwtResponse clientRegistration(RegisterRequest request); // Inscription en tant que client (particulier)

  public Collection<Compte> getInfosPersoComptes(String id);
}
