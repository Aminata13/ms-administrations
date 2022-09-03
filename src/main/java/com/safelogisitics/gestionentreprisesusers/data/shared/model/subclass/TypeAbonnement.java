package com.safelogisitics.gestionentreprisesusers.data.shared.model.subclass;

import java.math.BigDecimal;
import java.util.Set;

public class TypeAbonnement {

    private String id;

    private String libelle;

    private String icon;

    private int reduction;

    private BigDecimal prix;

    private Set<String> services;

    private Set<String> compteEligibles;

    private int statut;

    public TypeAbonnement() {
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLibelle() {
        return this.libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getIcon() {
        return this.icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getReduction() {
        return this.reduction;
    }

    public void setReduction(int reduction) {
        this.reduction = reduction;
    }

    public BigDecimal getPrix() {
        return this.prix;
    }

    public void setPrix(BigDecimal prix) {
        this.prix = prix;
    }

    public Set<String> getServices() {
        return services;
    }

    public void setServices(Set<String> services) {
        this.services = services;
    }

    public Set<String> getCompteEligibles() {
        return compteEligibles;
    }

    public void setCompteEligibles(Set<String> compteEligibles) {
        this.compteEligibles = compteEligibles;
    }

    public int getStatut() {
        return this.statut;
    }

    public void setStatut(int statut) {
        this.statut = statut;
    }
}
