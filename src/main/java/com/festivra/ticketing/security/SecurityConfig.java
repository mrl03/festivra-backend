package com.festivra.ticketing.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.security.authentication.*;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.*;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.festivra.ticketing.service.CustomUserDetailsService;

import org.springframework.security.web.SecurityFilterChain;


@Configuration
public class SecurityConfig {
@Autowired
private CustomUserDetailsService userDetailsService;
    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    public AuthenticationManager authManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors(Customizer.withDefaults()) // 👈 Enable CORS
            .csrf(csrf -> csrf.disable())
            .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/public/**").permitAll()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/agent/**").hasAnyRole("ADMIN", "AGENT")
                .requestMatchers("/user/**").hasRole("USER")
                .anyRequest().authenticated()
            );
    
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        http.userDetailsService(userDetailsService);
        return http.build();
    }
    
}