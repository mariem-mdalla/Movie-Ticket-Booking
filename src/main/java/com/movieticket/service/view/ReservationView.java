package com.movieticket.service.view;

import com.movieticket.entity.ReservationStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class ReservationView {

    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("EEEE, d MMMM yyyy", Locale.ENGLISH);
    private static final DateTimeFormatter TIME_FORMAT =
            DateTimeFormatter.ofPattern("HH:mm", Locale.ENGLISH);

    private final String confirmationCode;
    private final String movieTitle;
    private final String auditoriumName;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;
    private final List<String> seatLabels;
    private final BigDecimal totalPrice;
    private final ReservationStatus status;

    public ReservationView(String confirmationCode, String movieTitle, String auditoriumName,
                           LocalDateTime startTime, LocalDateTime endTime,
                           List<String> seatLabels, BigDecimal totalPrice,
                           ReservationStatus status) {
        this.confirmationCode = confirmationCode;
        this.movieTitle = movieTitle;
        this.auditoriumName = auditoriumName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.seatLabels = List.copyOf(seatLabels);
        this.totalPrice = totalPrice;
        this.status = status;
    }

    public String getConfirmationCode() { return confirmationCode; }
    public String getMovieTitle() { return movieTitle; }
    public String getAuditoriumName() { return auditoriumName; }
    public LocalDateTime getStartTime() { return startTime; }
    public LocalDateTime getEndTime() { return endTime; }
    public List<String> getSeatLabels() { return seatLabels; }
    public BigDecimal getTotalPrice() { return totalPrice; }
    public ReservationStatus getStatus() { return status; }
    public String getDisplayDate() { return startTime.format(DATE_FORMAT); }
    public String getDisplayStartTime() { return startTime.format(TIME_FORMAT); }
    public String getDisplayEndTime() { return endTime.format(TIME_FORMAT); }
}
