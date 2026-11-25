package com.movieticket.repository;

import com.movieticket.entity.Reservation;
import com.movieticket.entity.ReservationSeat;
import com.movieticket.entity.Showtime;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Subgraph;

import java.util.Optional;

@ApplicationScoped
public class ReservationRepository {

    @PersistenceContext(unitName = "MovieTicketBookingDevPU")
    private EntityManager entityManager;

    public void save(Reservation reservation) {
        entityManager.persist(reservation);
    }

    public Optional<Reservation> findByConfirmationCode(String code) {
        EntityGraph<Reservation> graph = entityManager.createEntityGraph(Reservation.class);
        Subgraph<Showtime> showtimeGraph = graph.addSubgraph("showtime", Showtime.class);
        showtimeGraph.addAttributeNodes("movie", "auditorium");
        Subgraph<ReservationSeat> reservationSeatsGraph =
                graph.addSubgraph("reservationSeats", ReservationSeat.class);
        reservationSeatsGraph.addAttributeNodes("seat");

        return entityManager.createQuery(
                        "SELECT reservation FROM Reservation reservation " +
                                "WHERE reservation.confirmationCode = :code",
                        Reservation.class
                )
                .setParameter("code", code)
                .setHint("jakarta.persistence.fetchgraph", graph)
                .getResultStream()
                .findFirst();
    }
}
