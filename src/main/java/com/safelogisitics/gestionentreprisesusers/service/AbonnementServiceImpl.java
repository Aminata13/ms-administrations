package com.safelogisitics.gestionentreprisesusers.service;

import java.util.Optional;

import com.safelogisitics.gestionentreprisesusers.dao.AbonnementDao;
import com.safelogisitics.gestionentreprisesusers.dao.CompteDao;
import com.safelogisitics.gestionentreprisesusers.dao.TypeAbonnementDao;
import com.safelogisitics.gestionentreprisesusers.model.Abonnement;
import com.safelogisitics.gestionentreprisesusers.model.Compte;
import com.safelogisitics.gestionentreprisesusers.model.TypeAbonnement;
import com.safelogisitics.gestionentreprisesusers.model.enums.ECompteType;
import com.safelogisitics.gestionentreprisesusers.payload.request.AbonnementRequest;
import com.safelogisitics.gestionentreprisesusers.security.services.UserDetailsImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AbonnementServiceImpl implements AbonnementService {

  @Autowired
  private AbonnementDao abonnementDao;

  @Autowired
  private TypeAbonnementDao typeAbonnementDao;

  @Autowired
  private CompteDao compteDao;

  @Override
  public Page<Abonnement> getAbonnements(Pageable pageable) {
    return abonnementDao.findByDeletedIsFalse(pageable);
  }

  @Override
  public Page<Abonnement> getAbonnements(TypeAbonnement typeAbonnement, Pageable pageable) {
    return abonnementDao.findByTypeAbonnementAndDeletedIsFalse(typeAbonnement, pageable);
  }

  @Override
  public Optional<Abonnement> getAbonnementByCompteClient(Compte client) {
    return abonnementDao.findByCompteClientAndDeletedIsFalse(client);
  }

  @Override
  public Page<Abonnement> getAbonnementByCompteCreateur(Compte createur, Pageable pageable) {
    return abonnementDao.findByCompteCreateurAndDeletedIsFalse(createur, pageable);
  }

  @Override
  public Optional<Abonnement> getAbonnementById(String id) {
    return abonnementDao.findById(id);
  }

  @Override
  public Abonnement createAbonnement(AbonnementRequest abonnementRequest) {
    Optional<TypeAbonnement> typeAbonnementExist = typeAbonnementDao.findById(abonnementRequest.getTypeAbonnementId());
    Optional<Compte> compteClientExist = compteDao.findByInfosPersoIdAndType(abonnementRequest.getInfosPersoId(), ECompteType.COMPTE_PARTICULIER);

    if (!typeAbonnementExist.isPresent() || !compteClientExist.isPresent() || compteClientExist.get().isDeleted()) {
      throw new IllegalArgumentException("TypeAbonnement or CompteClient with that id does not exists!");
    }

    if (abonnementRequest.getNumeroCarte() != null && abonnementDao.existsByNumeroCarte(abonnementRequest.getNumeroCarte())) {
      throw new IllegalArgumentException("Abonnement with that numeroCarte already exist!");
    }

    Compte compteClient = compteClientExist.get();

    TypeAbonnement typeAbonnement = typeAbonnementExist.get();

    Optional<Abonnement> abonnementExist = abonnementDao.findByCompteClient(compteClient);

    if (abonnementExist.isPresent() && !abonnementExist.get().isDeleted()) {
      throw new IllegalArgumentException("Abonnement with that client already exist!");
    }

    UserDetailsImpl currentUser = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    Compte compteCreateur = compteDao.findByInfosPersoIdAndType(currentUser.getInfosPerso().getId(), ECompteType.COMPTE_ADMINISTRATEUR).get();

    if (abonnementExist.isPresent() && abonnementExist.get().isDeleted()) {
      Abonnement abonnement = abonnementExist.get();
      abonnement.setDeleted(false);
      abonnement.setCompteCreateur(compteCreateur);
      abonnementDao.save(abonnement);
      return updateAbonnement(abonnement.getId(), abonnementRequest);
    }

    Abonnement abonnement = new Abonnement(typeAbonnement, compteClient, compteCreateur, abonnementRequest.getStatut());
    
    if (abonnementRequest.getNumeroCarte() != null) {
      abonnement.setNumeroCarte(abonnementRequest.getNumeroCarte());
      abonnement.setCarteBloquer(abonnementRequest.isCarteBloquer());
    }

    abonnementDao.save(abonnement);

    return abonnement;
  }

  @Override
  public Abonnement updateAbonnement(String id, AbonnementRequest abonnementRequest) {
    Optional<Abonnement> abonnementExist = abonnementDao.findById(id);

    if (!abonnementExist.isPresent() || !abonnementExist.get().isDeleted() || abonnementExist.get().getCompteClient().isDeleted()) {
      throw new IllegalArgumentException("Abonnement with that does not exist!");
    }

    Abonnement abonnement = abonnementExist.get();

    Optional<TypeAbonnement> typeAbonnementExist = typeAbonnementDao.findById(abonnementRequest.getTypeAbonnementId());

    if (!typeAbonnementExist.isPresent()) {
      throw new IllegalArgumentException("TypeAbonnement with that id does not exists!");
    }

    if (abonnementRequest.getNumeroCarte() != null && abonnement.getNumeroCarte() != abonnementRequest.getNumeroCarte() && abonnementDao.existsByNumeroCarte(abonnementRequest.getNumeroCarte())) {
      throw new IllegalArgumentException("Abonnement with that numeroCarte already exist!");
    }

    TypeAbonnement typeAbonnement = typeAbonnementExist.get();

    abonnement.setTypeAbonnement(typeAbonnement);
    abonnement.setStatut(abonnementRequest.getStatut());
    
    if (abonnementRequest.getNumeroCarte() != null) {
      abonnement.setNumeroCarte(abonnementRequest.getNumeroCarte());
      abonnement.setCarteBloquer(abonnementRequest.isCarteBloquer());
    }

    abonnementDao.save(abonnement);

    return abonnement;
  }

  @Override
  public void deleteAbonnement(String id) {
    Optional<Compte> compteClientExist = compteDao.findByInfosPersoIdAndType(id, ECompteType.COMPTE_PARTICULIER);

    if (!compteClientExist.isPresent()) {
      throw new IllegalArgumentException("CompteClient with that id does not exists!");
    }

    Optional<Abonnement> abonnementExist = abonnementDao.findByCompteClient(compteClientExist.get());

    if (abonnementExist.isPresent()) {
      Abonnement abonnement = abonnementExist.get();
      abonnement.setDeleted(true);
      abonnementDao.save(abonnement);
    }
  }
}
