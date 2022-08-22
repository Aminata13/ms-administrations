package com.safelogisitics.gestionentreprisesusers.data.shared.dao;

import com.safelogisitics.gestionentreprisesusers.data.shared.model.SharedTransactionModel;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SharedTransactionDao extends PagingAndSortingRepository<SharedTransactionModel, String> {
}
