package com.movieticket.service.view;

public class SeatCellView {

    private final Long id;
    private final int seatNumber;
    private final boolean taken;

    public SeatCellView(Long id, int seatNumber, boolean taken) {
        this.id = id;
        this.seatNumber = seatNumber;
        this.taken = taken;
    }

    public Long getId() { return id; }
    public int getSeatNumber() { return seatNumber; }
    public boolean isTaken() { return taken; }
}
