package com.safelogisitics.gestionentreprisesusers.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import com.safelogisitics.gestionentreprisesusers.data.dao.CompteDao;
import com.safelogisitics.gestionentreprisesusers.data.dao.EntrepriseDao;
import com.safelogisitics.gestionentreprisesusers.data.dao.InfosPersoDao;
import com.safelogisitics.gestionentreprisesusers.data.dao.ProspectDao;
import com.safelogisitics.gestionentreprisesusers.data.dto.request.ProspectRequest;
import com.safelogisitics.gestionentreprisesusers.data.dto.request.RegisterRequest;
import com.safelogisitics.gestionentreprisesusers.data.dto.response.UserInfosResponse;
import com.safelogisitics.gestionentreprisesusers.data.model.Compte;
import com.safelogisitics.gestionentreprisesusers.data.model.Entreprise;
import com.safelogisitics.gestionentreprisesusers.data.model.InfosPerso;
import com.safelogisitics.gestionentreprisesusers.data.model.Prospect;
import com.safelogisitics.gestionentreprisesusers.data.model.enums.ECompteType;
import com.safelogisitics.gestionentreprisesusers.data.model.enums.EProspecteurType;
import com.safelogisitics.gestionentreprisesusers.data.model.enums.ETypeProspect;
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
public class ProspectServiceImpl implements ProspectService {

  @Autowired
  private ProspectDao prospectDao;

  @Autowired
  private CompteDao compteDao;

  @Autowired
  private InfosPersoDao infosPersoDao;

  @Autowired
  private EntrepriseDao entrepriseDao;

  @Autowired
  private EntrepriseService entrepriseService;

  @Autowired
  private InfosPersoService infosPersoService;

  @Autowired
  private MongoTemplate mongoTemplate;

