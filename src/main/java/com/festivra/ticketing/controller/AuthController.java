package com.festivra.ticketing.controller;

import com.festivra.ticketing.dto.LoginRequest;
import com.festivra.ticketing.dto.RegisterRequest;

import com.festivra.ticketing.entity.User;
import com.festivra.ticketing.repository.RoleRepository;
import com.festivra.ticketing.repository.UserRepository;
import com.festivra.ticketing.security.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepo;
    private final RoleRepository roleRepo;
    private final PasswordEncoder encoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authManager;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        if (userRepo.existsByEmail(request.getEmail())) {
          return ResponseEntity
    .badRequest()
    .body(Map.of("error", "Email already in use."));
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(encoder.encode(request.getPassword()));
        user.setFullName(request.getFullName());
        user.setRoles(Collections.singleton(roleRepo.findByName("ROLE_USER").get()));

        userRepo.save(user);
        String token = jwtUtil.generateToken(user);
        return ResponseEntity.ok(Collections.singletonMap("token", token));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        try {
            Authentication auth = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            User user = userRepo.findByEmail(request.getEmail()).get();
            String token = jwtUtil.generateToken(user);
            return ResponseEntity.ok(Collections.singletonMap("token", token));

        } catch (AuthenticationException e) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid email or password."));
        }
    }
}