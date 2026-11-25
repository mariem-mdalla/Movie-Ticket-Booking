package com.movieticket.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;

@ApplicationScoped
public class ReservationSeatRepository {

    @PersistenceContext(unitName = "MovieTicketBookingDevPU")
    private EntityManager entityManager;

    public List<Long> findTakenSeatIds(Long showtimeId) {
        return entityManager.createQuery(
                        "SELECT reservationSeat.seat.id FROM ReservationSeat reservationSeat " +
                                "WHERE reservationSeat.showtime.id = :showtimeId",
                        Long.class
                )
                .setParameter("showtimeId", showtimeId)
                .getResultList();
    }

    public void save(com.movieticket.entity.ReservationSeat reservationSeat) {
        entityManager.persist(reservationSeat);
    }

    public void flush() {
        entityManager.flush();
    }
}
