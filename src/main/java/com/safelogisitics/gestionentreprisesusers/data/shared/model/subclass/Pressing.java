package com.safelogisitics.gestionentreprisesusers.data.shared.model.subclass;

import com.safelogisitics.gestionentreprisesusers.data.model.PointGeographique;

public class Pressing {

    private String typeLavage;

    private String vitesseLavage;

    private PointGeographique adresseRecuperation;

    private PointGeographique adresseLivraison;

    private boolean depositionPressing;

    private boolean recuperationPressing;

    public Pressing() {
    }

    public String getTypeLavage() {
        return typeLavage;
    }

    public void setTypeLavage(String typeLavage) {
        this.typeLavage = typeLavage;
    }

    public String getVitesseLavage() {
        return vitesseLavage;
    }

    public void setVitesseLavage(String vitesseLavage) {
        this.vitesseLavage = vitesseLavage;
    }

    public PointGeographique getAdresseRecuperation() {
        return adresseRecuperation;
    }

    public void setAdresseRecuperation(PointGeographique adresseRecuperation) {
        this.adresseRecuperation = adresseRecuperation;
    }

    public PointGeographique getAdresseLivraison() {
        return adresseLivraison;
    }

    public void setAdresseLivraison(PointGeographique adresseLivraison) {
        this.adresseLivraison = adresseLivraison;
    }

    public boolean isDepositionPressing() {
        return depositionPressing;
    }

    public void setDepositionPressing(boolean depositionPressing) {
        this.depositionPressing = depositionPressing;
    }

    public boolean isRecuperationPressing() {
        return recuperationPressing;
    }

    public void setRecuperationPressing(boolean recuperationPressing) {
        this.recuperationPressing = recuperationPressing;
    }
}
