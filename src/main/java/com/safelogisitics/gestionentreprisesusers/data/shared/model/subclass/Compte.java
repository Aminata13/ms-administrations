package com.safelogisitics.gestionentreprisesusers.data.shared.model.subclass;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.safelogisitics.gestionentreprisesusers.data.enums.ECompteType;
import com.safelogisitics.gestionentreprisesusers.data.enums.EServiceConciergeType;
import com.safelogisitics.gestionentreprisesusers.data.enums.EServiceType;

import java.time.LocalDateTime;
import java.util.Set;

public class Compte {

    private String id;

    private ECompteType type;

    private String entrepriseId;

    private boolean entrepriseUser;

    private Role role;

    private Set<EServiceConciergeType> serviceConciergeries;

    private Set<EServiceType> services;

    private Set<AffectationEquipement> equipements;

    private String moyenTransportId;

    private String numeroEmei;

    private String numeroReference;

    private int statut;

    @JsonIgnore
    private boolean deleted;

    private LocalDateTime dateCreation;

    public Compte() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ECompteType getType() {
        return this.type;
    }

    public void setType(ECompteType type) {
        this.type = type;
    }

    public String getEntrepriseId() {
        return entrepriseId;
    }

    public void setEntrepriseId(String entrepriseId) {
        this.entrepriseId = entrepriseId;
    }

    public boolean isEntrepriseUser() {
        return this.entrepriseUser;
    }

    public void setEntrepriseUser(boolean entrepriseUser) {
        this.entrepriseUser = entrepriseUser;
    }


    public Role getRole() {
        return this.role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Set<EServiceConciergeType> getServiceConciergeries() {
        return this.serviceConciergeries;
    }

    public void setServiceConciergeries(Set<EServiceConciergeType> serviceConciergeries) {
        this.serviceConciergeries = serviceConciergeries;
    }

    public Set<EServiceType> getServices() {
        return this.services;
    }

    public void setServices(Set<EServiceType> services) {
        this.services = services;
    }

    public Set<AffectationEquipement> getEquipements() {
        return this.equipements;
    }

    public void setEquipements(Set<AffectationEquipement> equipements) {
        this.equipements = equipements;
    }

    public String getMoyenTransportId() {
        return this.moyenTransportId;
    }

    public void setMoyenTransportId(String moyenTransportId) {
        this.moyenTransportId = moyenTransportId;
    }

    public String getNumeroEmei() {
        return this.numeroEmei;
    }

    public void setNumeroEmei(String numeroEmei) {
        this.numeroEmei = numeroEmei;
    }

    public String getNumeroReference() {
        return this.numeroReference;
    }

    public void setNumeroReference(String numeroReference) {
        this.numeroReference = numeroReference;
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

    public LocalDateTime getDateCreation() {
        return this.dateCreation;
    }

    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }

}
