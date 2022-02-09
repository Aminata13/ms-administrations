package com.safelogisitics.gestionentreprisesusers.data.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import com.safelogisitics.gestionentreprisesusers.data.enums.EClientType;
import com.safelogisitics.gestionentreprisesusers.data.enums.ECompteType;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "abonnements")
public class Abonnement extends AuditMetadata {

  @Id
  private String id;

  @Field(value = "typeAbonnement")
  private TypeAbonnement typeAbonnement;

  @Field(value = "compteClient")
  private Compte compteClient;

  @Field(value = "entreprise")
  private Entreprise entreprise;

  @Field(value = "compteCreateur")
  private Compte compteCreateur;

  @Field(value = "responsableId")
  private String responsableId;

  @Field(value = "numeroCarte")
  private String numeroCarte;

  @Field(value = "carteBloquer")
  private boolean carteBloquer;

  @Field(value = "solde")
  private BigDecimal solde;

  @Field(value = "pointGratuites")
  private long pointGratuites;

  @Field(value = "prixCarte")
  private BigDecimal prixCarte;

  @Field(value = "depotInitial")
  private BigDecimal depotInitial;

  @Field(value = "statut")
  private int statut;

  @JsonIgnore
  @Field(value = "deleted")
  private boolean deleted;

  @Field(value = "dateCreation")
  private LocalDate dateCreation;

  public Abonnement() {
    this.solde = BigDecimal.valueOf(0);
    this.pointGratuites = 0;
    this.deleted = false;
    this.dateCreation = LocalDate.now();
  }

  public Abonnement(TypeAbonnement typeAbonnement, Compte compteClient, Compte compteCreateur, int statut) {
    this.typeAbonnement = typeAbonnement;
    this.compteClient = compteClient;
    this.compteCreateur = compteCreateur;
    this.statut = statut;
    this.solde = BigDecimal.valueOf(0);
    this.pointGratuites = 0;
    this.deleted = false;
    this.dateCreation = LocalDate.now();
  }

  public Abonnement(TypeAbonnement typeAbonnement, Entreprise entreprise, Compte compteCreateur, int statut) {
    this.typeAbonnement = typeAbonnement;
    this.entreprise = entreprise;
    this.compteCreateur = compteCreateur;
    this.statut = statut;
    this.solde = BigDecimal.valueOf(0);
    this.pointGratuites = 0;
    this.deleted = false;
    this.dateCreation = LocalDate.now();
  }

  public String getId() {
    return this.id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public TypeAbonnement getTypeAbonnement() {
    return this.typeAbonnement;
  }

  public void setTypeAbonnement(TypeAbonnement typeAbonnement) {
    this.typeAbonnement = typeAbonnement;
  }

  public Compte getCompteClient() {
    return this.compteClient;
  }

  public void setCompteClient(Compte compteClient) {
    this.compteClient = compteClient;
  }

  public Entreprise getEntreprise() {
    return this.entreprise;
  }

  public void setEntreprise(Entreprise entreprise) {
    this.entreprise = entreprise;
  }

  public Compte getCompteCreateur() {
    return this.compteCreateur;
  }

  public void setCompteCreateur(Compte compteCreateur) {
    this.compteCreateur = compteCreateur;
  }

  public String getResponsableId() {
    return this.responsableId;
  }

  public void setResponsableId(String responsableId) {
    this.responsableId = responsableId;
  }

  public String getNumeroCarte() {
    return this.numeroCarte;
  }

  public void setNumeroCarte(String numeroCarte) {
    this.numeroCarte = numeroCarte;
  }

  public boolean isCarteBloquer() {
    return this.carteBloquer;
  }

  public void setCarteBloquer(boolean carteBloquer) {
    this.carteBloquer = carteBloquer;
  }

  public BigDecimal getSolde() {
    return this.solde;
  }

  public void setSolde(BigDecimal solde) {
    this.solde = solde;
  }

  public void rechargerSolde(BigDecimal montant) {
    this.solde = this.solde.add(montant);
  }

  public void debiterSolde(BigDecimal montant) {
    this.solde = this.solde.subtract(montant);
  }

  public long getPointGratuites() {
    return this.pointGratuites;
  }

  public void rechargerPointGratuites(long points) {
    this.pointGratuites = this.pointGratuites + points;
  }

  public void debiterPointGratuites(long points) {
    this.pointGratuites = this.pointGratuites - points;
  }

  public BigDecimal getPrixCarte() {
    return this.prixCarte;
  }

  public void setPrixCarte(BigDecimal prixCarte) {
    this.prixCarte = prixCarte;
  }

  public BigDecimal getDepotInitial() {
    return this.depotInitial;
  }

  public void setDepotInitial(BigDecimal depotInitial) {
    this.depotInitial = depotInitial;
  }

  public int getStatut() {
    return this.statut;
  }

  public void setStatut(int statut) {
    this.statut = statut;
  }

  public boolean isDeleted() {
    return this.deleted;
  }

  public void setDeleted(boolean deleted) {
    this.deleted = deleted;
  }

  public LocalDate getDateCreation() {
    return this.dateCreation;
  }

  public void setDateCreation(LocalDate dateCreation) {
    this.dateCreation = dateCreation;
  }

  public Object getCustomFields() {
    Map<String,Object> _customRoleField = new LinkedHashMap<>();

    _customRoleField.put("id", getId());
    _customRoleField.put("numeroCarte", getNumeroCarte());
    _customRoleField.put("type", getTypeAbonnement().getLibelle());
    _customRoleField.put("compteClientType", entreprise != null ? ECompteType.COMPTE_ENTREPRISE : ECompteType.COMPTE_PARTICULIER);
    _customRoleField.put("dateAbonnement", getDateCreation().toString());

    return _customRoleField;
  }

}
