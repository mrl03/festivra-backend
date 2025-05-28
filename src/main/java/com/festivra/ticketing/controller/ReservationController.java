package com.festivra.ticketing.controller;

import com.festivra.ticketing.entity.*;
import com.festivra.ticketing.repository.*;
import com.festivra.ticketing.service.PdfService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.time.LocalDateTime;

import org.springframework.http.HttpHeaders;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/user/reservations")
@RequiredArgsConstructor
public class ReservationController {
private final PdfService pdfService;
    private final UserRepository userRepo;
    private final EventRepository eventRepo;
    private final ReservationRepository reservationRepo;

    @PostMapping("/{eventId}")
    public ResponseEntity<?> reserveEvent(@PathVariable Long eventId, Authentication auth) {
        System.out.println("Usuario autenticado: " + auth.getName());
        String email = auth.getName();
        User user = userRepo.findByEmail(email).orElseThrow();
        Event event = eventRepo.findById(eventId).orElseThrow();
    
        
        if (reservationRepo.existsByUserAndEvent(user, event)) {
            return ResponseEntity
                .status(409)
                .body(Collections.singletonMap("message", "Ya tienes una reserva para este evento"));
        }
    
        
        long currentReservations = reservationRepo.countByEvent(event);
        if (currentReservations >= event.getCapacity()) {
            return ResponseEntity
                .status(403)
                .body(Collections.singletonMap("message", "Este evento ya está completo"));
        }
    
        Reservation reservation = new Reservation();
        reservation.setUser(user);
        reservation.setEvent(event);
        reservation.setReservationDate(LocalDateTime.now());
        event.setReserved(event.getReserved() + 1);
        reservationRepo.save(reservation);
    
        return ResponseEntity.ok(Collections.singletonMap("message", "Reserva creada"));
    }
    
    @GetMapping
    public List<Reservation> getMyReservations(Authentication auth) {
        String email = auth.getName();
        User user = userRepo.findByEmail(email).orElseThrow();
        return reservationRepo.findByUser(user);
    }

    @GetMapping("/{id}/download")
public ResponseEntity<?> downloadTicket(@PathVariable Long id, Authentication auth) {
    String email = auth.getName();
    User user = userRepo.findByEmail(email).orElseThrow();
    Reservation reservation = reservationRepo.findById(id)
            .filter(r -> r.getUser().getId().equals(user.getId()))
            .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));

    ByteArrayInputStream pdf = pdfService.generateTicketPdf(reservation);

    return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=reservation.pdf")
            .contentType(MediaType.APPLICATION_PDF)
            .body(pdf.readAllBytes());
}
}