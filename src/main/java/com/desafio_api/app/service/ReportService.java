package com.desafio_api.app.service;

import com.desafio_api.app.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ReportService {

    @Autowired
    private OrderRepository orderRepository;

    public List<Map<String, Object>> findTop5UsersByTotalSpent() {
        return orderRepository.findTop5UsersByTotalSpent();
    }

    public List<Map<String, Object>> findAverageTicketPerUser() {
        return orderRepository.findAverageTicketPerUser();
    }

    public List<Map<String, Object>> findMonthlyRevenue() {
        return orderRepository.findMonthlyRevenue();
    }
}