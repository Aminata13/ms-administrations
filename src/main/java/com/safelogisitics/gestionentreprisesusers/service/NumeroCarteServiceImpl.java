package com.safelogisitics.gestionentreprisesusers.service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import com.safelogisitics.gestionentreprisesusers.dao.NumeroCarteDao;
import com.safelogisitics.gestionentreprisesusers.dao.TypeAbonnementDao;
import com.safelogisitics.gestionentreprisesusers.helper.UploadFileHelper;
import com.safelogisitics.gestionentreprisesusers.model.NumeroCarte;
import com.safelogisitics.gestionentreprisesusers.model.TypeAbonnement;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class NumeroCarteServiceImpl implements NumeroCarteService {

  @Value("${upload.tmp_path}")
  private String uploadPath;

  @Autowired
  NumeroCarteDao numeroCarteDao;

  @Autowired
  TypeAbonnementDao typeAbonnementDao;

  private ScheduledExecutorService quickService = Executors.newScheduledThreadPool(20); 

  public Page<NumeroCarte> getNumeroCartes(String typeAbonnementId, Pageable pageable) {
    if (typeAbonnementId != null && !typeAbonnementId.isEmpty())
     return numeroCarteDao.findByTypeAbonnementId(typeAbonnementId, pageable);
    return numeroCarteDao.findAll(pageable);
  }

  public Optional<NumeroCarte> getNumeroCarteByNumero(String numero) {
    return numeroCarteDao.findByNumero(numero);
  }

  public NumeroCarte createNumeroCarte(NumeroCarte numeroCarte) {
    Optional<TypeAbonnement> _typeAbonnement = typeAbonnementDao.findById(numeroCarte.getTypeAbonnementId());

    if (!_typeAbonnement.isPresent())
      throw new IllegalArgumentException("Type abonnement invalide");

    if (numeroCarteDao.existsByNumero(numeroCarte.getNumero()))
      throw new IllegalArgumentException("Ce numero existe déjà");

    numeroCarte.setActive(false);
    numeroCarte.setDateCreation(LocalDateTime.now());

    numeroCarteDao.save(numeroCarte);

    return numeroCarte;
  }

  public Page<NumeroCarte> createNumeroCarte(MultipartFile file, String typeAbonnementId) {
    Optional<TypeAbonnement> _typeAbonnement = typeAbonnementDao.findById(typeAbonnementId);

    if (!_typeAbonnement.isPresent())
      throw new IllegalArgumentException("Type abonnement invalide");

    quickService.submit(new Runnable() {
      @Override
      public void run() {
        try {
          String tmpFileName = UploadFileHelper.uploadFile(file, uploadPath);
    
          File fileData = new File(String.format("%s/%s", uploadPath, tmpFileName));
    
          Workbook workbook = WorkbookFactory.create(fileData);
    
          Sheet sheet = workbook.getSheetAt(0);
          Iterator<Row> rows = sheet.iterator();
    
          int rowNumber = 0;
          while (rows.hasNext()) {
            Row currentRow = rows.next();
    
            // skip header
            if (rowNumber == 0) {
              rowNumber++;
              continue;
            }
    
            Iterator<Cell> cellsInRow = currentRow.iterator();
            Cell currentCell = cellsInRow.next();
    
            String _numero = currentCell.getCellType() == CellType.NUMERIC ? String.format("%.0f", currentCell.getNumericCellValue()) : currentCell.getStringCellValue();
    
            String numero = _numero.trim().replaceAll(" ", "");
            if (numeroCarteDao.existsByNumero(numero))
              continue;
    
            NumeroCarte numeroCarte = new NumeroCarte(numero, typeAbonnementId, false);
            numeroCarteDao.save(numeroCarte);
          }
          workbook.close();
          
        } catch (IOException e) {
          throw new RuntimeException("fail to store excel data: " + e.getMessage());
        }
      }
    });

    return getNumeroCartes(typeAbonnementId, PageRequest.of(0, 20));
  }

  public void deleteByNumero(String numeroCarte) {
    Optional<NumeroCarte> _numeroCarte = numeroCarteDao.findByNumero(numeroCarte);

    if (!_numeroCarte.isPresent())
      return;

    if (_numeroCarte.get().isActive())
      throw new IllegalArgumentException("Cette carte est activé");

    numeroCarteDao.delete(_numeroCarte.get());
  }
}