  @Override
  public Page<Map<String, Object>> getProspects(Map<String,String> parameters, Pageable pageable) {
    final Query query = new Query().with(pageable).with(Sort.by(Sort.Direction.DESC, "dateCreation"));
    final List<Criteria> criteria = new ArrayList<>();

    // Rechercher dans infos general
    if (parameters != null && parameters.containsKey("id") && !parameters.get("id").isEmpty())
      criteria.add(Criteria.where("id").is(parameters.get("id")));

    if (parameters != null && parameters.containsKey("type") && !parameters.get("type").isEmpty())
      criteria.add(Criteria.where("type").is(parameters.get("type")));

    if (parameters != null && parameters.containsKey("prospecteurType") && !parameters.get("prospecteurType").isEmpty())
      criteria.add(Criteria.where("prospecteurType").is(parameters.get("prospecteurType")));

    if (parameters != null && parameters.containsKey("statutProspection") && !parameters.get("statutProspection").isEmpty())
      criteria.add(Criteria.where("statutProspection").is(Integer.valueOf(parameters.get("statutProspection"))));

    if (parameters != null && parameters.containsKey("prospecteurId") && !parameters.get("prospecteurId").isEmpty())
      criteria.add(Criteria.where("prospecteurId").is(parameters.get("prospecteurId")));

    if (parameters != null && parameters.containsKey("enroleurId") && !parameters.get("enroleurId").isEmpty())
      criteria.add(Criteria.where("enroleurId").is(parameters.get("enroleurId")));


    // Rechercher dans infos entreprise
    if (parameters != null && parameters.containsKey("infosEntreprise.typeEntreprise") && !parameters.get("infosEntreprise.typeEntreprise").isEmpty())
      criteria.add(Criteria.where("infosEntreprise.typeEntreprise").is(parameters.get("infosEntreprise.typeEntreprise")));

    if (parameters != null && parameters.containsKey("infosEntreprise.denomination") && !parameters.get("infosEntreprise.denomination").isEmpty())
      criteria.add(Criteria.where("infosEntreprise.denomination").regex(".*"+parameters.get("infosEntreprise.denomination").trim()+".*","i"));

    if (parameters != null && parameters.containsKey("infosEntreprise.ninea") && !parameters.get("infosEntreprise.ninea").isEmpty())
      criteria.add(Criteria.where("infosEntreprise.ninea").regex(".*"+parameters.get("infosEntreprise.ninea").trim()+".*","i"));

    if (parameters != null && parameters.containsKey("infosEntreprise.email") && !parameters.get("infosEntreprise.email").isEmpty())
      criteria.add(Criteria.where("infosEntreprise.email").regex(".*"+parameters.get("infosEntreprise.email").trim()+".*","i"));

    if (parameters != null && parameters.containsKey("infosEntreprise.telephone") && !parameters.get("infosEntreprise.telephone").isEmpty())
      criteria.add(Criteria.where("infosEntreprise.telephone").regex(".*"+parameters.get("infosEntreprise.telephone").trim()+".*","i"));

    if (parameters != null && parameters.containsKey("infosEntreprise.adresse") && !parameters.get("infosEntreprise.adresse").isEmpty())
      criteria.add(Criteria.where("infosEntreprise.adresse").regex(".*"+parameters.get("infosEntreprise.adresse").trim()+".*","i"));


    // Recherche dans infos particulier 
    if (parameters != null && parameters.containsKey("infosParticulier.prenom") && !parameters.get("infosParticulier.prenom").isEmpty())
      criteria.add(Criteria.where("infosParticulier.prenom").regex(".*"+parameters.get("infosParticulier.prenom").trim()+".*","i"));

    if (parameters != null && parameters.containsKey("infosParticulier.nom") && !parameters.get("infosParticulier.nom").isEmpty())
      criteria.add(Criteria.where("infosParticulier.nom").regex(".*"+parameters.get("infosParticulier.nom").trim()+".*","i"));

    if (parameters != null && parameters.containsKey("infosParticulier.email") && !parameters.get("infosParticulier.email").isEmpty())
      criteria.add(Criteria.where("infosParticulier.email").regex(".*"+parameters.get("infosParticulier.email").trim()+".*","i"));

    if (parameters != null && parameters.containsKey("infosParticulier.telephone") && !parameters.get("infosParticulier.telephone").isEmpty())
      criteria.add(Criteria.where("infosParticulier.telephone").regex(".*"+parameters.get("infosParticulier.telephone").trim()+".*","i"));

    if (!criteria.isEmpty())
      query.addCriteria(new Criteria().andOperator(criteria.toArray(new Criteria[criteria.size()])));

    List<Prospect> listprospects = mongoTemplate.find(query, Prospect.class);

    Page<Prospect> prospects = PageableExecutionUtils.getPage(
      listprospects, 
      pageable, 
      () -> mongoTemplate.count(Query.of(query).limit(-1).skip(-1), Prospect.class));

    return prospects.map(new Function<Prospect, Map<String, Object>>() {
      @Override
      public Map<String, Object> apply(Prospect prospect) {
        return getDefaultFields(prospect);
      }
    });
  }

  @Override
  public Map<String, Object> getProspectById(String id) {
    Optional<Prospect> prospect = prospectDao.findById(id);
    if (!prospect.isPresent()) {
      return new LinkedHashMap<>();
    }
    return getDefaultFields(prospect.get());
  }

