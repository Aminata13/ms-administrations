package com.safelogisitics.gestionentreprisesusers.service;

import com.safelogisitics.gestionentreprisesusers.data.enums.EPeriode;
import org.bson.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

import com.safelogisitics.gestionentreprisesusers.data.model.Evenement;

public interface StatistiquesService {

    public Map<String, Long> getNumberClients();

    public Map<String, Long> getNumberAbonnement(EPeriode periode);

    public Page<Evenement> getFutureEvents(Pageable pageable);

    public Page<Evenement> getOwnEvents(Pageable pageable);

    public Map<String, Long> getNumberCartes();

    public Map<String, Long> getNumberClientsEnroles(EPeriode periode);
}
