package com.festivra.ticketing.controller;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.festivra.ticketing.entity.Event;
import com.festivra.ticketing.entity.Reservation;
import com.festivra.ticketing.entity.Role;
import com.festivra.ticketing.entity.User;
import com.festivra.ticketing.repository.EventRepository;
import com.festivra.ticketing.repository.ReservationRepository;
import com.festivra.ticketing.repository.RoleRepository;
import com.festivra.ticketing.repository.UserRepository;


import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserRepository userRepo;
    private final RoleRepository roleRepo;
    private final EventRepository eventRepo;
     private final ReservationRepository reservationRepo;

    @GetMapping("/events")
    public List<Event> getAllEvents() {
        return eventRepo.findAll();
    }

    @PostMapping("/events")
    public ResponseEntity<?> createEvent(@RequestBody Event event) {
        return ResponseEntity.ok(eventRepo.save(event));
    }

    @PutMapping("/events/{id}")
    public ResponseEntity<?> updateEvent(@PathVariable Long id, @RequestBody Event updated) {
        Event event = eventRepo.findById(id).orElseThrow();
        event.setName(updated.getName());
        event.setDate(updated.getDate());
        event.setLocation(updated.getLocation());
        event.setImageUrl(updated.getImageUrl());
        event.setCapacity(updated.getCapacity());
        event.setDescription(updated.getDescription());
        return ResponseEntity.ok(eventRepo.save(event));
    }

     @DeleteMapping("/events/{id}")
    @Transactional
    public ResponseEntity<?> deleteEvent(@PathVariable Long id) {
        Event event = eventRepo.findById(id).orElseThrow(() -> new RuntimeException("Event not found"));
    
    reservationRepo.deleteAllByEvent(event);
        eventRepo.deleteById(id);
        return ResponseEntity.ok("Event deleted");
    }
    
    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

@DeleteMapping("/users/{id}")
@Transactional
public ResponseEntity<?> deleteUser(@PathVariable Long id) {
    User user = userRepo.findById(id)
        .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

    
    List<Reservation> userReservations = reservationRepo.findByUser(user);
    reservationRepo.deleteAll(userReservations);

    
    userRepo.delete(user);

    return ResponseEntity.ok(Collections.singletonMap("message", "Usuario eliminado correctamente"));
}
    @PutMapping("/users/{id}/role")
    public ResponseEntity<?> updateRole(@PathVariable Long id, @RequestParam String role) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    
        Optional<Role> roleOpt = roleRepo.findByName("ROLE_" + role.toUpperCase());
        if (roleOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Role not found");
        }
    
        user.getRoles().clear();
        user.getRoles().add(roleOpt.get());
    
        userRepo.save(user);
        return ResponseEntity.ok("Role updated to " + role.toUpperCase());
    }

@GetMapping("/reservations")
public List<Reservation> getAllReservations() {
    return reservationRepo.findAll();
}

@DeleteMapping("/reservations/{id}")
@Transactional
public ResponseEntity<?> deleteReservation(@PathVariable Long id) {
    Reservation reservation = reservationRepo.findById(id)
        .orElseThrow(() -> new RuntimeException("Reservation not found"));

    Event event = reservation.getEvent();
    if (event.getReserved() > 0) {
        event.setReserved(event.getReserved() - 1);
        eventRepo.save(event);
    }
    reservationRepo.deleteById(id);
     return ResponseEntity.ok(Collections.singletonMap("message", "Reserva eliminada"));
}
}