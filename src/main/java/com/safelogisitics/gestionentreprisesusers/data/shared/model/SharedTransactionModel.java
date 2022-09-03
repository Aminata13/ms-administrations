package com.safelogisitics.gestionentreprisesusers.data.shared.model;

import com.safelogisitics.gestionentreprisesusers.data.enums.EServiceType;
import com.safelogisitics.gestionentreprisesusers.data.enums.ETransactionAction;
import com.safelogisitics.gestionentreprisesusers.data.enums.ETransactionType;
import com.safelogisitics.gestionentreprisesusers.data.shared.model.subclass.Abonnement;
import com.safelogisitics.gestionentreprisesusers.data.shared.model.subclass.Compte;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Document(collection = "transactions")
@TypeAlias("SharedTransactionModel")
public class SharedTransactionModel {

    @Id
    private String id;

    private Abonnement abonnement;

    private String reference;

    private ETransactionType type = ETransactionType.SOLDE_COMPTE;

    private ETransactionAction action;

    private String numeroCommande;

    private EServiceType service;

    private Compte compteCreateur;

    private Compte approbateur;

    private BigDecimal montant = BigDecimal.valueOf(0);

    private long points = 0;

    private BigDecimal nouveauSolde = BigDecimal.valueOf(0);

    private long totalPoints = 0;

    private int approbation;

    private LocalDateTime dateCreation;

    private LocalDateTime dateApprobation;

    public SharedTransactionModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Abonnement getAbonnement() {
        return abonnement;
    }

    public void setAbonnement(Abonnement abonnement) {
        this.abonnement = abonnement;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public ETransactionType getType() {
        return type;
    }

    public void setType(ETransactionType type) {
        this.type = type;
    }

    public ETransactionAction getAction() {
        return action;
    }

    public void setAction(ETransactionAction action) {
        this.action = action;
    }

    public String getNumeroCommande() {
        return numeroCommande;
    }

    public void setNumeroCommande(String numeroCommande) {
        this.numeroCommande = numeroCommande;
    }

    public EServiceType getService() {
        return service;
    }

    public void setService(EServiceType service) {
        this.service = service;
    }

    public Compte getCompteCreateur() {
        return compteCreateur;
    }

    public void setCompteCreateur(Compte compteCreateur) {
        this.compteCreateur = compteCreateur;
    }

    public Compte getApprobateur() {
        return approbateur;
    }

    public void setApprobateur(Compte approbateur) {
        this.approbateur = approbateur;
    }

    public BigDecimal getMontant() {
        return montant;
    }

    public void setMontant(BigDecimal montant) {
        this.montant = montant;
    }

    public long getPoints() {
        return points;
    }

    public void setPoints(long points) {
        this.points = points;
    }

    public BigDecimal getNouveauSolde() {
        return nouveauSolde;
    }

    public void setNouveauSolde(BigDecimal nouveauSolde) {
        this.nouveauSolde = nouveauSolde;
    }

    public long getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(long totalPoints) {
        this.totalPoints = totalPoints;
    }

    public int getApprobation() {
        return approbation;
    }

    public void setApprobation(int approbation) {
        this.approbation = approbation;
    }

    public LocalDateTime getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }

    public LocalDateTime getDateApprobation() {
        return dateApprobation;
    }

    public void setDateApprobation(LocalDateTime dateApprobation) {
        this.dateApprobation = dateApprobation;
    }
}
