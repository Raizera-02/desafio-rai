package com.desafio_api.app.service;

import com.desafio_api.app.domain.*;
import com.desafio_api.app.repository.OrderRepository;
import com.desafio_api.app.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final NotificationService notificationService; // Serviço de notificação (pode ser email, SMS, etc.)

    public OrderService(OrderRepository orderRepository, ProductRepository productRepository,
            NotificationService notificationService) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.notificationService = notificationService;
    }

    @Transactional
    public Order createOrder(User user, Set<OrderItem> itens) {
        Order order = new Order();
        order.setUser(user);

        order = orderRepository.save(order);

        // Atualiza os produtos com informações do banco
        for (OrderItem item : itens) {
            Product product = productRepository.findById(item.getProduto().getId())
                    .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

            item.setProduto(product); // Atualiza o objeto Produto dentro do item
            item.setPedido(order);
        }

        order.setItens(itens);
        order.setValorTotal(calculateTotalValue(itens));

        // Verifica estoque e atualiza status
        if (checkStock(itens)) {
            order.setStatus("PENDENTE");
        } else {
            order.setStatus("CANCELADO");
            notificationService.notifyUser(user, "Seu pedido foi cancelado devido à falta de estoque.");
        }

        // 🔥 Agora que os itens estão corretamente associados, salvamos a ordem
        // novamente
        return orderRepository.save(order);
    }

    private Double calculateTotalValue(Set<OrderItem> itens) {
        return itens.stream()
                .mapToDouble(item -> item.getProduto().getPreco() * item.getQuantidade())
                .sum();
    }

    private boolean checkStock(Set<OrderItem> itens) {
        for (OrderItem item : itens) {
            Product product = productRepository.findById(item.getProduto().getId())
                    .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
            if (product.getQuantidadeEmEstoque() < item.getQuantidade()) {
                return false;
            }
        }
        return true;
    }

    @Transactional
    public void processPayment(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));

        if ("PENDENTE".equals(order.getStatus())) {
            // Atualiza estoque
            for (OrderItem item : order.getItens()) {
                Product product = productRepository.findById(item.getProduto().getId())
                        .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
                product.setQuantidadeEmEstoque(product.getQuantidadeEmEstoque() - item.getQuantidade());
                productRepository.save(product);
            }
            order.setStatus("PAGO");
            orderRepository.save(order);
        } else {
            throw new RuntimeException("Pedido não está pendente");
        }
    }

    public List<Order> getOrdersByUser(User user) {
        return orderRepository.findByUser(user); // Busca pedidos do usuário
    }
}