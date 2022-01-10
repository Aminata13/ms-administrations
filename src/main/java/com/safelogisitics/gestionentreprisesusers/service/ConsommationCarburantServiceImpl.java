package com.safelogisitics.gestionentreprisesusers.service;

import com.safelogisitics.gestionentreprisesusers.dao.ConsommationCarburantDao;
import com.safelogisitics.gestionentreprisesusers.dao.MoyenTransportDao;
import com.safelogisitics.gestionentreprisesusers.model.ConsommationCarburant;
import com.safelogisitics.gestionentreprisesusers.model.MoyenTransport;
import com.safelogisitics.gestionentreprisesusers.payload.request.ConsommationCarburantRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ConsommationCarburantServiceImpl implements ConsommationCarburantService {

    private MongoTemplate mongoTemplate;
    private ConsommationCarburantDao consommationCarburantDao;
    private MoyenTransportDao moyenTransportDao;

    public ConsommationCarburantServiceImpl(MongoTemplate mongoTemplate, ConsommationCarburantDao consommationCarburantDao, MoyenTransportDao moyenTransportDao) {
        this.mongoTemplate = mongoTemplate;
        this.consommationCarburantDao = consommationCarburantDao;
        this.moyenTransportDao = moyenTransportDao;
    }

    @Override
    public Page<ConsommationCarburant> getConsommationCarburants(String dateDebut, String dateFin, String moyenTransportId, Pageable pageable) {
        final Query query = new Query().with(pageable).with(Sort.by(Sort.Direction.DESC, "createdDate"));
        final List<Criteria> criterias = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        if (moyenTransportId != null) criterias.add(Criteria.where("moyenTransportId").is(moyenTransportId));
        if (dateDebut != null) criterias.add(Criteria.where("dateConsommation").gte(LocalDateTime.parse(dateDebut, formatter)));
        if (dateFin != null) criterias.add(Criteria.where("dateConsommation").lte(LocalDateTime.parse(dateFin, formatter)));

        if (!criterias.isEmpty()) query.addCriteria(new Criteria().andOperator(criterias.toArray(new Criteria[criterias.size()])));
        List<ConsommationCarburant> conso = mongoTemplate.find(query, ConsommationCarburant.class);

        return PageableExecutionUtils.getPage(
                conso,
                pageable,
                () -> mongoTemplate.count(Query.of(query).limit(-1).skip(-1), ConsommationCarburant.class));
    }

    @Override
    public ConsommationCarburant addConsommationCarburant(ConsommationCarburantRequest consommationCarburantRequest) {
        Optional<MoyenTransport> moyenTransportExist = moyenTransportDao.findById(consommationCarburantRequest.getMoyenTransportId());

        if (!moyenTransportExist.isPresent()) {
            throw new IllegalArgumentException("Ce moyen de transport n'existe pas ou a été supprimé");
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        ConsommationCarburant consommationCarburant = new ConsommationCarburant(consommationCarburantRequest.getQuantite(), consommationCarburantRequest.getPrix(), consommationCarburantRequest.getMoyenTransportId(), consommationCarburantRequest.getPieceJointe(), LocalDateTime.parse(consommationCarburantRequest.getDateConsommation(), formatter));
        consommationCarburantDao.save(consommationCarburant);

        return consommationCarburant;
    }

    @Override
    public ConsommationCarburant updateConsommationCarburant(String id, ConsommationCarburantRequest consommationCarburantRequest) {
        Optional<ConsommationCarburant> consommationCarburantExist = consommationCarburantDao.findById(id);

        if (!consommationCarburantExist.isPresent()) {
            throw new IllegalArgumentException("Cette consommation n'existe pas ou a été supprimé");
        }

        ConsommationCarburant consommationCarburant = consommationCarburantExist.get();
        consommationCarburant.setQuantite(consommationCarburantRequest.getQuantite());
        consommationCarburant.setPrix(consommationCarburantRequest.getPrix());
        consommationCarburant.setMoyenTransportId(consommationCarburantRequest.getMoyenTransportId());
        consommationCarburant.setPieceJointe(consommationCarburantRequest.getPieceJointe());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        consommationCarburant.setDateConsommation(LocalDateTime.parse(consommationCarburantRequest.getDateConsommation(), formatter));

        consommationCarburantDao.save(consommationCarburant);

        return consommationCarburant;
    }
}
