package com.safelogisitics.gestionentreprisesusers.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import static java.time.temporal.TemporalAdjusters.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safelogisitics.gestionentreprisesusers.data.dao.CompteDao;
import com.safelogisitics.gestionentreprisesusers.data.dao.EvenementDao;
import com.safelogisitics.gestionentreprisesusers.data.dao.TypeEvenementDao;
import com.safelogisitics.gestionentreprisesusers.data.dto.kafka.EvenementDto;
import com.safelogisitics.gestionentreprisesusers.data.model.Compte;
import com.safelogisitics.gestionentreprisesusers.data.model.Evenement;
import com.safelogisitics.gestionentreprisesusers.data.model.enums.ECompteType;
import com.safelogisitics.gestionentreprisesusers.web.security.services.UserDetailsImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class EvenementServiceImpl implements EvenementService {
  
  @Autowired
  private TypeEvenementDao typeEvenementDao;

  @Autowired
  private EvenementDao evenementDao;

  @Autowired
  private CompteDao compteDao;

  @Autowired
  private MongoTemplate mongoTemplate;

  @Autowired
  private ObjectMapper objectMapper;

  @Override
  public Collection<EvenementDto> getEvenements(String _dateDebut, String _dateFin) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    LocalDateTime now = LocalDateTime.now();
    LocalDateTime dateDebut = _dateDebut != null && !_dateDebut.isEmpty() ? LocalDateTime.parse(_dateDebut, formatter) : now.with(firstDayOfMonth()).withHour(0).withMinute(0).withSecond(0);
    LocalDateTime dateFin = _dateFin != null && !_dateFin.isEmpty() ? LocalDateTime.parse(_dateFin, formatter) : now.with(lastDayOfMonth()).withHour(23).withMinute(59).withSecond(59);

    final Query query = new Query();
    final List<Criteria> criteria = new ArrayList<>();

    criteria.add(Criteria.where("dateDebut").gte(dateDebut));
    criteria.add(Criteria.where("dateFin").lte(dateFin));

    UserDetailsImpl currentUser = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    Compte compteAdmin = compteDao.findByInfosPersoIdAndType(currentUser.getInfosPerso().getId(), ECompteType.COMPTE_ADMINISTRATEUR).get();

    if (!compteAdmin.getRole().hasPrivilegeAction("GESTION_EVENEMENTS", "SUPERVISE"))
      criteria.add(Criteria.where("auteurId").is(compteAdmin.getId()));

    query.addCriteria(new Criteria().andOperator(criteria.toArray(new Criteria[criteria.size()])));

    return mongoTemplate.find(query, Evenement.class).stream().map(evenement -> {
      EvenementDto evenementDto = objectMapper.convertValue(evenement, EvenementDto.class);
      evenementDto.setTypeEvenement(typeEvenementDao.findById(evenement.getTypeEvenementId()));
      return evenementDto;
    }).collect(Collectors.toList());
  }

  @Override
  public EvenementDto getEvenementById(String id) {
    Optional<Evenement> evenementExist = evenementDao.findById(id);
    if (!evenementExist.isPresent())
      return null;

    Evenement evenement = evenementExist.get();
    EvenementDto evenementDto = objectMapper.convertValue(evenement, EvenementDto.class);
    evenementDto.setTypeEvenement(typeEvenementDao.findById(evenement.getTypeEvenementId()));
    return evenementDto;
  }

  @Override
  public EvenementDto createEvenement(Evenement evenement) {
    if (!typeEvenementDao.existsById(evenement.getTypeEvenementId()))
      throw new IllegalArgumentException("Ce type d'événement n'existe pas.");

    UserDetailsImpl currentUser = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    Compte compteAdmin = compteDao.findByInfosPersoIdAndType(currentUser.getInfosPerso().getId(), ECompteType.COMPTE_ADMINISTRATEUR).get();

    evenement.setAuteurId(compteAdmin.getId());
    evenementDao.save(evenement);

    EvenementDto evenementDto = objectMapper.convertValue(evenement, EvenementDto.class);
    evenementDto.setTypeEvenement(typeEvenementDao.findById(evenement.getTypeEvenementId()));
    return evenementDto;
  }

  @Override
  public EvenementDto updateEvenement(String id, Evenement evenement) {
    Optional<Evenement> evenementExist = evenementDao.findById(id);

    if (!evenementExist.isPresent())
      throw new IllegalArgumentException("Cet événement n'existe pas.");

    if (!typeEvenementDao.existsById(evenement.getTypeEvenementId()))
      throw new IllegalArgumentException("Ce type d'événement n'existe pas.");

    evenement.setId(evenementExist.get().getId());
    evenement.setAuteurId(evenementExist.get().getAuteurId());
    
    evenementDao.save(evenement);

    EvenementDto evenementDto = objectMapper.convertValue(evenement, EvenementDto.class);
    evenementDto.setTypeEvenement(typeEvenementDao.findById(evenement.getTypeEvenementId()));
    return evenementDto;
  }

  @Override
  public void deleteEvenement(String id) {
    Optional<Evenement> evenementExist = evenementDao.findById(id);

    if (!evenementExist.isPresent())
      throw new IllegalArgumentException("Cet événement n'existe pas.");

    evenementDao.delete(evenementExist.get());
  }
}
