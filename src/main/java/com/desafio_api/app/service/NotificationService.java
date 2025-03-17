package com.desafio_api.app.service;

import com.desafio_api.app.domain.User;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    public void notifyUser(User user, String message) {
        // Implementar a lógica de notificação aqui. Exemplo:
        System.out.println("Notificando usuário " + ": " + message);
        
        // Se você estiver enviando um e-mail, por exemplo:
        // emailService.sendEmail(user.getEmail(), "Status do Pedido", message);
    }
}
