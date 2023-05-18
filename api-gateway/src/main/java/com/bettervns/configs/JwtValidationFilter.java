package com.bettervns.configs;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;

@Slf4j
@Component
public class JwtValidationFilter implements GatewayFilter, Ordered {

    private final String roleAttribute;

    public JwtValidationFilter(String role) {
        roleAttribute = role;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        if (isValidToken(exchange) && isValidRole(exchange)) {
            return chain.filter(exchange);
        } else {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
    }

    private boolean isValidToken(ServerWebExchange exchange) {
        String authToken = parseJwt((HttpServletRequest) exchange.getRequest());
        try {
            Jwts
                    .parserBuilder()
                    .setSigningKey(getPublicKey())
                    .build()
                    .parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            log.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }

    private boolean isValidRole(ServerWebExchange exchange) {
        String authToken = parseJwt((HttpServletRequest) exchange.getRequest());
        Set<String> roles = getRoleFromJwtToken(authToken);
        boolean isValid = false;
        for (String role : roles) {
            if (roleAttribute.equals("ROLE_ANYONE") || role.equals("ROLE_ADMIN")) isValid = true;
            else isValid = role.equals(roleAttribute);
        }
        return isValid;
    }

    private PublicKey getPublicKey() {
        try {
            String filePath = System.getProperty("user.dir") + "/" + "public_key.pem";
            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);
            byte[] content = new byte[(int) file.length()];
            fis.read(content);
            fis.close();
            String publicKeyPEM = new String(content, StandardCharsets.UTF_8);
            String substring = publicKeyPEM.substring(0, publicKeyPEM.indexOf('\n'));
            publicKeyPEM = publicKeyPEM.replace(substring + "\n", "")
                    .replace("-----END PUBLIC KEY-----", "")
                    .replaceAll("\\s", "");
            byte[] publicKeyBytes = Base64.getDecoder().decode(publicKeyPEM);
            X509EncodedKeySpec spec = new X509EncodedKeySpec(publicKeyBytes);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return kf.generatePublic(spec);
        } catch (IOException e) {
            log.error("File is not found: {}", e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            log.error("No such algorithm: {}", e.getMessage());
        } catch (InvalidKeySpecException e) {
            log.error("Invalid key specifications: {}", e.getMessage());
        }
        return null;
    }

    public Set<String> getRoleFromJwtToken(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(getPublicKey()).build().parseClaimsJws(token).getBody();
        String roles = (String) claims.get("roles");
        roles = roles.replace("[", "").replace("]", "").replace(" ", "");
        String[] roleNames = roles.split(",");
        return new HashSet<>(Arrays.asList(roleNames));
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }

        return null;
    }
}