  @Override
  public Map<String, Object> createProspect(ProspectRequest prospectRequest, EProspecteurType prospecteurType) {
    if (
      prospectRequest.getType().equals(ETypeProspect.PARTICULIER) &&
      prospectDao.existsByInfosParticulierEmailOrInfosParticulierTelephone(prospectRequest.getInfosParticulier().getEmail(), prospectRequest.getInfosParticulier().getTelephone())
    ) throw new IllegalArgumentException("Ce particulier existe déjà");

    if (
      prospectRequest.getType().equals(ETypeProspect.ENTREPRISE) &&
      (prospectDao.existsByInfosEntrepriseDenomination(prospectRequest.getInfosEntreprise().getDenomination()) ||
      (prospectRequest.getInfosEntreprise().getNinea() != null && prospectDao.existsByInfosEntrepriseNinea(prospectRequest.getInfosEntreprise().getNinea())))
    ) throw new IllegalArgumentException("Cette entreprise existe déjà");

    UserDetailsImpl currentUser = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    String prospecteurId = prospecteurType.equals(EProspecteurType.COMPTE_ADMINISTRATEUR) ? 
      compteDao.findByInfosPersoIdAndType(currentUser.getInfosPerso().getId(), ECompteType.COMPTE_ADMINISTRATEUR).get().getId() :
      compteDao.findByInfosPersoIdAndType(currentUser.getInfosPerso().getId(), ECompteType.COMPTE_ENTREPRISE).get().getEntrepriseId();

    Prospect prospect = new Prospect(prospectRequest.getType(), prospecteurId, prospectRequest.getNiveauAvancement(), prospectRequest.getRapportVisites());

    prospect.setProspecteurType(prospecteurType.equals(EProspecteurType.COMPTE_ADMINISTRATEUR) ? EProspecteurType.COMPTE_ADMINISTRATEUR :
      EProspecteurType.COMPTE_ENTREPRISE);

    if (prospectRequest.getType().equals(ETypeProspect.PARTICULIER)) {
      if(prospectRequest.getInfosParticulier() == null)
        throw new IllegalArgumentException("Veuillez remplir les informations du client");

      prospect.setInfosParticulier(new InfosPerso(
        prospectRequest.getInfosParticulier().getPrenom(),
        prospectRequest.getInfosParticulier().getNom(),
        prospectRequest.getInfosParticulier().getEmail(),
        prospectRequest.getInfosParticulier().getTelephone(),
        prospectRequest.getInfosParticulier().getAdresse(),
        prospectRequest.getInfosParticulier().getDateNaissance()
      ));
    }

    if (prospectRequest.getType().equals(ETypeProspect.ENTREPRISE)) {
      if(prospectRequest.getInfosEntreprise() == null)
        throw new IllegalArgumentException("Veuillez remplir les informations de l'entreprise");

      prospect.setInfosEntreprise(new Entreprise(
        prospectRequest.getInfosEntreprise().getTypeEntreprise(),
        prospectRequest.getInfosEntreprise().getDomaineActivite(),
        prospectRequest.getInfosEntreprise().getDenomination(),
        prospectRequest.getInfosEntreprise().getNinea(),
        prospectRequest.getInfosEntreprise().getRaisonSociale(),
        prospectRequest.getInfosEntreprise().getEmail(),
        prospectRequest.getInfosEntreprise().getTelephone(),
        prospectRequest.getInfosEntreprise().getAdresse()
      ));
    }

    prospect.setPersonneRencontrer(prospectRequest.getPersonneRencontrer());

    prospectDao.save(prospect);

    return getDefaultFields(prospect);
  }

