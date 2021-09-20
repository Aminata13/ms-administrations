package com.safelogisitics.gestionentreprisesusers.controller;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import com.safelogisitics.gestionentreprisesusers.model.SMSModel;
import com.safelogisitics.gestionentreprisesusers.payload.request.SMSModelRequest;
import com.safelogisitics.gestionentreprisesusers.service.SMSService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
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
@RequestMapping("/sms-models")
@PostAuthorize("hasRole('COMPTE_ADMINISTRATEUR')")
@Api(tags = "Gestion des modèles de sms", description = "Api: gestion des modèles de sms")
public class SMSController {

  @Autowired
  private SMSService smsService;

  @ApiOperation(value = "Liste des modèles de sms")
  @GetMapping("/list")
  @PreAuthorize("hasPermission('GESTION_SMS', 'READ')")
	public ResponseEntity<Page<SMSModel>> allSMSModels(@RequestParam Map<String,String> parameters, Pageable pageable) {
    return ResponseEntity.status(HttpStatus.OK).body(smsService.getSMSModels(parameters, pageable));
	}

  @ApiOperation(value = "Affichage d'un modèle de sms")
  @GetMapping("/{id}")
  @PreAuthorize("hasPermission('GESTION_SMS', 'READ')")
	public ResponseEntity<Optional<SMSModel>> oneSMSModel(@PathVariable(value = "id") String id) {
    return ResponseEntity.status(HttpStatus.OK).body(smsService.getSMSModelById(id));
	}

  @ApiOperation(value = "Création d'un nouveau modèle de sms")
  @PostMapping("/add")
  @PreAuthorize("hasPermission('GESTION_SMS', 'CREATE')")
	public ResponseEntity<SMSModel> addSMSModel(@Valid @RequestBody SMSModelRequest request) {
    SMSModel smsModel = smsService.createSMSModel(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(smsModel);
	}

  @ApiOperation(value = "Mise à jour d'un modèle de sms")
  @PutMapping("/update/{id}")
  @PreAuthorize("hasPermission('GESTION_SMS', 'UPDATE')")
	public ResponseEntity<SMSModel> updateSMSModel(@PathVariable(value = "id") String id, @Valid @RequestBody SMSModelRequest request) {
    SMSModel smsModel = smsService.updateSMSModel(id, request);
		return ResponseEntity.status(HttpStatus.OK).body(smsModel);
	}

  @ApiOperation(value = "Suppression d'un modèle de sms")
  @DeleteMapping("/delete/{id}")
  @PreAuthorize("hasPermission('GESTION_SMS', 'DELETE')")
	public ResponseEntity<?> deleteSMSModel(@PathVariable(value = "id") String id) {
    smsService.deleteSMSModel(id);
    return ResponseEntity.status(HttpStatus.OK).body("OK!");
	}

  @ApiOperation(value = "Liste des types de données de sms")
  @GetMapping("/data-values")
  @PreAuthorize("hasPermission('GESTION_SMS', 'READ')")
	public ResponseEntity<Collection<String>> listDataValues() {
    Collection<String> data = smsService.getSMSData();
		return ResponseEntity.status(HttpStatus.OK).body(data);
	}

  @ApiOperation(value = "Liste des répétitions de données de sms")
  @GetMapping("/repetitions-values")
  @PreAuthorize("hasPermission('GESTION_SMS', 'READ')")
	public ResponseEntity<Collection<String>> listRepetionsValues() {
    Collection<String> data = smsService.getSMSRepetitions();
		return ResponseEntity.status(HttpStatus.OK).body(data);
	}
}
