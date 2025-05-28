package com.festivra.ticketing.controller;

import com.festivra.ticketing.entity.Event;
import com.festivra.ticketing.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/public")
@RequiredArgsConstructor
public class PublicController {

    private final EventRepository eventRepo;

   @GetMapping("/events")
public List<Event> getFilteredEvents(
        @RequestParam(required = false) String location,
        @RequestParam(required = false) LocalDate date) {

    if (location != null && date != null) {
        return eventRepo.findByLocationContainingIgnoreCaseAndDate(location, date);
    } else if (location != null) {
        return eventRepo.findByLocationContainingIgnoreCase(location);
    } else if (date != null) {
        return eventRepo.findByDate(date);
    } else {
        return eventRepo.findAll();
    }
}

    @GetMapping("/events/{id}")
    public Event getEventById(@PathVariable Long id) {
        return eventRepo.findById(id).orElseThrow(() -> new RuntimeException("Event not found"));
    }
}