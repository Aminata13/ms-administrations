package com.safelogisitics.gestionentreprisesusers.data.model;

import java.math.BigDecimal;
import java.util.Set;

import com.safelogisitics.gestionentreprisesusers.data.enums.EPaiementMethode;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "paiementCommissions")
public class PaiementCommissionModel extends AuditMetadata {
  
  @Id
  private String id;

  @Field(name = "montant")
  private BigDecimal montant;

  @Field(name = "commmandeIds")
  private Set<String> commmandeIds;

  @Field(name = "responsableId")
  private String responsableId;

  @Field(name = "auteurId")
  private String auteurId;

  @Field(name = "paiementMethode")
  private EPaiementMethode paiementMethode;

  public PaiementCommissionModel() {}

  public PaiementCommissionModel(BigDecimal montant, Set<String> commmandeIds, String responsableId, String auteurId, EPaiementMethode paiementMethode) {
    this.montant = montant;
    this.commmandeIds = commmandeIds;
    this.responsableId = responsableId;
    this.auteurId = auteurId;
    this.paiementMethode = paiementMethode;
  }

  public String getId() {
    return this.id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public BigDecimal getMontant() {
    return this.montant;
  }

  public void setMontant(BigDecimal montant) {
    this.montant = montant;
  }

  public Set<String> getCommmandeIds() {
    return this.commmandeIds;
  }

  public void setCommmandeIds(Set<String> commmandeIds) {
    this.commmandeIds = commmandeIds;
  }

  public String getResponsableId() {
    return this.responsableId;
  }

  public void setResponsableId(String responsableId) {
    this.responsableId = responsableId;
  }

  public String getAuteurId() {
    return this.auteurId;
  }

  public void setAuteurId(String auteurId) {
    this.auteurId = auteurId;
  }

  public EPaiementMethode getPaiementMethode() {
    return this.paiementMethode;
  }

  public void setPaiementMethode(EPaiementMethode paiementMethode) {
    this.paiementMethode = paiementMethode;
  }

}
