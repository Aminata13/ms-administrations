package com.safelogisitics.gestionentreprisesusers.controller;

import java.time.LocalDate;
import java.util.Optional;

import javax.validation.Valid;

import com.safelogisitics.gestionentreprisesusers.dao.CompteDao;
import com.safelogisitics.gestionentreprisesusers.dao.TypeAbonnementDao;
import com.safelogisitics.gestionentreprisesusers.dto.CreatePaiementDto;
import com.safelogisitics.gestionentreprisesusers.model.Abonnement;
import com.safelogisitics.gestionentreprisesusers.model.Compte;
import com.safelogisitics.gestionentreprisesusers.model.InfosPerso;
import com.safelogisitics.gestionentreprisesusers.model.Transaction;
import com.safelogisitics.gestionentreprisesusers.model.TypeAbonnement;
import com.safelogisitics.gestionentreprisesusers.model.enums.ECompteType;
import com.safelogisitics.gestionentreprisesusers.model.enums.ETransactionAction;
import com.safelogisitics.gestionentreprisesusers.payload.request.AbonnementRequest;
import com.safelogisitics.gestionentreprisesusers.payload.request.ApprouveTransactionRequest;
import com.safelogisitics.gestionentreprisesusers.payload.request.EnrollmentRequest;
import com.safelogisitics.gestionentreprisesusers.payload.request.PaiementTransactionRequest;
import com.safelogisitics.gestionentreprisesusers.payload.request.RechargementTransactionRequest;
import com.safelogisitics.gestionentreprisesusers.service.AbonnementService;
import com.safelogisitics.gestionentreprisesusers.service.InfosPersoService;
import com.safelogisitics.gestionentreprisesusers.service.TransactionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
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
  @PostAuthorize("hasRole('COMPTE_ADMINISTRATEUR')")
  @PreAuthorize("hasPermission('GESTION_ABONNEMENTS', 'READ')")
	public ResponseEntity<?> allAbonnements(@PageableDefault(size = 20) Pageable pageable) {
    return ResponseEntity.status(HttpStatus.OK).body(abonnementService.getAbonnements(pageable));
	}

  @ApiOperation(value = "Liste des abonnements par type d'abonnement", tags = "Gestion des abonnements")
  @GetMapping("/list-by-type/{id}")
  @PostAuthorize("hasRole('COMPTE_ADMINISTRATEUR')")
  @PreAuthorize("hasPermission('GESTION_ABONNEMENTS', 'READ')")
	public ResponseEntity<?> allAbonnementsByType(@PathVariable(value = "id") String id, @PageableDefault(size = 20) Pageable pageable) {
    Optional<TypeAbonnement> typeAbonnementExist = typeAbonnementDao.findById(id);
    if (!typeAbonnementExist.isPresent()) {
      ResponseEntity.status(HttpStatus.NOT_FOUND).body("Type abonnement is not found");
    }
    return ResponseEntity.status(HttpStatus.OK).body(abonnementService.getAbonnements(typeAbonnementExist.get(), pageable));
	}

  @ApiOperation(value = "Infos d'un abonnement, NB: on passe en paramètre l'id de l'abonnement", tags = "Gestion des abonnements")
  @GetMapping("/get/{id}")
  @PostAuthorize("hasRole('COMPTE_ADMINISTRATEUR')")
  @PreAuthorize("hasPermission('GESTION_ABONNEMENTS', 'READ')")
	public ResponseEntity<?> getAbonnement(@PathVariable(value = "id") String id) {
    return ResponseEntity.status(HttpStatus.OK).body(abonnementService.getAbonnementById(id));
	}

  @ApiOperation(value = "Infos d'un abonnement d'un client, NB: on passe en paramètre l'id infosPerso du client", tags = "Gestion des abonnements")
  @GetMapping("/get-by-client/{id}")
  @PostAuthorize("hasRole('COMPTE_ADMINISTRATEUR')")
  @PreAuthorize("hasPermission('GESTION_ABONNEMENTS', 'READ')")
	public ResponseEntity<?> getAbonnementByClient(@PathVariable(value = "id") String id) {
    Optional<Compte> compteExist = compteDao.findByInfosPersoIdAndType(id, ECompteType.COMPTE_PARTICULIER);
    if (!compteExist.isPresent() || compteExist.get().isDeleted()) {
      ResponseEntity.status(HttpStatus.NOT_FOUND).body("Compte is not found");
    }
    return ResponseEntity.status(HttpStatus.OK).body(abonnementService.getAbonnementByCompteClient(compteExist.get()));
	}

  @ApiOperation(value = "Liste des abonnements crées par un admnistrateur, NB: on passe en paramètre l'id infosPerso de l'administrateur", tags = "Gestion des abonnements")
  @GetMapping("/get-by-createur/{id}")
  @PostAuthorize("hasRole('COMPTE_ADMINISTRATEUR')")
  @PreAuthorize("hasPermission('GESTION_ABONNEMENTS', 'READ')")
	public ResponseEntity<?> getAbonnementByCreateur(@PathVariable(value = "id") String id, @PageableDefault(size = 20) Pageable pageable) {
    Optional<Compte> compteExist = compteDao.findByInfosPersoIdAndType(id, ECompteType.COMPTE_ADMINISTRATEUR);
    if (!compteExist.isPresent() || compteExist.get().isDeleted()) {
      ResponseEntity.status(HttpStatus.NOT_FOUND).body("Compte is not found");
    }
    return ResponseEntity.status(HttpStatus.OK).body(abonnementService.getAbonnementByCompteCreateur(compteExist.get(), pageable));
	}

  @PostMapping("/add")
  @PostAuthorize("hasRole('COMPTE_ADMINISTRATEUR')")
  @PreAuthorize("hasPermission('GESTION_ABONNEMENTS', 'CREATE')")
	public ResponseEntity<?> addAbonnement(@Valid @RequestBody AbonnementRequest request) {
    Abonnement abonnement = abonnementService.createAbonnement(request, ECompteType.COMPTE_ADMINISTRATEUR);
		return ResponseEntity.status(HttpStatus.CREATED).body(abonnement);
	}

  @PostMapping("/add-by-agent")
  @PostAuthorize("hasRole('COMPTE_ADMINISTRATEUR') or hasRole('COMPTE_COURSIER')")
	public ResponseEntity<?> addAbonnementByAgent(@Valid @RequestBody EnrollmentRequest request) {
    InfosPerso infosPerso = infosPersoService.newEnrollment(request);
    if (infosPerso == null)
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Informaions d'inscription incomplet");
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
  @PostAuthorize("hasRole('COMPTE_ADMINISTRATEUR')")
  @PreAuthorize("hasPermission('GESTION_ABONNEMENTS', 'WRITE')")
	public ResponseEntity<?> updateAbonnement(@PathVariable(value = "id") String id, @Valid @RequestBody AbonnementRequest request) {
    Abonnement abonnement = abonnementService.changerAbonnement(id, request, ECompteType.COMPTE_ADMINISTRATEUR);
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

  @PostMapping("/recharger-carte")
  @PostAuthorize("hasRole('COMPTE_ADMINISTRATEUR')")
  @PreAuthorize("hasPermission('GESTION_ABONNEMENTS', 'CREATE')")
	public ResponseEntity<?> rechargerCarte(@Valid @RequestBody RechargementTransactionRequest request) {
    Transaction transaction = transactionService.createRechargementTransaction(request, ECompteType.COMPTE_ADMINISTRATEUR);
		return ResponseEntity.status(HttpStatus.CREATED).body(transaction);
	}

  @PostMapping("/paiement-carte")
  @PostAuthorize("hasRole('COMPTE_ADMINISTRATEUR')")
  @PreAuthorize("hasPermission('GESTION_ABONNEMENTS', 'CREATE')")
	public ResponseEntity<?> paiementCarte(@Valid @RequestBody PaiementTransactionRequest request) {
    Transaction transaction = transactionService.createPaiementTransaction(request);

    try {
      CreatePaiementDto createPaiementDto = new CreatePaiementDto(
        request.getTypePaiementId(),
        transaction.getReference(),
        request.getService(),
        request.getServiceId(),
        transaction.getAbonnement().getCompteClient().getId(),
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
  @PostAuthorize("hasRole('COMPTE_ADMINISTRATEUR')")
  @PreAuthorize("hasPermission('GESTION_ABONNEMENTS', 'READ')")
	public ResponseEntity<?> listTransactions(@PathVariable(value = "id", required = true) String id, @RequestParam Optional<String> date, @PageableDefault(size = 20) Pageable pageable) {
    if (date.isPresent()) {
      return ResponseEntity.status(HttpStatus.OK).body(transactionService.findByAbonnementAndDateCreation(id, LocalDate.parse(date.get()), pageable));
    }
    return ResponseEntity.status(HttpStatus.OK).body(transactionService.findByAbonnement(id, pageable));
	}

  @ApiOperation(value = "Liste des transactions à approuver", tags = "Gestion des abonnements")
  @GetMapping("/transactions/approbations")
  @PostAuthorize("hasRole('COMPTE_ADMINISTRATEUR')")
  @PreAuthorize("hasPermission('GESTION_TRANSACTIONS', 'VALIDATE')")
	public ResponseEntity<?> listTransactionsEnApprobations(@PageableDefault(size = 20) Pageable pageable) {
    return ResponseEntity.status(HttpStatus.OK).body(transactionService.findTransactionsEnApprobations(pageable));
	}

  @ApiOperation(value = "Liste des transactions d'un abonnement, NB: on passe en paramètre l'id des transaction", tags = "Gestion des abonnements")
  @PostMapping("/transactions/approbations")
  @PostAuthorize("hasRole('COMPTE_ADMINISTRATEUR')")
  @PreAuthorize("hasPermission('GESTION_TRANSACTIONS', 'VALIDATE')")
	public ResponseEntity<?> approuveTransaction(@Valid @RequestBody ApprouveTransactionRequest request) {
    return ResponseEntity.status(HttpStatus.OK).body(transactionService.approuveTransaction(request));
	}
}
