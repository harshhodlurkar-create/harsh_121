package com.itbs.banking.service;

import com.itbs.banking.entity.Account;
import com.itbs.banking.entity.FraudAlert;
import com.itbs.banking.entity.Transaction;
import com.itbs.banking.repository.FraudAlertRepository;
import com.itbs.banking.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class FraudDetectionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private FraudAlertRepository fraudAlertRepository;

    private static final double LARGE_AMOUNT_THRESHOLD = 50000.0;
    private static final int RAPID_TXN_LIMIT = 3;
    private static final int RAPID_TXN_WINDOW_MINUTES = 5;

    public void checkTransaction(Transaction txn, Account account) {

        // Rule 1: Large single transaction
        if (txn.getAmount() > LARGE_AMOUNT_THRESHOLD) {D
            flagTransaction(txn, "Large transaction amount exceeds threshold");
            return;
        }

        // Rule 2: Rapid transactions in short time window
        LocalDateTime windowStart = LocalDateTime.now().minusMinutes(RAPID_TXN_WINDOW_MINUTES);
        List<Transaction> recentTxns = transactionRepository
                .findByFromAccountIdOrToAccountId(account.getId(), account.getId());

        long recentCount = recentTxns.stream()
                .filter(t -> t.getTimestamp().isAfter(windowStart))
                .count();

        if (recentCount >= RAPID_TXN_LIMIT) {
            flagTransaction(txn, "Multiple rapid transactions detected");
            return;
        }

        // Rule 3: Unusual time (12 AM - 5 AM)
        int hour = LocalDateTime.now().getHour();
        if (hour >= 0 && hour < 5) {
            flagTransaction(txn, "Transaction performed during unusual hours");
        }
    }

    private void flagTransaction(Transaction txn, String reason) {
        txn.setStatus(Transaction.TransactionStatus.FLAGGED);
        transactionRepository.save(txn);

        FraudAlert alert = new FraudAlert();
        alert.setTransaction(txn);
        alert.setReason(reason);
        fraudAlertRepository.save(alert);
    }
}