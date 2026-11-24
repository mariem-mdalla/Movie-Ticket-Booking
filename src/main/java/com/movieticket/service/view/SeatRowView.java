package com.movieticket.service.view;

import java.util.List;

public class SeatRowView {

    private final String label;
    private final List<SeatCellView> seats;

    public SeatRowView(String label, List<SeatCellView> seats) {
        this.label = label;
        this.seats = List.copyOf(seats);
    }

    public String getLabel() { return label; }
    public List<SeatCellView> getSeats() { return seats; }
}
