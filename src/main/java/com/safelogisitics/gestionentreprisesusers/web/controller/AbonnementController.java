package com.safelogisitics.gestionentreprisesusers.web.controller;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import com.safelogisitics.gestionentreprisesusers.data.dao.CompteDao;
import com.safelogisitics.gestionentreprisesusers.data.dao.TypeAbonnementDao;
import com.safelogisitics.gestionentreprisesusers.data.dto.kafka.CreatePaiementDto;
import com.safelogisitics.gestionentreprisesusers.data.dto.request.AbonnementRequest;
import com.safelogisitics.gestionentreprisesusers.data.dto.request.ApprouveTransactionRequest;
import com.safelogisitics.gestionentreprisesusers.data.dto.request.EnrollmentRequest;
import com.safelogisitics.gestionentreprisesusers.data.dto.request.PaiementTransactionRequest;
import com.safelogisitics.gestionentreprisesusers.data.dto.request.RechargementTransactionRequest;
import com.safelogisitics.gestionentreprisesusers.data.model.Abonnement;
import com.safelogisitics.gestionentreprisesusers.data.model.Compte;
import com.safelogisitics.gestionentreprisesusers.data.model.InfosPerso;
import com.safelogisitics.gestionentreprisesusers.data.model.PaiementValidation;
import com.safelogisitics.gestionentreprisesusers.data.model.Transaction;
import com.safelogisitics.gestionentreprisesusers.data.model.TypeAbonnement;
import com.safelogisitics.gestionentreprisesusers.data.model.enums.ECompteType;
import com.safelogisitics.gestionentreprisesusers.data.model.enums.ETransactionAction;
import com.safelogisitics.gestionentreprisesusers.data.model.enums.ETransactionType;
import com.safelogisitics.gestionentreprisesusers.service.AbonnementService;
import com.safelogisitics.gestionentreprisesusers.service.InfosPersoService;
import com.safelogisitics.gestionentreprisesusers.service.TransactionService;
import com.safelogisitics.gestionentreprisesusers.web.security.services.UserDetailsImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
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
@RequestMapping("/abonnements")
@Api(tags = "Gestion des abonnements", description = "Api gestion des abonnements")
public class AbonnementController {

  @Autowired
  private KafkaTemplate<String, CreatePaiementDto> createPaiementKafkaTemplate;

  @Value(value = "${kafka.topics.createPaiement.name}")
  private String createPaiementTopicName;

  @Autowired
  private InfosPersoService infosPersoService;

  @Autowired
  private AbonnementService abonnementService;

  @Autowired
  private TransactionService transactionService;

  @Autowired
  private TypeAbonnementDao typeAbonnementDao;

  @Autowired
  private CompteDao compteDao;

  @ApiOperation(value = "Liste de tous les abonnements", tags = "Gestion des abonnements")
  @GetMapping("/type-abonnements/list")
	public ResponseEntity<?> allTypeAbonnements() {
    return ResponseEntity.status(HttpStatus.OK).body(typeAbonnementDao.findAll());
	}

  @ApiOperation(value = "Liste de tous les abonnements", tags = "Gestion des abonnements")
  @GetMapping("/list")
  @PreAuthorize("hasRole('COMPTE_ADMINISTRATEUR') && hasPermission('GESTION_ABONNEMENTS', 'READ')")
	public ResponseEntity<?> allAbonnements(@RequestParam Map<String,String> parameters, @PageableDefault(size = 20) Pageable pageable) {
    return ResponseEntity.status(HttpStatus.OK).body(abonnementService.findByCustomSearch(parameters, pageable));
	}

