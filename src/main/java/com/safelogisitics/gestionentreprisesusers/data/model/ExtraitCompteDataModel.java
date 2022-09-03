package com.safelogisitics.gestionentreprisesusers.data.model;

import com.safelogisitics.gestionentreprisesusers.data.enums.EServiceType;
import com.safelogisitics.gestionentreprisesusers.data.enums.ETransactionAction;
import com.safelogisitics.gestionentreprisesusers.data.enums.ETransactionType;
import com.safelogisitics.gestionentreprisesusers.data.shared.model.subclass.*;
import com.safelogisitics.gestionentreprisesusers.data.shared.model.subclass.Abonnement;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ExtraitCompteDataModel {

    private Abonnement abonnement;

    private ETransactionAction action;

    private String numeroCommande;

    private EServiceType service;

    private BigDecimal montant = BigDecimal.valueOf(0);

    private BigDecimal nouveauSolde = BigDecimal.valueOf(0);

    private LocalDateTime dateApprobation;

    private Commande commande;


    public Abonnement getAbonnement() {
        return abonnement;
    }

    public void setAbonnement(Abonnement abonnement) {
        this.abonnement = abonnement;
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

    public BigDecimal getMontant() {
        return montant;
    }

    public void setMontant(BigDecimal montant) {
        this.montant = montant;
    }

    public BigDecimal getNouveauSolde() {
        return nouveauSolde;
    }

    public void setNouveauSolde(BigDecimal nouveauSolde) {
        this.nouveauSolde = nouveauSolde;
    }

    public LocalDateTime getDateApprobation() {
        return dateApprobation;
    }

    public void setDateApprobation(LocalDateTime dateApprobation) {
        this.dateApprobation = dateApprobation;
    }

    public Commande getCommande() {
        return commande;
    }

    public void setCommande(Commande commande) {
        this.commande = commande;
    }
}
class Commande {

    private String numero;

    private EServiceType type;

    private Demenagement demenagement;

    private Pressing pressing;

    private Bricolage bricolage;

    private Banque banque;

    private Course course;

    private VisiteTechnique visiteTechnique;

    private String clientId;

    private String typeService;

    private TypeLivraison typeLivraison;

    private PointGeographique pointDepart;

    private PointGeographique pointArriver;

    private BigDecimal prix = BigDecimal.valueOf(0);

    public Commande() {
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public EServiceType getType() {
        return type;
    }

    public void setType(EServiceType type) {
        this.type = type;
    }

    public Demenagement getDemenagement() {
        return demenagement;
    }

    public void setDemenagement(Demenagement demenagement) {
        this.demenagement = demenagement;
    }

    public Pressing getPressing() {
        return pressing;
    }

    public void setPressing(Pressing pressing) {
        this.pressing = pressing;
    }

    public Bricolage getBricolage() {
        return bricolage;
    }

    public void setBricolage(Bricolage bricolage) {
        this.bricolage = bricolage;
    }

    public Banque getBanque() {
        return banque;
    }

    public void setBanque(Banque banque) {
        this.banque = banque;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public VisiteTechnique getVisiteTechnique() {
        return visiteTechnique;
    }

    public void setVisiteTechnique(VisiteTechnique visiteTechnique) {
        this.visiteTechnique = visiteTechnique;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getTypeService() {
        return typeService;
    }

    public void setTypeService(String typeService) {
        this.typeService = typeService;
    }

    public TypeLivraison getTypeLivraison() {
        return typeLivraison;
    }

    public void setTypeLivraison(TypeLivraison typeLivraison) {
        this.typeLivraison = typeLivraison;
    }

    public PointGeographique getPointDepart() {
        return pointDepart;
    }

    public void setPointDepart(PointGeographique pointDepart) {
        this.pointDepart = pointDepart;
    }

    public PointGeographique getPointArriver() {
        return pointArriver;
    }

    public void setPointArriver(PointGeographique pointArriver) {
        this.pointArriver = pointArriver;
    }

    public BigDecimal getPrix() {
        return prix;
    }

    public void setPrix(BigDecimal prix) {
        this.prix = prix;
    }
}