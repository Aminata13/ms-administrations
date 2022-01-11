package com.safelogisitics.gestionentreprisesusers.data.dto.request;

import java.util.Collection;
import java.util.Map;

import com.safelogisitics.gestionentreprisesusers.data.model.enums.ETypeProspect;

public class ProspectRequest {

  private ETypeProspect type;

  private EntrepriseProspectRequest infosEntreprise;

  private InfosPersoRequest infosParticulier;

  private Map<String, String> personneRencontrer;

  private Collection<String> rapportVisites;

  private int niveauAvancement;

  private String typeAbonnementId;

  private String numeroCarte;

  public ETypeProspect getType() {
    return this.type;
  }

  public EntrepriseProspectRequest getInfosEntreprise() {
    return this.infosEntreprise;
  }

  public InfosPersoRequest getInfosParticulier() {
    return this.infosParticulier;
  }

  public Map<String, String> getPersonneRencontrer() {
    return this.personneRencontrer;
  }

  public Collection<String> getRapportVisites() {
    return this.rapportVisites;
  }

  public int getNiveauAvancement() {
    return this.niveauAvancement;
  }

  public String getTypeAbonnementId() {
    return this.typeAbonnementId;
  }

  public void setTypeAbonnementId(String typeAbonnementId) {
    this.typeAbonnementId = typeAbonnementId;
  }

  public String getNumeroCarte() {
    return this.numeroCarte;
  }

  public void setNumeroCarte(String numeroCarte) {
    this.numeroCarte = numeroCarte;
  }

}
