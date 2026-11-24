package com.movieticket.repository;

import com.movieticket.entity.Auditorium;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;

@ApplicationScoped
public class AuditoriumRepository {

    @PersistenceContext(unitName = "MovieTicketBookingDevPU")
    private EntityManager entityManager;

    public List<Auditorium> findAll() {
        return entityManager.createQuery(
                "SELECT auditorium FROM Auditorium auditorium ORDER BY auditorium.name ASC",
                Auditorium.class
        ).getResultList();
    }

    public void save(Auditorium auditorium) {
        entityManager.persist(auditorium);
    }
}
