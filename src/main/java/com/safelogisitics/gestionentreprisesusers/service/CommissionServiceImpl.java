package com.safelogisitics.gestionentreprisesusers.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safelogisitics.gestionentreprisesusers.data.dao.CompteDao;
import com.safelogisitics.gestionentreprisesusers.data.dto.request.CommissionRequestDto;
import com.safelogisitics.gestionentreprisesusers.data.dto.request.CommissionSearchRequestDto;
import com.safelogisitics.gestionentreprisesusers.data.dto.request.PayerCommissionsRequestDto;
import com.safelogisitics.gestionentreprisesusers.data.dto.response.BeneficiaireCommissionResponseDto;
import com.safelogisitics.gestionentreprisesusers.data.enums.ECompteType;
import com.safelogisitics.gestionentreprisesusers.data.model.CommissionModel;
import com.safelogisitics.gestionentreprisesusers.data.model.Compte;
import com.safelogisitics.gestionentreprisesusers.data.model.InfosPersoModel;
import com.safelogisitics.gestionentreprisesusers.data.model.PaiementCommissionModel;
import com.safelogisitics.gestionentreprisesusers.data.repository.CommissionRepository;
import com.safelogisitics.gestionentreprisesusers.data.repository.PaiementCommissionRepository;
import com.safelogisitics.gestionentreprisesusers.web.security.services.UserDetailsImpl;
import org.bson.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.ConvertOperators;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommissionServiceImpl implements CommissionService {

  private CommissionRepository commissionRepository;

  private CompteDao compteDao;

  private PaiementCommissionRepository paiementCommissionRepository;

  private MongoTemplate mongoTemplate;

  private ObjectMapper objectMapper;

  public CommissionServiceImpl(CommissionRepository commissionRepository, CompteDao compteDao, PaiementCommissionRepository paiementCommissionRepository, MongoTemplate mongoTemplate, ObjectMapper objectMapper) {
    this.commissionRepository = commissionRepository;
    this.compteDao = compteDao;
    this.paiementCommissionRepository = paiementCommissionRepository;
    this.mongoTemplate = mongoTemplate;
    this.objectMapper = objectMapper;
  }

  @Override
  public Page<CommissionModel> getListCommissions(CommissionSearchRequestDto commissionSearchRequest, Pageable pageable) {
    return this.commissionRepository.customSearch(commissionSearchRequest, pageable);
  }

  @Override
  public Optional<CommissionModel> getOneCommission(String id) {
    return this.commissionRepository.findById(id);
  }

  @Override
  public CommissionModel createCommission(CommissionRequestDto commissionRequest) {
    if (this.commissionRepository.existsByNumeroAndService(commissionRequest.getNumero(), commissionRequest.getService())) {
      throw new IllegalArgumentException("Cette commande est déjà commissionée.");
    }

    CommissionModel commission = new CommissionModel(commissionRequest);
    commission.calculCommission(commissionRequest.getPrix());
    this.commissionRepository.save(commission);

    return commission;
  }

  @Override
  public CommissionModel updateCommission(String id, CommissionRequestDto commissionRequest) {
    Optional<CommissionModel> commissionExist = this.commissionRepository.findById(id);
    if (!commissionExist.isPresent()) {
      throw new IllegalArgumentException("Cette commission n'existe pas.");
    }

    Optional<CommissionModel> commissionDuplicate = this.commissionRepository.findByNumeroAndService(
      commissionRequest.getNumero(), commissionRequest.getService());

    if (commissionDuplicate.isPresent() && commissionDuplicate.get().getId().equals(commissionExist.get().getId())) {
      throw new IllegalArgumentException("Cette commande est déjà commissionée.");
    }

    CommissionModel commission = commissionExist.get();

    commission.setData(commissionRequest);
    commission.calculCommission(commissionRequest.getPrix());
    this.commissionRepository.save(commission);

    return commission;
  }

  @Override
  public PaiementCommissionModel payerCommissions(PayerCommissionsRequestDto payerCommissionsRequest) {
    CommissionSearchRequestDto commissionSearch = new CommissionSearchRequestDto();
    commissionSearch.setIds(payerCommissionsRequest.getIds());
    commissionSearch.setResponsableId(payerCommissionsRequest.getResponsableId());
    commissionSearch.setPayer(false);

    Page<CommissionModel> commissions = commissionRepository.customSearch(commissionSearch, PageRequest.of(0, Integer.MAX_VALUE));

    BigDecimal montant = commissions.stream().map(commision -> commision.getMontant()).reduce(BigDecimal.ZERO, BigDecimal::add);

    if (montant.compareTo(BigDecimal.ZERO) == 0) {
      throw new IllegalArgumentException("Aucune commission à payer.");
    }

    UserDetailsImpl currentUser = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    Compte compteAuteur = compteDao.findByInfosPersoIdAndType(currentUser.getInfosPerso().getId(), ECompteType.COMPTE_ADMINISTRATEUR).get();

    PaiementCommissionModel paiementCommission = new PaiementCommissionModel(montant, payerCommissionsRequest.getIds(),
      payerCommissionsRequest.getResponsableId(), compteAuteur.getId(), payerCommissionsRequest.getPaiementMethode());

    this.paiementCommissionRepository.save(paiementCommission);

    for (CommissionModel commission : commissions) {
      commission.payer(paiementCommission.getId());
      this.commissionRepository.save(commission);
    }

    return paiementCommission;
  }

  @Override
  public void deleteCommission(String id) {
    Optional<CommissionModel> commissionExist = commissionRepository.findById(id);
    if (!commissionExist.isPresent()) {
      return;
    }

    CommissionModel commission = commissionExist.get();
    
    if (commission.isPayer()) {
      Optional<PaiementCommissionModel> paiementCommissionExist = this.paiementCommissionRepository.findBycommmandeIds(id);

      if (paiementCommissionExist.isPresent()) {
        PaiementCommissionModel paiementCommission = paiementCommissionExist.get();
        paiementCommission.getCommmandeIds().remove(id);
        paiementCommission.setMontant(paiementCommission.getMontant().subtract(commission.getMontant()));
        this.paiementCommissionRepository.save(paiementCommission);
      }
    }

    commissionRepository.delete(commission);
  }

  @Override
  public void deleteCommissionByCommandeId(String id) {
    Optional<CommissionModel> commissionExist = commissionRepository.findByCommandeId(id);
    if (!commissionExist.isPresent()) {
      return;
    }
    this.deleteCommission(commissionExist.get().getId());
  }

  @Override
  public Page<PaiementCommissionModel> getListPaiementCommissions(CommissionSearchRequestDto commissionSearchRequest, Pageable pageable) {
    return this.paiementCommissionRepository.customSearch(commissionSearchRequest, pageable);
  }

  @Override
  public List<BeneficiaireCommissionResponseDto> getListBeneficiaires() {
    final List<AggregationOperation> aggregations = new ArrayList<AggregationOperation>();

    aggregations.add(Aggregation.addFields().addFieldWithValue("infosPersoId", ConvertOperators.ToString.toString("$_id")).build());
    aggregations.add(Aggregation.lookup("comptes", "infosPersoId", "infosPersoId", "compte"));
    aggregations.add(Aggregation.unwind("compte"));

    aggregations.add(Aggregation.addFields().addFieldWithValue("compteId", ConvertOperators.ToString.toString("$compte._id")).build());
    aggregations.add(Aggregation.lookup("commissions", "compteId", "responsableId", "commission"));
    aggregations.add(Aggregation.unwind("commission"));

    aggregations.add(Aggregation.group("infosPersoId", "prenom", "nom"));

    Aggregation aggregation = Aggregation.newAggregation(aggregations);

    List<BeneficiaireCommissionResponseDto> beneficiaires = mongoTemplate.aggregate(aggregation, InfosPersoModel.class, Document.class)
            .getMappedResults()
            .stream().map(document -> objectMapper.convertValue(document.get("_id"), BeneficiaireCommissionResponseDto.class))
            .collect(Collectors.toList());

    return beneficiaires;
  }

}
