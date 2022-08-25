package com.safelogisitics.gestionentreprisesusers.data.shared.model.subclass;

import com.safelogisitics.gestionentreprisesusers.data.model.PointGeographique;

import java.math.BigDecimal;

public class Course {

    private String typeCourse;

    private PointGeographique adresseLivraison;

    private BigDecimal fraisLivraison = BigDecimal.valueOf(0);

    public Course() {
    }

    public String getTypeCourse() {
        return typeCourse;
    }

    public void setTypeCourse(String typeCourse) {
        this.typeCourse = typeCourse;
    }

    public PointGeographique getAdresseLivraison() {
        return adresseLivraison;
    }

    public void setAdresseLivraison(PointGeographique adresseLivraison) {
        this.adresseLivraison = adresseLivraison;
    }

    public BigDecimal getFraisLivraison() {
        return fraisLivraison;
    }

    public void setFraisLivraison(BigDecimal fraisLivraison) {
        this.fraisLivraison = fraisLivraison;
    }
}