  @Override
  public Map<String, Object> updateProspect(String id, ProspectRequest prospectRequest, boolean force) {
    Optional<Prospect> prospectExist = prospectDao.findById(id);
    if (!prospectExist.isPresent())
      throw new IllegalArgumentException("Ce prospect n'existe pas");

    Prospect prospect = prospectExist.get();

    UserDetailsImpl currentUser = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    if (!force) {
      try {
        String prospecteurId = prospect.getProspecteurType().equals(EProspecteurType.COMPTE_ADMINISTRATEUR) ? 
        compteDao.findByInfosPersoIdAndType(currentUser.getInfosPerso().getId(), ECompteType.COMPTE_ADMINISTRATEUR).get().getId() :
        compteDao.findByInfosPersoIdAndType(currentUser.getInfosPerso().getId(), ECompteType.COMPTE_ENTREPRISE).get().getEntrepriseId();
  
        if (!prospect.getProspecteurId().equals(prospecteurId) || prospect.getStatutProspection() == 1) {
          throw new IllegalArgumentException("Accès refusé");
        }
      } catch (Exception e) {
        throw new IllegalArgumentException("Accès refusé");
      }
    }

    if (
      prospectRequest.getType().equals(ETypeProspect.PARTICULIER) &&
      !prospect.getInfosParticulier().getEmail().equals(prospectRequest.getInfosParticulier().getEmail()) &&
      prospectDao.existsByInfosParticulierEmail(prospectRequest.getInfosParticulier().getEmail())
    ) throw new IllegalArgumentException("Cette adresse email existe déjà");

    if (
      prospectRequest.getType().equals(ETypeProspect.PARTICULIER) &&
      !prospect.getInfosParticulier().getTelephone().equals(prospectRequest.getInfosParticulier().getTelephone()) &&
      prospectDao.existsByInfosParticulierTelephone(prospectRequest.getInfosParticulier().getTelephone())
    ) throw new IllegalArgumentException("Ce numéro de téléphone existe déjà");

    if (
      prospectRequest.getType().equals(ETypeProspect.ENTREPRISE) &&
      !prospect.getInfosEntreprise().getDenomination().equals(prospectRequest.getInfosEntreprise().getDenomination()) &&
      prospectDao.existsByInfosEntrepriseDenomination(prospectRequest.getInfosEntreprise().getDenomination())
    ) throw new IllegalArgumentException("Cette dénomination existe déjà");

    if (
      prospectRequest.getType().equals(ETypeProspect.ENTREPRISE) && prospectRequest.getInfosEntreprise().getNinea() != null &&
      !prospect.getInfosEntreprise().getNinea().equals(prospectRequest.getInfosEntreprise().getNinea()) &&
      prospectDao.existsByInfosEntrepriseNinea(prospectRequest.getInfosEntreprise().getNinea())
    ) throw new IllegalArgumentException("Ce ninéa existe déjà");

    if (prospectRequest.getType().equals(ETypeProspect.PARTICULIER)) {
      prospect.setInfosParticulier(new InfosPerso(
        prospectRequest.getInfosParticulier().getPrenom(),
        prospectRequest.getInfosParticulier().getNom(),
        prospectRequest.getInfosParticulier().getEmail(),
        prospectRequest.getInfosParticulier().getTelephone(),
        prospectRequest.getInfosParticulier().getAdresse(),
        prospectRequest.getInfosParticulier().getDateNaissance()
      ));
    } else {
      prospect.setInfosEntreprise(new Entreprise(
        prospectRequest.getInfosEntreprise().getTypeEntreprise(),
        prospectRequest.getInfosEntreprise().getDomaineActivite(),
        prospectRequest.getInfosEntreprise().getDenomination(),
        prospectRequest.getInfosEntreprise().getNinea(),
        prospectRequest.getInfosEntreprise().getRaisonSociale(),
        prospectRequest.getInfosEntreprise().getEmail(),
        prospectRequest.getInfosEntreprise().getTelephone(),
        prospectRequest.getInfosEntreprise().getAdresse()
      ));
    }

    if (currentUser.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals(String.format("ROLE_%s", ECompteType.COMPTE_ADMINISTRATEUR.name())))) {
      Compte dernierEditeur = compteDao.findByInfosPersoIdAndType(currentUser.getInfosPerso().getId(), ECompteType.COMPTE_ADMINISTRATEUR).get();
      prospect.setDernierEditeurId(dernierEditeur.getId());
    }

    prospect.setNiveauAvancement(prospectRequest.getNiveauAvancement());
    prospect.setPersonneRencontrer(prospectRequest.getPersonneRencontrer());
    prospect.setRapportVisites(prospectRequest.getRapportVisites());
    prospect.setDateDerniereModification(LocalDateTime.now());
    prospectDao.save(prospect);

