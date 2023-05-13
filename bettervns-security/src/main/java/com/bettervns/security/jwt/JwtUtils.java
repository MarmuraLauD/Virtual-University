package com.bettervns.security.jwt;

import com.bettervns.models.User;
import com.bettervns.security.services.UserDetailsImpl;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${vns.app.privateKeyFileName}")
    private String privateKeyFileName;

    @Value("${vns.app.publicKeyFileName}")
    private String publicKeyFileName;

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
                .signWith(getPrivateKey(privateKeyFileName))
                .compact();
    }

    public void generateSecretKeyPair() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            KeyPair keyPair = keyPairGenerator.genKeyPair();

            PrivateKey privateKey = keyPair.getPrivate();
            PublicKey publicKey = keyPair.getPublic();

            savePrivateKey(privateKey, privateKeyFileName);
            savePublicKey(publicKey, publicKeyFileName);
        } catch (NoSuchAlgorithmException e) {
            logger.error("No such algorithm: {}", e.getMessage());
        }
    }

    public void savePrivateKey(PrivateKey privateKey, String fileName) {
        String filePath = System.getProperty("user.home") + "/" + fileName;
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            PKCS8EncodedKeySpec encryptedPrivateKeyInfo = new PKCS8EncodedKeySpec(privateKey.getEncoded());
            fos.write(encryptedPrivateKeyInfo.getEncoded());
        } catch (IOException e) {
            logger.error("Failed to save private key to file: {}", e.getMessage());
        }
    }

    public PrivateKey getPrivateKey(String fileName) {
        String filePath = System.getProperty("user.home") + "/" + fileName;
        Path path = Paths.get(filePath);
        byte[] bytes = new byte[0];
        try {
            bytes = Files.readAllBytes(path);
        } catch (IOException e) {
            logger.error("File is not found: {}", e.getMessage());
        }
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(bytes);
        KeyFactory keyFactory = null;
        try {
            keyFactory = KeyFactory.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            logger.error("No such algorithm: {}", e.getMessage());
        }
        try {
            assert keyFactory != null;
            return keyFactory.generatePrivate(keySpec);
        } catch (InvalidKeySpecException e) {
            logger.error("Invalid key specifications: {}", e.getMessage());
        }
        return null;
    }

    public PublicKey getPublicKey(String fileName) {
        try {
            String filePath = System.getProperty("user.dir") + "/" + fileName;
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
            logger.error("File is not found: {}", e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            logger.error("No such algorithm: {}", e.getMessage());
        } catch (InvalidKeySpecException e) {
            logger.error("Invalid key specifications: {}", e.getMessage());
        }
        return null;
    }

    public void savePublicKey(PublicKey publicKey, String fileName) {
        String filePath = System.getProperty("user.dir") + "/" + fileName;
        try (PemWriter pemWriter = new PemWriter(new FileWriter(filePath))) {
            pemWriter.writeObject(new PemObject("PUBLIC KEY", publicKey.getEncoded()));
        } catch (IOException e) {
            logger.error("File is not found: {}", e.getMessage());
        }
    }

    public String generateJwtToken(User user) {
        return Jwts.builder()
                .setSubject((user.getEmail()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .claim("roles", user.getRoles().toString())
                .signWith(getPrivateKey(privateKeyFileName))
                .compact();
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getPublicKey(publicKeyFileName))
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
                    .setSigningKey(getPublicKey(publicKeyFileName))
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
}
