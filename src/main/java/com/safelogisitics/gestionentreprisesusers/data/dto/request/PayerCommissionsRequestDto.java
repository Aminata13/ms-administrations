package com.safelogisitics.gestionentreprisesusers.data.dto.request;

import java.util.Set;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.safelogisitics.gestionentreprisesusers.data.enums.EPaiementMethode;

public class PayerCommissionsRequestDto {
  
  @NotNull(message = "Le méthode de paiement est obligatoire.")
  private EPaiementMethode paiementMethode;

  @NotNull(message = "Les ids des commandes sont obligatoires.")
  private Set<String> ids;

  @NotBlank(message = "L'id du responsable est obligatoire.")
  private String responsableId;

  public PayerCommissionsRequestDto() {}

  public EPaiementMethode getPaiementMethode() {
    return this.paiementMethode;
  }

  public void setPaiementMethode(EPaiementMethode paiementMethode) {
    this.paiementMethode = paiementMethode;
  }

  public Set<String> getIds() {
    return this.ids;
  }

  public void setIds(Set<String> ids) {
    this.ids = ids;
  }

  public String getResponsableId() {
    return this.responsableId;
  }

  public void setResponsableId(String responsableId) {
    this.responsableId = responsableId;
  }

}
