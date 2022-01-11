package com.safelogisitics.gestionentreprisesusers.data.dto.response;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.safelogisitics.gestionentreprisesusers.data.enums.ETypePartenariat;
import com.safelogisitics.gestionentreprisesusers.data.model.Abonnement;
import com.safelogisitics.gestionentreprisesusers.data.model.Entreprise;
import com.safelogisitics.gestionentreprisesusers.data.model.InfosPerso;

public class EntrepriseInfosResponse {
  
  private String id;

  private String typeEntreprise;

  private String domaineActivite;

  private Set<ETypePartenariat> typePartenariats;

  private String denomination;

  private String ninea;

  private String raisonSociale;

  private InfosPerso gerant;

  private String email;

  private String telephone;

  private String adresse;

  private String numeroCarte;

  private Abonnement abonnement;

  public EntrepriseInfosResponse(Entreprise entreprise, InfosPerso gerant, Abonnement abonnement) {
    this.id = entreprise.getId();
    this.typeEntreprise = entreprise.getTypeEntreprise();
    this.domaineActivite = entreprise.getDomaineActivite();
    this.typePartenariats = entreprise.getTypePartenariats();
    this.denomination = entreprise.getDenomination();
    this.ninea = entreprise.getNinea();
    this.raisonSociale = entreprise.getRaisonSociale();
    this.email = entreprise.getEmail();
    this.telephone = entreprise.getTelephone();
    this.adresse = entreprise.getAdresse();
    this.numeroCarte = entreprise.getNumeroCarte();
    this.gerant = gerant;
    this.abonnement = abonnement;
  }

  public String getId() {
    return this.id;
  }

  public String getTypeEntreprise() {
    return this.typeEntreprise;
  }

  public String getDomaineActivite() {
    return this.domaineActivite;
  }

  public Set<ETypePartenariat> getTypePartenariats() {
    return this.typePartenariats;
  }

  public String getDenomination() {
    return this.denomination;
  }

  public String getNinea() {
    return this.ninea;
  }

  public String getRaisonSociale() {
    return this.raisonSociale;
  }

  public Object getGerant() {
    return this.gerant != null ? this.gerant.getDefaultFields() : null;
  }

  public String getEmail() {
    return this.email;
  }

  public String getTelephone() {
    return this.telephone;
  }

  public String getAdresse() {
    return this.adresse;
  }

  public String getNumeroCarte() {
    return this.numeroCarte;
  }

  public Map<String, Object> getAbonnement() {
    Map<String, Object> _customRoleField = new LinkedHashMap<>();

    if (this.abonnement == null)
      return _customRoleField;

    _customRoleField.put("id", this.abonnement.getId());
    _customRoleField.put("typeAbonnement", this.abonnement.getTypeAbonnement());
    _customRoleField.put("numeroCarte", this.abonnement.getNumeroCarte());
    _customRoleField.put("carteBloquer", this.abonnement.isCarteBloquer());
    _customRoleField.put("solde", this.abonnement.getSolde());
    _customRoleField.put("prixCarte", this.abonnement.getPrixCarte());
    _customRoleField.put("depotInitial", this.abonnement.getDepotInitial());
    _customRoleField.put("statut", this.abonnement.getStatut());
    _customRoleField.put("dateCreation", this.abonnement.getDateCreation());

    return _customRoleField;
  }
}
