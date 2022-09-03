package com.safelogisitics.gestionentreprisesusers.service;

import com.lowagie.text.DocumentException;
import com.safelogisitics.gestionentreprisesusers.data.model.ExtraitCompteDataModel;
import com.safelogisitics.gestionentreprisesusers.data.model.ExtraitCompteEntrepriseData;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ExtraitComptePdfService {

    public File exportExtraitCompteClientToPdf(LocalDateTime dateDebut, LocalDateTime dateFin, List<ExtraitCompteDataModel> extraitCompteData, String prenom, String nom, String adresse) throws IOException, DocumentException;

    public File exportExtraitCompteEntrepriseToPdf(LocalDateTime dateDebut, LocalDateTime dateFin, LocalDate dateAbonnement, List<ExtraitCompteEntrepriseData> extraitCompteData, String numeroCarte, String denomination, String adresse, BigDecimal montantSolde) throws IOException, DocumentException;
}
