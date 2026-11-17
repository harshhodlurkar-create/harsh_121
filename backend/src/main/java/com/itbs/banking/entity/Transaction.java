package com.itbs.banking.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "from_account_id")
    private Account fromAccount;

    @ManyToOne
    @JoinColumn(name = "to_account_id")
    private Account toAccount;

    @Column(nullable = false)
    private Double amount;

    @Column(nullable = false)
    private LocalDateTime timestamp = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    private TransactionStatus status = TransactionStatus.SUCCESS;

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    public enum TransactionStatus {
        SUCCESS, FLAGGED, BLOCKED
    }

    public enum TransactionType {
        DEPOSIT, WITHDRAW, TRANSFER
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Account getFromAccount() { return fromAccount; }
    public void setFromAccount(Account fromAccount) { this.fromAccount = fromAccount; }

    public Account getToAccount() { return toAccount; }
    public void setToAccount(Account toAccount) { this.toAccount = toAccount; }

    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public TransactionStatus getStatus() { return status; }
    public void setStatus(TransactionStatus status) { this.status = status; }

    public TransactionType getType() { return type; }
    public void setType(TransactionType type) { this.type = type; }
}