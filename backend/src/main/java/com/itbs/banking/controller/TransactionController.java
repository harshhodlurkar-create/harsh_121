package com.itbs.banking.controller;

import com.itbs.banking.entity.Transaction;
import com.itbs.banking.repository.TransactionRepository;
import com.itbs.banking.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private TransactionRepository transactionRepository;

    @PostMapping("/deposit")
    public Transaction deposit(@RequestParam Long accountId, @RequestParam Double amount) {
        return transactionService.deposit(accountId, amount);
    }

    @PostMapping("/withdraw")
    public Transaction withdraw(@RequestParam Long accountId, @RequestParam Double amount) {
        return transactionService.withdraw(accountId, amount);
    }

    @PostMapping("/transfer")
    public Transaction transfer(@RequestParam Long fromAccountId, @RequestParam Long toAccountId, @RequestParam Double amount) {
        return transactionService.transfer(fromAccountId, toAccountId, amount);
    }

    @GetMapping("/{accountId}")
    public List<Transaction> getTransactionHistory(@PathVariable Long accountId) {
        return transactionRepository.findByFromAccountIdOrToAccountId(accountId, accountId);
    }
}