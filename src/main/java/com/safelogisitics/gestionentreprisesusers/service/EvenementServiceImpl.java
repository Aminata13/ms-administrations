package com.safelogisitics.gestionentreprisesusers.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safelogisitics.gestionentreprisesusers.dao.CompteDao;
import com.safelogisitics.gestionentreprisesusers.dao.EvenementDao;
import com.safelogisitics.gestionentreprisesusers.dao.TypeEvenementDao;
import com.safelogisitics.gestionentreprisesusers.dto.EvenementDto;
import com.safelogisitics.gestionentreprisesusers.model.Compte;
import com.safelogisitics.gestionentreprisesusers.model.Evenement;
import com.safelogisitics.gestionentreprisesusers.model.enums.ECompteType;
import com.safelogisitics.gestionentreprisesusers.security.services.UserDetailsImpl;

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
    TemporalField fieldUS = WeekFields.of(Locale.getDefault()).dayOfWeek();
    LocalDateTime dateDebut = _dateDebut != null && !_dateDebut.isEmpty() ? LocalDateTime.parse(_dateDebut, formatter) : now.with(fieldUS, 1).withHour(0).withMinute(0).withSecond(0);
    LocalDateTime dateFin = _dateFin != null && !_dateFin.isEmpty() ? LocalDateTime.parse(_dateFin, formatter) : now.with(fieldUS, 7).withHour(23).withMinute(59).withSecond(59);

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
      return objectMapper.convertValue(evenement, EvenementDto.class);
    }).collect(Collectors.toList());
  }

  @Override
  public EvenementDto getEvenementById(String id) {
    Optional<Evenement> evenementExist = evenementDao.findById(id);
    if (evenementExist.isPresent())
      return null;

    return objectMapper.convertValue(evenementExist.get(), EvenementDto.class);
  }

  @Override
  public EvenementDto createEvenement(Evenement evenement) {
    if (!typeEvenementDao.existsById(evenement.getTypeEvenementId()))
      throw new IllegalArgumentException("Ce type d'événement n'existe pas.");

    UserDetailsImpl currentUser = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    Compte compteAdmin = compteDao.findByInfosPersoIdAndType(currentUser.getInfosPerso().getId(), ECompteType.COMPTE_ADMINISTRATEUR).get();

    evenement.setAuteurId(compteAdmin.getId());
    evenementDao.save(evenement);

    return objectMapper.convertValue(evenement, EvenementDto.class);
  }

  @Override
  public EvenementDto updateEvenement(String id, Evenement evenement) {
    Optional<Evenement> evenementExist = evenementDao.findById(id);

    if (evenementExist.isPresent())
      throw new IllegalArgumentException("Cet événement n'existe pas.");

    if (!typeEvenementDao.existsById(evenement.getTypeEvenementId()))
      throw new IllegalArgumentException("Ce type d'événement n'existe pas.");

    evenement.setId(evenementExist.get().getId());
    evenement.setAuteurId(evenementExist.get().getAuteurId());
    
    evenementDao.save(evenement);

    return objectMapper.convertValue(evenement, EvenementDto.class);
  }

  @Override
  public void deleteEvenement(String id) {
    Optional<Evenement> evenementExist = evenementDao.findById(id);

    if (!evenementExist.isPresent())
      throw new IllegalArgumentException("Cet événement n'existe pas.");

    evenementDao.delete(evenementExist.get());
  }
}
