package com.movieticket.service;

import com.movieticket.entity.Auditorium;
import com.movieticket.entity.Movie;
import com.movieticket.entity.Seat;
import com.movieticket.entity.Showtime;
import com.movieticket.repository.AuditoriumRepository;
import com.movieticket.repository.MovieRepository;
import com.movieticket.repository.ReservationSeatRepository;
import com.movieticket.repository.SeatRepository;
import com.movieticket.repository.ShowtimeRepository;
import com.movieticket.service.view.SeatCellView;
import com.movieticket.service.view.SeatMapView;
import com.movieticket.service.view.SeatRowView;
import com.movieticket.service.view.ShowtimeView;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Stateless
public class ShowtimeService {

    @Inject
    private ShowtimeRepository showtimeRepository;
    @Inject
    private AuditoriumRepository auditoriumRepository;
    @Inject
    private SeatRepository seatRepository;
    @Inject
    private ReservationSeatRepository reservationSeatRepository;
    @Inject
    private MovieRepository movieRepository;

    public List<ShowtimeView> listShowtimes(LocalDate date) {
        LocalDate selectedDate = date == null ? LocalDate.now() : date;
        LocalDateTime start = selectedDate.atStartOfDay();
        LocalDateTime end = selectedDate.plusDays(1).atStartOfDay();

        return showtimeRepository.findBetween(start, end).stream()
                .map(this::toShowtimeView)
                .toList();
    }

    public Optional<SeatMapView> findSeatMap(Long showtimeId) {
        Optional<Showtime> optionalShowtime =
                showtimeRepository.findWithMovieAndAuditorium(showtimeId);
        if (optionalShowtime.isEmpty()) {
            return Optional.empty();
        }

        Showtime showtime = optionalShowtime.get();
        List<Seat> seats = seatRepository.findByAuditoriumId(showtime.getAuditorium().getId());
        Set<Long> takenIds = Set.copyOf(
                reservationSeatRepository.findTakenSeatIds(showtimeId)
        );

        Map<String, List<SeatCellView>> seatsByRow = new LinkedHashMap<>();
        for (Seat seat : seats) {
            seatsByRow.computeIfAbsent(seat.getRowLabel(), ignored -> new ArrayList<>())
                    .add(new SeatCellView(seat.getSeatNumber(), takenIds.contains(seat.getId())));
        }

        List<SeatRowView> rows = seatsByRow.entrySet().stream()
                .map(entry -> new SeatRowView(entry.getKey(), entry.getValue()))
                .toList();
        return Optional.of(new SeatMapView(toShowtimeView(showtime), rows));
    }

    public void seedShowtimesIfEmpty() {
        if (!showtimeRepository.findAll().isEmpty() || !auditoriumRepository.findAll().isEmpty()) {
            return;
        }

        List<Movie> movies = movieRepository.findAll();
        if (movies.isEmpty()) {
            return;
        }

        List<Auditorium> auditoriums = new ArrayList<>();
        for (int roomNumber = 1; roomNumber <= 2; roomNumber++) {
            Auditorium auditorium = new Auditorium();
            auditorium.setName("Room " + roomNumber);
            auditoriumRepository.save(auditorium);
            auditoriums.add(auditorium);
            seedSeats(auditorium);
        }

        LocalDate today = LocalDate.now();
        int[] hours = {10, 14, 18};
        for (int dayOffset = 0; dayOffset <= 1; dayOffset++) {
            LocalDate date = today.plusDays(dayOffset);
            for (int roomIndex = 0; roomIndex < auditoriums.size(); roomIndex++) {
                for (int showingIndex = 0; showingIndex < hours.length; showingIndex++) {
                    int movieIndex = (dayOffset + roomIndex + showingIndex) % movies.size();
                    Showtime showtime = new Showtime();
                    showtime.setMovie(movies.get(movieIndex));
                    showtime.setAuditorium(auditoriums.get(roomIndex));
                    showtime.setStartTime(date.atTime(hours[showingIndex], 0));
                    showtime.setPrice(new BigDecimal("12.50"));
                    showtimeRepository.save(showtime);
                }
            }
        }
    }

    private void seedSeats(Auditorium auditorium) {
        for (char row = 'A'; row <= 'F'; row++) {
            for (int number = 1; number <= 8; number++) {
                Seat seat = new Seat();
                seat.setAuditorium(auditorium);
                seat.setRowLabel(String.valueOf(row));
                seat.setSeatNumber(number);
                seatRepository.save(seat);
            }
        }
    }

    private ShowtimeView toShowtimeView(Showtime showtime) {
        return new ShowtimeView(
                showtime.getId(),
                showtime.getMovie().getTitle(),
                showtime.getAuditorium().getName(),
                showtime.getStartTime(),
                showtime.calculateEndTime()
        );
    }
}
