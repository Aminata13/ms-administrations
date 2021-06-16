package com.safelogisitics.gestionentreprisesusers.controller;

import java.util.Set;

import javax.validation.Valid;

import com.safelogisitics.gestionentreprisesusers.model.Entreprise;
import com.safelogisitics.gestionentreprisesusers.model.enums.ETypeEntreprise;
import com.safelogisitics.gestionentreprisesusers.model.enums.ETypePartenariat;
import com.safelogisitics.gestionentreprisesusers.payload.request.EntrepriseRequest;
import com.safelogisitics.gestionentreprisesusers.service.EntrepriseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
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
@RequestMapping("/entreprises")
@PostAuthorize("hasRole('COMPTE_ADMINISTRATEUR')")
@Api(tags = "Gestion des entreprises", description = "Api client: gestion des entreprises")
public class EntrepriseController {

  @Autowired
  private EntrepriseService entrepriseService;

  @ApiOperation(value = "Liste des entreprises")
  @GetMapping("/list")
	public ResponseEntity<?> allEntreprises(
    @RequestParam(required = false) ETypeEntreprise typeEntreprise,
    @RequestParam(required = false) Set<ETypePartenariat> typePartenariats,
    @RequestParam(required = false) String agentId,
    @RequestParam(required = false) String clientId,
    @RequestParam(required = false) String denomination,
    @RequestParam(required = false) String ninea,
    Pageable pageable
  ) {
    Page<Entreprise> entreprises = entrepriseService.getEntreprises(typeEntreprise, typePartenariats, agentId, denomination, ninea, pageable);

    return ResponseEntity.status(HttpStatus.OK).body(entreprises);
	}

  @ApiOperation(value = "Affichage d'une entreprise")
  @GetMapping("/{id}")
	public ResponseEntity<?> oneEntreprise(@PathVariable(value = "id") String id) {
    return ResponseEntity.status(HttpStatus.OK).body(entrepriseService.getEntrepriseById(id));
	}

  @ApiOperation(value = "Création d'une nouvelle entreprise")
  @PostMapping("/add")
	public Entreprise addEntreprise(@Valid @RequestBody EntrepriseRequest request) {
    Entreprise entreprise = entrepriseService.createEntreprise(request);
		return entreprise;
	}

  @ApiOperation(value = "Mise à jour d'une entreprise")
  @PutMapping("/update/{id}")
	public ResponseEntity<?> updateEntreprise(@PathVariable(value = "id") String id, @Valid @RequestBody EntrepriseRequest request) {
    Entreprise entreprise = entrepriseService.updateEntreprise(id, request);

    if (entreprise == null) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Entreprise is not found!");
    }
		return ResponseEntity.status(HttpStatus.OK).body(entreprise);
	}

  @ApiOperation(value = "Suppression d'une entreprise")
  @PutMapping("/delete/{id}")
	public ResponseEntity<?> deleteEntreprise(@PathVariable(value = "id") String id, @Valid @RequestBody String commentaire) {
    entrepriseService.deleteEntreprise(id);
    return ResponseEntity.status(HttpStatus.OK).body("OK!");
	}
}
