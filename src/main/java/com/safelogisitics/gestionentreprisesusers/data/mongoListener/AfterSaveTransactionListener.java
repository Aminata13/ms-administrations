package com.safelogisitics.gestionentreprisesusers.data.mongoListener;

import com.safelogisitics.gestionentreprisesusers.data.model.Transaction;
import com.safelogisitics.gestionentreprisesusers.service.SharedTransactionService;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.AfterSaveEvent;
import org.springframework.stereotype.Component;

@Component
public class AfterSaveTransactionListener extends AbstractMongoEventListener<Transaction> {

    private SharedTransactionService sharedTransactionService;

    public AfterSaveTransactionListener(SharedTransactionService sharedTransactionService) {
        this.sharedTransactionService = sharedTransactionService;
    }

    @Override
    public void onAfterSave(AfterSaveEvent<Transaction> event) {

        Transaction transaction = event.getSource();

        if (transaction.getId() != null) {
            this.sharedTransactionService.createOrUpdateTransaction(transaction);
        }
    }
}
