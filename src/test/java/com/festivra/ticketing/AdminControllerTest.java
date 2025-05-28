package com.festivra.ticketing;

import com.festivra.ticketing.controller.AdminController;
import com.festivra.ticketing.entity.Event;
import com.festivra.ticketing.entity.Reservation;
import com.festivra.ticketing.repository.EventRepository;
import com.festivra.ticketing.repository.ReservationRepository;
import com.festivra.ticketing.repository.RoleRepository;
import com.festivra.ticketing.repository.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AdminControllerTest {

    @InjectMocks
    private AdminController adminController;

    @Mock
    private EventRepository eventRepo;

    @Mock
    private ReservationRepository reservationRepo;

    @Mock
    private RoleRepository roleRepo;

    @Mock
    private UserRepository userRepo;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCreateEvent() {
        Event event = new Event(null, "Concert", "Music event", "Madrid", LocalDate.now(), "image.jpg", 100, 0);
        when(eventRepo.save(event)).thenReturn(event);

        ResponseEntity<?> response = adminController.createEvent(event);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(event, response.getBody());
    }

    @Test
    void shouldUpdateEvent() {
        Event event = new Event(1L, "Old", "Old", "Old", LocalDate.now(), "old.jpg", 100, 0);
        Event updated = new Event(1L, "New", "New Desc", "Valencia", LocalDate.now(), "new.jpg", 200, 0);

        when(eventRepo.findById(1L)).thenReturn(Optional.of(event));
        when(eventRepo.save(any(Event.class))).thenReturn(updated);

        ResponseEntity<?> response = adminController.updateEvent(1L, updated);

        assertEquals(200, response.getStatusCodeValue());
        Event result = (Event) response.getBody();
        assertEquals("New", result.getName());
        assertEquals("Valencia", result.getLocation());
    }

    @Test
    void shouldDeleteEvent() {
        Event event = new Event(1L, "Concert", "desc", "loc", LocalDate.now(), "img", 100, 0);
        when(eventRepo.findById(1L)).thenReturn(Optional.of(event));

        ResponseEntity<?> response = adminController.deleteEvent(1L);
        verify(reservationRepo).deleteAllByEvent(event);
        verify(eventRepo).deleteById(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Event deleted", response.getBody());
    }

    @Test
    void shouldReturnAllReservations() {
        List<Reservation> reservations = List.of(new Reservation());
        when(reservationRepo.findAll()).thenReturn(reservations);

        List<Reservation> result = adminController.getAllReservations();
        assertEquals(1, result.size());
    }
@Test
void shouldDeleteReservation() {
    
    Event event = new Event();
    event.setId(1L);
    event.setReserved(1);

    
    Reservation reservation = new Reservation();
    reservation.setId(1L);
    reservation.setEvent(event);

    
    when(reservationRepo.findById(1L)).thenReturn(Optional.of(reservation));

    
    ResponseEntity<?> response = adminController.deleteReservation(1L);

    
    verify(reservationRepo).deleteById(1L);
   @SuppressWarnings("unchecked")
   Map<String, String> body = (Map<String, String>) response.getBody();
   assertEquals("Reserva eliminada", body.get("message"));
    assertEquals(0, event.getReserved()); 
}
}