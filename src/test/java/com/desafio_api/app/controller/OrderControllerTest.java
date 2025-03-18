package com.desafio_api.app.controller;

import com.desafio_api.app.domain.Order;
import com.desafio_api.app.domain.OrderItem;
import com.desafio_api.app.domain.User;
import com.desafio_api.app.security.UserDetailsImpl;
import com.desafio_api.app.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("deprecation")
class OrderControllerTest {

    @InjectMocks
    private OrderController orderController;

    @Mock
    private OrderService orderService;

    @Mock
    private UserDetailsImpl userDetails;

    private User mockUser;
    private Set<OrderItem> mockItems;
    private Order mockOrder;
    private List<Order> mockOrders;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Criação de um usuário mock
        mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername("user1");

        // Criando itens do pedido mock
        mockItems = new HashSet<>();
        OrderItem item = new OrderItem();
        mockItems.add(item);

        // Criando um pedido mock
        mockOrder = new Order();
        mockOrder.setId(1L);
        mockOrder.setUser(mockUser);

        // Criando uma lista de pedidos mock
        mockOrders = List.of(new Order(), new Order());
    }

    @Test
    void createOrder() {
        // Arrange
        when(userDetails.getUser()).thenReturn(mockUser); 
        when(orderService.createOrder(mockUser, mockItems)).thenReturn(mockOrder);  

        // Act
        ResponseEntity<Order> response = orderController.createOrder(userDetails, mockItems);

        // Assert
        assertNotNull(response.getBody(), "O corpo da resposta não pode ser null");
        assertEquals(1L, response.getBody().getId(), "O ID do pedido não corresponde ao esperado");
        assertEquals(200, response.getStatusCodeValue(), "O status da resposta não é 200");
    }

    @Test
    void payOrder() {
        // Arrange
        Long orderId = 1L;

        // Act
        ResponseEntity<Void> response = orderController.payOrder(orderId);

        // Assert
        verify(orderService, times(1)).processPayment(orderId);  // Verifica se o método processPayment foi chamado uma vez
        assertEquals(204, response.getStatusCodeValue(), "O status da resposta não é 204");
    }

    @Test
    void getUserOrders() {
        // Arrange
        when(userDetails.getUser()).thenReturn(mockUser);  // Simula o retorno do usuário autenticado
        when(orderService.getOrdersByUser(mockUser)).thenReturn(mockOrders);  // Simula o serviço de recuperação de pedidos

        // Act
        ResponseEntity<List<Order>> response = orderController.getUserOrders(userDetails);

        // Assert
        assertNotNull(response.getBody(), "O corpo da resposta não pode ser null");
        assertEquals(2, response.getBody().size(), "A lista de pedidos não contém o número esperado de pedidos");
        assertEquals(200, response.getStatusCodeValue(), "O status da resposta não é 200");
    }
}
