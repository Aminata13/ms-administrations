package com.safelogisitics.gestionentreprisesusers.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.safelogisitics.gestionentreprisesusers.data.dao.AbonnementDao;
import com.safelogisitics.gestionentreprisesusers.data.dao.CompteDao;
import com.safelogisitics.gestionentreprisesusers.data.dao.EntrepriseDao;
import com.safelogisitics.gestionentreprisesusers.data.dao.NumeroCarteDao;
import com.safelogisitics.gestionentreprisesusers.data.dao.TypeAbonnementDao;
import com.safelogisitics.gestionentreprisesusers.data.dto.request.AbonnementRequest;
import com.safelogisitics.gestionentreprisesusers.data.model.Abonnement;
import com.safelogisitics.gestionentreprisesusers.data.model.Compte;
import com.safelogisitics.gestionentreprisesusers.data.model.Entreprise;
import com.safelogisitics.gestionentreprisesusers.data.model.InfosPerso;
import com.safelogisitics.gestionentreprisesusers.data.model.NumeroCarte;
import com.safelogisitics.gestionentreprisesusers.data.model.TypeAbonnement;
import com.safelogisitics.gestionentreprisesusers.data.model.enums.ECompteType;
import com.safelogisitics.gestionentreprisesusers.web.security.services.UserDetailsImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AbonnementServiceImpl implements AbonnementService {

  @Autowired
  private AbonnementDao abonnementDao;

  @Autowired
  private TypeAbonnementDao typeAbonnementDao;

  @Autowired
  private NumeroCarteDao numeroCarteDao;

  @Autowired
  private CompteDao compteDao;

  @Autowired
  private EntrepriseDao entrepriseDao;

  @Autowired
  private MongoTemplate mongoTemplate;

  @Override
  public Page<Abonnement> getAbonnements(Pageable pageable) {
    return abonnementDao.findByDeletedIsFalseOrderByDateCreationDesc(pageable);
  }

  @Override
  public Page<Abonnement> findByCustomSearch(Map<String,String> parameters, Pageable pageable) {
    final Query query = new Query().with(pageable).with(Sort.by(Sort.Direction.DESC, "dateCreation"));;
    final List<Criteria> criteria = new ArrayList<>();

    final Query queryInfosPerso = new Query();
    final List<Criteria> criteriaInfosPerso = new ArrayList<>();
    List<String> infosPersoIds = new ArrayList<>();

    // Recherche dans infos particulier 
    if (parameters != null && parameters.containsKey("prenom") && !parameters.get("prenom").isEmpty())
      criteriaInfosPerso.add(Criteria.where("prenom").regex(".*"+parameters.get("prenom").trim()+".*","i"));

    if (parameters != null && parameters.containsKey("nom") && !parameters.get("nom").isEmpty())
      criteriaInfosPerso.add(Criteria.where("nom").regex(".*"+parameters.get("nom").trim()+".*","i"));

    if (parameters != null && parameters.containsKey("email") && !parameters.get("prenom").isEmpty())
      criteriaInfosPerso.add(Criteria.where("email").regex(".*"+parameters.get("email").trim()+".*","i"));

    if (parameters != null && parameters.containsKey("telephone") && !parameters.get("telephone").isEmpty())
      criteriaInfosPerso.add(Criteria.where("telephone").regex(".*"+parameters.get("telephone").trim()+".*","xi"));

    if (!criteriaInfosPerso.isEmpty()) {
      queryInfosPerso.addCriteria(new Criteria().andOperator(criteriaInfosPerso.toArray(new Criteria[criteriaInfosPerso.size()])));
      infosPersoIds = mongoTemplate.find(queryInfosPerso, InfosPerso.class).stream().map(infosPerso -> infosPerso.getId()).collect(Collectors.toList());
      if (infosPersoIds.size() <= 0)
        return PageableExecutionUtils.getPage(
          new ArrayList<Abonnement>(), 
          pageable, 
          () -> mongoTemplate.count(Query.of(query).limit(-1).skip(-1), Abonnement.class));
    }

    if (infosPersoIds.size() > 0)
      criteria.add(Criteria.where("compteClient.infosPersoId").in(infosPersoIds));

    if (parameters != null && parameters.containsKey("numeroCarte") && !parameters.get("numeroCarte").isEmpty())
      criteria.add(Criteria.where("numeroCarte").regex(".*"+parameters.get("numeroCarte").replaceAll("\\D+","")+".*","i"));

    if (parameters != null && parameters.containsKey("typeAbonnement") && !parameters.get("typeAbonnement").isEmpty())
      criteria.add(Criteria.where("typeAbonnement._id").is(parameters.get("typeAbonnement")));

    if (!criteria.isEmpty())
      query.addCriteria(new Criteria().andOperator(criteria.toArray(new Criteria[criteria.size()])));

    List<Abonnement> listAbonnements = mongoTemplate.find(query, Abonnement.class);

    return PageableExecutionUtils.getPage(
      listAbonnements, 
      pageable, 
      () -> mongoTemplate.count(Query.of(query).limit(-1).skip(-1), Abonnement.class));
  }

  @Override
  public Page<Abonnement> getAbonnements(TypeAbonnement typeAbonnement, Pageable pageable) {
    return abonnementDao.findByTypeAbonnementIdAndDeletedIsFalse(typeAbonnement.getId(), pageable);
  }

  @Override
  public Optional<Abonnement> getAbonnementByCompteClient(Compte client) {
    return abonnementDao.findByCompteClientIdAndDeletedIsFalse(client.getId());
  }

  @Override
  public Optional<Abonnement> getByCompteClientInfosPersoId(String infosPerso) {
    return abonnementDao.findByCompteClientInfosPersoIdAndDeletedIsFalse(infosPerso);
  }

  @Override
  public Optional<Abonnement> getByNumeroCarte(String numeroCarte) {
    return abonnementDao.findByNumeroCarte(numeroCarte);
  }

  @Override
  public Object getCustomResponseByNumeroCarte(String numeroCarte) {
    Optional<Abonnement> _abonnement = abonnementDao.findByNumeroCarte(numeroCarte.replaceAll("\\D+",""));
    if (!_abonnement.isPresent() || _abonnement.get().isDeleted() || _abonnement.get().isCarteBloquer() || _abonnement.get().getCompteClient().isDeleted())
      return null;

    Abonnement abonnement = _abonnement.get();

    Map <String,Object> customFields = new LinkedHashMap<>();
    customFields.put("abonnementId", abonnement.getId());
    customFields.put("typeAbonnement", abonnement.getTypeAbonnement());
    customFields.put("compteClient", abonnement.getCompteClient().getId());
    customFields.put("numeroCarte", abonnement.getNumeroCarte());
    customFields.put("solde", abonnement.getSolde());
    customFields.put("dateCreation", abonnement.getDateCreation());

    return customFields;
  }

  @Override
  public Page<Abonnement> getAbonnementByCompteCreateur(Compte createur, Pageable pageable) {
    return abonnementDao.findByCompteCreateurIdAndDeletedIsFalseOrderByDateCreationDesc(createur.getId(), pageable);
  }

  @Override
  public Optional<Abonnement> getAbonnementById(String id) {
    return abonnementDao.findById(id);
  }

  @Override
  public Abonnement createAbonnement(AbonnementRequest abonnementRequest, ECompteType typeCompteCreateur) {
    NumeroCarte numeroCarte = validateNewCarteAbonnement(abonnementRequest);

    TypeAbonnement typeAbonnement = typeAbonnementDao.findById(abonnementRequest.getTypeAbonnementId()).get();

    if (abonnementRequest.getInfosPersoId() == null && abonnementRequest.getEntrepriseId() == null)
      throw new IllegalArgumentException("Client ou entreprise est obligatoire!");

    final Query query = new Query();

    query.addCriteria(abonnementRequest.getInfosPersoId() != null ?
      Criteria.where("compteClient.infosPersoId").is(abonnementRequest.getInfosPersoId()) :
      Criteria.where("entreprise.id").is(abonnementRequest.getInfosPersoId())
    );

    Abonnement abonnement = mongoTemplate.findOne(query, Abonnement.class);

    if (abonnement != null && !abonnement.isDeleted())
      throw new IllegalArgumentException("Client ou entreprise déjà abonné!");

    UserDetailsImpl currentUser = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    Compte compteCreateur = compteDao.findByInfosPersoIdAndType(currentUser.getInfosPerso().getId(), typeCompteCreateur).get();

    Compte compteClient = null;
    Entreprise entreprise = null;

    if (abonnement == null) {
      if (abonnementRequest.getInfosPersoId() != null) {
        compteClient = compteDao.findByInfosPersoIdAndType(abonnementRequest.getInfosPersoId(), ECompteType.COMPTE_PARTICULIER).get();
        abonnement = new Abonnement(typeAbonnement, compteClient, compteCreateur, abonnementRequest.getStatut());
      } else {
        entreprise = entrepriseDao.findById(abonnementRequest.getEntrepriseId()).get();
        abonnement = new Abonnement(typeAbonnement, entreprise, compteCreateur, abonnementRequest.getStatut());
      }
    }

    abonnement.setResponsableId(compteCreateur.getId());
    abonnement.setCompteCreateur(compteCreateur);
    abonnement.setDeleted(false);
    abonnement.setSolde(BigDecimal.valueOf(0));

    abonnement.setTypeAbonnement(typeAbonnement);
    abonnement.setNumeroCarte(abonnementRequest.getNumeroCarte());
    abonnement.setCarteBloquer(false);

    abonnement.setPrixCarte(typeAbonnement.getPrix());
    abonnement.setDepotInitial(BigDecimal.valueOf(0));
    abonnementDao.save(abonnement);

    numeroCarte.setActive(true);
    numeroCarteDao.save(numeroCarte);

    if (compteClient != null) {
      compteClient.setServices(typeAbonnement.getServices());
      compteDao.save(compteClient);
    }

    return abonnement;
  }

  @Override
  public Abonnement changerAbonnement(String id, AbonnementRequest abonnementRequest, ECompteType typeCompteCreateur) {
    Optional<Abonnement> abonnementExist = abonnementDao.findById(id);

    if (!abonnementExist.isPresent() || abonnementExist.get().isDeleted() || abonnementExist.get().getCompteClient().isDeleted())
      throw new IllegalArgumentException("Cette abonnement n'existe pas!");

    if (abonnementExist.get().getEntreprise() != null)
      throw new IllegalArgumentException("Cette abonnement ne peut être changer!");

    NumeroCarte newNumeroCarte = validateNewCarteAbonnement(abonnementRequest);

    Abonnement abonnement = abonnementExist.get();

    TypeAbonnement typeAbonnement = typeAbonnementDao.findById(abonnementRequest.getTypeAbonnementId()).get();

    if (abonnement.getTypeAbonnement().getId().equals(typeAbonnement.getId()))
      throw new IllegalArgumentException("Ce client est déjà inscrit sur ce type d'abonnement!");
    
    UserDetailsImpl currentUser = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    Compte compteCreateur = compteDao.findByInfosPersoIdAndType(currentUser.getInfosPerso().getId(), typeCompteCreateur).get();

    NumeroCarte oldNumeroCarte = numeroCarteDao.findByNumero(abonnement.getNumeroCarte()).get();

    abonnement.setTypeAbonnement(typeAbonnement);
    abonnement.setNumeroCarte(abonnementRequest.getNumeroCarte());
    abonnement.setCompteCreateur(compteCreateur);
    abonnementDao.save(abonnement);

    newNumeroCarte.setActive(true);
    oldNumeroCarte.setActive(false);
    numeroCarteDao.saveAll(Arrays.asList(newNumeroCarte, oldNumeroCarte));

    if (abonnement.getCompteClient() != null) {
      Compte compteClient = compteDao.findByInfosPersoIdAndType(abonnement.getCompteClient().getId(), ECompteType.COMPTE_PARTICULIER).get();
      compteClient.setServices(typeAbonnement.getServices());
      compteDao.save(compteClient);
    }

    return abonnement;
  }

  @Override
  public void deleteAbonnement(String id) {
    Optional<Compte> compteClientExist = compteDao.findByInfosPersoIdAndType(id, ECompteType.COMPTE_PARTICULIER);

    if (!compteClientExist.isPresent() || compteClientExist.get().isDeleted())
      throw new IllegalArgumentException("Ce compte client n'existe pas!");

    Optional<Abonnement> abonnementExist = abonnementDao.findByCompteClientId(compteClientExist.get().getId());

    if (abonnementExist.isPresent() && !abonnementExist.get().isDeleted()) {
      Abonnement abonnement = abonnementExist.get();
      NumeroCarte oldNumeroCarte = numeroCarteDao.findByNumero(abonnement.getNumeroCarte()).get();

      abonnement.setDeleted(true);
      abonnement.setNumeroCarte(null);
      abonnement.setTypeAbonnement(null);
      abonnementDao.save(abonnement);

      oldNumeroCarte.setActive(false);
      numeroCarteDao.save(oldNumeroCarte);
    }
  }

  private NumeroCarte validateNewCarteAbonnement(AbonnementRequest abonnementRequest) {
    if (abonnementRequest.getInfosPersoId() != null && !compteDao.existsByInfosPersoIdAndTypeAndDeletedIsFalse(abonnementRequest.getInfosPersoId(), ECompteType.COMPTE_PARTICULIER))
        throw new IllegalArgumentException("Cette compte client n'existe pas!");
        
    if (abonnementRequest.getEntrepriseId() != null && !entrepriseDao.existsByIdAndDeletedIsFalse(abonnementRequest.getEntrepriseId()))
        throw new IllegalArgumentException("Cette entreprise n'existe pas!"); 

    if (!typeAbonnementDao.existsById(abonnementRequest.getTypeAbonnementId()))
      throw new IllegalArgumentException("Ce type d'abonnement n'existe pas!");

    Optional<NumeroCarte> numeroCarteExist = numeroCarteDao.findByNumero(abonnementRequest.getNumeroCarte());

    if (!numeroCarteExist.isPresent())
      throw new IllegalArgumentException("Cette carte n'existe pas!");

    if (!numeroCarteExist.get().getTypeAbonnementId().equals(abonnementRequest.getTypeAbonnementId()))
      throw new IllegalArgumentException("Cette carte ne correspond pas avec ce type d'abonnement!");

    if (numeroCarteExist.get().isActive())
      throw new IllegalArgumentException("Cette carte est déjà activé!");

    return numeroCarteExist.get();
  }
}