  @ApiOperation(value = "Liste des abonnements par type d'abonnement", tags = "Gestion des abonnements")
  @GetMapping("/list-by-type/{id}")
  @PreAuthorize("hasRole('COMPTE_ADMINISTRATEUR') && hasPermission('GESTION_ABONNEMENTS', 'READ')")
	public ResponseEntity<?> allAbonnementsByType(@PathVariable(value = "id") String id, @PageableDefault(size = 20) Pageable pageable) {
    Optional<TypeAbonnement> typeAbonnementExist = typeAbonnementDao.findById(id);
    if (!typeAbonnementExist.isPresent()) {
      ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("message", "Type abonnement is not found"));
    }
    return ResponseEntity.status(HttpStatus.OK).body(abonnementService.getAbonnements(typeAbonnementExist.get(), pageable));
	}

  @ApiOperation(value = "Infos d'un abonnement, NB: on passe en paramètre l'id de l'abonnement", tags = "Gestion des abonnements")
  @GetMapping("/get/{id}")
  @PreAuthorize("hasRole('COMPTE_ADMINISTRATEUR') && hasPermission('GESTION_ABONNEMENTS', 'READ')")
	public ResponseEntity<?> getAbonnement(@PathVariable(value = "id") String id) {
    return ResponseEntity.status(HttpStatus.OK).body(abonnementService.getAbonnementById(id));
	}

  @ApiOperation(value = "Infos d'un abonnement d'un client, NB: on passe en paramètre l'id infosPerso du client", tags = "Gestion des abonnements")
  @GetMapping("/get-by-client/{id}")
  @PreAuthorize("hasRole('COMPTE_ADMINISTRATEUR') && hasPermission('GESTION_ABONNEMENTS', 'READ')")
	public ResponseEntity<?> getAbonnementByClient(@PathVariable(value = "id") String id) {
    Optional<Compte> compteExist = compteDao.findByInfosPersoIdAndType(id, ECompteType.COMPTE_PARTICULIER);
    if (!compteExist.isPresent() || compteExist.get().isDeleted()) {
      ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("message", "Compte is not found"));
    }
    return ResponseEntity.status(HttpStatus.OK).body(abonnementService.getAbonnementByCompteClient(compteExist.get()));
	}

  @ApiOperation(value = "Liste des abonnements crées par un admnistrateur, NB: on passe en paramètre l'id infosPerso de l'administrateur", tags = "Gestion des abonnements")
  @GetMapping("/get-by-createur/{id}")
  @PreAuthorize("hasRole('COMPTE_ADMINISTRATEUR') && hasPermission('GESTION_ABONNEMENTS', 'READ')")
	public ResponseEntity<?> getAbonnementByCreateur(@PathVariable(value = "id") String id, @PageableDefault(size = 20) Pageable pageable) {
    Optional<Compte> compteExist = compteDao.findByInfosPersoIdAndType(id, ECompteType.COMPTE_ADMINISTRATEUR);
    if (!compteExist.isPresent() || compteExist.get().isDeleted()) {
      ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("message", "Compte is not found"));
    }
    return ResponseEntity.status(HttpStatus.OK).body(abonnementService.getAbonnementByCompteCreateur(compteExist.get(), pageable));
	}

  @PostMapping("/add")
  @PreAuthorize("hasRole('COMPTE_ADMINISTRATEUR') && hasPermission('GESTION_ABONNEMENTS', 'CREATE')")
	public ResponseEntity<?> addAbonnement(@Valid @RequestBody AbonnementRequest request) {
    Abonnement abonnement = abonnementService.createAbonnement(request, ECompteType.COMPTE_ADMINISTRATEUR);
		return ResponseEntity.status(HttpStatus.CREATED).body(abonnement);
	}

  @PostMapping("/add-by-agent")
  @PostAuthorize("hasRole('COMPTE_ADMINISTRATEUR') or hasRole('COMPTE_COURSIER')")
	public ResponseEntity<?> addAbonnementByAgent(@Valid @RequestBody EnrollmentRequest request) {
    InfosPerso infosPerso = infosPersoService.newEnrollment(request);
    if (infosPerso == null)
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("message", "Informaions d'inscription incomplet"));
		return ResponseEntity.status(HttpStatus.CREATED).body(infosPerso);
	}

  @ApiOperation(value = "Liste des abonnements crées par un admnistrateur ou agent", tags = "Gestion des abonnements")
  @GetMapping("/get-by-agent")
  @PostAuthorize("hasRole('COMPTE_ADMINISTRATEUR') or hasRole('COMPTE_COURSIER')")
	public ResponseEntity<?> getAbonnementByAgent(@PageableDefault(size = 20) Pageable pageable) {
    return ResponseEntity.status(HttpStatus.OK).body(infosPersoService.getMyEnrollments(pageable));
	}

  @PostMapping("/recharger-carte-by-agent")
  @PostAuthorize("hasRole('COMPTE_ADMINISTRATEUR') or hasRole('COMPTE_COURSIER')")
	public ResponseEntity<?> rechargerCarteByAgent(@Valid @RequestBody RechargementTransactionRequest request) {
    Transaction transaction = transactionService.createRechargementTransaction(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(transaction);
	}

  @ApiOperation(value = "Liste des transactions d'un abonnement, NB: on passe en paramètre l'id de l'infosPerso", tags = "Gestion des abonnements")
  @GetMapping("/rechargements/get-by-agent")
  @PostAuthorize("hasRole('COMPTE_ADMINISTRATEUR') or hasRole('COMPTE_COURSIER')")
	public ResponseEntity<?> getRechargementsByAgent(@PageableDefault(size = 20) Pageable pageable) {
    return ResponseEntity.status(HttpStatus.OK).body(transactionService.findByCompteCreateurAndAction(ETransactionAction.RECHARGEMENT, pageable));
	}

  @PutMapping("/changer/{id}")
  @PreAuthorize("hasRole('COMPTE_ADMINISTRATEUR') && hasPermission('GESTION_ABONNEMENTS', 'UPDATE')")
	public ResponseEntity<?> updateAbonnement(@PathVariable(value = "id") String id, @Valid @RequestBody AbonnementRequest request) {
    Abonnement abonnement = abonnementService.changerAbonnement(id, request, ECompteType.COMPTE_ADMINISTRATEUR);
		return ResponseEntity.status(HttpStatus.OK).body(abonnement);
	}

  @DeleteMapping("/delete/{id}")
  @PreAuthorize("hasPermission('GESTION_ABONNEMENTS', 'DELETE')")
	public ResponseEntity<?> deleteAbonnement(@PathVariable(value = "id") String id) {
    if (!infosPersoService.findInfosPersoById(id).isPresent())
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("message", "Personnel with that id does not exists!"));

    abonnementService.deleteAbonnement(id);
    return ResponseEntity.status(HttpStatus.CREATED).body(Collections.singletonMap("message", "Supprimé"));
	}

  @PostMapping("/recharger-carte")
  @PreAuthorize("hasRole('COMPTE_ADMINISTRATEUR') && hasPermission('GESTION_ABONNEMENTS', 'CREATE')")
	public ResponseEntity<?> rechargerCarte(@Valid @RequestBody RechargementTransactionRequest request) {
    Transaction transaction = transactionService.createRechargementTransaction(request, ETransactionType.SOLDE_COMPTE, ECompteType.COMPTE_ADMINISTRATEUR);
		return ResponseEntity.status(HttpStatus.CREATED).body(transaction);
	}

  @PostMapping("/ajouter-points")
  @PreAuthorize("hasRole('COMPTE_ADMINISTRATEUR') && hasPermission('GESTION_ABONNEMENTS', 'POINT_GRATUITE')")
	public ResponseEntity<?> ajouterPoints(@Valid @RequestBody RechargementTransactionRequest request) {
    Transaction transaction = transactionService.createRechargementTransaction(request, ETransactionType.POINT_GRATUITE, ECompteType.COMPTE_ADMINISTRATEUR);
		return ResponseEntity.status(HttpStatus.CREATED).body(transaction);
	}

  @PostMapping("/paiement-validation/sms")
  @PreAuthorize("hasRole('COMPTE_ADMINISTRATEUR') && hasPermission('GESTION_ABONNEMENTS', 'CREATE')")
	public ResponseEntity<?> smsPaiementValidation(@Valid @RequestBody PaiementValidation request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(transactionService.createPaiementValidation(request));
	}

  @PostMapping("/paiement-carte")
  @PreAuthorize("hasRole('COMPTE_ADMINISTRATEUR') && hasPermission('GESTION_ABONNEMENTS', 'CREATE')")
	public ResponseEntity<?> paiementCarte(@Valid @RequestBody PaiementTransactionRequest request) {
    Transaction transaction = transactionService.createPaiementTransaction(request);

    UserDetailsImpl currentUser = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    Compte compteAdmin = compteDao.findByInfosPersoIdAndType(currentUser.getInfosPerso().getId(), ECompteType.COMPTE_ADMINISTRATEUR).get();

    try {
      CreatePaiementDto createPaiementDto = new CreatePaiementDto(
        request.getTypePaiementId(),
        transaction.getReference(),
        transaction.getType(),
        request.getService(),
        request.getServiceId(),
        transaction.getAbonnement().getCompteClient().getId(),
        compteAdmin.getId(),
        transaction.getMontant()
      );

      createPaiementKafkaTemplate.send(createPaiementTopicName, createPaiementDto);
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }

		return ResponseEntity.status(HttpStatus.CREATED).body(transaction);
	}

  @ApiOperation(value = "Liste des transactions d'un abonnement, NB: on passe en paramètre l'id de l'infosPerso", tags = "Gestion des abonnements")
  @GetMapping("/transactions/{id}")
  @PreAuthorize("hasRole('COMPTE_ADMINISTRATEUR') && hasPermission('GESTION_ABONNEMENTS', 'READ')")
	public ResponseEntity<?> listTransactions(@PathVariable(value = "id", required = true) String id, @RequestParam Optional<String> date, @PageableDefault(size = 20) Pageable pageable) {
    if (date.isPresent()) {
      return ResponseEntity.status(HttpStatus.OK).body(transactionService.findByAbonnementAndDateCreation(id, LocalDate.parse(date.get()), pageable));
    }
    return ResponseEntity.status(HttpStatus.OK).body(transactionService.findByAbonnement(id, pageable));
	}

  @ApiOperation(value = "Rapport des transactions d'un abonnement, NB: on passe en paramètre l'id de l'infosPerso", tags = "Gestion des abonnements")
  @GetMapping("/transactions/rapport/pdf")
  @PreAuthorize("hasRole('COMPTE_ADMINISTRATEUR') && hasPermission('GESTION_ABONNEMENTS', 'READ')")
	public ResponseEntity<?> transactionsRapport(
    @RequestParam(required = false) String idClient,
    @RequestParam(required = false) String dateDebut,
    @RequestParam(required = false) String dateFin
  ) {
    ByteArrayInputStream file = transactionService.getRapportByAbonnement(idClient, "pdf", dateDebut, dateFin);

    HttpHeaders headers = new HttpHeaders();
    headers.add("Content-Disposition", "inline; filename=rapport_transactions.pdf");

    return ResponseEntity.status(HttpStatus.OK)
      .headers(headers)
      .contentType(MediaType.APPLICATION_PDF)
      .body(new InputStreamResource(file));
	}

  @ApiOperation(value = "Liste des transactions à approuver", tags = "Gestion des abonnements")
  @GetMapping("/transactions/approbations")
  @PreAuthorize("hasRole('COMPTE_ADMINISTRATEUR') && hasPermission('GESTION_TRANSACTIONS', 'VALIDATE')")
	public ResponseEntity<?> listTransactionsEnApprobations(@PageableDefault(size = 20) Pageable pageable) {
    return ResponseEntity.status(HttpStatus.OK).body(transactionService.findTransactionsEnApprobations(pageable));
	}

  @ApiOperation(value = "Liste des transactions d'un abonnement, NB: on passe en paramètre l'id des transaction", tags = "Gestion des abonnements")
  @PostMapping("/transactions/approbations")
  @PreAuthorize("hasRole('COMPTE_ADMINISTRATEUR') && hasPermission('GESTION_TRANSACTIONS', 'VALIDATE')")
	public ResponseEntity<?> approuveTransaction(@Valid @RequestBody ApprouveTransactionRequest request) {
    return ResponseEntity.status(HttpStatus.OK).body(transactionService.approuveTransaction(request));
	}

  @ApiOperation(value = "Historique des transactions approuver", tags = "Gestion des abonnements")
  @GetMapping("/transactions/approbations/historique")
  @PreAuthorize("hasRole('COMPTE_ADMINISTRATEUR') && hasPermission('GESTION_TRANSACTIONS', 'VALIDATE')")
	public ResponseEntity<?> historiqueTransactionsApprobations(@PageableDefault(size = 20) Pageable pageable) {
    return ResponseEntity.status(HttpStatus.OK).body(transactionService.findMyHistoriqueTransactionsApprobations(pageable));
	}
}
