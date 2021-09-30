package com.safelogisitics.gestionentreprisesusers.controller;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import com.safelogisitics.gestionentreprisesusers.model.Equipement;
import com.safelogisitics.gestionentreprisesusers.model.FournitureEquipement;
import com.safelogisitics.gestionentreprisesusers.service.EquipementService;

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
@RequestMapping("/equipements")
@PreAuthorize("hasRole('COMPTE_ADMINISTRATEUR')")
@Api(tags = "Gestion des équipements", description = "Api client: gestion des équipements")
public class EquipementController {

  @Autowired
  private EquipementService equipementService;

  @ApiOperation(value = "Liste des équipements")
  @GetMapping("/list")
  @PreAuthorize("hasPermission('GESTION_MATERIELS', 'READ')")
	public ResponseEntity<Collection<Equipement>> allEquipements(@RequestParam Map<String,String> parameters) {
    Collection<Equipement> equipements = equipementService.getEquipements(parameters);
    return ResponseEntity.status(HttpStatus.OK).body(equipements);
	}

  @ApiOperation(value = "Affichage d'un équipement")
  @GetMapping("/{id}")
  @PreAuthorize("hasPermission('GESTION_MATERIELS', 'READ')")
	public ResponseEntity<Optional<Equipement>> oneEquipement(@PathVariable(value = "id") String id) {
    return ResponseEntity.status(HttpStatus.OK).body(equipementService.getEquipementById(id));
	}

  @ApiOperation(value = "Création d'un nouveau équipement")
  @PostMapping("/add")
  @PreAuthorize("hasPermission('GESTION_MATERIELS', 'CREATE')")
	public ResponseEntity<Equipement> addEquipement(@Valid @RequestBody Equipement request) {
    Equipement equipement = equipementService.createEquipement(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(equipement);
	}

  @ApiOperation(value = "Mise à jour d'un équipement")
  @PutMapping("/update/{id}")
  @PreAuthorize("hasPermission('GESTION_MATERIELS', 'UPDATE')")
	public ResponseEntity<Equipement> updateEquipement(@PathVariable(value = "id") String id, @Valid @RequestBody Equipement request) {
    Equipement equipement = equipementService.updateEquipement(id, request);
		return ResponseEntity.status(HttpStatus.OK).body(equipement);
	}

  @ApiOperation(value = "Fourniture d'un équipement")
  @GetMapping("/historique/fournitures/{id}")
  @PreAuthorize("hasPermission('GESTION_MATERIELS', 'UPDATE')")
	public ResponseEntity<Collection<FournitureEquipement>> historiqueFournituresEquipement(@PathVariable(value = "id") String id) {
    Collection<FournitureEquipement> historiqueFournitures = equipementService.getHistoriqueFournitures(id);
		return ResponseEntity.status(HttpStatus.OK).body(historiqueFournitures);
	}

  @ApiOperation(value = "Fourniture d'un équipement")
  @PutMapping("/fourniture/{id}")
  @PreAuthorize("hasPermission('GESTION_MATERIELS', 'UPDATE')")
	public ResponseEntity<Equipement> fournitureEquipement(@PathVariable(value = "id") String id, @Valid @RequestBody FournitureEquipement request) {
    Equipement equipement = equipementService.fournitureEquipement(id, request);
		return ResponseEntity.status(HttpStatus.OK).body(equipement);
	}

  @ApiOperation(value = "Suppression d'un équipement")
  @DeleteMapping("/delete/{id}")
  @PreAuthorize("hasPermission('GESTION_MATERIELS', 'DELETE')")
	public ResponseEntity<?> deleteEquipement(@PathVariable(value = "id") String id) {
    equipementService.deleteEquipement(id);
    return ResponseEntity.status(HttpStatus.OK).body("OK!");
	}
}
