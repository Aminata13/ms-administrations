package com.safelogisitics.gestionentreprisesusers.controller;


import java.util.Date;

import com.safelogisitics.gestionentreprisesusers.service.TransactionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/transactions")
@PostAuthorize("hasRole('COMPTE_ADMINISTRATEUR')")
@Api(tags = "Transactions", description = "Api gestion des transactions")
public class TransactionController {

  @Autowired
  private TransactionService transactionService;


  @ApiOperation(value = "Liste de tous les transactions", tags = "transactions")
  @GetMapping("/list-by-date/{date}")
  @PreAuthorize("hasPermission('GESTION_ABONNEMENTS', 'READ')")
	public ResponseEntity<?> allTransactions(@PathVariable(value = "date") String date, @PageableDefault(size = 20) Pageable pageable) {
    Date dateFilter = new Date(date);
    return ResponseEntity.status(HttpStatus.OK).body(transactionService.findByDateCreation(dateFilter, pageable));
	}
}
