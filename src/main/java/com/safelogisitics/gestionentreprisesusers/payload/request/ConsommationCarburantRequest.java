package com.safelogisitics.gestionentreprisesusers.payload.request;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class ConsommationCarburantRequest {

    @NotNull(message = "La quantité est obligatoire.")
    private double quantite;

    @DecimalMin(value = "0", message = "Le montant est invalide.")
    private BigDecimal prix;

    @NotNull(message = "L'id du moyen de transport est obligatoire.")
    @NotBlank(message = "L'id du moyen de transport est obligatoire.")
    private String moyenTransportId;

    private String pieceJointe;

    @NotNull(message = "La date de consommation est obligatoire.")
    private String dateConsommation;

    public double getQuantite() {
        return quantite;
    }

    public void setQuantite(double quantite) {
        this.quantite = quantite;
    }

    public BigDecimal getPrix() {
        return prix;
    }

    public void setPrix(BigDecimal prix) {
        this.prix = prix;
    }

    public String getMoyenTransportId() {
        return moyenTransportId;
    }

    public void setMoyenTransportId(String moyenTransportId) {
        this.moyenTransportId = moyenTransportId;
    }

    public String getPieceJointe() {
        return pieceJointe;
    }

    public void setPieceJointe(String pieceJointe) {
        this.pieceJointe = pieceJointe;
    }

    public String getDateConsommation() {
        return dateConsommation;
    }

    public void setDateConsommation(String dateConsommation) {
        this.dateConsommation = dateConsommation;
    }
}
