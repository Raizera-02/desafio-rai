package com.desafio_api.app.repository;

import com.desafio_api.app.domain.Order;
import com.desafio_api.app.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface OrderRepository extends JpaRepository<Order, Long> {

        // Top 5 usuários que mais compraram
        @Query("SELECT o.user.username AS username, SUM(o.valorTotal) AS totalSpent " +
                        "FROM Order o GROUP BY o.user.username ORDER BY totalSpent DESC LIMIT 5")
        List<Map<String, Object>> findTop5UsersByTotalSpent();

        // Ticket médio dos pedidos de cada usuário
        @Query("SELECT o.user.username AS username, AVG(o.valorTotal) AS averageTicket " +
                        "FROM Order o GROUP BY o.user.username")
        List<Map<String, Object>> findAverageTicketPerUser();

        // Valor total faturado por mês
        @Query("SELECT FUNCTION('MONTH', o.dataCriacao) AS month, SUM(o.valorTotal) AS totalRevenue " +
                        "FROM Order o GROUP BY FUNCTION('MONTH', o.dataCriacao)")
        List<Map<String, Object>> findMonthlyRevenue();

        // Listar pedidos de um usuário
        List<Order> findByUser(User user);

        List<Order> findByUserId(Long userId);

}