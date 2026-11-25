package com.movieticket.service.view;

import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class ShowtimeView {

    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("EEEE, d MMMM yyyy", Locale.ENGLISH);
    private static final DateTimeFormatter TIME_FORMAT =
            DateTimeFormatter.ofPattern("HH:mm", Locale.ENGLISH);

    private final Long id;
    private final String movieTitle;
    private final String auditoriumName;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;
    private final BigDecimal price;

    public ShowtimeView(Long id, String movieTitle, String auditoriumName,
                        LocalDateTime startTime, LocalDateTime endTime, BigDecimal price) {
        this.id = id;
        this.movieTitle = movieTitle;
        this.auditoriumName = auditoriumName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.price = price;
    }

    public Long getId() { return id; }
    public String getMovieTitle() { return movieTitle; }
    public String getAuditoriumName() { return auditoriumName; }
    public LocalDateTime getStartTime() { return startTime; }
    public LocalDateTime getEndTime() { return endTime; }
    public BigDecimal getPrice() { return price; }
    public String getDisplayDate() { return startTime.format(DATE_FORMAT); }
    public String getDisplayStartTime() { return startTime.format(TIME_FORMAT); }
    public String getDisplayEndTime() { return endTime.format(TIME_FORMAT); }
}
