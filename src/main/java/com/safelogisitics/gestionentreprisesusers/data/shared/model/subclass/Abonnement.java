package com.safelogisitics.gestionentreprisesusers.data.shared.model.subclass;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.safelogisitics.gestionentreprisesusers.data.shared.model.SharedEntrepriseModel;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Abonnement {

    private String id;

    private TypeAbonnement typeAbonnement;

    private Compte compteClient;

    private SharedEntrepriseModel entreprise;

    private Compte compteCreateur;

    private String responsableId;

    private String numeroCarte;

    private boolean carteBloquer;

    private BigDecimal solde;

    private long pointGratuites;

    private BigDecimal prixCarte;

    private BigDecimal depotInitial;

    private int statut;

    @JsonIgnore
    private boolean deleted;

    private LocalDate dateCreation;

    public Abonnement() {
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

    public SharedEntrepriseModel getEntreprise() {
        return this.entreprise;
    }

    public void setEntreprise(SharedEntrepriseModel entreprise) {
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
}
