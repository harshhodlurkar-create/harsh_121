package com.itbs.banking.repository;

import com.itbs.banking.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByFromAccountIdOrToAccountId(Long fromId, Long toId);
}