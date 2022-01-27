package com.safelogisitics.gestionentreprisesusers.data.dto.request;

import java.util.Set;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.safelogisitics.gestionentreprisesusers.data.enums.ETypePartenariat;

public class EntrepriseRequest {
  
  @NotBlank
  private String typeEntreprise;

  @NotBlank
  private String domaineActivite;

  @NotEmpty
  private Set<ETypePartenariat> typePartenariats;

  @NotBlank
  private String denomination;

  private String ninea;

  private String raisonSociale;

  @NotNull
  private InfosPersoRequest gerant;

  @NotBlank
  private String email;

  @NotBlank
  private String telephone;

  private String adresse;

  @NotBlank
  private String numeroCarte;

  private String logo;

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

  public InfosPersoRequest getGerant() {
    return this.gerant;
  }

  public String getEmail() {
    return this.email;
  }

  public String getTelephone() {
    return this.telephone.replaceAll("\\s+","");
  }

  public String getAdresse() {
    return this.adresse;
  }

  public String getNumeroCarte() {
    return this.numeroCarte.replaceAll("\\D+","");
  }

  public String getLogo() {
    return logo;
  }

  public void setLogo(String logo) {
    this.logo = logo;
  }
}
