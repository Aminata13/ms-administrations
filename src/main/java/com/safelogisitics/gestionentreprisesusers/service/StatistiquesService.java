package com.safelogisitics.gestionentreprisesusers.service;

import com.safelogisitics.gestionentreprisesusers.model.Evenement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface StatistiquesService {

    public Map<String, Long> getNumberClients();

    public Map<String, Long> getNumberAbonnement();

    public Page<Evenement> getFutureEvents(Pageable pageable);

    public Page<Evenement> getOwnEvents(Pageable pageable);

    public Map<String, Long> getNumberCartes();
}
