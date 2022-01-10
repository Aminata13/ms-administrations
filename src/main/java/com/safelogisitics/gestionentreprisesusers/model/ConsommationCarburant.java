package com.safelogisitics.gestionentreprisesusers.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Document(collection = "consommationCarburants")
public class ConsommationCarburant extends AuditMetadata {

    @Id
    private String id;

    @Field(value = "quantite")
    private double quantite;

    @Field(value = "prix")
    private BigDecimal prix;

    @Field(value = "moyenTransportId")
    private String moyenTransportId;

    @Field(value = "pieceJointe")
    private String pieceJointe;

    @Field(value = "dateConsommation")
    private LocalDateTime dateConsommation;

    public ConsommationCarburant() {
    }

    public ConsommationCarburant(double quantite, BigDecimal prix, String moyenTransportId, String pieceJointe, LocalDateTime dateConsommation) {
        this.quantite = quantite;
        this.prix = prix;
        this.moyenTransportId = moyenTransportId;
        this.pieceJointe = pieceJointe;
        this.dateConsommation = dateConsommation;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public LocalDateTime getDateConsommation() {
        return dateConsommation;
    }

    public void setDateConsommation(LocalDateTime dateConsommation) {
        this.dateConsommation = dateConsommation;
    }
}
