package com.safelogisitics.gestionentreprisesusers.controller;

import com.safelogisitics.gestionentreprisesusers.model.Evenement;
import com.safelogisitics.gestionentreprisesusers.service.StatistiquesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/statistiques")
@PreAuthorize("hasPermission('GESTION_COMMANDE_LIVRAISONS', 'READ')")
@Api(tags = "Statistiques", description = "Api statistiques")
public class StatistiquesController {

    private StatistiquesService service;

    public StatistiquesController(StatistiquesService service) {
        this.service = service;
    }

    @ApiOperation(value = "Nombre total de clients")
    @GetMapping("/clients")
    public ResponseEntity<?> getNombreClients() {
        Map<String, Long> result = this.service.getNumberClients();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @ApiOperation(value = "Nombre d'abonnements")
    @GetMapping("/abonnements")
    public ResponseEntity<?> getNombreAbonnement() {
        Map<String, Long> result = this.service.getNumberAbonnement();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @ApiOperation(value = "Evènements à venir")
    @GetMapping("/future-events")
    public ResponseEntity<?> getIncomingEvents(Pageable pageable) {
        Page<Evenement> result = this.service.getFutureEvents(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @ApiOperation(value = "Evènements du user connecté")
    @GetMapping("/own-events")
    public ResponseEntity<?> getOwnEvents(Pageable pageable) {
        Page<Evenement> result = this.service.getOwnEvents(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @ApiOperation(value = "Nombre de cartes par type")
    @GetMapping("/cartes-by-type")
    public ResponseEntity<?> getNombreCartesByType() {
        Map<String, Long> results = this.service.getNumberCartes();
        return ResponseEntity.status(HttpStatus.OK).body(results);
    }
}
