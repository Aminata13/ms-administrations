package com.safelogisitics.gestionentreprisesusers.web.controller;

import java.util.Collection;
import java.util.Optional;

import javax.validation.Valid;

import com.safelogisitics.gestionentreprisesusers.data.model.TypeEvenement;
import com.safelogisitics.gestionentreprisesusers.service.TypeEvenementService;

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
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/types-evenements")
@PreAuthorize("hasRole('COMPTE_ADMINISTRATEUR')")
@Api(tags = "Gestion des types d'événement", description = "Api de gestion des types d'événement")
public class TypeEvenementController {

  @Autowired
  private TypeEvenementService typeEvenementService;

  @ApiOperation(value = "Liste des types d'événement")
  @GetMapping("/list")
  @PreAuthorize("hasPermission('GESTION_EVENEMENTS', 'READ')")
	public ResponseEntity<Collection<TypeEvenement>> allTypeEvenements() {
    Collection<TypeEvenement> typeEvenements = typeEvenementService.getTypeEvenements();

    return ResponseEntity.status(HttpStatus.OK).body(typeEvenements);
	}

  @ApiOperation(value = "Affichage d'un type d'événement")
  @GetMapping("/{id}")
  @PreAuthorize("hasPermission('GESTION_EVENEMENTS', 'READ')")
	public ResponseEntity<Optional<TypeEvenement>> oneTypeEvenement(@PathVariable(value = "id") String id) {
    return ResponseEntity.status(HttpStatus.OK).body(typeEvenementService.getTypeEvenementById(id));
	}

  @ApiOperation(value = "Création d'un nouveau type d'événement")
  @PostMapping("/add")
  @PreAuthorize("hasPermission('GESTION_EVENEMENTS', 'SUPERVISE')")
	public ResponseEntity<TypeEvenement> addTypeEvenement(@Valid @RequestBody TypeEvenement typeEvenementData) {
    TypeEvenement typeEvenement = typeEvenementService.createTypeEvenement(typeEvenementData);
		return ResponseEntity.status(HttpStatus.CREATED).body(typeEvenement);
	}

  @ApiOperation(value = "Mise à jour d'un type d'événement")
  @PutMapping("/update/{id}")
  @PreAuthorize("hasPermission('GESTION_EVENEMENTS', 'SUPERVISE')")
	public ResponseEntity<TypeEvenement> updateTypeEvenement(@PathVariable(value = "id") String id, @Valid @RequestBody TypeEvenement typeEvenementData) {
    TypeEvenement typeEvenement = typeEvenementService.updateTypeEvenement(id, typeEvenementData);

		return ResponseEntity.status(HttpStatus.OK).body(typeEvenement);
	}

  @ApiOperation(value = "Suppression d'un type d'événement")
  @DeleteMapping("/delete/{id}")
  @PreAuthorize("hasPermission('GESTION_EVENEMENTS', 'SUPERVISE')")
	public ResponseEntity<?> deleteTypeEvenement(@PathVariable(value = "id") String id) {
    typeEvenementService.deleteTypeEvenement(id);
    return ResponseEntity.status(HttpStatus.OK).body("OK!");
	}
}
