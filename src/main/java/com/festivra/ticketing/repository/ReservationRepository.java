package com.festivra.ticketing.repository;

import com.festivra.ticketing.entity.Event;
import com.festivra.ticketing.entity.Reservation;
import com.festivra.ticketing.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByUser(User user);
    boolean existsByUserAndEvent(User user, Event event);
    long countByEvent(Event event);
    void deleteById(Long id);
  void deleteAllByEvent(Event event);
    
}