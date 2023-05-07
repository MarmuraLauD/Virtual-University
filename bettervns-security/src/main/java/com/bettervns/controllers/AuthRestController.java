package com.bettervns.controllers;

import com.bettervns.models.ERole;
import com.bettervns.models.Role;
import com.bettervns.models.User;
import com.bettervns.exception.TokenRefreshException;
import com.bettervns.models.RefreshToken;
import com.bettervns.repository.RoleRepository;
import com.bettervns.repository.UserRepository;
import com.bettervns.payloads.request.LoginRequest;
import com.bettervns.payloads.request.SignupRequest;
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

import java.net.URI;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

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

    @PostMapping("/signin")
    public ResponseEntity<Void> authenticateUser(@Valid LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
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
            headers.setLocation(URI.create("/home"));
            headers.setBearerAuth(jwtUtils.generateJwtToken(userPrincipal));
            return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
        } catch (AuthenticationException e) {
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(URI.create("/signin?error"));
            return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
        }
    }


    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository != null) {
            if (userRepository.existsByEmail(signUpRequest.getEmail())) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Error: Email is already in use!"));
            }
        }

        // Create new user's account
        User user = new User(signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_STUDENT)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin" -> {
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);
                    }
                    case "teacher" -> {
                        Role modRole = roleRepository.findByName(ERole.ROLE_TEACHER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(modRole);
                    }
                    default -> {
                        Role userRole = roleRepository.findByName(ERole.ROLE_STUDENT)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                    }
                }
            });
        }

        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
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
        headers.setLocation(URI.create("/signin"));
        return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
    }

    @PostMapping("/refreshtoken")
    public ResponseEntity<?> refreshtoken(HttpServletRequest request, @RequestHeader("Referer") String referer) {
        String refreshToken = jwtUtils.getJwtRefreshFromCookies(request);

        if ((refreshToken != null) && (refreshToken.length() > 0)) {
            return refreshTokenService.findByToken(refreshToken)
                    .map(refreshTokenService::verifyExpiration)
                    .map(RefreshToken::getUser)
                    .map(user -> {
                        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(user);
                        HttpHeaders headers = new HttpHeaders();
                        headers.add(HttpHeaders.SET_COOKIE, jwtCookie.toString());
                        headers.setLocation(URI.create(referer));

                        return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
                    })
                    .orElseThrow(() -> new TokenRefreshException(refreshToken,
                            "Refresh token is not in database!"));
        }

        return ResponseEntity.badRequest().body(new MessageResponse("Refresh Token is empty!"));
    }
}
