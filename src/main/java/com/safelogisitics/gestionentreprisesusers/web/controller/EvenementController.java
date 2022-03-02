package com.safelogisitics.gestionentreprisesusers.web.controller;

import java.util.Collection;

import javax.validation.Valid;

import com.safelogisitics.gestionentreprisesusers.data.dto.request.EvenementDto;
import com.safelogisitics.gestionentreprisesusers.data.model.Evenement;
import com.safelogisitics.gestionentreprisesusers.service.EvenementService;

import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/evenements")
@PreAuthorize("hasRole('COMPTE_ADMINISTRATEUR')")
@Api(tags = "Gestion des événements", description = "Api de gestion des événements")
public class EvenementController {

  @Autowired
  private EvenementService EvenementService;

  @ApiOperation(value = "Liste des types d'événement")
  @GetMapping("/list")
  @PreAuthorize("hasPermission('GESTION_EVENEMENTS', 'READ')")
	public ResponseEntity<Collection<EvenementDto>> allEvenements(
    @RequestParam(required = false) String dateDebut,
    @RequestParam(required = false) String dateFin
  ) {
    Collection<EvenementDto> Evenements = EvenementService.getEvenements(dateDebut, dateFin);

    return ResponseEntity.status(HttpStatus.OK).body(Evenements);
	}

  @ApiOperation(value = "Affichage d'un événement")
  @GetMapping("/{id}")
  @PreAuthorize("hasPermission('GESTION_EVENEMENTS', 'READ')")
	public ResponseEntity<EvenementDto> oneEvenement(@PathVariable(value = "id") String id) {
    return ResponseEntity.status(HttpStatus.OK).body(EvenementService.getEvenementById(id));
	}

  @ApiOperation(value = "Création d'un nouveau événement")
  @PostMapping("/add")
  @PreAuthorize("hasPermission('GESTION_EVENEMENTS', 'CREATE')")
	public ResponseEntity<EvenementDto> addEvenement(@Valid @RequestBody Evenement EvenementData) {
    EvenementDto Evenement = EvenementService.createEvenement(EvenementData);
		return ResponseEntity.status(HttpStatus.CREATED).body(Evenement);
	}

  @ApiOperation(value = "Mise à jour d'un événement")
  @PutMapping("/update/{id}")
  @PreAuthorize("hasPermission('GESTION_EVENEMENTS', 'UPDATE')")
	public ResponseEntity<EvenementDto> updateEvenement(@PathVariable(value = "id") String id, @Valid @RequestBody Evenement EvenementData) {
    EvenementDto Evenement = EvenementService.updateEvenement(id, EvenementData);

		return ResponseEntity.status(HttpStatus.OK).body(Evenement);
	}

  @ApiOperation(value = "Suppression d'un événement")
  @DeleteMapping("/delete/{id}")
  @PreAuthorize("hasPermission('GESTION_EVENEMENTS', 'DELETE')")
	public ResponseEntity<?> deleteEvenement(@PathVariable(value = "id") String id) {
    EvenementService.deleteEvenement(id);
    return ResponseEntity.status(HttpStatus.OK).body("OK!");
	}
}
