package com.desafio_api.app.controller;

import com.desafio_api.app.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;

    // Top 5 usuários que mais compraram
    @GetMapping("/top-users")
    public ResponseEntity<List<Map<String, Object>>> getTop5UsersByTotalSpent() {
        List<Map<String, Object>> topUsers = reportService.findTop5UsersByTotalSpent();
        return ResponseEntity.ok(topUsers);
    }

    // Ticket médio dos pedidos de cada usuário
    @GetMapping("/average-ticket")
    public ResponseEntity<List<Map<String, Object>>> getAverageTicketPerUser() {
        List<Map<String, Object>> averageTickets = reportService.findAverageTicketPerUser();
        return ResponseEntity.ok(averageTickets);
    }

    // Valor total faturado por mês
    @GetMapping("/monthly-revenue")
    public ResponseEntity<List<Map<String, Object>>> getMonthlyRevenue() {
        List<Map<String, Object>> monthlyRevenue = reportService.findMonthlyRevenue();
        return ResponseEntity.ok(monthlyRevenue);
    }
}