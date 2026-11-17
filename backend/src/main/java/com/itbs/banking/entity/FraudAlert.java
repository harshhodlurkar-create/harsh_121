package com.itbs.banking.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "fraud_alerts")
public class FraudAlert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "transaction_id", nullable = false)
    private Transaction transaction;

    @Column(nullable = false)
    private String reason;

    @ManyToOne
    @JoinColumn(name = "reviewed_by")
    private User reviewedBy;

    @Enumerated(EnumType.STRING)
    private AdminAction adminAction = AdminAction.PENDING;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public enum AdminAction {
        PENDING, APPROVED, BLOCKED
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Transaction getTransaction() { return transaction; }
    public void setTransaction(Transaction transaction) { this.transaction = transaction; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public User getReviewedBy() { return reviewedBy; }
    public void setReviewedBy(User reviewedBy) { this.reviewedBy = reviewedBy; }

    public AdminAction getAdminAction() { return adminAction; }
    public void setAdminAction(AdminAction adminAction) { this.adminAction = adminAction; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}