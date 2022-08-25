package com.safelogisitics.gestionentreprisesusers.data.shared.model.subclass;

import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

public class PaiementFacture {

  @Field(value = "referencePaiement")
  private String referencePaiement;

  @Field(value = "montantPayer")
  private BigDecimal montantPayer;

  @Field(value = "uploadedFiles")
  private Set<String> uploadedFiles;

  @Field(value = "datePaiement")
  private LocalDateTime datePaiement;

  @Field(value = "auteurId")
  private String auteurId;

  public PaiementFacture() {
    this.datePaiement = LocalDateTime.now();
  }

  public PaiementFacture(String referencePaiement, BigDecimal montantPayer, Set<String> uploadedFiles) {
    this.referencePaiement = referencePaiement;
    this.montantPayer = montantPayer;
    this.uploadedFiles = uploadedFiles;
    this.datePaiement = LocalDateTime.now();
  }

  public String getReferencePaiement() {
    return this.referencePaiement;
  }

  public void setReferencePaiement(String referencePaiement) {
    this.referencePaiement = referencePaiement;
  }

  public BigDecimal getMontantPayer() {
    return this.montantPayer;
  }

  public void setMontantPayer(BigDecimal montantPayer) {
    this.montantPayer = montantPayer;
  }

  public Set<String> getUploadedFiles() {
    return this.uploadedFiles;
  }

  public void setUploadedFiles(Set<String> uploadedFiles) {
    this.uploadedFiles = uploadedFiles;
  }

  public LocalDateTime getDatePaiement() {
    return this.datePaiement;
  }

  public void setDatePaiement(LocalDateTime datePaiement) {
    this.datePaiement = datePaiement;
  }

  public String getAuteurId() {
    return this.auteurId;
  }

  public void setAuteurId(String auteurId) {
    this.auteurId = auteurId;
  }
}
