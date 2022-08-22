package com.safelogisitics.gestionentreprisesusers.service;

import com.lowagie.text.DocumentException;
import com.safelogisitics.gestionentreprisesusers.data.model.ExtraitCompteDataModel;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public interface ExtraitComptePdfService {

    public File exportToPdf(LocalDateTime dateDebut, LocalDateTime dateFin, List<ExtraitCompteDataModel> extraitCompteData, String prenom, String nom, String adresse) throws IOException, DocumentException;
}
