package com.safelogisitics.gestionentreprisesusers.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.safelogisitics.gestionentreprisesusers.dao.AbonnementDao;
import com.safelogisitics.gestionentreprisesusers.dao.InfosPersoDao;
import com.safelogisitics.gestionentreprisesusers.model.Abonnement;
import com.safelogisitics.gestionentreprisesusers.model.InfosPerso;
import com.safelogisitics.gestionentreprisesusers.model.Transaction;

@Service
public class PDFGeneratorService {

  @Autowired
  private InfosPersoDao infosPersoDao;

  @Autowired
  private AbonnementDao abonnementDao;

  private static Logger logger = LoggerFactory.getLogger(PDFGeneratorService.class);

  public ByteArrayInputStream exportToPdf(List<Transaction> transactions, String idClient, String dateDebut, String dateFin) {
    List<String> headers = Arrays.asList("Ref", "Action", "N° commande", "Client", "Auteur", "Montant", "Solde", "Date");
    Document document = new Document();
    ByteArrayOutputStream out = new ByteArrayOutputStream();

    try {
      PdfWriter.getInstance(document, out);

      document.open();

      PdfPTable table = new PdfPTable(headers.size());
      table.setWidthPercentage(100);

      addText(document, "SAFE LOGISTICS", Element.ALIGN_LEFT, 12);
      String subtitle = "";
      String solde = "";
      if (idClient != null) {
        InfosPerso client = infosPersoDao.findById(idClient).get();
        subtitle = String.format("Nom du client: %s \nTéléphone: %s", client.getNomComplet(), client.getTelephone());
        Optional<Abonnement> abonnement = abonnementDao.findByCompteClientInfosPersoIdAndDeletedIsFalse(idClient);
        if (abonnement.isPresent()) {
          subtitle += String.format("\nNuméro carte: %s \nType d'abonnement: %s", String.join(" ", abonnement.get().getNumeroCarte().split("(?<=\\G....)")), abonnement.get().getTypeAbonnement().getLibelle());
          solde = new DecimalFormat("Solde: #,### FCFA").format(abonnement.get().getSolde());
        }
      }
      addText(document, subtitle, Element.ALIGN_LEFT, 12);
      addText(document, solde, Element.ALIGN_RIGHT, 12);
      addText(document, "Liste des transactions", Element.ALIGN_CENTER, 14);
      addTableHeader(table, headers);
      for (Transaction transaction : transactions) {
        addTableRow(table, transaction.getReference());
        addTableRow(table, transaction.getAction().name());
        addTableRow(table, transaction.getNumeroCommande());
        addTableRow(table, infosPersoDao.findById(transaction.getAbonnement().getCompteClient().getInfosPersoId()).get().getNomComplet());
        addTableRow(table, infosPersoDao.findById(transaction.getCompteCreateur().getInfosPersoId()).get().getNomComplet());
        addTableRow(table, new DecimalFormat("#,### FCFA").format(transaction.getMontant()));
        addTableRow(table, new DecimalFormat("#,### FCFA").format(transaction.getNouveauSolde()));
        addTableRow(table, transaction.getDateCreation().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
      }

      document.add(table);
      document.close();
    } catch (DocumentException e) {
      logger.error(e.toString());
    }

    return new ByteArrayInputStream(out.toByteArray());
  }

  private void addText(Document document, String title, int position, int fontSize) {
    try {
      if (!document.isOpen())
        document.open();

      Paragraph para = new Paragraph(title, FontFactory.getFont(FontFactory.COURIER, fontSize, BaseColor.BLACK));
      para.setAlignment(position);
      document.add(para);
      document.add(Chunk.NEWLINE);

    } catch (DocumentException e) {
      logger.error(e.toString());
    }
  }

  private void addTableHeader(PdfPTable table, List<String> headers) {
    headers.stream().forEach(columnTitle -> {
      PdfPCell header = new PdfPCell();
      header.setBackgroundColor(new BaseColor(246, 246, 248));
      header.setBorderWidth(1);
      header.setPhrase(new Phrase(columnTitle, FontFactory.getFont(FontFactory.COURIER, 10, new BaseColor(19, 49, 75))));
      header.setPadding(5);
      header.setVerticalAlignment(Element.ALIGN_MIDDLE);
      header.setHorizontalAlignment(Element.ALIGN_CENTER);
      table.addCell(header);
    });
  }

  private void addTableRow(PdfPTable table, String value) {
    PdfPCell cell = new PdfPCell(new Phrase(value, FontFactory.getFont(FontFactory.COURIER, 8, BaseColor.BLACK)));
    cell.setPadding(5);
    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
    table.addCell(cell);
  }
}
