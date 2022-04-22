package com.safelogisitics.gestionentreprisesusers.data.shared.dto;

import java.util.Set;

import com.safelogisitics.gestionentreprisesusers.data.model.Abonnement;
import com.safelogisitics.gestionentreprisesusers.data.model.Compte;
import com.safelogisitics.gestionentreprisesusers.data.model.InfosPersoModel;

public class SharedInfosPersoRequestDto {
  
  private InfosPersoModel infosPerso;

  private Abonnement abonnement;

  private Set<Compte> comptes;

  public SharedInfosPersoRequestDto() {}

  public InfosPersoModel getInfosPerso() {
    return this.infosPerso;
  }

  public void setInfosPerso(InfosPersoModel infosPerso) {
    this.infosPerso = infosPerso;
  }

  public Abonnement getAbonnement() {
    return this.abonnement;
  }

  public void setAbonnement(Abonnement abonnement) {
    this.abonnement = abonnement;
  }

  public Set<Compte> getComptes() {
    return this.comptes;
  }

  public void setComptes(Set<Compte> comptes) {
    this.comptes = comptes;
  }


}
