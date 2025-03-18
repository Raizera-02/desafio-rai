package com.desafio_api.app.controller;

import com.desafio_api.app.service.ReportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SuppressWarnings("deprecation")
class ReportControllerTest {

    @Mock
    private ReportService reportService;

    @InjectMocks
    private ReportController reportController;

    @BeforeEach
    void setUp() {
        // Inicializa os mocks
        MockitoAnnotations.openMocks(this);
    }

    
    @Test
    void getTop5UsersByTotalSpent() {
        // Arrange: Preparando os dados mockados
        Map<String, Object> user = Map.of("user", "John", "totalSpent", 1000);
        List<Map<String, Object>> mockResult = Arrays.asList(user);

        // Definindo o comportamento do serviço mockado
        when(reportService.findTop5UsersByTotalSpent()).thenReturn(mockResult);

        // Act: Chamando o método do controller
        ResponseEntity<List<Map<String, Object>>> response = reportController.getTop5UsersByTotalSpent();

        // Assert: Verificando se o resultado está correto
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody()); 
        assertEquals(mockResult, response.getBody()); 
    }

    @Test
    void getAverageTicketPerUser() {
        // Arrange
        Map<String, Object> userTicket = Map.of("user", "John", "averageTicket", 200);
        List<Map<String, Object>> mockResult = Arrays.asList(userTicket);
        
        when(reportService.findAverageTicketPerUser()).thenReturn(mockResult);

        // Act
        ResponseEntity<List<Map<String, Object>>> response = reportController.getAverageTicketPerUser();

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(mockResult, response.getBody());
    }

    @Test
    void getMonthlyRevenue() {
        // Arrange
        Map<String, Object> revenue = Map.of("month", "January", "revenue", 5000);
        List<Map<String, Object>> mockResult = Arrays.asList(revenue);

        when(reportService.findMonthlyRevenue()).thenReturn(mockResult);

        // Act
        ResponseEntity<List<Map<String, Object>>> response = reportController.getMonthlyRevenue();

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(mockResult, response.getBody());
    }
}
