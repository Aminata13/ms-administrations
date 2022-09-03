package com.safelogisitics.gestionentreprisesusers.data.shared.model.subclass;

import com.safelogisitics.gestionentreprisesusers.data.model.PointGeographique;

public class Demenagement {

    private PointGeographique adresseDepart;

    private PointGeographique adresseDestination;

    public Demenagement() {
    }

    public PointGeographique getAdresseDepart() {
        return adresseDepart;
    }

    public void setAdresseDepart(PointGeographique adresseDepart) {
        this.adresseDepart = adresseDepart;
    }

    public PointGeographique getAdresseDestination() {
        return adresseDestination;
    }

    public void setAdresseDestination(PointGeographique adresseDestination) {
        this.adresseDestination = adresseDestination;
    }
}
