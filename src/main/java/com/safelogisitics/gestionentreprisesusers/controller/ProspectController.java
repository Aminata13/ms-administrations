package com.safelogisitics.gestionentreprisesusers.controller;

import java.util.Map;

import javax.validation.Valid;

import com.safelogisitics.gestionentreprisesusers.payload.request.ProspectRequest;
import com.safelogisitics.gestionentreprisesusers.service.ProspectService;

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
@RequestMapping("/prospects")
@PostAuthorize("hasRole('COMPTE_ADMINISTRATEUR')")
@Api(tags = "Gestion des prospects", description = "Api client: gestion des prospects")
public class ProspectController {

  @Autowired
  private ProspectService prospectService;

  @ApiOperation(value = "Liste des prospects")
  @PreAuthorize("hasPermission('GESTION_PROSPECTS', 'READ')")
  @GetMapping("/list")
	public ResponseEntity<?> allProspects(@RequestParam Map<String,String> parameters, Pageable pageable) {
    System.out.print(parameters);
    Page<Map<String, Object>> prospects = prospectService.getProspects(parameters, pageable);

    return ResponseEntity.status(HttpStatus.OK).body(prospects);
	}

  @ApiOperation(value = "Affichage d'une prospect")
  @GetMapping("/{id}")
	public ResponseEntity<?> oneProspect(@PathVariable(value = "id") String id) {
    return ResponseEntity.status(HttpStatus.OK).body(prospectService.getProspectById(id));
	}

  @ApiOperation(value = "Création d'une nouveau prospect")
  @PostMapping("/add")
	public ResponseEntity<Map<String, Object>> addProspect(@Valid @RequestBody ProspectRequest request) {
    Map<String, Object> prospect = prospectService.createProspect(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(prospect);
	}

  @ApiOperation(value = "Mise à jour d'un prospect")
  @PutMapping("/update/{id}")
	public ResponseEntity<Map<String, Object>> updateProspect(@PathVariable(value = "id") String id, @Valid @RequestBody ProspectRequest request) {
    Map<String, Object> prospect = prospectService.updateProspect(id, request);

		return ResponseEntity.status(HttpStatus.OK).body(prospect);
	}

  @ApiOperation(value = "Enrolement d'un prospect")
  @PutMapping("/enrole/{id}")
	public ResponseEntity<Map<String, Object>> enroleProspect(@PathVariable(value = "id") String id, @Valid @RequestBody ProspectRequest request) {
    Map<String, Object> prospect = prospectService.enroleProspect(id, request);

		return ResponseEntity.status(HttpStatus.OK).body(prospect);
	}

  @ApiOperation(value = "Suppression d'un prospect")
  @DeleteMapping("/delete/{id}")
	public ResponseEntity<?> deleteProspect(@PathVariable(value = "id") String id) {
    prospectService.deleteProspect(id);
    return ResponseEntity.status(HttpStatus.OK).body("OK!");
	}
}
