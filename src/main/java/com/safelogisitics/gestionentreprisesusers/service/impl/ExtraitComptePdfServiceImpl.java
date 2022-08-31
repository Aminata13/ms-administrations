package com.safelogisitics.gestionentreprisesusers.service.impl;

import com.lowagie.text.DocumentException;
import com.safelogisitics.gestionentreprisesusers.data.model.ExtraitCompteDataModel;
import com.safelogisitics.gestionentreprisesusers.service.ExtraitComptePdfService;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

@Service
public class ExtraitComptePdfServiceImpl implements ExtraitComptePdfService {

    private static final String PDF_RESOURCES = "/pdf-resources/";

    @Override
    public File exportToPdf(LocalDateTime dateDebut, LocalDateTime dateFin, List<ExtraitCompteDataModel> extraitCompteData, String prenom, String nom, String adresse) throws IOException, DocumentException {
        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.addDialect(new Java8TimeDialect());

        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix("templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setCharacterEncoding("UTF-8");

        templateEngine.setTemplateResolver(templateResolver);

        Context context = new Context();
        context.setVariable("transactions", extraitCompteData);
        context.setVariable("dateDebut", dateDebut);
        context.setVariable("dateFin", dateFin);
        context.setVariable("prenom", prenom);
        context.setVariable("nom", nom);
        context.setVariable("adresse", adresse);
        context.setVariable("montant", extraitCompteData.get(extraitCompteData.size()-1).getNouveauSolde());
        context.setVariable("numeroCarte", extraitCompteData.get(0).getAbonnement().getNumeroCarte());
        context.setVariable("jourAbonnement", extraitCompteData.get(0).getAbonnement().getDateCreation().getDayOfMonth());
        context.setVariable("moisAbonnement", Month.of(extraitCompteData.get(0).getAbonnement().getDateCreation().getMonthValue()).getDisplayName(TextStyle.FULL , Locale.FRANCE));
        context.setVariable("anneeAbonnement", extraitCompteData.get(0).getAbonnement().getDateCreation().getYear());

        String html = templateEngine.process("extrait_compte", context);

        File file = File.createTempFile("extrait_compte_client", ".pdf");
        OutputStream outputStream = new FileOutputStream(file);
        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(html, new ClassPathResource(PDF_RESOURCES).getURL().toExternalForm());
        renderer.getFontResolver().addFont("/pdf-resources/fonts/Poppins-Regular-400.ttf", true);
        renderer.getFontResolver().addFont("/pdf-resources/fonts/Lato-Regular-400.ttf", true);
        renderer.getFontResolver().addFont("/pdf-resources/fonts/Raleway-400.ttf", true);
        renderer.layout();
        renderer.createPDF(outputStream);
        outputStream.close();
        file.deleteOnExit();

        return file;
    }
}