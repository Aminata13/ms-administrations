package com.safelogisitics.gestionentreprisesusers.payload.request;

import java.util.Collection;
import java.util.Map;

import com.safelogisitics.gestionentreprisesusers.model.enums.ETypeProspect;

public class ProspectRequest {

  private ETypeProspect type;

  private EntrepriseProspectRequest infosEntreprise;

  private InfosPersoRequest infosParticulier;

  private Map<String, String> personneRencontrer;

  private Collection<String> rapportVisites;

  private int niveauAvancement;

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

}
