package com.safelogisitics.gestionentreprisesusers.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class AffectationEquipement extends AuditMetadata {

  @NotBlank
  private String idEquipement;

  @NotNull
  private double quantite;

  private Map<String, String> specificites = new HashMap<>();

  public String getIdEquipement() {
    return this.idEquipement;
  }

  public void setIdEquipement(String idEquipement) {
    this.idEquipement = idEquipement;
  }

  public double getQuantite() {
    return this.quantite;
  }

  public void setQuantite(double quantite) {
    this.quantite = quantite;
  }

  public Map<String, String> getSpecificites() {
    return this.specificites;
  }

  public void setSpecificites(Map<String, String> specificites) {
    this.specificites = specificites;
  }

  public int hashCode() {
    return Objects.hash(idEquipement, 1000);
  }

  public boolean equals(Object obj) {
    if (obj instanceof AffectationEquipement) {
        AffectationEquipement pp = (AffectationEquipement) obj;
        return (pp.hashCode() == this.hashCode());
    } else {
        return false;
    }
  }
}
