package com.safelogisitics.gestionentreprisesusers.service;

import com.safelogisitics.gestionentreprisesusers.data.model.Transaction;
import com.safelogisitics.gestionentreprisesusers.data.shared.model.SharedTransactionModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SharedTransactionService {

    public Page<SharedTransactionModel> getTransactions(Pageable pageable);

    public SharedTransactionModel createOrUpdateTransaction(Transaction transactionRequest);

    public void deleteTransaction(String transactionId);
}
