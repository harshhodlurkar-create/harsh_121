package com.itbs.banking.service;

import com.itbs.banking.entity.Account;
import com.itbs.banking.entity.FraudAlert;
import com.itbs.banking.entity.FraudAlert.AdminAction;
import com.itbs.banking.entity.Transaction;
import com.itbs.banking.entity.Transaction.TransactionStatus;
import com.itbs.banking.entity.User;
import com.itbs.banking.repository.AccountRepository;
import com.itbs.banking.repository.FraudAlertRepository;
import com.itbs.banking.repository.TransactionRepository;
import com.itbs.banking.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FraudAlertService {

    @Autowired
    private FraudAlertRepository fraudAlertRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    public List<FraudAlert> getAllAlerts() {
        return fraudAlertRepository.findAll();
    }

    @Transactional
    public FraudAlert approve(Long alertId, Long adminId) {
        FraudAlert alert = getPendingAlert(alertId);
        User admin = getAdmin(adminId);

        Transaction txn = alert.getTransaction();
        txn.setStatus(TransactionStatus.SUCCESS);
        transactionRepository.save(txn);

        alert.setAdminAction(AdminAction.APPROVED);
        alert.setReviewedBy(admin);
        return fraudAlertRepository.save(alert);
    }

    @Transactional
    public FraudAlert block(Long alertId, Long adminId) {
        FraudAlert alert = getPendingAlert(alertId);
        User admin = getAdmin(adminId);

        Transaction txn = alert.getTransaction();
        Double amount = txn.getAmount();

        if (txn.getFromAccount() != null) {
            Account from = txn.getFromAccount();
            from.setBalance(from.getBalance() + amount);
            accountRepository.save(from);
        }
        if (txn.getToAccount() != null) {
            Account to = txn.getToAccount();
            to.setBalance(to.getBalance() - amount);
            accountRepository.save(to);
        }

        txn.setStatus(TransactionStatus.BLOCKED);
        transactionRepository.save(txn);

        alert.setAdminAction(AdminAction.BLOCKED);
        alert.setReviewedBy(admin);
        return fraudAlertRepository.save(alert);
    }

    private FraudAlert getPendingAlert(Long alertId) {
        FraudAlert alert = fraudAlertRepository.findById(alertId)
                .orElseThrow(() -> new RuntimeException("Alert not found"));
        if (alert.getAdminAction() != AdminAction.PENDING) {
            throw new RuntimeException("Alert already reviewed");
        }
        return alert;
    }

    private User getAdmin(Long adminId) {
        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found"));
        if (admin.getRole() != User.Role.ADMIN) {
            throw new RuntimeException("Only an admin can review alerts");
        }
        return admin;
    }
}