package com.safelogisitics.gestionentreprisesusers.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safelogisitics.gestionentreprisesusers.data.enums.ETransactionAction;
import com.safelogisitics.gestionentreprisesusers.data.model.Transaction;
import com.safelogisitics.gestionentreprisesusers.data.shared.model.SharedEntrepriseModel;
import com.safelogisitics.gestionentreprisesusers.data.shared.model.SharedTransactionModel;
import com.safelogisitics.gestionentreprisesusers.data.shared.repository.SharedTransactionRepository;
import com.safelogisitics.gestionentreprisesusers.service.SharedTransactionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class SharedTransactionServiceImpl implements SharedTransactionService {

    private SharedTransactionRepository sharedTransactionRepository;

    private ObjectMapper objectMapper;

    public SharedTransactionServiceImpl(SharedTransactionRepository sharedTransactionRepository, ObjectMapper objectMapper) {
        this.sharedTransactionRepository = sharedTransactionRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public Page<SharedTransactionModel> getTransactions(Pageable pageable) {
        return this.sharedTransactionRepository.findAll(pageable);
    }

    @Override
    public SharedTransactionModel createOrUpdateTransaction(Transaction transactionRequest) {
        SharedTransactionModel transaction = null;

        if (this.sharedTransactionRepository.existsById(transactionRequest.getId())) {
            transaction = this.sharedTransactionRepository.findById(transactionRequest.getId()).get();
        } else {
            transaction = this.objectMapper.convertValue(transactionRequest, SharedTransactionModel.class);
            if (transaction.getAction().equals(ETransactionAction.PAIEMENT)) {
                transaction.setDateApprobation(transaction.getDateCreation());
                transaction.setApprobation(1);
            }
        }

        this.sharedTransactionRepository.save(transaction);

        return transaction;
    }

    @Override
    public void deleteTransaction(String transactionId) {
        this.sharedTransactionRepository.deleteById(transactionId);
    }
}
