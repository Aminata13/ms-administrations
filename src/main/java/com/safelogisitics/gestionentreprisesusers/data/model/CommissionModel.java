package com.safelogisitics.gestionentreprisesusers.data.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.safelogisitics.gestionentreprisesusers.data.dto.request.CommissionRequestDto;
import com.safelogisitics.gestionentreprisesusers.data.enums.EServiceType;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "commissions")
public class CommissionModel extends AuditMetadata {
  
  @Id
  private String id;

  @Indexed
  @Field(name = "numero")
  private String numero;

  @Indexed
  @Field(name = "service")
  private EServiceType service;

  @Indexed
  @Field(name = "commandeId")
  private String commandeId;

  @Field(name = "montant")
  private BigDecimal montant;

  @Indexed
  @Field(name = "responsableId")
  private String responsableId;

  @Field(name = "payer")
  private boolean payer = false;

  @Field(name = "paiementId")
  private String paiementId;

  @Field(name = "clientId")
  private String clientId;

  public CommissionModel() {}

  public CommissionModel(CommissionRequestDto commissionRequest) {
    this.setData(commissionRequest);
  }

  public void setData(CommissionRequestDto commissionRequest) {
    this.service = commissionRequest.getService();
    this.commandeId = commissionRequest.getCommandeId();
    this.numero = commissionRequest.getNumero();
    this.responsableId = commissionRequest.getResponsableId();
    this.clientId = commissionRequest.getClientId();
  }

  public String getId() {
    return this.id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getNumero() {
    return this.numero;
  }

  public void setNumero(String numero) {
    this.numero = numero;
  }

  public EServiceType getService() {
    return this.service;
  }

  public void setService(EServiceType service) {
    this.service = service;
  }

  public String getCommandeId() {
    return this.commandeId;
  }

  public void setCommandeId(String commandeId) {
    this.commandeId = commandeId;
  }

  public BigDecimal getMontant() {
    return this.montant;
  }

  public void setMontant(BigDecimal montant) {
    this.montant = montant;
  }

  public String getResponsableId() {
    return this.responsableId;
  }

  public void setResponsableId(String responsableId) {
    this.responsableId = responsableId;
  }

  public String getClientId() {
    return clientId;
  }

  public void setClientId(String clientId) {
    this.clientId = clientId;
  }

  public boolean isPayer() {
    return this.payer;
  }

  public String getPaiementId() {
    return this.paiementId;
  }

  public void calculCommission(BigDecimal prix) {
    BigDecimal prixHt = prix.divide(BigDecimal.valueOf(1.18), 2, RoundingMode.HALF_UP);
    BigDecimal valeurCommission = prixHt.multiply(BigDecimal.valueOf(0.07));
    this.montant = valeurCommission.setScale(0, RoundingMode.HALF_UP);
  }

  public void payer(String paiementId) {
    this.paiementId = paiementId;
    this.payer = true;
  }
  
}
