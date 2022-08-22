package com.safelogisitics.gestionentreprisesusers.data.fixture;

import com.safelogisitics.gestionentreprisesusers.data.dao.TransactionDao;
import com.safelogisitics.gestionentreprisesusers.data.model.Transaction;
import com.safelogisitics.gestionentreprisesusers.service.SharedTransactionService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class InitSharedDbFixture implements CommandLineRunner {

    private TransactionDao transactionDao;

    private SharedTransactionService sharedTransactionService;

    public InitSharedDbFixture(TransactionDao transactionDao, SharedTransactionService sharedTransactionService) {
        this.transactionDao = transactionDao;
        this.sharedTransactionService = sharedTransactionService;
    }

    @Override
    public void run(String... args) throws Exception {
        Iterable<Transaction> transactions = transactionDao.findAll();

        for (Transaction transaction : transactions) {
            this.sharedTransactionService.createOrUpdateTransaction(transaction);
        }
    }
}
