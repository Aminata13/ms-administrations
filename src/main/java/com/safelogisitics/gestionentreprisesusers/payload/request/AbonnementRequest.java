package com.safelogisitics.gestionentreprisesusers.payload.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class AbonnementRequest {

  @NotBlank
  private String typeAbonnementId;

  private String infosPersoId;

  private String entrepriseId;

  @NotNull
  private int statut;

  @NotBlank
  private String numeroCarte;

  private boolean carteBloquer = false;

  public AbonnementRequest(String typeAbonnementId, String infosPersoId, String entrepriseId, int statut, String numeroCarte, boolean carteBloquer) {
    this.typeAbonnementId = typeAbonnementId;
    this.infosPersoId = infosPersoId;
    this.entrepriseId = entrepriseId;
    this.statut = statut;
    this.numeroCarte = numeroCarte;
    this.carteBloquer = carteBloquer;
  }

  public String getTypeAbonnementId() {
    return this.typeAbonnementId;
  }

  public String getInfosPersoId() {
    return this.infosPersoId;
  }

  public String getEntrepriseId() {
    return this.entrepriseId;
  }

  public int getStatut() {
    return this.statut;
  }

  public String getNumeroCarte() {
    return this.numeroCarte.replaceAll("\\D+","");
  }

  public boolean isCarteBloquer() {
    return this.carteBloquer;
  }
}
