package com.festivra.ticketing.controller;

import com.festivra.ticketing.entity.User;
import com.festivra.ticketing.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepo;

    @GetMapping("/me")
    public ResponseEntity<?> getProfile(Authentication authentication) {
        String email = authentication.getName();

        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return ResponseEntity.ok(Map.of(
                "email", user.getEmail(),
                "fullName", user.getFullName(),
                "roles", user.getRoles().stream().map(r -> r.getName()).collect(Collectors.toList())
        ));
    }
}