package com.safelogisitics.gestionentreprisesusers.service.impl;

import com.safelogisitics.gestionentreprisesusers.data.dao.CompteDao;
import com.safelogisitics.gestionentreprisesusers.data.dao.TypeAbonnementDao;
import com.safelogisitics.gestionentreprisesusers.data.enums.ECompteType;
import com.safelogisitics.gestionentreprisesusers.data.enums.EPeriode;
import com.safelogisitics.gestionentreprisesusers.data.model.*;
import com.safelogisitics.gestionentreprisesusers.service.StatistiquesService;
import com.safelogisitics.gestionentreprisesusers.web.security.services.UserDetailsImpl;
import org.bson.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class StatistiquesServiceImpl implements StatistiquesService {

    private MongoTemplate mongoTemplate;
    private CompteDao compteDao;
    private TypeAbonnementDao typeAbonnementDao;

    public StatistiquesServiceImpl(MongoTemplate mongoTemplate, CompteDao compteDao, TypeAbonnementDao typeAbonnementDao) {
        this.mongoTemplate = mongoTemplate;
        this.compteDao = compteDao;
        this.typeAbonnementDao = typeAbonnementDao;
    }

    @Override
    public Map<String, Long> getNumberClients() {
        final Query query = new Query();
        query.addCriteria(Criteria.where("type").is("COMPTE_PARTICULIER").andOperator(Criteria.where("deleted").is(false)));
        Long numberClientsParticuliers = mongoTemplate.count(query, Compte.class);
        Long numberClientsEntreprises = mongoTemplate.estimatedCount(Entreprise.class);

        Map<String, Long> results = new HashMap<String, Long>();
        results.put("particuliers", numberClientsParticuliers);
        results.put("entreprises", numberClientsEntreprises);

        return results;
    }

    @Override
    public Map<String, String> getMontantAbonnement(EPeriode periode) {
        Map<String, String> results = new HashMap<String, String>();
        if(periode == null) periode = EPeriode.JOUR;
        LocalDateTime dateDebut;
        LocalDateTime dateFin;

        switch(periode) {
            case SEMAINE:
                dateDebut = LocalDate.now().with(DayOfWeek.MONDAY).atStartOfDay();
                dateFin = LocalDate.now().atTime(23, 59);
                break;
            case MOIS:
                dateDebut = LocalDate.now().withDayOfMonth(1).atStartOfDay();
                dateFin = LocalDate.now().atTime(23, 59);
                break;
            default:
                dateDebut = LocalDate.now().atStartOfDay();
                dateFin = LocalDate.now().atTime(23, 59);
        }

        final Query query = new Query();
        final List<Criteria> criterias = new ArrayList<>();

        criterias.add(Criteria.where("createdDate").gte(dateDebut));
        criterias.add(Criteria.where("createdDate").lte(dateFin));

        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(new Criteria().andOperator(criterias.toArray(new Criteria[criterias.size()]))),
                l -> new Document("$group", new Document("_id", Arrays.asList())
                        .append("montantTotal", new Document("$sum", new Document("$toDouble", "$prixCarte")))
                )
        );

        Document montantDoc = mongoTemplate.aggregate(aggregation, Abonnement.class, Document.class).getUniqueMappedResult();
        if (montantDoc != null) results.put("montant", montantDoc.get("montantTotal").toString());
        else results.put("montant", "0");

        return results;
    }

    @Override
    public Page<Evenement> getFutureEvents(Pageable pageable) {
        final Query query = new Query(Criteria.where("dateDebut").gte(LocalDateTime.now()));

        List<Evenement> evenements = mongoTemplate.find(query, Evenement.class);

        return PageableExecutionUtils.getPage(
                evenements,
                pageable,
                () -> mongoTemplate.count(Query.of(query).limit(-1).skip(-1), Evenement.class));
    }

    @Override
    public Page<Evenement> getOwnEvents(Pageable pageable) {
        UserDetailsImpl currentUser = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Compte compte = compteDao.findByInfosPersoIdAndType(currentUser.getInfosPerso().getId(), ECompteType.COMPTE_ADMINISTRATEUR).get();

        final Query query = new Query(Criteria.where("auteurId").is(compte.getId()));

        List<Evenement> evenements = mongoTemplate.find(query, Evenement.class);

        return PageableExecutionUtils.getPage(
                evenements,
                pageable,
                () -> mongoTemplate.count(Query.of(query).limit(-1).skip(-1), Evenement.class));
    }

    @Override
    public Map<String, Long> getNumberCartes() {

        Query query = this.getQueryCarte("Silver");

        Long silver = mongoTemplate.count(query, NumeroCarte.class);

        Query query1 = this.getQueryCarte("Gold");
        Long gold = mongoTemplate.count(query1, NumeroCarte.class);

        Query query2 = this.getQueryCarte("Platinum");
        Long platinum = mongoTemplate.count(query2, NumeroCarte.class);


        Map<String, Long> results = new HashMap<String, Long>();
        results.put("silver", silver);
        results.put("gold", gold);
        results.put("platinum", platinum);


        return results;
    }

    @Override
    public Map<String, Long> getNumberClientsEnroles(EPeriode periode) {
        Map<String, Long> results = new HashMap<String, Long>();
        if(periode == null) periode = EPeriode.JOUR;

        switch(periode) {
            case SEMAINE:
                results = this.numberClients(results, LocalDate.now().with(DayOfWeek.MONDAY).atStartOfDay(), LocalDate.now().atTime(23, 59));
                break;
            case MOIS:
                results = this.numberClients(results, LocalDate.now().withDayOfMonth(1).atStartOfDay(), LocalDate.now().atTime(23, 59));
                break;
            default:
                results = this.numberClients(results, LocalDate.now().atStartOfDay(), LocalDate.now().atTime(23, 59));
        }

        return results;
    }

    private Query getQueryCarte(String typeAbonnement) {
        String typeAbonnementId = typeAbonnementDao.findByLibelle(typeAbonnement).get().getId();

        Query query = new Query(Criteria.where("typeAbonnementId").is(typeAbonnementId).andOperator(Criteria.where("active").is(true)));

        return query;
    }

    private Map<String, Long> numberClients(Map<String, Long> results, LocalDateTime dateDebut, LocalDateTime dateFin) {
        Long nombreCommandes = countNombreResults(1, dateDebut, dateFin, Compte.class, "COMPTE_PARTICULIER", false);
        results.put("clients", nombreCommandes);

        return results;
    }

    private Long countNombreResults(Integer statut, LocalDateTime dateDebut, LocalDateTime dateFin, Class<?> cls, String type, Boolean deleted) {
        final Query query = new Query();
        final List<Criteria> criterias = new ArrayList<>();

        if (statut != null) criterias.add(Criteria.where("statut").is(statut));
        if (dateDebut != null) criterias.add(Criteria.where("dateCreation").gte(dateDebut));
        if (dateFin != null) criterias.add(Criteria.where("dateCreation").lte(dateFin));
        if (type != null) criterias.add(Criteria.where("type").is(type));
        if (deleted != null) criterias.add(Criteria.where("deleted").is(deleted));

        query.addCriteria(new Criteria().andOperator(criterias.toArray(new Criteria[criterias.size()])));

        Long nombreCommandes = mongoTemplate.count(query, cls);

        return nombreCommandes;
    }
}
