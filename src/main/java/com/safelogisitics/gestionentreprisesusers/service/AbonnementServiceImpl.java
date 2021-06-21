package com.safelogisitics.gestionentreprisesusers.service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;

import com.safelogisitics.gestionentreprisesusers.dao.AbonnementDao;
import com.safelogisitics.gestionentreprisesusers.dao.CompteDao;
import com.safelogisitics.gestionentreprisesusers.dao.NumeroCarteDao;
import com.safelogisitics.gestionentreprisesusers.dao.TypeAbonnementDao;
import com.safelogisitics.gestionentreprisesusers.model.Abonnement;
import com.safelogisitics.gestionentreprisesusers.model.Compte;
import com.safelogisitics.gestionentreprisesusers.model.NumeroCarte;
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
  private NumeroCarteDao numeroCarteDao;

  @Autowired
  private CompteDao compteDao;

  @Override
  public Page<Abonnement> getAbonnements(Pageable pageable) {
    return abonnementDao.findByDeletedIsFalse(pageable);
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
    return abonnementDao.findByCompteClientInfosPersoIdAndDeletedIsFalse(infosPerso, ECompteType.COMPTE_PARTICULIER);
  }

  @Override
  public Page<Abonnement> getAbonnementByCompteCreateur(Compte createur, Pageable pageable) {
    return abonnementDao.findByCompteCreateurIdAndDeletedIsFalse(createur.getId(), pageable);
  }

  @Override
  public Optional<Abonnement> getAbonnementById(String id) {
    return abonnementDao.findById(id);
  }

  @Override
  public Abonnement createAbonnement(AbonnementRequest abonnementRequest, ECompteType typeCompteCreateur) {
    NumeroCarte numeroCarte = validateNewCarteAbonnement(abonnementRequest);

    TypeAbonnement typeAbonnement = typeAbonnementDao.findById(abonnementRequest.getTypeAbonnementId()).get();

    Compte compteClient = compteDao.findByInfosPersoIdAndType(abonnementRequest.getInfosPersoId(), ECompteType.COMPTE_PARTICULIER).get();

    Optional<Abonnement> abonnementExist = abonnementDao.findByCompteClientId(compteClient.getId());

    if (abonnementExist.isPresent() && !abonnementExist.get().isDeleted())
      throw new IllegalArgumentException("Client déjà abonné!");

    UserDetailsImpl currentUser = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    Compte compteCreateur = compteDao.findByInfosPersoIdAndType(currentUser.getInfosPerso().getId(), typeCompteCreateur).get();

    Abonnement abonnement;

    if (abonnementExist.isPresent()) {
      abonnement = abonnementExist.get();
      abonnement.setCompteCreateur(compteCreateur);
      abonnement.setDeleted(false);
      abonnement.setSolde(BigDecimal.valueOf(0));
    } else {
      abonnement = new Abonnement(typeAbonnement, compteClient, compteCreateur, abonnementRequest.getStatut());
    }

    abonnement.setTypeAbonnement(typeAbonnement);
    abonnement.setNumeroCarte(abonnementRequest.getNumeroCarte());
    abonnement.setCarteBloquer(false);
    abonnementDao.save(abonnement);

    numeroCarte.setActive(true);
    numeroCarteDao.save(numeroCarte);

    return abonnement;
  }

  @Override
  public Abonnement changerAbonnement(String id, AbonnementRequest abonnementRequest, ECompteType typeCompteCreateur) {
    Optional<Abonnement> abonnementExist = abonnementDao.findById(id);

    if (!abonnementExist.isPresent() || !abonnementExist.get().isDeleted() || abonnementExist.get().getCompteClient().isDeleted())
      throw new IllegalArgumentException("Cette abonnement n'existe pas!");

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
    if (!compteDao.existsByInfosPersoIdAndTypeAndDeletedIsFalse(abonnementRequest.getInfosPersoId(), ECompteType.COMPTE_PARTICULIER))
      throw new IllegalArgumentException("Cette compte client n'existe pas!");

    if (!typeAbonnementDao.existsById(abonnementRequest.getTypeAbonnementId()))
      throw new IllegalArgumentException("Ce type d'abonnement n'existe pas!");

    Optional<NumeroCarte> numeroCarteExist = numeroCarteDao.findByNumero(abonnementRequest.getNumeroCarte());

    if (!numeroCarteExist.isPresent())
      throw new IllegalArgumentException("Cette carte n'existe pas!");

    if (!numeroCarteExist.get().getTypeAbonnementId().equals(abonnementRequest.getTypeAbonnementId()))
      throw new IllegalArgumentException("Cette carte ne correspond avec ce type d'abonnement!");

    if (numeroCarteExist.get().isActive())
      throw new IllegalArgumentException("Cette carte est déjà activé!");

    return numeroCarteExist.get();
  }
}