    return getDefaultFields(prospect);
  }

  @Override
  public Map<String, Object> enroleProspect(String id, ProspectRequest prospectRequest) {
    updateProspect(id, prospectRequest, true);
    Prospect prospect = prospectDao.findById(id).get();

    if (prospect.getNiveauAvancement() == 100) {
      throw new IllegalArgumentException("Le niveau d'avancement doit atteindre 100%.");
    }

    UserDetailsImpl currentUser = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    Compte enroleur = compteDao.findByInfosPersoIdAndType(currentUser.getInfosPerso().getId(), ECompteType.COMPTE_ADMINISTRATEUR).get();

    if (!prospect.getProspecteurId().equals(enroleur.getId()) && prospect.getProspecteurType().equals(EProspecteurType.COMPTE_ADMINISTRATEUR)) {
      throw new IllegalArgumentException("Vous ne pouvez pas.");
    }

    if (prospect.getType().equals(ETypeProspect.PARTICULIER)) {
      RegisterRequest registerRequest = new RegisterRequest(
        prospectRequest.getInfosParticulier().getPrenom(),
        prospectRequest.getInfosParticulier().getNom(),
        prospectRequest.getInfosParticulier().getEmail(),
        prospectRequest.getInfosParticulier().getTelephone(),
        prospectRequest.getInfosParticulier().getEmail(),
        prospectRequest.getInfosParticulier().getTelephone(),
        prospectRequest.getInfosParticulier().getAdresse(),
        null, null, null, null
      );

      UserInfosResponse infosPerso = infosPersoService.createCompteClient(registerRequest);
      if (prospect.getProspecteurType().equals(EProspecteurType.COMPTE_ENTREPRISE)) {
        Compte compteClient = compteDao.findByInfosPersoIdAndType(infosPerso.getId(), ECompteType.COMPTE_PARTICULIER).get();
        compteClient.setEntrepriseId(prospect.getProspecteurId());
        compteDao.save(compteClient);
      }
      prospect.getInfosParticulier().setId(infosPerso.getId());
    }

    if (prospect.getType().equals(ETypeProspect.ENTREPRISE)) {
      Entreprise entreprise = entrepriseService.createEntreprise(prospectRequest.getInfosEntreprise());
      prospect.getInfosEntreprise().setId(entreprise.getId());
    }

    prospect.setStatutProspection(1);
    prospect.setEnroleurId(enroleur.getId());
    prospect.setDateEnrolement(LocalDateTime.now());

    prospectDao.save(prospect);

    return getDefaultFields(prospect);
  }

  @Override
  public void deleteProspect(String id) {
    Optional<Prospect> prospectExist = prospectDao.findById(id);
    if (!prospectExist.isPresent())
      throw new IllegalArgumentException("Ce prospect n'existe pas");

    Prospect prospect = prospectExist.get();

    UserDetailsImpl currentUser = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    if (!currentUser.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals(String.format("ROLE_%s", ECompteType.COMPTE_ADMINISTRATEUR.name())))) {
      String prospecteurId = prospect.getProspecteurType().equals(EProspecteurType.COMPTE_ADMINISTRATEUR) ? 
      compteDao.findByInfosPersoIdAndType(currentUser.getInfosPerso().getId(), ECompteType.COMPTE_ADMINISTRATEUR).get().getId() :
      compteDao.findByInfosPersoIdAndType(currentUser.getInfosPerso().getId(), ECompteType.COMPTE_ENTREPRISE).get().getEntrepriseId();

      if (!prospect.getProspecteurId().equals(prospecteurId)) {
        throw new IllegalArgumentException("Accès refusé");
      } 
    }

    prospectDao.delete(prospect);
  }

  private Map<String, Object> getDefaultFields(Prospect prospect) {
    Map<String, Object> customFields = new LinkedHashMap<>();

    if (prospect == null) {
      return customFields;
    }

    Object prospecteur = prospect.getProspecteurType().equals(EProspecteurType.COMPTE_ADMINISTRATEUR) ? 
      infosPersoDao.findById(compteDao.findById(prospect.getProspecteurId()).get().getInfosPersoId()).get().getDefaultFields() :
      entrepriseDao.findById(prospect.getProspecteurId()).get().getDefaultFields();

    customFields.put("id", prospect.getId());
    customFields.put("type", prospect.getType().name());
    customFields.put("prospecteurType", prospect.getProspecteurType().name());
    customFields.put("statutProspection", prospect.getStatutProspection());
    customFields.put("rapportVisites", prospect.getRapportVisites());
    customFields.put("niveauAvancement", prospect.getNiveauAvancement());
    customFields.put("infosEntreprise", prospect.getInfosEntreprise());
    customFields.put("infosParticulier", prospect.getInfosParticulier());
    customFields.put("personneRencontrer", prospect.getPersonneRencontrer());
    customFields.put("prospecteur", prospecteur);
    customFields.put("dernierEditeur", prospect.getDernierEditeurId() != null ? infosPersoDao.findById(compteDao.findById(prospect.getDernierEditeurId()).get().getInfosPersoId()).get().getDefaultFields() : null);
    customFields.put("enroleur", prospect.getEnroleurId() != null ? infosPersoDao.findById(compteDao.findById(prospect.getEnroleurId()).get().getInfosPersoId()).get().getDefaultFields() : null);
    customFields.put("dateEnrolement", prospect.getDateEnrolement());
    customFields.put("dateDerniereModification", prospect.getDateDerniereModification());
    customFields.put("dateCreation", prospect.getDateCreation());

    return customFields;
  }
}
