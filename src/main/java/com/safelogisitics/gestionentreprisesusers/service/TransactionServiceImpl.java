package com.safelogisitics.gestionentreprisesusers.service;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.function.Function;

import com.safelogisitics.gestionentreprisesusers.data.dao.AbonnementDao;
import com.safelogisitics.gestionentreprisesusers.data.dao.CompteDao;
import com.safelogisitics.gestionentreprisesusers.data.dao.InfosPersoDao;
import com.safelogisitics.gestionentreprisesusers.data.dao.PaiementValidationDao;
import com.safelogisitics.gestionentreprisesusers.data.dao.TransactionDao;
import com.safelogisitics.gestionentreprisesusers.data.dao.UserDao;
import com.safelogisitics.gestionentreprisesusers.data.dao.filter.TransactionDefaultFields;
import com.safelogisitics.gestionentreprisesusers.data.dto.kafka.PaiementServiceDto;
import com.safelogisitics.gestionentreprisesusers.data.dto.request.ApprouveTransactionRequest;
import com.safelogisitics.gestionentreprisesusers.data.dto.request.PaiementTransactionRequest;
import com.safelogisitics.gestionentreprisesusers.data.dto.request.RechargementTransactionRequest;
import com.safelogisitics.gestionentreprisesusers.data.dto.request.SendSmsRequest;
import com.safelogisitics.gestionentreprisesusers.data.enums.ECompteType;
import com.safelogisitics.gestionentreprisesusers.data.enums.ETransactionAction;
import com.safelogisitics.gestionentreprisesusers.data.enums.ETransactionType;
import com.safelogisitics.gestionentreprisesusers.data.model.Abonnement;
import com.safelogisitics.gestionentreprisesusers.data.model.Compte;
import com.safelogisitics.gestionentreprisesusers.data.model.InfosPerso;
import com.safelogisitics.gestionentreprisesusers.data.model.PaiementValidation;
import com.safelogisitics.gestionentreprisesusers.data.model.PushNotification;
import com.safelogisitics.gestionentreprisesusers.data.model.Transaction;
import com.safelogisitics.gestionentreprisesusers.data.model.User;
import com.safelogisitics.gestionentreprisesusers.web.security.services.UserDetailsImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class TransactionServiceImpl implements TransactionService {

  private static final String S_S_S = "%s%s%s";

  private static final int LENGTH_REFERENCE = 7;

  @Autowired
  private AbonnementDao abonnementDao;

  @Autowired
  private TransactionDao transactionDao;

  @Autowired
  private CompteDao compteDao;

  @Autowired
  private InfosPersoDao infosPersoDao;

  @Autowired
  private UserDao userDao;

  @Autowired
  private PaiementValidationDao paiementValidationDao;

  @Autowired
  MongoTemplate mongoTemplate;

  @Autowired
  PDFGeneratorService PDFGeneratorService;

  @Autowired
  private FirebaseMessagingService firebaseMessagingService;

  @Autowired
  private SMSService smsService;

  @Autowired
	PasswordEncoder encoder;

  @Override
  public Page<Transaction> findByDateCreation(LocalDate _dateCreation, Pageable pageable) {
    LocalDateTime dateCreation = _dateCreation.atTime(LocalTime.parse("00:00"));
    return transactionDao.findByDateCreationOrderByDateCreationDesc(dateCreation, pageable);
  }

  @Override
  public Page<TransactionDefaultFields> findByAbonnement(String infosPersoId, Pageable pageable) {
    Abonnement abonnement = getAbonnementByInfosPerso(infosPersoId);

    return transactionDao.findByAbonnementIdOrderByDateCreationDesc(abonnement.getId(), pageable);
  }

  @Override
  public Page<Transaction> findByAbonnementAndDateCreation(String infosPersoId, LocalDate _dateCreation, Pageable pageable) {
    Abonnement abonnement = getAbonnementByInfosPerso(infosPersoId);

    LocalDateTime dateCreation = _dateCreation.atTime(LocalTime.parse("00:00"));

    return transactionDao.findByAbonnementIdAndDateCreationGreaterThanEqual(abonnement.getId(), dateCreation, pageable);
  }

  @Override
  public Page<Transaction> findByAbonnementAndAction(String infosPersoId, ETransactionAction action, Pageable pageable) {
    Abonnement abonnement = getAbonnementByInfosPerso(infosPersoId);

    return transactionDao.findByAbonnementIdAndActionOrderByDateCreationDesc(abonnement.getId(), action, pageable);
  }

  @Override
  public Page<Transaction> findByAbonnementAndActionAndDateCreation(String infosPersoId, ETransactionAction action, LocalDate _dateCreation, Pageable pageable) {
    Abonnement abonnement = getAbonnementByInfosPerso(infosPersoId);

    LocalDateTime dateCreation = _dateCreation.atTime(LocalTime.parse("00:00"));

    return transactionDao.findByAbonnementIdAndActionAndDateCreationGreaterThanEqual(abonnement.getId(), action, dateCreation, pageable);
  }

  @Override
  public Page<Transaction> findByCompteCreateur(String infosPersoId, Pageable pageable) {
    Compte compteAdmin = getCompteByType(infosPersoId, ECompteType.COMPTE_ADMINISTRATEUR);

    return transactionDao.findByCompteCreateurIdOrderByDateCreationDesc(compteAdmin.getId(), pageable);
  }

  @Override
  public Page<Transaction> findByCompteCreateurAndDateCreation(String infosPersoId, LocalDate _dateCreation, Pageable pageable) {
    Compte compteAdmin = getCompteByType(infosPersoId, ECompteType.COMPTE_ADMINISTRATEUR);

    LocalDateTime dateCreation = _dateCreation.atTime(LocalTime.parse("00:00"));

    return transactionDao.findByCompteCreateurIdAndDateCreationGreaterThanEqual(compteAdmin.getId(), dateCreation, pageable);
  }

  @Override
  public Page<Transaction> findByCompteCreateurAndActionAndDateCreation(String infosPersoId, ETransactionAction action, LocalDate _dateCreation, Pageable pageable) {
    Compte compteAdmin = getCompteByType(infosPersoId, ECompteType.COMPTE_ADMINISTRATEUR);

    LocalDateTime dateCreation = _dateCreation.atTime(LocalTime.parse("00:00"));

    return transactionDao.findByCompteCreateurIdAndActionAndDateCreationGreaterThanEqual(compteAdmin.getId(), action, dateCreation, pageable);
  }

  @Override
  public Page<Map<String, Object>> findByCompteCreateurAndAction(ETransactionAction action, Pageable pageable) {
    UserDetailsImpl currentUser = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    ECompteType compteType = compteDao.existsByInfosPersoIdAndType(currentUser.getInfosPerso().getId(), ECompteType.COMPTE_COURSIER) ? ECompteType.COMPTE_COURSIER : ECompteType.COMPTE_ADMINISTRATEUR;

    Compte compteCreateur = getCompteByType(currentUser.getInfosPerso().getId(), compteType);

    Page<Transaction> transactions = transactionDao.findByCompteCreateurIdAndActionOrderByDateCreationDesc(compteCreateur.getId(), action, pageable);

    return customTransactionsData(transactions);
  }

  @Override
  public Page<Map<String, Object>> findTransactionsEnApprobations(Pageable pageable) {
    Page<Transaction> transactions = transactionDao.findByActionAndApprobation(ETransactionAction.RECHARGEMENT, 0, pageable);
    return customTransactionsData(transactions);
  }

  @Override
  public Page<Map<String, Object>> findMyHistoriqueTransactionsApprobations(Pageable pageable) {
    UserDetailsImpl currentUser = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    Compte approbateur = getCompteByType(currentUser.getInfosPerso().getId(), ECompteType.COMPTE_ADMINISTRATEUR);

    Page<Transaction> transactions = transactionDao.findByApprobateurIdOrderByDateCreationDesc(approbateur.getId(), pageable);
    return customTransactionsData(transactions);
  }

  @Override
  public Map<String, Set<String>> approuveTransaction(ApprouveTransactionRequest request) {
    int approbation = request.getApprobation();
    if (approbation != -1 && approbation != 1) {
      throw new IllegalArgumentException("Approbation invalide!");
    }

    UserDetailsImpl currentUser = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    Compte approbateur = getCompteByType(currentUser.getInfosPerso().getId(), ECompteType.COMPTE_ADMINISTRATEUR);

    Set<String> traites = new HashSet<>();
    Set<String> noTraites = new HashSet<>();

    for (String transactionId : request.getTransactionIds()) {
      Optional<Transaction> _transaction = transactionDao.findByIdAndApprobation(transactionId, 0);
      if (!_transaction.isPresent() || !_transaction.get().getType().equals(ETransactionType.SOLDE_COMPTE)) {
        noTraites.add(transactionId);
        continue;
      }
      Transaction transaction = _transaction.get();
      if (approbation == 1) {
        Abonnement abonnement = abonnementDao.findById(transaction.getAbonnement().getId()).get();
        BigDecimal montant = transaction.getMontant();
        abonnement.rechargerSolde(montant);
        abonnementDao.save(abonnement);
        transaction.setNouveauSolde(abonnement.getSolde());
      }
      transaction.setApprobation(approbation);
      transaction.setApprobateur(approbateur);
      transaction.setDateApprobation(LocalDateTime.now());
      transactionDao.save(transaction);

      traites.add(transactionId);
    }

    Map<String, Set<String>> data = new LinkedHashMap<>();
    data.put("traites", traites);
    data.put("noTraites", noTraites);

    return data;
  }

  @Override
  public Optional<Transaction> findByReference(String reference) {
    return transactionDao.findByReference(reference);
  }

  @Override
  public Transaction createRechargementTransaction(RechargementTransactionRequest transactionRequest, ETransactionType transactionType, ECompteType type) {
    UserDetailsImpl currentUser = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    Compte compteCreateur = getCompteByType(currentUser.getInfosPerso().getId(), type);

    Optional<Abonnement> abonnementExist = abonnementDao.findByNumeroCarte(transactionRequest.getNumeroCarte());

    if (!abonnementExist.isPresent() || abonnementExist.get().isDeleted()) {
      throw new IllegalArgumentException("Abonnement with that client does not exist!");
    }

    if (abonnementExist.get().isCarteBloquer()) {
      throw new IllegalArgumentException("Cette carte est bloqué!");
    }

    Abonnement abonnement = abonnementExist.get();

    String reference = genererReferenceTransction(ETransactionAction.RECHARGEMENT);

    Transaction transaction = new Transaction(abonnement, reference, ETransactionAction.RECHARGEMENT, compteCreateur);

    if (transactionType.equals(ETransactionType.POINT_GRATUITE)) {
      if (transactionRequest.getPoints() == null) {
        throw new IllegalArgumentException("Veuillez donner le nombre de points à offrir.");
      }
      abonnement.rechargerPointGratuites(transactionRequest.getPoints().longValue());
      abonnementDao.save(abonnement);

      transaction.setPoints(transactionRequest.getPoints());
      transaction.setTotalPoints(abonnement.getPointGratuites());
      transaction.setApprobation(1);
    } else {
      if (transactionRequest.getMontant() == null) {
        throw new IllegalArgumentException("Veuillez donner le montant à recharger.");
      }
      transaction.setMontant(transactionRequest.getMontant());
      transaction.setApprobation(0);
    }

    transaction.setType(transactionType);

    transactionDao.save(transaction);

    if (transaction.getType().equals(ETransactionType.SOLDE_COMPTE)) {
      try {
        Map<String, String> data = new HashMap<>();
  
        PushNotification pushNotification = new PushNotification(
          String.valueOf("Nouveau rechargement"),
          String.valueOf("Vous avez un nouveau rechargement à valider."),
          String.valueOf("validation-nouveau-rechargement")
        );
  
        data.put("transactionId", transaction.getId());
  
        pushNotification.setData(data);
  
        String resp = this.firebaseMessagingService.sendNotification(pushNotification);
        System.out.println("RESPONSE ================================> "+resp);
      } catch (Exception e) {
        System.out.println(e.getMessage());
      }
    }

    return transaction;
  }

  @Override
  public Transaction createRechargementTransaction(RechargementTransactionRequest transactionRequest) {
    UserDetailsImpl currentUser = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    Optional<User> userExist = userDao.findByInfosPersoId(currentUser.getInfosPerso().getId());

    if (transactionRequest.getAgentPassword() == null || !userExist.isPresent() || !encoder.matches(transactionRequest.getAgentPassword(), userExist.get().getPassword()))
      throw new UsernameNotFoundException("Mot de passe invalide!");

    ECompteType type = compteDao.existsByInfosPersoIdAndType(currentUser.getInfosPerso().getId(), ECompteType.COMPTE_COURSIER) ? ECompteType.COMPTE_COURSIER : ECompteType.COMPTE_ADMINISTRATEUR;

    Transaction transaction = createRechargementTransaction(transactionRequest, ETransactionType.SOLDE_COMPTE, type);

    return transaction;
  }

  @Override
  public Boolean createPaiementValidation(PaiementValidation paiementValidationRequest) {
    Optional<Abonnement> abonnementExist = abonnementDao.findByNumeroCarte(paiementValidationRequest.getNumeroCarte());

    if (!abonnementExist.isPresent() || abonnementExist.get().isDeleted() || abonnementExist.get().getCompteClient().isDeleted()) {
      throw new IllegalArgumentException("Cette abonnement n'existe pas.");
    }

    if (abonnementExist.get().isCarteBloquer()) {
      throw new IllegalArgumentException("Cette carte est bloqué");
    }

    Abonnement abonnement = abonnementExist.get();

    InfosPerso infosPerso = infosPersoDao.findById(abonnement.getCompteClient().getInfosPersoId()).get();

    Optional<PaiementValidation> oldPaiementValidation = paiementValidationDao.findByNumeroCommande(paiementValidationRequest.getNumeroCommande());

    if (oldPaiementValidation.isPresent()) {
      paiementValidationDao.delete(oldPaiementValidation.get());
    }

    String codeValidation = this.genererCodeValidation(paiementValidationRequest.getNumeroCarte());

    PaiementValidation paiementValidation = new PaiementValidation(paiementValidationRequest.getNumeroCarte(),
      paiementValidationRequest.getNumeroCommande(), paiementValidationRequest.getService(), paiementValidationRequest.getMontant(), codeValidation);

    paiementValidationDao.save(paiementValidation);

    String smsText  = String.format("Bonjour %s. Le code de validation de votre commande est: %s. Le code expire dans 5 minutes.\nSafelogistics vous remercie.\nService commercial : 78 306 45 45", 
    infosPerso.getNomComplet(), paiementValidation.getCodeValidation());

    SendSmsRequest sms = new SendSmsRequest("RAK IN TAK", "Code de validation paiement", smsText, Arrays.asList(infosPerso.getTelephone()));

    smsService.sendSms(sms);

    return true;
  }

  @Override
  public Transaction createPaiementTransaction(PaiementTransactionRequest transactionRequest) {
    Optional<PaiementValidation> _paiementValidation = paiementValidationDao.findByCodeValidationAndNumeroCommande(
      transactionRequest.getCodeValidation(), transactionRequest.getNumeroCommande());

    if (!_paiementValidation.isPresent() || _paiementValidation.get().getApprobation() == true || !_paiementValidation.get().getNumeroCarte().equals(transactionRequest.getNumeroCarte())) {
      throw new UsernameNotFoundException("Code de validation invalide.");
    }

    PaiementValidation paiementValidation = _paiementValidation.get();

    LocalDateTime dateNow = LocalDateTime.now();
    LocalDateTime previous = paiementValidation.getDateCreation().plusMinutes(5);

    if (!dateNow.isBefore(previous)) {
      throw new UsernameNotFoundException("Code de validation expirée.");
    }

    Abonnement abonnement = abonnementDao.findByNumeroCarte(paiementValidation.getNumeroCarte()).get();

    InfosPerso infosPerso = infosPersoDao.findById(abonnement.getCompteClient().getInfosPersoId()).get();

    UserDetailsImpl currentUser = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    Optional<Compte> compteClientExist = null;

    if (transactionRequest.getClientId() != null) {
      compteClientExist = compteDao.findByIdAndType(transactionRequest.getClientId(), ECompteType.COMPTE_PARTICULIER);
    } else {
      compteClientExist = compteDao.findByInfosPersoIdAndType(currentUser.getInfosPerso().getId(), ECompteType.COMPTE_PARTICULIER);
    }

    if (!compteClientExist.isPresent() || compteClientExist.get().isDeleted()) {
      throw new IllegalArgumentException("Ce compte client n'existe pas");
    }

    if (transactionDao.existsByNumeroCommande(transactionRequest.getNumeroCommande())) {
      throw new UsernameNotFoundException("Cette commande est déjà payée.");
    }

    Compte compteClient = compteClientExist.get();

    String reference = genererReferenceTransction(ETransactionAction.PAIEMENT);

    Transaction transaction = new Transaction(abonnement, reference, ETransactionAction.PAIEMENT, compteClient);

    if (transactionRequest.getCompteDebiter().equals(ETransactionType.POINT_GRATUITE)) {
      if (transactionRequest.getPoints() == null) {
        throw new IllegalArgumentException("Nombre de points à débiter invalide.");
      }
      if (abonnement.getPointGratuites() < transactionRequest.getPoints().longValue()) {
        throw new IllegalArgumentException(String.format("Nombre de points gratuite insuffisant, total points gratuites actuel:", abonnement.getPointGratuites()));
      }
      abonnement.debiterPointGratuites(transactionRequest.getPoints().longValue());
      transaction.setPoints(transactionRequest.getPoints().longValue());
      transaction.setTotalPoints(abonnement.getPointGratuites());
    } else {
      if (transactionRequest.getMontant() == null) {
        throw new UsernameNotFoundException("Montant invalide");
      }
      if (abonnement.getSolde().compareTo(transactionRequest.getMontant()) == -1) {
        throw new IllegalArgumentException(String.format("Solde insuffisant, solde actuel:", abonnement.getSolde()));
      }
      abonnement.debiterSolde(transactionRequest.getMontant());
      transaction.setMontant(transactionRequest.getMontant());
      transaction.setNouveauSolde(abonnement.getSolde());
    }

    abonnementDao.save(abonnement);

    transaction.setType(transactionRequest.getCompteDebiter());
    transaction.setService(transactionRequest.getService());
    transaction.setNumeroCommande(transactionRequest.getNumeroCommande());

    transactionDao.save(transaction);

    paiementValidation.setApprobation(true);

    paiementValidationDao.save(paiementValidation);

    String smsText  = String.format("Bonjour %s,\nSuite au paiement de votre commande n° %s, nous vous informons que votre solde actuel est de %sFCFA.\nPour recharger votre compte vous pouvez le faire via \n• WAVE : xxxxxx\n• OM : xxxxxxxxx\n• Espèces (dans nos locaux ou points relais)\nSafelogistics vous remercie\nService commercial : 78 306 45 45", 
    infosPerso.getNomComplet(), transaction.getNumeroCommande(), abonnement.getSolde());

    SendSmsRequest sms = new SendSmsRequest("RAK IN TAK", "Paiement commande", smsText, Arrays.asList(infosPerso.getTelephone()));

    smsService.sendSms(sms);

    return transaction;
  }

  public void annulerPaiementTransaction(PaiementServiceDto paiementServiceDto) {
    Optional<Transaction> _transaction = transactionDao.findByReference(paiementServiceDto.getServiceReference());
    if (!_transaction.isPresent() || !_transaction.get().getAction().equals(ETransactionAction.PAIEMENT)) {
      return;
    }
    Compte compteAnnulateur = compteDao.findById(paiementServiceDto.getAnnulateurId()).get();
    Transaction transaction = _transaction.get();
    Abonnement abonnement = abonnementDao.findById(transaction.getAbonnement().getId()).get();
    BigDecimal montant = transaction.getMontant();

    abonnement.rechargerSolde(montant);
    abonnementDao.save(abonnement);

    Optional<PaiementValidation> paiementValidation = paiementValidationDao.findByNumeroCommande(transaction.getNumeroCommande());
    if (paiementValidation.isPresent()) {
      paiementValidationDao.delete(paiementValidation.get());
    }

    transactionDao.delete(transaction);

    String reference = genererReferenceTransction(ETransactionAction.RECHARGEMENT);
    Transaction rechargeTransaction = new Transaction(abonnement, reference, ETransactionAction.RECHARGEMENT, compteAnnulateur, montant);
    rechargeTransaction.setApprobation(1);
    rechargeTransaction.setApprobateur(compteAnnulateur);
    rechargeTransaction.setDateApprobation(LocalDateTime.now());
    transactionDao.save(rechargeTransaction);
  }

  @Override
  public ByteArrayInputStream getRapportByAbonnement(String idClient, String rapportType, String dateDebut, String dateFin) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    final Query query = new Query().with(Sort.by(Sort.Direction.DESC, "abonnement.compteClient.infosPersoId", "dateCreation"));

    final List<Criteria> criteria = new ArrayList<>();

    criteria.add(Criteria.where("abonnement.compteClient.deleted").is(false));

    if (idClient != null && !idClient.isEmpty())
      criteria.add(Criteria.where("abonnement.compteClient.infosPersoId").is(idClient));

    if (dateDebut != null && !dateDebut.isEmpty())
      criteria.add(Criteria.where("dateCreation").gte(LocalDateTime.parse(dateDebut, formatter)));

    if (dateFin != null && !dateFin.isEmpty())
      criteria.add(Criteria.where("dateCreation").lte(LocalDateTime.parse(dateFin, formatter)));

    if (!criteria.isEmpty())
      query.addCriteria(new Criteria().andOperator(criteria.toArray(new Criteria[criteria.size()])));

    List<Transaction> transactions = mongoTemplate.find(query, Transaction.class);

    if (transactions == null || transactions.isEmpty())
      return null;

    return PDFGeneratorService.exportToPdf(transactions, idClient, dateDebut, dateFin);
  }

  private Abonnement getAbonnementByInfosPerso(String infosPersoId) {
    Optional<Compte> compteClientExist = compteDao.findByInfosPersoIdAndType(infosPersoId, ECompteType.COMPTE_PARTICULIER);

    if (!compteClientExist.isPresent() || compteClientExist.get().isDeleted()) {
      throw new IllegalArgumentException("CompteClient with that id does not exists!");
    }

    Compte compteClient = compteClientExist.get();

    Optional<Abonnement> abonnementExist = abonnementDao.findByCompteClientId(compteClient.getId());

    if (!abonnementExist.isPresent() || abonnementExist.get().isDeleted()) {
      throw new IllegalArgumentException("Abonnement with that client does not exist!");
    }

    return abonnementExist.get();
  }

  private Compte getCompteByType(String infosPersoId, ECompteType type) {
    Optional<Compte> compteAdminExist = compteDao.findByInfosPersoIdAndType(infosPersoId, type);

    if (!compteAdminExist.isPresent() || compteAdminExist.get().isDeleted()) {
      throw new IllegalArgumentException("Compte with that id does not exists!");
    }

    return compteAdminExist.get();
  }

  private String genererReferenceTransction(ETransactionAction action) {
    int m = (int) Math.pow(10, LENGTH_REFERENCE - 1);
    int randomInt = m + new Random().nextInt(9 * m);
    String year = String.valueOf((LocalDate.now()).getYear()).substring(4 - 2);
    String type = action.equals(ETransactionAction.RECHARGEMENT) ? "1" : "0";

    String referenceTransction = String.format(S_S_S, type, year, randomInt);

    while (transactionDao.existsByReference(referenceTransction)) {
      referenceTransction = String.format(S_S_S, year, genererReferenceTransction(action));
    }

    return referenceTransction;
  }

  private String genererCodeValidation(String numeroCarte) {
    int m = (int) Math.pow(10, 6 - 1);
    int randomInt = m + new Random().nextInt(9 * m);

    String codeValidation = String.valueOf(randomInt);

    while (paiementValidationDao.existsByNumeroCarteAndCodeValidation(numeroCarte, codeValidation)) {
      codeValidation = genererCodeValidation(numeroCarte);
    }

    return codeValidation;
  }

  private Page<Map<String, Object>> customTransactionsData(Page<Transaction> transactions) {
    Page<Map<String, Object>> customData = transactions.map(new Function<Transaction, Map<String, Object>>() {
      @Override
      public Map<String, Object> apply(Transaction transaction) {
        Map<String, Object> customFields = new LinkedHashMap<>();
        Map<String, Object> abonnement = new LinkedHashMap<>();

        Abonnement _abonnement = transaction.getAbonnement();

        abonnement.put("id", _abonnement.getId());
        abonnement.put("numeroCarte", _abonnement.getNumeroCarte());
        abonnement.put("typeAbonnement", _abonnement.getTypeAbonnement());
        abonnement.put("solde", _abonnement.getSolde());
        abonnement.put("compteClient", infosPersoDao.findById(_abonnement.getCompteClient().getInfosPersoId()).get().getDefaultFields());
        abonnement.put("dateCreation", _abonnement.getDateCreation());

        customFields.put("id", transaction.getId());
        customFields.put("type", transaction.getType());
        customFields.put("action", transaction.getAction());
        customFields.put("reference", transaction.getReference());
        customFields.put("numeroCommande", transaction.getNumeroCommande());
        customFields.put("abonnement", abonnement);
        customFields.put("compteCreateur", infosPersoDao.findById(transaction.getCompteCreateur().getInfosPersoId()).get().getDefaultFields());
        customFields.put("montant", transaction.getMontant());
        customFields.put("points", transaction.getPoints());
        customFields.put("nouveauSolde", transaction.getNouveauSolde());
        customFields.put("totalPoints", transaction.getTotalPoints());
        customFields.put("approbation", transaction.getApprobation());
        customFields.put("dateCreation", transaction.getDateCreation());

        return customFields;
      }
    });

    return customData;
  }
}
