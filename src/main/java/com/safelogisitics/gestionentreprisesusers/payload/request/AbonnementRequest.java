package com.safelogisitics.gestionentreprisesusers.payload.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class AbonnementRequest {

  @NotBlank
  private String typeAbonnementId;

  @NotBlank
  private String infosPersoId;

  @NotNull
  private int statut;

  private String numeroCarte;

  private boolean carteBloquer = false;

  public String getTypeAbonnementId() {
    return this.typeAbonnementId;
  }

  public String getInfosPersoId() {
    return this.infosPersoId;
  }

  public int getStatut() {
    return this.statut;
  }

  public String getNumeroCarte() {
    return this.numeroCarte;
  }

  public boolean isCarteBloquer() {
    return this.carteBloquer;
  }
}