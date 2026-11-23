package com.movieticket.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Auditorium {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @OneToMany(mappedBy = "auditorium", fetch = FetchType.LAZY)
    private Set<Seat> seats = new HashSet<>();

    @OneToMany(mappedBy = "auditorium", fetch = FetchType.LAZY)
    private Set<Showtime> showtimes = new HashSet<>();

    public Auditorium() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Seat> getSeats() {
        return seats;
    }

    public Set<Showtime> getShowtimes() {
        return showtimes;
    }
}
