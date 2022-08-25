package com.safelogisitics.gestionentreprisesusers.data.model;

import com.safelogisitics.gestionentreprisesusers.data.enums.EServiceType;
import com.safelogisitics.gestionentreprisesusers.data.shared.model.subclass.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ExtraitCompteEntrepriseData implements Comparable<ExtraitCompteEntrepriseData> {

    private String dataType = "Commande"; /*Paiement ou Commande*/

    private BigDecimal montantRestant;

    private BigDecimal montantPayer;

    private LocalDateTime createdDate;

    private String clientId;

    private String numero;

    private EServiceType type;

    private Demenagement demenagement;

    private Pressing pressing;

    private Bricolage bricolage;

    private Banque banque;

    private Course course;

    private VisiteTechnique visiteTechnique;

    private String typeService;

    private TypeLivraison typeLivraison;

    private PointGeographique pointDepart;

    private PointGeographique pointArriver;

    private BigDecimal prix;

    public ExtraitCompteEntrepriseData() {
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public BigDecimal getMontantRestant() {
        return montantRestant;
    }

    public void setMontantRestant(BigDecimal montantRestant) {
        this.montantRestant = montantRestant;
    }

    public BigDecimal getMontantPayer() {
        return montantPayer;
    }

    public void setMontantPayer(BigDecimal montantPayer) {
        this.montantPayer = montantPayer;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
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

    @Override
    public int compareTo(ExtraitCompteEntrepriseData e) {
        return getCreatedDate().compareTo(e.getCreatedDate());
    }
}