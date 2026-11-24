package com.movieticket.repository;

import com.movieticket.entity.Seat;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;

@ApplicationScoped
public class SeatRepository {

    @PersistenceContext(unitName = "MovieTicketBookingDevPU")
    private EntityManager entityManager;

    public List<Seat> findByAuditoriumId(Long auditoriumId) {
        return entityManager.createQuery(
                        "SELECT seat FROM Seat seat " +
                                "WHERE seat.auditorium.id = :auditoriumId " +
                                "ORDER BY seat.rowLabel ASC, seat.seatNumber ASC",
                        Seat.class
                )
                .setParameter("auditoriumId", auditoriumId)
                .getResultList();
    }

    public void save(Seat seat) {
        entityManager.persist(seat);
    }
}
