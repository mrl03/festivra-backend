package com.festivra.ticketing.security;

import com.festivra.ticketing.repository.UserRepository;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import java.io.IOException;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepo;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);
            if (jwtUtil.validateToken(jwt)) {
                String email = jwtUtil.extractUsername(jwt);
                var userOpt = userRepo.findByEmail(email);
                if (userOpt.isPresent()) {
                    UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                            .username(email)
                            .password(userOpt.get().getPassword())
                            .authorities(userOpt.get().getRoles().stream().map(r -> r.getName()).toArray(String[]::new))
                            .build();

                    UsernamePasswordAuthenticationToken auth =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                    auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}