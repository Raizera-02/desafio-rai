package com.desafio_api.app.controller;

import com.desafio_api.app.domain.User;
import com.desafio_api.app.dto.LoginRequest;
import com.desafio_api.app.repository.UserRepository;
import com.desafio_api.app.security.JwtUtil;
import com.desafio_api.app.security.UserDetailsImpl;
import com.desafio_api.app.security.UserDetailsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Map;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("deprecation")
class AuthControllerTest {

    @InjectMocks
    private AuthController authController;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserDetailsServiceImpl userDetailsService;

    private User mockUser;
    private LoginRequest loginRequest;
    private UserDetails mockUserDetails;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Criação do usuário mock
        mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername("testUser");
        mockUser.setPassword("testPassword");

        // Criação do LoginRequest mock
        loginRequest = new LoginRequest();
        loginRequest.setUsername("testUser");
        loginRequest.setPassword("testPassword");

        // Criação do UserDetails mock
        mockUserDetails = new UserDetailsImpl(mockUser);
    }

    @Test
    void testLogin_Success() {
        // Arrange
        String token = "mockJwtToken";
        when(authenticationManager.authenticate(any())).thenReturn(null); // Simula sucesso na autenticação
        when(userDetailsService.loadUserByUsername("testUser")).thenReturn(mockUserDetails);
        when(jwtUtil.generateToken(any(), any(), any())).thenReturn(token);

        // Act
        ResponseEntity<String> response = authController.login(loginRequest);

        // Assert
        assertNotNull(response.getBody());
        assertEquals(token, response.getBody());
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testLogin_Failure() {
        // Arrange
        when(authenticationManager.authenticate(any())).thenThrow(new RuntimeException("Invalid credentials"));

        // Act
        ResponseEntity<String> response = authController.login(loginRequest);

        // Assert
        assertEquals(401, response.getStatusCodeValue());
        assertEquals("Credenciais inválidas", response.getBody());
    }

    @Test
    void testRegister_Success() {
        // Arrange
        Map<String, String> userData = Map.of("username", "newUser", "password", "newPassword");
        when(userRepository.findByUsername("newUser")).thenReturn(Optional.empty());

        // Act
        Map<String, String> response = authController.register(userData);

        // Assert
        assertEquals("Usuário registrado com sucesso!", response.get("message"));
        verify(userRepository, times(1)).save(any(User.class));  // Verifica se o usuário foi salvo no repositório
    }

    @Test
    void testRegister_UserAlreadyExists() {
        // Arrange
        Map<String, String> userData = Map.of("username", "existingUser", "password", "password123");
        when(userRepository.findByUsername("existingUser")).thenReturn(Optional.of(mockUser));

        // Act
        Map<String, String> response = authController.register(userData);

        // Assert
        assertEquals("Usuário já existe!", response.get("error"));
        verify(userRepository, never()).save(any(User.class));  // Verifica que o usuário não foi salvo
    }
}

