package com.safelogisitics.gestionentreprisesusers.controller;

import com.safelogisitics.gestionentreprisesusers.model.ConsommationCarburant;
import com.safelogisitics.gestionentreprisesusers.payload.request.ConsommationCarburantRequest;
import com.safelogisitics.gestionentreprisesusers.service.ConsommationCarburantService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/consommation-carburant")
@Tag(name = "Gestion des consommations de carburant")
public class ConsommationCarburantController {

    private ConsommationCarburantService service;

    public ConsommationCarburantController(ConsommationCarburantService service) {
        this.service = service;
    }

    @PreAuthorize("hasPermission('GESTION_CARBURANTS', 'READ')")
    @GetMapping("/list")
    public ResponseEntity<?> getHistoriqueConso(@RequestParam(required = false) String dateDebut, @RequestParam(required = false) String dateFin, @RequestParam(required = false) String moyenTransportId, Pageable pageable) {
        Page<ConsommationCarburant> result = this.service.getConsommationCarburants(dateDebut, dateFin, moyenTransportId, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @PreAuthorize("hasPermission('GESTION_CARBURANTS', 'CREATE')")
    @PostMapping("/add")
    public ResponseEntity<?> addConsommationCarburant(@Valid @RequestBody ConsommationCarburantRequest consommationCarburantRequest) {
        ConsommationCarburant consommationCarburant = this.service.addConsommationCarburant(consommationCarburantRequest);
        return ResponseEntity.status(HttpStatus.OK).body(consommationCarburant);
    }

    @PreAuthorize("hasPermission('GESTION_CARBURANTS', 'UPDATE')")
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateConsommationCarburant(@PathVariable(value = "id") String id, @Valid @RequestBody ConsommationCarburantRequest consommationCarburantRequest) {
        ConsommationCarburant consommationCarburant = this.service.updateConsommationCarburant(id, consommationCarburantRequest);
        return ResponseEntity.status(HttpStatus.OK).body(consommationCarburant);
    }
}
