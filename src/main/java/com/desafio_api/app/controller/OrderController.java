package com.desafio_api.app.controller;

import com.desafio_api.app.domain.Order;
import com.desafio_api.app.domain.OrderItem;
import com.desafio_api.app.domain.User;
import com.desafio_api.app.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    // Criar pedido (apenas USER)
    @PostMapping
    public ResponseEntity<Order> createOrder(
            @AuthenticationPrincipal User user,
            @RequestBody Set<OrderItem> itens) {
        Order order = orderService.createOrder(user, itens);
        return ResponseEntity.ok(order);
    }

    // Realizar pagamento de um pedido (apenas USER)
    @PostMapping("/{id}/pay")
    public ResponseEntity<Void> payOrder(@PathVariable Long id) {
        orderService.processPayment(id);
        return ResponseEntity.noContent().build();
    }

    // Listar pedidos do usuário autenticado (apenas USER)
    @GetMapping
    public ResponseEntity<List<Order>> getUserOrders(@AuthenticationPrincipal User user) {
        List<Order> orders = orderService.getOrdersByUser(user);
        return ResponseEntity.ok(orders);
    }
}