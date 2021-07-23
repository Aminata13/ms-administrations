package com.safelogisitics.gestionentreprisesusers.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.safelogisitics.gestionentreprisesusers.dao.InfosPersoDao;
import com.safelogisitics.gestionentreprisesusers.model.Transaction;

@Service
public class PDFGeneratorService {

  @Autowired
  private InfosPersoDao infosPersoDao;

  private static Logger logger = LoggerFactory.getLogger(PDFGeneratorService.class);

  public ByteArrayInputStream exportToPdf(List<Transaction> transactions) {
    List<String> headers = Arrays.asList("Ref", "Action", "N° commande", "Client", "Auteur", "Montant", "Solde progressif", "Date");
    Document document = new Document();
    ByteArrayOutputStream out = new ByteArrayOutputStream();

    try {
      PdfWriter.getInstance(document, out);

      document.open();

      PdfPTable table = new PdfPTable(headers.size());
      table.setWidthPercentage(100);

      addTitle(document, "Liste des transactions");
      addTableHeader(table, headers);
      for (Transaction transaction : transactions) {
        addTableRow(table, transaction.getReference());
        addTableRow(table, transaction.getAction().name());
        addTableRow(table, transaction.getNumeroCommande());
        addTableRow(table, infosPersoDao.findById(transaction.getAbonnement().getCompteClient().getInfosPersoId()).get().getNomComplet());
        addTableRow(table, infosPersoDao.findById(transaction.getCompteCreateur().getInfosPersoId()).get().getNomComplet());
        addTableRow(table, String.format("%s FCFA", transaction.getMontant().toString()));
        addTableRow(table, String.format("%s FCFA", transaction.getNouveauSolde() != null ? transaction.getNouveauSolde().toString() : 0));
        addTableRow(table, transaction.getDateCreation().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
      }

      document.add(table);
      document.close();
    } catch (DocumentException e) {
      logger.error(e.toString());
    }

    return new ByteArrayInputStream(out.toByteArray());
  }

  private void addTitle(Document document, String title) {
    try {
      if (!document.isOpen())
        document.open();

      Paragraph para = new Paragraph(title, FontFactory.getFont(FontFactory.COURIER, 14, BaseColor.BLACK));
      para.setAlignment(Element.ALIGN_CENTER);
      document.add(para);
      document.add(Chunk.NEWLINE);

    } catch (DocumentException e) {
      logger.error(e.toString());
    }
  }

  private void addTableHeader(PdfPTable table, List<String> headers) {
    headers.stream().forEach(columnTitle -> {
      PdfPCell header = new PdfPCell();
      header.setBackgroundColor(BaseColor.LIGHT_GRAY);
      header.setBorderWidth(1);
      header.setPhrase(new Phrase(columnTitle, FontFactory.getFont(FontFactory.COURIER, 10, BaseColor.BLACK)));
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
