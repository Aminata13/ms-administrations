package com.safelogisitics.gestionentreprisesusers.payload.request;

import java.util.Set;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import com.safelogisitics.gestionentreprisesusers.model.enums.ETypeEntreprise;
import com.safelogisitics.gestionentreprisesusers.model.enums.ETypePartenariat;

public class EntrepriseRequest {
  
  @NotBlank
  private ETypeEntreprise typeEntreprise;

  @NotEmpty
  private Set<ETypePartenariat> typePartenariats;

  @NotBlank
  private String denomination;

  private String ninea;

  private String raisonSociale;

  @NotBlank
  private InfosPersoRequest gerant;

  @NotBlank
  private String email;

  @NotBlank
  private String telephone;

  private String adresse;

  public ETypeEntreprise getTypeEntreprise() {
    return this.typeEntreprise;
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

  public InfosPersoRequest getGerant() {
    return this.gerant;
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
}