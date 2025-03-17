package com.desafio_api.app.controller;

import com.desafio_api.app.domain.Role;
import com.desafio_api.app.domain.User;
import com.desafio_api.app.dto.LoginRequest;
import com.desafio_api.app.repository.UserRepository;
import com.desafio_api.app.security.JwtUtil;
import com.desafio_api.app.security.UserDetailsImpl;
import com.desafio_api.app.security.UserDetailsServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsServiceImpl userDetailsService; // Injeção do UserDetailsServiceImpl

    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil,
                          UserRepository userRepository, PasswordEncoder passwordEncoder,
                          UserDetailsServiceImpl userDetailsService) { // Adicionado UserDetailsServiceImpl
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService; // Inicializado corretamente
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        try {
            // Autenticar o usuário
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

            // Carregar os detalhes do usuário
            UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getUsername());

            // Gerar o token JWT com username, userId e authorities
            String token = jwtUtil.generateToken(
                    userDetails.getUsername(),
                    ((UserDetailsImpl) userDetails).getUser().getId().toString(), // Acessa o ID do usuário
                    userDetails.getAuthorities()); // Passa as authorities (roles)

            return ResponseEntity.ok(token);
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Credenciais inválidas");
        }
    }

    @PostMapping("/register")
    public Map<String, String> register(@RequestBody Map<String, String> userData) {
        String username = userData.get("username");
        String password = userData.get("password");
        String role = userData.getOrDefault("role", "USER").toUpperCase();

        // Verifica se o usuário já existe
        if (userRepository.findByUsername(username).isPresent()) {
            return Map.of("error", "Usuário já existe!");
        }

        // Cria o usuário
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRoles(Set.of(Role.valueOf(role))); // Converte a string para o enum Role

        userRepository.save(user);

        return Map.of("message", "Usuário registrado com sucesso!");
    }
}