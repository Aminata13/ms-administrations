package com.safelogisitics.gestionentreprisesusers.data.shared.model.subclass;

import com.safelogisitics.gestionentreprisesusers.data.model.PointGeographique;

public class Bricolage {

    private String typePrestation;

    private PointGeographique lieuPrestation;

    public Bricolage() {
    }

    public String getTypePrestation() {
        return typePrestation;
    }

    public void setTypePrestation(String typePrestation) {
        this.typePrestation = typePrestation;
    }

    public PointGeographique getLieuPrestation() {
        return lieuPrestation;
    }

    public void setLieuPrestation(PointGeographique lieuPrestation) {
        this.lieuPrestation = lieuPrestation;
    }
}
