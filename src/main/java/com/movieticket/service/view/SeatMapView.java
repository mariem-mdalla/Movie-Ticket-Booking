package com.movieticket.service.view;

import java.util.List;

public class SeatMapView {

    private final ShowtimeView showtime;
    private final List<SeatRowView> rows;

    public SeatMapView(ShowtimeView showtime, List<SeatRowView> rows) {
        this.showtime = showtime;
        this.rows = List.copyOf(rows);
    }

    public ShowtimeView getShowtime() { return showtime; }
    public List<SeatRowView> getRows() { return rows; }
}
