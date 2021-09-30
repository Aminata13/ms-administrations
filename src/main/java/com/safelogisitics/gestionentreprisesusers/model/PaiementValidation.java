package com.safelogisitics.gestionentreprisesusers.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.safelogisitics.gestionentreprisesusers.model.enums.EServiceType;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;


@Document(collection = "paiementValidations")
public class PaiementValidation extends AuditMetadata {

  @Id
  private String id;

  @NotBlank(message = "Le numéro de la carte est obligatoire.")
  @Field(value = "numeroCarte")
  private String numeroCarte;

  @NotBlank(message = "Le numéro de la commande est obligatoire.")
  @Field(value = "numeroCommande")
  private String numeroCommande;

  @NotBlank(message = "Le type de service est obligatoire.")
  @Field(value = "service")
  private EServiceType service;

  @NotBlank(message = "Le montant est obligatoire.")
  @Field(value = "montant")
  private BigDecimal montant;

  @JsonIgnore
  @Field(value = "codeValidation")
  private String codeValidation;

  @Field(value = "approbation")
  private boolean approbation;

  @JsonIgnore
  @Field(value = "dateCreation")
  private LocalDateTime dateCreation;

  public PaiementValidation() {
    this.approbation = false;
    this.dateCreation = LocalDateTime.now();
  }

  public PaiementValidation(String numeroCarte, String numeroCommande, EServiceType service, BigDecimal montant, String codeValidation) {
    this.numeroCarte = numeroCarte;
    this.numeroCommande = numeroCommande;
    this.service = service;
    this.montant = montant;
    this.codeValidation = codeValidation;
    this.approbation = false;
    this.dateCreation = LocalDateTime.now();
  }

  public String getId() {
    return this.id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getNumeroCarte() {
    return this.numeroCarte;
  }

  public void setNumeroCarte(String numeroCarte) {
    this.numeroCarte = numeroCarte;
  }

  public String getNumeroCommande() {
    return this.numeroCommande;
  }

  public void setNumeroCommande(String numeroCommande) {
    this.numeroCommande = numeroCommande;
  }

  public EServiceType getService() {
    return this.service;
  }

  public void setService(EServiceType service) {
    this.service = service;
  }

  public BigDecimal getMontant() {
    return this.montant;
  }

  public void setMontant(BigDecimal montant) {
    this.montant = montant;
  }

  public String getCodeValidation() {
    return this.codeValidation;
  }

  public void setCodeValidation(String codeValidation) {
    this.codeValidation = codeValidation;
  }

  public boolean getApprobation() {
    return this.approbation;
  }

  public void setApprobation(boolean approbation) {
    this.approbation = approbation;
  }

  public LocalDateTime getDateCreation() {
    return this.dateCreation;
  }

  public void setDateCreation(LocalDateTime dateCreation) {
    this.dateCreation = dateCreation;
  }
}
