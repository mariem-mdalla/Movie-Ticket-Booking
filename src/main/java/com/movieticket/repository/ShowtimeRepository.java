package com.movieticket.repository;

import com.movieticket.entity.Showtime;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class ShowtimeRepository {

    @PersistenceContext(unitName = "MovieTicketBookingDevPU")
    private EntityManager entityManager;

    public List<Showtime> findBetween(LocalDateTime start, LocalDateTime end) {
        return entityManager.createQuery(
                        "SELECT showtime FROM Showtime showtime " +
                                "JOIN FETCH showtime.movie " +
                                "JOIN FETCH showtime.auditorium " +
                                "WHERE showtime.startTime >= :start AND showtime.startTime < :end " +
                                "ORDER BY showtime.startTime ASC, showtime.auditorium.name ASC",
                        Showtime.class
                )
                .setParameter("start", start)
                .setParameter("end", end)
                .getResultList();
    }

    public Optional<Showtime> findWithMovieAndAuditorium(Long id) {
        return entityManager.createQuery(
                        "SELECT showtime FROM Showtime showtime " +
                                "JOIN FETCH showtime.movie " +
                                "JOIN FETCH showtime.auditorium " +
                                "WHERE showtime.id = :id",
                        Showtime.class
                )
                .setParameter("id", id)
                .getResultStream()
                .findFirst();
    }

    public List<Showtime> findAll() {
        return entityManager.createQuery("SELECT showtime FROM Showtime showtime", Showtime.class)
                .getResultList();
    }

    public void save(Showtime showtime) {
        entityManager.persist(showtime);
    }
}
