package com.safelogisitics.gestionentreprisesusers.controller;

import java.util.Arrays;
import java.util.List;

import javax.validation.Valid;

import com.safelogisitics.gestionentreprisesusers.model.NumeroCarte;
import com.safelogisitics.gestionentreprisesusers.service.NumeroCarteService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/cartes")
@PostAuthorize("hasRole('COMPTE_ADMINISTRATEUR')")
@Api(tags = "Gestion des cartes", description = "Api client: gestion des cartes et numéros")
public class NumeroCarteController {

  public static List<String> TYPES = Arrays.asList(
    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
    "application/vnd.ms-excel"
  );

  @Autowired
  private NumeroCarteService numeroCarteService;

  @ApiOperation(value = "Liste des carte")
  @GetMapping("/list")
  @PreAuthorize("hasPermission('GESTION_CARTES', 'READ')")
	public ResponseEntity<?> allCartes(
    @RequestParam(required = false) String typeAbonnementId,
    Pageable pageable
  ) {
    Page<NumeroCarte> numeroCartes = numeroCarteService.getNumeroCartes(typeAbonnementId, pageable);

    return ResponseEntity.status(HttpStatus.OK).body(numeroCartes);
	}

  @ApiOperation(value = "Affichage d'une carte")
  @GetMapping("/{numero}")
  @PreAuthorize("hasPermission('GESTION_CARTES', 'READ')")
	public ResponseEntity<?> oneCarte(@PathVariable(value = "numero") String numero) {
    return ResponseEntity.status(HttpStatus.OK).body(numeroCarteService.getNumeroCarteByNumero(numero));
	}

  @ApiOperation(value = "Création d'une nouvelle carte")
  @PostMapping("/add")
  @PreAuthorize("hasPermission('GESTION_CARTES', 'CREATE')")
	public NumeroCarte addCarte(@Valid @RequestBody NumeroCarte request) {
    NumeroCarte numeroCarte = numeroCarteService.createNumeroCarte(request);
		return numeroCarte;
	}

  @ApiOperation(value = "Création d'une nouvelle carte")
  @PostMapping("/add-multiple")
  @PreAuthorize("hasPermission('GESTION_CARTES', 'CREATE')")
	public Page<NumeroCarte> addMultipleCarte(@RequestParam(name = "file", required = true) MultipartFile file, @RequestParam(name = "typeAbonnementId", required = true) String typeAbonnementId) {
    if (!TYPES.contains(file.getContentType())) {
      throw new IllegalArgumentException("Fichier invalide");
    }

    Page<NumeroCarte> numeroCartes = numeroCarteService.createNumeroCarte(file, typeAbonnementId);
		return numeroCartes;
	}

  @ApiOperation(value = "Suppression d'une carte")
  @PutMapping("/delete/{numero}")
  @PreAuthorize("hasPermission('GESTION_CARTES', 'DELETE')")
	public ResponseEntity<?> deleteCarte(@PathVariable(value = "numero") String numero) {
    numeroCarteService.deleteByNumero(numero);
    return ResponseEntity.status(HttpStatus.OK).body("OK!");
	}
}
