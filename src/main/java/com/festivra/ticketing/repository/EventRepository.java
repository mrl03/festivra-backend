package com.festivra.ticketing.repository;

import com.festivra.ticketing.entity.Event;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByLocationContainingIgnoreCase(String location);
List<Event> findByDate(LocalDate date);
List<Event> findByLocationContainingIgnoreCaseAndDate(String location, LocalDate date);
void deleteById(Long id);


}