package com.itbs.banking.controller;

import com.itbs.banking.entity.Account;
import com.itbs.banking.entity.User;
import com.itbs.banking.repository.AccountRepository;
import com.itbs.banking.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public Account createAccount(@RequestParam Long userId, @RequestParam Account.AccountType accountType) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Account account = new Account();
        account.setUser(user);
        account.setAccountType(accountType);
        account.setAccountNumber("ACC" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        account.setBalance(0.0);

        return accountRepository.save(account);
    }

    @GetMapping("/{id}")
    public Account getAccount(@PathVariable Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found"));
    }
}