package com.safelogisitics.gestionentreprisesusers.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.function.Function;

import com.safelogisitics.gestionentreprisesusers.dao.AbonnementDao;
import com.safelogisitics.gestionentreprisesusers.dao.CompteDao;
import com.safelogisitics.gestionentreprisesusers.dao.InfosPersoDao;
import com.safelogisitics.gestionentreprisesusers.dao.TransactionDao;
import com.safelogisitics.gestionentreprisesusers.dao.UserDao;
import com.safelogisitics.gestionentreprisesusers.dao.filter.TransactionDefaultFields;
import com.safelogisitics.gestionentreprisesusers.model.Abonnement;
import com.safelogisitics.gestionentreprisesusers.model.Compte;
import com.safelogisitics.gestionentreprisesusers.model.InfosPerso;
import com.safelogisitics.gestionentreprisesusers.model.Transaction;
import com.safelogisitics.gestionentreprisesusers.model.User;
import com.safelogisitics.gestionentreprisesusers.model.enums.ECompteType;
import com.safelogisitics.gestionentreprisesusers.model.enums.ETransactionAction;
import com.safelogisitics.gestionentreprisesusers.payload.request.ApprouveTransactionRequest;
import com.safelogisitics.gestionentreprisesusers.payload.request.PaiementTransactionRequest;
import com.safelogisitics.gestionentreprisesusers.payload.request.RechargementTransactionRequest;
import com.safelogisitics.gestionentreprisesusers.security.services.UserDetailsImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
  public Page<Transaction> findByCompteCreateurAndActionAndDateCreation(ETransactionAction action, LocalDate _dateCreation, Pageable pageable) {
    UserDetailsImpl currentUser = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    ECompteType compteType = compteDao.existsByInfosPersoIdAndType(currentUser.getInfosPerso().getId(), ECompteType.COMPTE_COURSIER) ? ECompteType.COMPTE_COURSIER : ECompteType.COMPTE_ADMINISTRATEUR;

    Compte compteCreateur = getCompteByType(currentUser.getInfosPerso().getId(), compteType);

    LocalDateTime dateCreation = _dateCreation.atTime(LocalTime.parse("00:00"));

    return transactionDao.findByCompteCreateurIdAndActionAndDateCreationGreaterThanEqual(compteCreateur.getId(), action, dateCreation, pageable);
  }

  @Override
  public Page<Map<String, Object>> findTransactionsEnApprobations(Pageable pageable) {
    Page<Transaction> transactions = transactionDao.findByApprobation(0, pageable);

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
        abonnement.put("compteClient", infosPersoDao.findById(_abonnement.getCompteClient().getInfosPersoId()));
        abonnement.put("dateCreation", _abonnement.getDateCreation());

        customFields.put("id", transaction.getId());
        customFields.put("action", transaction.getAction());
        customFields.put("reference", transaction.getReference());
        customFields.put("abonnement", abonnement);
        customFields.put("compteCreateur", infosPersoDao.findById(transaction.getCompteCreateur().getInfosPersoId()));
        customFields.put("montant", transaction.getMontant());
        customFields.put("approbation", transaction.getApprobation());
        customFields.put("dateCreation", transaction.getDateCreation());

        return customFields;
      }
    });

    return customData;
  }

  @Override
  public Map<String, Set<String>> approuveTransaction(ApprouveTransactionRequest request) {
    int approbation = request.getApprobation();
    if (approbation != -1 && approbation != 1) {
      throw new IllegalArgumentException("Approbation invalide!");
    }

    Set<String> traites = new HashSet<>();
    Set<String> noTraites = new HashSet<>();

    for (String transactionId : request.getTransactionIds()) {
      Optional<Transaction> _transaction = transactionDao.findByIdAndApprobation(transactionId, 0);
      if (!_transaction.isPresent()) {
        noTraites.add(transactionId);
        continue;
      }
      Transaction transaction = _transaction.get();
      if (approbation == 1) {
        Abonnement abonnement = transaction.getAbonnement();
        BigDecimal montant = transaction.getMontant();
        abonnement.rechargerCarte(montant);
        abonnementDao.save(abonnement);
        transaction.setNouveauSolde(abonnement.getSolde());
        transaction.setApprobation(approbation);
        transactionDao.save(transaction);
      } else {
        transactionDao.delete(transaction);
      }
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
  public Transaction createRechargementTransaction(RechargementTransactionRequest transactionRequest, ECompteType type) {
    UserDetailsImpl currentUser = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    Compte compteCreateur = getCompteByType(currentUser.getInfosPerso().getId(), type);

    Optional<Abonnement> abonnementExist = abonnementDao.findByNumeroCarte(transactionRequest.getNumeroCarte());

    if (!abonnementExist.isPresent() || abonnementExist.get().isDeleted()) {
      throw new IllegalArgumentException("Abonnement with that client does not exist!");
    }

    if (abonnementExist.get().isCarteBloquer()) {
      throw new IllegalArgumentException("Cette carte est bloqué!");
    }

    BigDecimal montant = transactionRequest.getMontant();

    String reference = genererReferenceTransction(ETransactionAction.RECHARGEMENT);

    Transaction transaction = new Transaction(abonnementExist.get(), reference, ETransactionAction.RECHARGEMENT, compteCreateur, montant);

    transaction.setApprobation(0);

    transactionDao.save(transaction);

    return transaction;
  }

  @Override
  public Transaction createPaiementTransaction(PaiementTransactionRequest transactionRequest) {
    Optional<Abonnement> abonnementExist = abonnementDao.findByNumeroCarte(transactionRequest.getNumeroCarte());

    if (!abonnementExist.isPresent() || abonnementExist.get().isDeleted() || abonnementExist.get().getCompteClient().isDeleted()) {
      throw new IllegalArgumentException("Abonnement with that client does not exist!");
    }

    if (abonnementExist.get().isCarteBloquer()) {
      throw new IllegalArgumentException("Cette carte est bloqué!");
    }

    Abonnement abonnement = abonnementExist.get();

    InfosPerso infosPerso = infosPersoDao.findById(abonnement.getCompteClient().getInfosPersoId()).get();

    Optional<User> userExist = userDao.findByInfosPersoId(infosPerso.getId());

    if (!userExist.isPresent() || !encoder.matches(transactionRequest.getPassword(), userExist.get().getPassword())) {
      throw new UsernameNotFoundException("Numero carte ou mot de passe invalide!");
    }

    UserDetailsImpl currentUser = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    Optional<Compte> compteClientExist = null;

    if (transactionRequest.getClientId() != null) {
      compteClientExist = compteDao.findByIdAndType(transactionRequest.getClientId(), ECompteType.COMPTE_PARTICULIER);
    } else {
      compteClientExist = compteDao.findByInfosPersoIdAndType(currentUser.getInfosPerso().getId(), ECompteType.COMPTE_PARTICULIER);
    }

    if (!compteClientExist.isPresent() || compteClientExist.get().isDeleted()) {
      throw new IllegalArgumentException("CompteClient with that id does not exists!");
    }

    Compte compteClient = compteClientExist.get();

    BigDecimal montant = transactionRequest.getMontant();

    int res = abonnement.getSolde().compareTo(montant);

    if (res == -1) {
      throw new IllegalArgumentException(String.format("Solde insuffisant, votre solde actuel est de %s !", abonnement.getSolde()));
    }

    abonnement.debiterCarte(montant);

    abonnementDao.save(abonnement);

    String reference = genererReferenceTransction(ETransactionAction.PAIEMENT);

    Transaction transaction = new Transaction(abonnement, reference, ETransactionAction.PAIEMENT, compteClient, montant);

    transaction.setNouveauSolde(abonnement.getSolde());

    transaction.setService(transactionRequest.getService());

    transactionDao.save(transaction);

    return transaction;
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
}
