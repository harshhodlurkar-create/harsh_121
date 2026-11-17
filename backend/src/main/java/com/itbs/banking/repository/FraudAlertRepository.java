package com.itbs.banking.repository;

import com.itbs.banking.entity.FraudAlert;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FraudAlertRepository extends JpaRepository<FraudAlert, Long> {
}