package com.festivra.ticketing.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime reservationDate;

@ManyToOne(fetch = FetchType.EAGER)
@JoinColumn(name = "user_id")
private User user;

@ManyToOne(fetch = FetchType.EAGER)
@JoinColumn(name = "event_id")
private Event event;
   
}