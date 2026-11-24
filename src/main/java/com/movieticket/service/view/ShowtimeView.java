package com.movieticket.service.view;

import java.time.LocalDateTime;

public class ShowtimeView {

    private final Long id;
    private final String movieTitle;
    private final String auditoriumName;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;

    public ShowtimeView(Long id, String movieTitle, String auditoriumName,
                        LocalDateTime startTime, LocalDateTime endTime) {
        this.id = id;
        this.movieTitle = movieTitle;
        this.auditoriumName = auditoriumName;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Long getId() { return id; }
    public String getMovieTitle() { return movieTitle; }
    public String getAuditoriumName() { return auditoriumName; }
    public LocalDateTime getStartTime() { return startTime; }
    public LocalDateTime getEndTime() { return endTime; }
}
