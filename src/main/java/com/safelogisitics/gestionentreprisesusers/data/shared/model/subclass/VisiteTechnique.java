package com.safelogisitics.gestionentreprisesusers.data.shared.model.subclass;

import com.safelogisitics.gestionentreprisesusers.data.model.PointGeographique;

public class VisiteTechnique {

    private String typeVisite;

    private String matricule;

    private PointGeographique adresseRecuperation;

    private PointGeographique adresseLivraison;

    public VisiteTechnique() {
    }

    public String getTypeVisite() {
        return typeVisite;
    }

    public void setTypeVisite(String typeVisite) {
        this.typeVisite = typeVisite;
    }

    public String getMatricule() {
        return matricule;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
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
}
