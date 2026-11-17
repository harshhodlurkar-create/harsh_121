package com.itbs.banking.service;

import com.itbs.banking.entity.FraudAlert;
import com.itbs.banking.service.FraudAlertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/alerts")
public class AdminController {

    @Autowired
    private FraudAlertService fraudAlertService;

    @GetMapping
    public List<FraudAlert> getAlerts() {
        return fraudAlertService.getAllAlerts();
    }

    @PutMapping("/{id}/approve")
    public FraudAlert approve(@PathVariable Long id, @RequestParam Long adminId) {
        return fraudAlertService.approve(id, adminId);
    }

    @PutMapping("/{id}/block")
    public FraudAlert block(@PathVariable Long id, @RequestParam Long adminId) {
        return fraudAlertService.block(id, adminId);
    }
}