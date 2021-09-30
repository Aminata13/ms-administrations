package com.safelogisitics.gestionentreprisesusers.controller;


import java.time.LocalDate;
import java.util.Optional;

import javax.validation.Valid;

import com.safelogisitics.gestionentreprisesusers.dao.CompteDao;
import com.safelogisitics.gestionentreprisesusers.dto.CreatePaiementDto;
import com.safelogisitics.gestionentreprisesusers.model.Compte;
import com.safelogisitics.gestionentreprisesusers.model.Transaction;
import com.safelogisitics.gestionentreprisesusers.model.enums.ECompteType;
import com.safelogisitics.gestionentreprisesusers.payload.request.PaiementTransactionRequest;
import com.safelogisitics.gestionentreprisesusers.security.services.UserDetailsImpl;
import com.safelogisitics.gestionentreprisesusers.service.AbonnementService;
import com.safelogisitics.gestionentreprisesusers.service.TransactionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/clients")
@PreAuthorize("hasRole('COMPTE_PARTICULIER')")
@Api(tags = "Mon abonnement", description = "Api de gestion de mon abonnement")
public class ClientAbonnementController {

  @Autowired
  private KafkaTemplate<String, CreatePaiementDto> createPaiementKafkaTemplate;

  @Autowired
  private TransactionService transactionService;

  @Autowired
  private AbonnementService abonnementService;

  @Autowired
  private CompteDao compteDao;

  @Value(value = "${kafka.topics.createPaiement.name}")
  private String createPaiementTopicName;


  @ApiOperation(value = "Infos de mon abonnement", tags = "Mon abonnement")
  @GetMapping("/abonnement")
	public ResponseEntity<?> infosAbonnement() {
    UserDetailsImpl currentUser = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    Optional<Compte> compteExist = compteDao.findByInfosPersoIdAndType(currentUser.getInfosPerso().getId(), ECompteType.COMPTE_PARTICULIER);
    if (!compteExist.isPresent() || compteExist.get().isDeleted()) {
      ResponseEntity.status(HttpStatus.NOT_FOUND).body("Compte is not found");
    }
    return ResponseEntity.status(HttpStatus.OK).body(abonnementService.getAbonnementByCompteClient(compteExist.get()));
	}

  @ApiOperation(value = "Liste des transactions du client connecté", tags = "Mon abonnement")
  @GetMapping("/transactions")
	public ResponseEntity<?> listTransactions(@RequestParam(required = false) Optional<String> date, @PageableDefault(size = 20) Pageable pageable) {
    UserDetailsImpl currentUser = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    String id = currentUser.getInfosPerso().getId();
    if (date.isPresent()) {
      return ResponseEntity.status(HttpStatus.OK).body(transactionService.findByAbonnementAndDateCreation(id, LocalDate.parse(date.get()), pageable));
    }
    return ResponseEntity.status(HttpStatus.OK).body(transactionService.findByAbonnement(id, pageable));
	}

  @ApiOperation(value = "Paiement par carte pour un service", tags = "Mon abonnement")
  @PostMapping("/paiement-carte")
	public ResponseEntity<?> paiementCarte(@Valid @RequestBody PaiementTransactionRequest request) {
    Transaction transaction = transactionService.createPaiementTransaction(request);

    UserDetailsImpl currentUser = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    Compte compteClient = compteDao.findByInfosPersoIdAndType(currentUser.getInfosPerso().getId(), ECompteType.COMPTE_PARTICULIER).get();

    CreatePaiementDto createPaiementDto = new CreatePaiementDto(
      request.getTypePaiementId(),
      transaction.getReference(),
      request.getService(),
      request.getServiceId(),
      transaction.getAbonnement().getCompteClient().getId(),
      compteClient.getId(),
      transaction.getMontant()
    );

    createPaiementKafkaTemplate.send(createPaiementTopicName, createPaiementDto);

		return ResponseEntity.status(HttpStatus.CREATED).body(transaction);
	}
}
