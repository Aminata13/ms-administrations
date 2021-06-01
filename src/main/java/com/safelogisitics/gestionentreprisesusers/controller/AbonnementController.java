package com.safelogisitics.gestionentreprisesusers.controller;

import java.util.Optional;

import javax.validation.Valid;

import com.safelogisitics.gestionentreprisesusers.dao.CompteDao;
import com.safelogisitics.gestionentreprisesusers.dao.TypeAbonnementDao;
import com.safelogisitics.gestionentreprisesusers.model.Abonnement;
import com.safelogisitics.gestionentreprisesusers.model.Compte;
import com.safelogisitics.gestionentreprisesusers.model.TypeAbonnement;
import com.safelogisitics.gestionentreprisesusers.model.enums.ECompteType;
import com.safelogisitics.gestionentreprisesusers.payload.request.AbonnementRequest;
import com.safelogisitics.gestionentreprisesusers.service.AbonnementService;
import com.safelogisitics.gestionentreprisesusers.service.InfosPersoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/abonnements")
@PostAuthorize("hasRole('COMPTE_ADMINISTRATEUR')")
@Api(tags = "Abonnements", description = "Api gestion des abonnements")
public class AbonnementController {

  @Autowired
  private InfosPersoService infosPersoService;

  @Autowired
  private AbonnementService abonnementService;

  @Autowired
  private TypeAbonnementDao typeAbonnementDao;

  @Autowired
  private CompteDao compteDao;

  @ApiOperation(value = "Liste de tous les abonnements", tags = "abonnements")
  @GetMapping("/list")
  @PreAuthorize("hasPermission('GESTION_ABONNEMENTS', 'READ')")
	public ResponseEntity<?> allAbonnements(@PageableDefault(size = 20) Pageable pageable) {
    return ResponseEntity.status(HttpStatus.OK).body(abonnementService.getAbonnements(pageable));
	}

  @ApiOperation(value = "Liste des abonnements par type d'abonnement", tags = "abonnements")
  @GetMapping("/list-by-type/{id}")
  @PreAuthorize("hasPermission('GESTION_ABONNEMENTS', 'READ')")
	public ResponseEntity<?> allAbonnementsByType(@PathVariable(value = "id") String id, @PageableDefault(size = 20) Pageable pageable) {
    Optional<TypeAbonnement> typeAbonnementExist = typeAbonnementDao.findById(id);
    if (!typeAbonnementExist.isPresent()) {
      ResponseEntity.status(HttpStatus.NOT_FOUND).body("Type abonnement is not found");
    }
    return ResponseEntity.status(HttpStatus.OK).body(abonnementService.getAbonnements(typeAbonnementExist.get(), pageable));
	}

  @ApiOperation(value = "Infos d'un abonnement, NB: on passe en paramètre l'id de l'abonnement", tags = "abonnements")
  @GetMapping("/get/{id}")
  @PreAuthorize("hasPermission('GESTION_ABONNEMENTS', 'READ')")
	public ResponseEntity<?> getAbonnement(@PathVariable(value = "id") String id) {
    return ResponseEntity.status(HttpStatus.OK).body(abonnementService.getAbonnementById(id));
	}

  @ApiOperation(value = "Infos d'un abonnement d'un client, NB: on passe en paramètre l'id infosPerso du client", tags = "abonnements")
  @GetMapping("/get-by-client/{id}")
  @PreAuthorize("hasPermission('GESTION_ABONNEMENTS', 'READ')")
	public ResponseEntity<?> getAbonnementByClient(@PathVariable(value = "id") String id) {
    Optional<Compte> compteExist = compteDao.findByInfosPersoIdAndType(id, ECompteType.COMPTE_PARTICULIER);
    if (!compteExist.isPresent()) {
      ResponseEntity.status(HttpStatus.NOT_FOUND).body("Compte is not found");
    }
    return ResponseEntity.status(HttpStatus.OK).body(abonnementService.getAbonnementByCompteClient(compteExist.get()));
	}

  @ApiOperation(value = "Liste des abonnements crées par un admnistrateur, NB: on passe en paramètre l'id infosPerso de l'administrateur", tags = "abonnements")
  @GetMapping("/get-by-createur/{id}")
  @PreAuthorize("hasPermission('GESTION_ABONNEMENTS', 'READ')")
	public ResponseEntity<?> getAbonnementByCreateur(@PathVariable(value = "id") String id, @PageableDefault(size = 20) Pageable pageable) {
    Optional<Compte> compteExist = compteDao.findByInfosPersoIdAndType(id, ECompteType.COMPTE_ADMINISTRATEUR);
    if (!compteExist.isPresent()) {
      ResponseEntity.status(HttpStatus.NOT_FOUND).body("Compte is not found");
    }
    return ResponseEntity.status(HttpStatus.OK).body(abonnementService.getAbonnementByCompteCreateur(compteExist.get(), pageable));
	}

  @PostMapping("/add")
  @PreAuthorize("hasPermission('GESTION_ABONNEMENTS', 'CREATE')")
	public ResponseEntity<?> addAbonnement(@Valid @RequestBody AbonnementRequest request) {
    Abonnement abonnement = abonnementService.createAbonnement(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(abonnement);
	}

  @PutMapping("/update/{id}")
  @PreAuthorize("hasPermission('GESTION_ABONNEMENTS', 'CREATE')")
	public ResponseEntity<?> updateAbonnement(@PathVariable(value = "id") String id, @Valid @RequestBody AbonnementRequest request) {
    Abonnement abonnement = abonnementService.updateAbonnement(id, request);
		return ResponseEntity.status(HttpStatus.OK).body(abonnement);
	}

  @DeleteMapping("/delete/{id}")
  @PreAuthorize("hasPermission('GESTION_ABONNEMENTS', 'DELETE')")
	public ResponseEntity<?> deleteAbonnement(@PathVariable(value = "id") String id) {
    if (!infosPersoService.findInfosPersoById(id).isPresent())
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Personnel with that id does not exists!");

    abonnementService.deleteAbonnement(id);
    return ResponseEntity.status(HttpStatus.CREATED).body("DELETED");
	}
}
