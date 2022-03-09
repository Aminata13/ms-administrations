package com.safelogisitics.gestionentreprisesusers.web.controller;

import com.safelogisitics.gestionentreprisesusers.data.dto.request.CommissionRequestDto;
import com.safelogisitics.gestionentreprisesusers.data.dto.request.CommissionSearchRequestDto;
import com.safelogisitics.gestionentreprisesusers.data.dto.request.PayerCommissionsRequestDto;
import com.safelogisitics.gestionentreprisesusers.data.dto.response.BeneficiaireCommissionResponseDto;
import com.safelogisitics.gestionentreprisesusers.data.model.CommissionModel;
import com.safelogisitics.gestionentreprisesusers.data.model.PaiementCommissionModel;
import com.safelogisitics.gestionentreprisesusers.service.CommissionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/commissions")
@PreAuthorize("hasRole('COMPTE_ADMINISTRATEUR')")
@Api(tags = "Gestion des commissions", description = "Api de gestion des commissions")
public class CommissionController {

    @Autowired
    private CommissionService commissionService;

    @ApiOperation(value = "Liste des commissions")
    @GetMapping("/list")
    @PreAuthorize("hasPermission('GESTION_COMMISSIONS', 'READ')")
    public ResponseEntity<Page<CommissionModel>> allCommissions(@Valid CommissionSearchRequestDto commissionSearchRequest, Pageable pageable) {
        Page<CommissionModel> commissions = commissionService.getListCommissions(commissionSearchRequest, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(commissions);
    }

    @ApiOperation(value = "Affichage d'une commission")
    @GetMapping("/{id}")
    @PreAuthorize("hasPermission('GESTION_COMMISSIONS', 'READ')")
    public ResponseEntity<Optional<CommissionModel>> oneCommission(@PathVariable(value = "id") String id) {
        return ResponseEntity.status(HttpStatus.OK).body(commissionService.getOneCommission(id));
    }

    @ApiOperation(value = "Paiement des commissions")
    @PostMapping("/payer")
    @PreAuthorize("hasPermission('GESTION_COMMISSIONS', 'VALIDATE')")
    public ResponseEntity<PaiementCommissionModel> payerCommissions(@Valid @RequestBody PayerCommissionsRequestDto request) {
        PaiementCommissionModel paiement = commissionService.payerCommissions(request);
        return ResponseEntity.status(HttpStatus.OK).body(paiement);
    }

    @ApiOperation(value = "Mise à jour d'une commission")
    @PutMapping("/update/{id}")
    @PreAuthorize("hasPermission('GESTION_COMMISSIONS', 'UPDATE')")
    public ResponseEntity<CommissionModel> updateCommission(@PathVariable(value = "id") String id, @Valid @RequestBody CommissionRequestDto request) {
        CommissionModel commission = commissionService.updateCommission(id, request);

        return ResponseEntity.status(HttpStatus.OK).body(commission);
    }

    @ApiOperation(value = "Suppression d'une commission")
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasPermission('GESTION_COMMISSIONS', 'DELETE')")
    public ResponseEntity<?> deleteCommission(@PathVariable(value = "id") String id) {
        commissionService.deleteCommission(id);
        return ResponseEntity.status(HttpStatus.OK).body(Collections.singletonMap("message", "Supprimé"));
    }

    @ApiOperation(value = "Liste des paiments commissions")
    @GetMapping("paiements/list")
    @PreAuthorize("hasPermission('GESTION_COMMISSIONS', 'READ')")
    public ResponseEntity<Page<PaiementCommissionModel>> allPaiementsCommissions(@Valid CommissionSearchRequestDto commissionSearchRequest, Pageable pageable) {
        Page<PaiementCommissionModel> paiements = commissionService.getListPaiementCommissions(commissionSearchRequest, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(paiements);
    }

    @ApiOperation(value = "Liste des bénéficiaires")
    @GetMapping("beneficiaires/list")
    @PreAuthorize("hasPermission('GESTION_COMMISSIONS', 'READ')")
    public ResponseEntity<List<BeneficiaireCommissionResponseDto>> allBeneficiaires() {
        List<BeneficiaireCommissionResponseDto> beneficiaires = commissionService.getListBeneficiaires();
        return ResponseEntity.status(HttpStatus.OK).body(beneficiaires);
    }
}
