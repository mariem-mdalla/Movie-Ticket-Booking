package com.movieticket.service;

import com.movieticket.entity.Reservation;
import com.movieticket.entity.ReservationSeat;
import com.movieticket.entity.ReservationStatus;
import com.movieticket.entity.Seat;
import com.movieticket.entity.Showtime;
import com.movieticket.entity.User;
import com.movieticket.repository.ReservationRepository;
import com.movieticket.repository.ReservationSeatRepository;
import com.movieticket.repository.SeatRepository;
import com.movieticket.repository.ShowtimeRepository;
import com.movieticket.repository.UserRepository;
import com.movieticket.service.view.ReservationView;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.PersistenceException;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

@Stateless
public class BookingService {

    private static final String TEMPORARY_DEMO_USERNAME = "demo-customer";

    @Inject
    private ShowtimeRepository showtimeRepository;
    @Inject
    private SeatRepository seatRepository;
    @Inject
    private ReservationRepository reservationRepository;
    @Inject
    private ReservationSeatRepository reservationSeatRepository;
    @Inject
    private UserRepository userRepository;

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public String reserveSeats(Long showtimeId, List<Long> seatIds, Long userId) {
        if (showtimeId == null || showtimeId < 1) {
            throw new IllegalArgumentException("Choose a valid showtime.");
        }
        if (seatIds == null || seatIds.isEmpty() || seatIds.size() > 6) {
            throw new IllegalArgumentException("Choose between 1 and 6 seats.");
        }
        if (seatIds.stream().anyMatch(id -> id == null || id < 1)
                || new HashSet<>(seatIds).size() != seatIds.size()) {
            throw new IllegalArgumentException("The selected seat list is invalid.");
        }

        Showtime showtime = showtimeRepository.findWithMovieAndAuditorium(showtimeId)
                .orElseThrow(() -> new IllegalArgumentException("That showtime could not be found."));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("The demo customer is unavailable."));
        List<Seat> seats = seatRepository.findByIdsAndAuditoriumId(
                seatIds, showtime.getAuditorium().getId()
        );
        if (seats.size() != seatIds.size()) {
            throw new IllegalArgumentException("One or more selected seats do not belong to this room.");
        }

        List<Long> takenSeatIds = reservationSeatRepository.findTakenSeatIds(showtimeId);
        if (seatIds.stream().anyMatch(takenSeatIds::contains)) {
            throw new SeatAlreadyTakenException("That seat was just taken, please choose another.");
        }

        String confirmationCode = UUID.randomUUID().toString()
                .replace("-", "")
                .toUpperCase(Locale.ROOT);
        Reservation reservation = new Reservation();
        reservation.setUser(user);
        reservation.setShowtime(showtime);
        reservation.setConfirmationCode(confirmationCode);
        reservation.setTotalPrice(showtime.getPrice().multiply(java.math.BigDecimal.valueOf(seats.size())));
        reservation.setStatus(ReservationStatus.CONFIRMED);
        reservation.setCreatedAt(LocalDateTime.now());

        try {
            reservationRepository.save(reservation);
            for (Seat seat : seats) {
                ReservationSeat reservationSeat = new ReservationSeat();
                reservationSeat.setReservation(reservation);
                reservationSeat.setShowtime(showtime);
                reservationSeat.setSeat(seat);
                reservationSeat.setPriceAtBooking(showtime.getPrice());
                reservationSeatRepository.save(reservationSeat);
            }
            // Send pending INSERTs now, while we can translate a seat uniqueness conflict.
            reservationSeatRepository.flush();
        } catch (PersistenceException exception) {
            if (isSeatUniqueConstraintViolation(exception)) {
                throw new SeatAlreadyTakenException(
                        "That seat was just taken, please choose another.");
            }
            throw exception;
        }

        return confirmationCode;
    }

    public Long temporaryDemoCustomerId() {
        return userRepository.findByUsername(TEMPORARY_DEMO_USERNAME)
                .map(User::getId)
                .orElseThrow(() -> new IllegalStateException("Temporary demo customer was not seeded."));
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Optional<ReservationView> findByConfirmationCode(String code) {
        return reservationRepository.findByConfirmationCode(code).map(reservation -> {
            List<String> seatLabels = reservation.getReservationSeats().stream()
                    .map(reservationSeat -> reservationSeat.getSeat().getRowLabel()
                            + reservationSeat.getSeat().getSeatNumber())
                    .sorted(Comparator.naturalOrder())
                    .toList();
            Showtime showtime = reservation.getShowtime();
            return new ReservationView(
                    reservation.getConfirmationCode(),
                    showtime.getMovie().getTitle(),
                    showtime.getAuditorium().getName(),
                    showtime.getStartTime(),
                    showtime.calculateEndTime(),
                    seatLabels,
                    reservation.getTotalPrice(),
                    reservation.getStatus()
            );
        });
    }

    private boolean isSeatUniqueConstraintViolation(Throwable exception) {
        Throwable cause = exception;
        while (cause != null) {
            if (cause instanceof SQLException sqlException
                    && "23505".equals(sqlException.getSQLState())) {
                String message = String.valueOf(sqlException.getMessage())
                        .toLowerCase(Locale.ROOT);
                return message.contains("reservation_seats")
                        || message.contains("uk_reservation_seat_showtime_seat");
            }
            cause = cause.getCause();
        }
        return false;
    }
}
