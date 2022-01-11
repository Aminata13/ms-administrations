package com.safelogisitics.gestionentreprisesusers.service;

import java.util.Map;
import java.util.Optional;

import com.safelogisitics.gestionentreprisesusers.data.dto.request.AbonnementRequest;
import com.safelogisitics.gestionentreprisesusers.data.model.Abonnement;
import com.safelogisitics.gestionentreprisesusers.data.model.Compte;
import com.safelogisitics.gestionentreprisesusers.data.model.TypeAbonnement;
import com.safelogisitics.gestionentreprisesusers.data.model.enums.ECompteType;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AbonnementService {
  
  public Page<Abonnement> getAbonnements(Pageable pageable);

  public Page<Abonnement> findByCustomSearch(Map<String,String> parameters, Pageable pageable);

  public Page<Abonnement> getAbonnements(TypeAbonnement typeAbonnement, Pageable pageable);

  public Optional<Abonnement> getAbonnementByCompteClient(Compte client);

  public Optional<Abonnement> getByCompteClientInfosPersoId(String infosPerso);

  public Optional<Abonnement> getByNumeroCarte(String numeroCarte);

  public Object getCustomResponseByNumeroCarte(String numeroCarte);

  public Page<Abonnement> getAbonnementByCompteCreateur(Compte createur, Pageable pageable);

  public Optional<Abonnement> getAbonnementById(String id);
  
  public Abonnement createAbonnement(AbonnementRequest abonnementRequest, ECompteType typeCompteCreateur);

  public Abonnement changerAbonnement(String id, AbonnementRequest abonnementRequest, ECompteType typeCompteCreateur);

  public void deleteAbonnement(String id);
}
