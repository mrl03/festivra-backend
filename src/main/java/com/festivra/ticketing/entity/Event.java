package com.festivra.ticketing.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private String location;
    private LocalDate date;
    private String imageUrl;

    @Column(nullable = false)
    @Min(1)
private int capacity;

@Column(nullable = false)
private int reserved = 0;


}