package com.safelogisitics.gestionentreprisesusers.service;

import java.util.Optional;

import com.safelogisitics.gestionentreprisesusers.model.Abonnement;
import com.safelogisitics.gestionentreprisesusers.model.Compte;
import com.safelogisitics.gestionentreprisesusers.model.TypeAbonnement;
import com.safelogisitics.gestionentreprisesusers.payload.request.AbonnementRequest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AbonnementService {
  
  public Page<Abonnement> getAbonnements(Pageable pageable);

  public Page<Abonnement> getAbonnements(TypeAbonnement typeAbonnement, Pageable pageable);

  public Optional<Abonnement> getAbonnementByCompteClient(Compte client);

  public Page<Abonnement> getAbonnementByCompteCreateur(Compte createur, Pageable pageable);

  public Optional<Abonnement> getAbonnementById(String id);
  
  public Abonnement createAbonnement(AbonnementRequest abonnementRequest);

  public Abonnement updateAbonnement(String id, AbonnementRequest abonnementRequest);

  public void deleteAbonnement(String id);
}
