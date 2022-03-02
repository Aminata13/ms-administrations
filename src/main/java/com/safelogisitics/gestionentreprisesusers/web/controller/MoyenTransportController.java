package com.safelogisitics.gestionentreprisesusers.web.controller;

import java.util.Collection;
import java.util.Optional;

import javax.validation.Valid;

import com.safelogisitics.gestionentreprisesusers.data.dto.request.MoyenTransportSearchDto;
import com.safelogisitics.gestionentreprisesusers.data.model.MoyenTransport;
import com.safelogisitics.gestionentreprisesusers.service.MoyenTransportService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/moyens-transport")
@PreAuthorize("hasRole('COMPTE_ADMINISTRATEUR')")
@Api(tags = "Gestion des moyens de transport", description = "Api client: gestion des moyens de transport")
public class MoyenTransportController {

  @Autowired
  private MoyenTransportService moyenTransportService;

  @ApiOperation(value = "Liste des moyens de transport")
  @GetMapping("/list")
  @PreAuthorize("hasPermission('GESTION_MATERIELS', 'READ')")
	public ResponseEntity<Page<MoyenTransport>> allMoyensTransport(MoyenTransportSearchDto moyenTransportSearch, Pageable pageable) {
    Page<MoyenTransport> moyenTransports = moyenTransportService.getMoyenTransports(moyenTransportSearch, pageable);
    return ResponseEntity.status(HttpStatus.OK).body(moyenTransports);
	}

  @ApiOperation(value = "Rechercher un moyen de transport")
  @GetMapping("/search/{searchText}")
  @PreAuthorize("hasPermission('GESTION_MATERIELS', 'READ')")
	public ResponseEntity<Collection<MoyenTransport>> searchMoyensTransport(@PathVariable(value = "searchText") String searchText) {
    Collection<MoyenTransport> moyenTransports = moyenTransportService.searchMoyenTransport(searchText);

    return ResponseEntity.status(HttpStatus.OK).body(moyenTransports);
	}

  @ApiOperation(value = "Affichage d'un moyen de transport")
  @GetMapping("/{id}")
  @PreAuthorize("hasPermission('GESTION_MATERIELS', 'READ')")
	public ResponseEntity<Optional<MoyenTransport>> oneMoyenTransport(@PathVariable(value = "id") String id) {
    return ResponseEntity.status(HttpStatus.OK).body(moyenTransportService.getMoyenTransportById(id));
	}

  @ApiOperation(value = "Création d'un nouveau moyen de transport")
  @PostMapping("/add")
  @PreAuthorize("hasPermission('GESTION_MATERIELS', 'CREATE')")
	public ResponseEntity<MoyenTransport> addMoyenTransport(@Valid @RequestBody MoyenTransport request) {
    MoyenTransport moyenTransport = moyenTransportService.createMoyenTransport(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(moyenTransport);
	}

  @ApiOperation(value = "Mise à jour d'un moyen de transport")
  @PutMapping("/update/{id}")
  @PreAuthorize("hasPermission('GESTION_MATERIELS', 'UPDATE')")
	public ResponseEntity<MoyenTransport> updateMoyenTransport(@PathVariable(value = "id") String id, @Valid @RequestBody MoyenTransport request) {
    MoyenTransport moyenTransport = moyenTransportService.updateMoyenTransport(id, request);
		return ResponseEntity.status(HttpStatus.OK).body(moyenTransport);
	}

  @ApiOperation(value = "Suppression d'un moyen de transport")
  @DeleteMapping("/delete/{id}")
  @PreAuthorize("hasPermission('GESTION_MATERIELS', 'DELETE')")
	public ResponseEntity<?> deleteMoyenTransport(@PathVariable(value = "id") String id) {
    moyenTransportService.deleteMoyenTransport(id);
    return ResponseEntity.status(HttpStatus.OK).body("OK!");
	}
}
