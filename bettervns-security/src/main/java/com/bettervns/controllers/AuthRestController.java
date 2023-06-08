package com.bettervns.controllers;

import com.bettervns.exception.TokenRefreshException;
import com.bettervns.models.RefreshToken;
import com.bettervns.repository.RoleRepository;
import com.bettervns.repository.UserRepository;
import com.bettervns.payloads.request.LoginRequest;
import com.bettervns.payloads.response.MessageResponse;
import com.bettervns.security.jwt.JwtUtils;
import com.bettervns.security.services.RefreshTokenService;
import com.bettervns.security.services.UserDetailsImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthRestController {

    AuthenticationManager authenticationManager;

    UserRepository userRepository;

    RoleRepository roleRepository;

    PasswordEncoder encoder;

    JwtUtils jwtUtils;

    RefreshTokenService refreshTokenService;

    public AuthRestController(AuthenticationManager authenticationManager,
                              UserRepository userRepository,
                              RoleRepository roleRepository,
                              PasswordEncoder encoder,
                              JwtUtils jwtUtils,
                              RefreshTokenService refreshTokenService) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
        this.jwtUtils = jwtUtils;
        this.refreshTokenService = refreshTokenService;
    }

    @Value("${vns.app.jwtRefreshMonthExpirationMs}")
    private Long refreshTokenMonthDurationMs;

    @Value("${vns.app.jwtRefreshExpirationMs}")
    private Long refreshTokenDurationMs;

    @Value("${vns.app.privateKeyFileName}")
    private String privateKeyFileName;

    @Value("${vns.app.publicKeyFileName}")
    private String publicKeyFileName;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

            //KMS in future

            String homeDir = System.getProperty("user.home");
            String filePathForPrivateKey = homeDir + "/" + privateKeyFileName;
            String filePathForPublicKey = "../bettervns/" + publicKeyFileName;

            File privateFile = new File(filePathForPrivateKey);
            boolean created = privateFile.createNewFile();
            File publicFile = new File(filePathForPublicKey);
            boolean createdSecond = publicFile.createNewFile();
            if (created || createdSecond || privateFile.length() == 0 || publicFile.length() == 0) {
                jwtUtils.generateSecretKeyPair();
            }

            ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userPrincipal);

            RefreshToken refreshToken = refreshTokenService.createRefreshToken(userPrincipal.getId(), loginRequest.isRememberMe());
            ResponseCookie jwtRefreshCookie;
            if (loginRequest.isRememberMe()) {
                jwtRefreshCookie = jwtUtils.generateRefreshJwtCookie(refreshToken.getToken(), refreshTokenMonthDurationMs * 1000);
            } else {
                jwtRefreshCookie = jwtUtils.generateRefreshJwtCookie(refreshToken.getToken(), refreshTokenDurationMs * 1000);
            }
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.SET_COOKIE, jwtCookie.toString());
            headers.add(HttpHeaders.SET_COOKIE, jwtRefreshCookie.toString());
            String responseBody = jwtUtils.getRoleFromJwtToken(userPrincipal);
            return new ResponseEntity<>(responseBody, headers, HttpStatus.OK);
        } catch (AuthenticationException e) {
            HttpHeaders headers = new HttpHeaders();
            return new ResponseEntity<>(headers, HttpStatus.UNAUTHORIZED);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/signout")
    public ResponseEntity<?> logoutUser(@CurrentSecurityContext SecurityContext context) {
        Object principal = context.getAuthentication().getPrincipal();
        if (!Objects.equals(principal.toString(), "anonymousUser")) {
            Long userId = ((UserDetailsImpl) principal).getId();
            refreshTokenService.deleteByUserId(userId);
        }
        ResponseCookie jwtCookie = jwtUtils.getCleanJwtCookie();
        ResponseCookie jwtRefreshCookie = jwtUtils.getCleanJwtRefreshCookie();
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE, jwtCookie.toString());
        headers.add(HttpHeaders.SET_COOKIE, jwtRefreshCookie.toString());
        return new ResponseEntity<>(headers, HttpStatus.OK);
    }

    @PostMapping("/refreshtoken")
    public ResponseEntity<?> refreshtoken(HttpServletRequest request) {
        String refreshToken = jwtUtils.getJwtRefreshFromCookies(request);

        if ((refreshToken != null) && (refreshToken.length() > 0)) {
            return refreshTokenService.findByToken(refreshToken)
                    .map(refreshTokenService::verifyExpiration)
                    .map(RefreshToken::getUser)
                    .map(user -> {
                        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(user);
                        HttpHeaders headers = new HttpHeaders();
                        headers.add(HttpHeaders.SET_COOKIE, jwtCookie.toString());
                        return new ResponseEntity<>(headers, HttpStatus.OK);
                    })
                    .orElseThrow(() -> new TokenRefreshException(refreshToken,
                            "Refresh token is not in database!"));
        }
        return ResponseEntity.badRequest().body(new MessageResponse("Refresh Token is empty!"));
    }
}
