package com.movieticket.service.view;

public class SeatCellView {

    private final int seatNumber;
    private final boolean taken;

    public SeatCellView(int seatNumber, boolean taken) {
        this.seatNumber = seatNumber;
        this.taken = taken;
    }

    public int getSeatNumber() { return seatNumber; }
    public boolean isTaken() { return taken; }
}
