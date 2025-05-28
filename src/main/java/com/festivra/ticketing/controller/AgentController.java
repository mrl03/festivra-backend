package com.festivra.ticketing.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import com.festivra.ticketing.entity.Event;

import com.festivra.ticketing.repository.EventRepository;
import com.festivra.ticketing.repository.ReservationRepository;

import lombok.RequiredArgsConstructor;
@RestController
@RequestMapping("/agent")
@RequiredArgsConstructor
public class AgentController {

    private final EventRepository eventRepo;
     private final ReservationRepository reservationRepo;

    @GetMapping("/events")
    public List<Event> getEvents() {
        return eventRepo.findAll();
    }

    @PostMapping("/events")
    public ResponseEntity<?> createEvent(@RequestBody Event event) {
        return ResponseEntity.ok(eventRepo.save(event));
    }

    @PutMapping("/events/{id}")
    public ResponseEntity<?> updateEvent(@PathVariable Long id, @RequestBody Event updated) {
        Event event = eventRepo.findById(id).orElseThrow(() -> new RuntimeException("Event not found"));
        event.setName(updated.getName());
        event.setDate(updated.getDate());
        event.setLocation(updated.getLocation());
        event.setDescription(updated.getDescription());
        event.setCapacity(updated.getCapacity());
        event.setImageUrl(updated.getImageUrl());
        return ResponseEntity.ok(eventRepo.save(event));
    }

    @DeleteMapping("/events/{id}")
    @Transactional
    public ResponseEntity<?> deleteEvent(@PathVariable Long id) {
        Event event = eventRepo.findById(id).orElseThrow(() -> new RuntimeException("Event not found"));
    
    reservationRepo.deleteAllByEvent(event);
        eventRepo.deleteById(id);
       return ResponseEntity.ok(Map.of("message", "Event deleted"));
    }
}