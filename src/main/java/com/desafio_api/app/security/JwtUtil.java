package com.desafio_api.app.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class JwtUtil {

    @Value("${jwt.secret.key}")
    private String secretKey;

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(Base64.getDecoder().decode(secretKey)); // Decodifica a chave base64
    }

    // ✅ Gerar token JWT com userId, username e roles
    public String generateToken(String username, String userId, Collection<? extends GrantedAuthority> authorities) {
        return Jwts.builder()
                .setSubject(username)
                .claim("userId", userId)
                .claim("roles", authorities.stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList()))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24)) // 24 horas de validade
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // ✅ Extrair username do token
    public String extractUsername(String token) {
        return getClaims(token).getSubject();
    }

    // ✅ Extrair userId do token
    public String extractUserId(String token) {
        return getClaims(token).get("userId", String.class);
    }

    // ✅ Extrair roles do token
    public Collection<? extends GrantedAuthority> extractRoles(String token) {
        List<String> roles = getClaims(token).get("roles", List.class);
        return roles != null ? roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()) : Collections.emptyList();
    }

    // ✅ Extrair data de expiração do token
    private Date extractExpiration(String token) {
        return getClaims(token).getExpiration();
    }

    // ✅ Verificar se o token é válido (temporariamente sem a validação de roles)
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    // ✅ Verificar se o token está expirado
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // ✅ Método auxiliar para obter claims
    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
