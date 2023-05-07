package com.bettervns.security.jwt;

import com.bettervns.models.User;
import com.bettervns.security.services.UserDetailsImpl;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${vns.app.jwtSecret}")
    private String jwtSecret;

    @Value("${vns.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    @Value("${vns.app.jwtCookieName}")
    private String jwtCookie;

    @Value("${vns.app.jwtRefreshCookieName}")
    private String jwtRefreshCookie;

    public ResponseCookie generateJwtCookie(UserDetailsImpl userPrincipal) {
        String jwt = generateJwtToken(userPrincipal);
        return generateAccessCookie(jwtCookie, jwt);
    }

    public ResponseCookie generateJwtCookie(User user) {
        String jwt = generateJwtToken(user);
        return generateAccessCookie(jwtCookie, jwt);
    }

    public ResponseCookie generateRefreshJwtCookie(String refreshToken, Long maxAge) {
        return generateCookie(jwtRefreshCookie, refreshToken, maxAge);
    }

    public String getJwtFromCookies(HttpServletRequest request) {
        return getCookieValueByName(request, jwtCookie);
    }

    public String getJwtRefreshFromCookies(HttpServletRequest request) {
        return getCookieValueByName(request, jwtRefreshCookie);
    }

    public ResponseCookie getCleanJwtCookie() {
        return ResponseCookie.from(jwtCookie, "").path("/api").build();
    }

    public ResponseCookie getCleanJwtRefreshCookie() {
        return ResponseCookie.from(jwtRefreshCookie, "").path("/api/auth/refreshtoken").build();
    }
    public String generateJwtToken(UserDetailsImpl userPrincipal) {
        return Jwts.builder()
                .setSubject((userPrincipal.getUsername()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .claim("roles", userPrincipal.getAuthorities().toString())
                .signWith(getSignInKey())
                .compact();
    }

    public String generateJwtToken(User user) {
        return Jwts.builder()
                .setSubject((user.getEmail()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .claim("roles", user.getRoles().toString())
                .signWith(getSignInKey())
                .compact();
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build().parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    private ResponseCookie generateCookie(String name, String value, Long maxAge) {
        return ResponseCookie.from(name, value).path("/api/auth/refreshtoken").maxAge(maxAge).httpOnly(true).build();
    }

    private ResponseCookie generateAccessCookie(String name, String value) {
        return ResponseCookie.from(name, value).path("/api").maxAge(60 * 60).httpOnly(false).build();
    }

    private String getCookieValueByName(HttpServletRequest request, String name) {
        Cookie cookie = WebUtils.getCookie(request, name);
        if (cookie != null) {
            return cookie.getValue();
        } else {
            return null;
        }
    }

//    public List<String> getRoleFromJwtToken(String token) {
//        Claims claims = Jwts.parserBuilder().setSigningKey(getSignInKey()).build().parseClaimsJws(token).getBody();
//        String roles = (String) claims.get("roles");
//        roles = roles.replace("[", "").replace("]", "");
//        String[] roleNames = roles.split(",");
//        return new ArrayList<>(Arrays.asList(roleNames));
//    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts
                    .parserBuilder()
                    .setSigningKey(getSignInKey())
                    .build()
                    .parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            logger.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
