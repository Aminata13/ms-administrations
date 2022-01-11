package com.safelogisitics.gestionentreprisesusers.service;

import com.safelogisitics.gestionentreprisesusers.data.dao.CompteDao;
import com.safelogisitics.gestionentreprisesusers.data.dao.TypeAbonnementDao;
import com.safelogisitics.gestionentreprisesusers.data.enums.ECompteType;
import com.safelogisitics.gestionentreprisesusers.data.model.*;
import com.safelogisitics.gestionentreprisesusers.web.security.services.UserDetailsImpl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

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
        results.put("Nombre de clients particuliers", numberClientsParticuliers);
        results.put("Nombre de clients entreprises", numberClientsEntreprises);

        return results;
    }

    @Override
    public Map<String, Long> getNumberAbonnement() {
        final Query query1 = new Query();
        final Query query2 = new Query();
        final List<Criteria> criterias1 = new ArrayList<>();
        final List<Criteria> criterias2 = new ArrayList<>();

        LocalDateTime startDate = LocalDateTime.of(LocalDate.now().getYear(), LocalDate.now().getMonth(), LocalDate.now().getDayOfMonth(), 0, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(LocalDate.now().getYear(), LocalDate.now().getMonth(), LocalDate.now().getDayOfMonth(), 23, 59, 59);
        criterias1.add(Criteria.where("dateCreation").gte(startDate));
        criterias1.add(Criteria.where("dateCreation").lte(endDate));
        query1.addCriteria(new Criteria().andOperator(criterias1.toArray(new Criteria[criterias1.size()])));


        criterias2.add(Criteria.where("dateCreation").gte(startDate.minusDays(1)));
        criterias2.add(Criteria.where("dateCreation").lte(endDate.minusDays(1)));
        query2.addCriteria(new Criteria().andOperator(criterias2.toArray(new Criteria[criterias2.size()])));

        Long yesterdayCount = mongoTemplate.count(query2, Abonnement.class);
        Long todayCount = mongoTemplate.count(query1, Abonnement.class);
        long percentage = yesterdayCount != 0 ? (todayCount - yesterdayCount) * 100 / yesterdayCount : 100;

        Map<String, Long> results = new HashMap<String, Long>();
        results.put("Nombre d'abonnements", todayCount);
        results.put("Pourcentage", percentage);

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
        results.put("Nombre de cartes Silver", silver);
        results.put("Nombre de cartes Gold", gold);
        results.put("Nombre de cartes Platinum", platinum);

        return results;
    }

    private Query getQueryCarte(String typeAbonnement) {
        String typeAbonnementId = typeAbonnementDao.findByLibelle(typeAbonnement).get().getId();

        Query query = new Query(Criteria.where("typeAbonnementId").is(typeAbonnementId));

        return query;
    }
}
