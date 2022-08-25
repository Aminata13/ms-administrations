package com.safelogisitics.gestionentreprisesusers.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lowagie.text.DocumentException;
import com.safelogisitics.gestionentreprisesusers.data.dao.*;
import com.safelogisitics.gestionentreprisesusers.data.dao.filter.TransactionDefaultFields;
import com.safelogisitics.gestionentreprisesusers.data.dto.kafka.PaiementServiceDto;
import com.safelogisitics.gestionentreprisesusers.data.dto.request.*;
import com.safelogisitics.gestionentreprisesusers.data.enums.*;
import com.safelogisitics.gestionentreprisesusers.data.model.*;
import com.safelogisitics.gestionentreprisesusers.data.shared.dao.SharedEntrepriseDao;
import com.safelogisitics.gestionentreprisesusers.data.shared.dao.SharedInfosPersoDao;
import com.safelogisitics.gestionentreprisesusers.data.shared.model.*;
import com.safelogisitics.gestionentreprisesusers.data.shared.model.subclass.PaiementFacture;
import com.safelogisitics.gestionentreprisesusers.service.*;
import com.safelogisitics.gestionentreprisesusers.web.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;

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
    @Qualifier(value = "mongoTemplate")
    MongoTemplate mongoTemplate;

    @Autowired
    @Qualifier(value = "sharedMongoTemplate")
    MongoTemplate sharedMongoTemplate;

    @Autowired
    PDFGeneratorService PDFGeneratorService;

    @Autowired
    private FirebaseMessagingService firebaseMessagingService;

    @Autowired
    private SMSService smsService;

    @Autowired
    private CommissionService commissionService;

    @Autowired
    PasswordEncoder encoder;
    @Autowired
    private ExtraitComptePdfService extraitComptePdfService;

    @Autowired
    private SharedInfosPersoDao sharedInfosPersoDao;

    @Autowired
    private SharedEntrepriseDao sharedEntrepriseDao;

    @Autowired
    private ObjectMapper objectMapper;

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

                InfosPersoModel infosPerso = infosPersoDao.findById(abonnement.getCompteClient().getInfosPersoId()).get();
                String smsText = String.format("Bonjour M./Mme %s,\nSuite à votre rechargement de %s FCFA, nous vous informons que le solde actuel de votre compte principal est de %sFCFA et celui de votre compte points gratuits est de %s.\nSafelogistics vous remercie\nService commercial : 78 306 45 45",
                        infosPerso.getNomComplet(), transaction.getMontant(), abonnement.getSolde(), abonnement.getPointGratuites());
                SendSmsRequest sms = new SendSmsRequest("SFLOGISTICS", "Rechargement", smsText, Arrays.asList(infosPerso.getTelephone()));
                smsService.sendSms(sms);
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
        InfosPersoModel infosPerso = infosPersoDao.findById(abonnement.getCompteClient().getInfosPersoId()).get();

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

            String smsText = String.format("Bonjour M./Mme %s,\nPour vous remercier de votre fidélité, SafeLogistics vous offre %s points gratuits et le solde actuel de votre compte points gratuits est %s.\nSafelogistics vous remercie\nService commercial : 78 306 45 45",
                    infosPerso.getNomComplet(), transactionRequest.getPoints(), abonnement.getPointGratuites());

            SendSmsRequest sms = new SendSmsRequest("SFLOGISTICS", "Rechargement points gratuits", smsText, Arrays.asList(infosPerso.getTelephone()));
            smsService.sendSms(sms);

        } else {
            if (transactionRequest.getMontant() == null) {
                throw new IllegalArgumentException("Veuillez donner le montant à recharger.");
            }
            transaction.setMontant(transactionRequest.getMontant());
            transaction.setApprobation(0);

            String smsText = String.format("Bonjour M./Mme %s,\nVotre demande de rechargement de %s FCFA est en cours de traitement, vous recevrez un message dès que la demande sera traitée.\nSafelogistics vous remercie\nService commercial : 78 306 45 45",
                    infosPerso.getNomComplet(), transactionRequest.getMontant());

            SendSmsRequest sms = new SendSmsRequest("SFLOGISTICS", "Rechargement", smsText, Arrays.asList(infosPerso.getTelephone()));
            smsService.sendSms(sms);
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
                System.out.println("RESPONSE ================================> " + resp);
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
            throw new IllegalArgumentException("Mot de passe invalide!");

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

        InfosPersoModel infosPerso = infosPersoDao.findById(abonnement.getCompteClient().getInfosPersoId()).get();

        Optional<PaiementValidation> oldPaiementValidation = paiementValidationDao.findByNumeroCommande(paiementValidationRequest.getNumeroCommande());

        if (oldPaiementValidation.isPresent()) {
            paiementValidationDao.delete(oldPaiementValidation.get());
        }

        String codeValidation = this.genererCodeValidation(paiementValidationRequest.getNumeroCarte());

        PaiementValidation paiementValidation = new PaiementValidation(paiementValidationRequest.getNumeroCarte(),
                paiementValidationRequest.getNumeroCommande(), paiementValidationRequest.getService(), paiementValidationRequest.getMontant(), codeValidation);

        paiementValidationDao.save(paiementValidation);

        String smsText = String.format("Bonjour M./Mme %s. Le code de validation de votre commande est: %s. Le code expire dans 5 minutes.\nSafelogistics vous remercie.\nService commercial : 78 306 45 45",
                infosPerso.getNomComplet(), paiementValidation.getCodeValidation());

        SendSmsRequest sms = new SendSmsRequest("SFLOGISTICS", "Code de validation paiement", smsText, Arrays.asList(infosPerso.getTelephone()));

        smsService.sendSms(sms);

        return true;
    }

    @Override
    public Transaction createPaiementTransaction(PaiementTransactionRequest transactionRequest) {
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
            throw new IllegalArgumentException("Cette commande est déjà payée.");
        }

        Compte compteClient = compteClientExist.get();

        Abonnement abonnement = null;

        if (transactionRequest.getPaiementValidation().equals(EPaimentValidation.SMS_VALIDATION)) {
            Optional<PaiementValidation> _paiementValidation = paiementValidationDao.findByCodeValidationAndNumeroCommande(
                    transactionRequest.getCodeValidation().replaceAll("\\D+", ""), transactionRequest.getNumeroCommande());

            if (!_paiementValidation.isPresent() || _paiementValidation.get().getApprobation() == true || !_paiementValidation.get().getNumeroCarte().equals(transactionRequest.getNumeroCarte())) {
                throw new IllegalArgumentException("Code de validation invalide.");
            }

            PaiementValidation paiementValidation = _paiementValidation.get();

            LocalDateTime dateNow = LocalDateTime.now();
            LocalDateTime previous = paiementValidation.getDateCreation().plusMinutes(5);

            if (!dateNow.isBefore(previous)) {
                throw new IllegalArgumentException("Code de validation expirée.");
            }

            Optional<Abonnement> _abonnement = abonnementDao.findByNumeroCarte(paiementValidation.getNumeroCarte());
            if (!_abonnement.isPresent()) {
                throw new IllegalArgumentException("Cette carte n'a pas d'abonnement.");
            }

            abonnement = _abonnement.get();

            paiementValidation.setApprobation(true);

            paiementValidationDao.save(paiementValidation);
        } else {
            Optional<User> userExist = userDao.findByInfosPersoId(currentUser.getInfosPerso().getId());

            if (transactionRequest.getCodeValidation() == null || !userExist.isPresent() || !encoder.matches(transactionRequest.getCodeValidation(), userExist.get().getPassword()))
                throw new IllegalArgumentException("Mot de passe invalide!");

            Optional<Abonnement> _abonnement = abonnementDao.findByCompteClientIdAndDeletedIsFalse(compteClient.getId());

            if (!_abonnement.isPresent()) {
                throw new IllegalArgumentException("Vous n'avez pas d'abonnement.");
            }

            abonnement = _abonnement.get();
        }

        InfosPersoModel infosPerso = infosPersoDao.findById(abonnement.getCompteClient().getInfosPersoId()).get();

        String reference = genererReferenceTransction(ETransactionAction.PAIEMENT);

        Transaction transaction = new Transaction(abonnement, reference, ETransactionAction.PAIEMENT, compteClient);

        if (transactionRequest.getCompteDebiter().equals(ETransactionType.POINT_GRATUITE)) {
            if (transactionRequest.getPoints() == null) {
                throw new IllegalArgumentException("Nombre de points à débiter invalide.");
            }
            if (abonnement.getPointGratuites() < transactionRequest.getPoints().longValue()) {
                throw new IllegalArgumentException(String.format("Nombre de points gratuits insuffisant, total points gratuites actuel:", abonnement.getPointGratuites()));
            }
            abonnement.debiterPointGratuites(transactionRequest.getPoints().longValue());
            transaction.setPoints(transactionRequest.getPoints().longValue());
            transaction.setTotalPoints(abonnement.getPointGratuites());
        } else {
            if (transactionRequest.getMontant() == null) {
                throw new IllegalArgumentException("Montant invalide");
            }
            if (abonnement.getSolde().compareTo(transactionRequest.getMontant()) == -1) {
                throw new IllegalArgumentException(String.format("Solde insuffisant, solde actuel:", abonnement.getSolde()));
            }
            abonnement.debiterSolde(transactionRequest.getMontant());
            transaction.setMontant(transactionRequest.getMontant());
            transaction.setNouveauSolde(abonnement.getSolde());
            if (abonnement.getResponsableId() != null && !abonnement.getResponsableId().isEmpty()) {
                CommissionRequestDto commissionRequest = new CommissionRequestDto(transactionRequest);
                commissionRequest.setResponsableId(abonnement.getResponsableId());
                this.commissionService.createCommission(commissionRequest);
            }
        }

        abonnementDao.save(abonnement);

        Map<String, Integer> resultMap = this.genererInitialEtOrdreFacture();
        transaction.setInitialFacture(resultMap.get("initialFacture"));
        transaction.setOrdreFacture(resultMap.get("ordreFacture"));
        transaction.setType(transactionRequest.getCompteDebiter());
        transaction.setService(transactionRequest.getService());
        transaction.setNumeroCommande(transactionRequest.getNumeroCommande());

        transactionDao.save(transaction);

        String smsText;
        if (abonnement.getSolde().compareTo(BigDecimal.valueOf(1500)) == -1) {
            smsText = String.format("Bonjour M./Mme %s,\nSuite au paiement de votre commande n° %s, nous vous informons que votre solde actuel est de %sFCFA.\nPour recharger votre compte vous pouvez le faire via \n• Espèces (dans nos locaux ou points relais)\nSafelogistics vous remercie\nService commercial : 78 306 45 45",
                    infosPerso.getNomComplet(), transaction.getNumeroCommande(), abonnement.getSolde());
        } else {
            smsText = String.format("Bonjour M./Mme %s,\nSuite au paiement de votre commande n° %s, nous vous informons que le solde de votre compte principal est de %sFCFA et celui de votre compte points gratuits est de %s.\n• Safelogistics vous remercie\nService commercial : 78 306 45 45",
                    infosPerso.getNomComplet(), transaction.getNumeroCommande(), abonnement.getSolde(), abonnement.getPointGratuites());
        }

        SendSmsRequest sms = new SendSmsRequest("SFLOGISTICS", "Paiement commande", smsText, Arrays.asList(infosPerso.getTelephone()));

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

        this.commissionService.deleteCommissionByCommandeId(paiementServiceDto.getServiceId());
    }

    @Override
    public Map<String, String> getFactureCommandeNumber(String numeroCommande) {
        Optional<Transaction> transactionExist = this.transactionDao.findByNumeroCommande(numeroCommande);
        if (!transactionExist.isPresent()) {
            throw new IllegalArgumentException("Cette transaction n'existe pas.");
        }
        Transaction transaction = transactionExist.get();
        Map<String, String> result = new HashMap<String, String>();

        int month = LocalDate.now().getMonthValue();
        String monthStr = month < 10 ? "0" + month : String.valueOf(month);

        int m = (int) Math.pow(10, 1);
        int randomInt = m + new Random().nextInt(9 * m);

        if (transaction.getInitialFacture() == 0) {
            result.put("numero", "300" + monthStr + "-" + "001" + randomInt);
            return result;
        }

        String ordre = String.format("%1$" + 3 + "s", transaction.getOrdreFacture()).replace(' ', '0');
        result.put("numero", transaction.getInitialFacture() + monthStr + "-" + ordre + randomInt);

        return result;
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

    @Override
    public File getExtraitCompteClientPdf(String clientId, String dateDebut, String dateFin) throws DocumentException, IOException {
        Optional<SharedInfosPersoModel> _infosPerso = sharedInfosPersoDao.findByComptesId(clientId);
        if (!_infosPerso.isPresent()) {
            throw new IllegalArgumentException("Ce client n'existe pas.");
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime debut = LocalDate.parse(dateDebut, formatter).atTime(0, 0);
        LocalDateTime fin = LocalDate.parse(dateFin, formatter).atTime(23, 59);

        SharedInfosPersoModel infosPerso = _infosPerso.get();

        List<ExtraitCompteDataModel> extraitCompteData = this.extraitCompteQuery(clientId, debut, fin);

        return extraitComptePdfService.exportExtraitCompteClientToPdf(debut, fin, extraitCompteData, infosPerso.getPrenom(), infosPerso.getNom(), infosPerso.getAdresse());
    }

    @Override
    public File getExtraitCompteEntreprisePdf(String entrepriseId, String dateDebut, String dateFin) throws DocumentException, IOException {
        Optional<SharedEntrepriseModel> _entreprise = sharedEntrepriseDao.findByIdAndDeletedIsFalse(entrepriseId);
        if (!_entreprise.isPresent()) {
            throw new IllegalArgumentException("Cette entreprise n'existe pas.");
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime debut = LocalDate.parse(dateDebut, formatter).atTime(0, 0);
        LocalDateTime fin = LocalDate.parse(dateFin, formatter).atTime(23, 59);

        SharedEntrepriseModel entreprise = _entreprise.get();
        Abonnement abonnement = this.abonnementDao.findByEntrepriseId(entrepriseId).get();

        final Query query = new Query();
        final List<Criteria> criterias = new ArrayList<>();

        criterias.add(Criteria.where("statut").ne(-1));
        criterias.add(Criteria.where("clientId").is(entrepriseId));
        criterias.add(Criteria.where("createdDate").gte(debut));
        criterias.add(Criteria.where("createdDate").lte(fin));
        query.addCriteria(new Criteria().andOperator(criterias.toArray(new Criteria[criterias.size()])));

        List<ExtraitCompteEntrepriseData> extraitData = new ArrayList<>();
        sharedMongoTemplate.find(query, SharedCommandeModel.class).forEach(c -> extraitData.add(objectMapper.convertValue(c, ExtraitCompteEntrepriseData.class)));

        List<Criteria> factureCriteria = new ArrayList<>();
        criterias.add(Criteria.where("clientId").is(entrepriseId));
        criterias.add(Criteria.where("createdDate").gte(debut));
        criterias.add(Criteria.where("createdDate").lte(fin));

        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(new Criteria().andOperator(criterias.toArray(new Criteria[factureCriteria.size()]))),
                Aggregation.project("paiements", "montantRestant")
        );

        List<SharedFactureModel> result = sharedMongoTemplate.aggregate(aggregation, SharedFactureModel.class, SharedFactureModel.class).getMappedResults();

        BigDecimal montantRestant = BigDecimal.ZERO;
        for (SharedFactureModel r : result) {
            if(!r.getPaiements().isEmpty()) {
                for (PaiementFacture p : r.getPaiements()) {
                    ExtraitCompteEntrepriseData data = new ExtraitCompteEntrepriseData();
                    data.setDataType("Paiement");
                    data.setMontantPayer(p.getMontantPayer());
                    data.setMontantRestant(r.getMontantRestant());
                    data.setCreatedDate(p.getDatePaiement());
                    extraitData.add(data);
                }
            }
            montantRestant = montantRestant.add(r.getMontantRestant());
        }
        Collections.sort(extraitData);

        return extraitComptePdfService.exportExtraitCompteEntrepriseToPdf(debut, fin, abonnement.getDateCreation(), extraitData, abonnement.getNumeroCarte(), entreprise.getDenomination(), entreprise.getAdresse(), montantRestant);
    }

    private List<ExtraitCompteDataModel> extraitCompteQuery(String clientId, LocalDateTime dateDebut, LocalDateTime dateFin) {
        List<Criteria> criterias = new ArrayList<>();
        criterias.add(Criteria.where("abonnement.compteClient.id").is(clientId));
        criterias.add(Criteria.where("dateApprobation").gte(dateDebut));
        criterias.add(Criteria.where("dateApprobation").lte(dateFin));
        criterias.add(Criteria.where("approbation").is(1));

        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(new Criteria().andOperator(criterias.toArray(new Criteria[criterias.size()]))),
                Aggregation.lookup("commandes", "numeroCommande", "numero", "commande"),
                Aggregation.unwind("commande", true),
                Aggregation.addFields().addFieldWithValue("commande", "$commande").build(),
                Aggregation.sort(Sort.Direction.ASC, "dateApprobation")
        );

        List<ExtraitCompteDataModel> result = sharedMongoTemplate.aggregate(aggregation, SharedTransactionModel.class, ExtraitCompteDataModel.class).getMappedResults();

        return result;
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

    private Map<String, Integer> genererInitialEtOrdreFacture() {
        Map<String, Integer> results = new HashMap<>();
        int initialFacture = 300;
        int ordreFacture = 1;

        Optional<Transaction> transactionExist = this.transactionDao.findFirstByActionOrderByIdDesc(ETransactionAction.PAIEMENT);
        if (transactionExist.isPresent() && transactionExist.get().getInitialFacture() != 0) {
            Transaction transaction = transactionExist.get();
            int initial = transaction.getInitialFacture();
            int ordre = transaction.getOrdreFacture();

            if (ordre == 999) initialFacture = initial + 1;
            else {
                ordreFacture = ordre + 1;
                initialFacture = initial;
            }
        }
        results.put("initialFacture", initialFacture);
        results.put("ordreFacture", ordreFacture);

        return results;
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
