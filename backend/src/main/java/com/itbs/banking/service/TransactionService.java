package com.itbs.banking.service;

import com.itbs.banking.entity.Account;
import com.itbs.banking.entity.Transaction;
import com.itbs.banking.entity.Transaction.TransactionType;
import com.itbs.banking.repository.AccountRepository;
import com.itbs.banking.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransactionService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private FraudDetectionService fraudDetectionService;

    @Transactional
    public Transaction deposit(Long accountId, Double amount) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        account.setBalance(account.getBalance() + amount);
        accountRepository.save(account);

        Transaction txn = new Transaction();
        txn.setToAccount(account);
        txn.setAmount(amount);
        txn.setType(TransactionType.DEPOSIT);

        Transaction saved = transactionRepository.save(txn);
        fraudDetectionService.checkTransaction(saved, account);
        return saved;
    }

    @Transactional
    public Transaction withdraw(Long accountId, Double amount) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        if (account.getBalance() < amount) {
            throw new RuntimeException("Insufficient balance");
        }

        account.setBalance(account.getBalance() - amount);
        accountRepository.save(account);

        Transaction txn = new Transaction();
        txn.setFromAccount(account);
        txn.setAmount(amount);
        txn.setType(TransactionType.WITHDRAW);

        Transaction saved = transactionRepository.save(txn);
        fraudDetectionService.checkTransaction(saved, account);
        return saved;
    }

    @Transactional
    public Transaction transfer(Long fromAccountId, Long toAccountId, Double amount) {
        Account fromAccount = accountRepository.findById(fromAccountId)
                .orElseThrow(() -> new RuntimeException("Sender account not found"));
        Account toAccount = accountRepository.findById(toAccountId)
                .orElseThrow(() -> new RuntimeException("Receiver account not found"));

        if (fromAccount.getBalance() < amount) {
            throw new RuntimeException("Insufficient balance");
        }

        fromAccount.setBalance(fromAccount.getBalance() - amount);
        toAccount.setBalance(toAccount.getBalance() + amount);
        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        Transaction txn = new Transaction();
        txn.setFromAccount(fromAccount);
        txn.setToAccount(toAccount);
        txn.setAmount(amount);
        txn.setType(TransactionType.TRANSFER);

        Transaction saved = transactionRepository.save(txn);
        fraudDetectionService.checkTransaction(saved, fromAccount);
        return saved;
    }
}