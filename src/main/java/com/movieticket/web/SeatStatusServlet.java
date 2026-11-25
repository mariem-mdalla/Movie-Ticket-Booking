package com.movieticket.web;

import com.movieticket.service.ShowtimeService;
import jakarta.inject.Inject;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@WebServlet("/showtimes/seats/status")
public class SeatStatusServlet extends HttpServlet {

    @Inject
    private ShowtimeService showtimeService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        long showtimeId;
        try {
            showtimeId = Long.parseLong(request.getParameter("id"));
            if (showtimeId < 1) {
                throw new NumberFormatException("id must be positive");
            }
        } catch (NumberFormatException | NullPointerException exception) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\":\"A positive showtime id is required.\"}");
            return;
        }

        Optional<List<Long>> takenSeats = showtimeService.listTakenSeatIds(showtimeId);
        if (takenSeats.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().write("{\"error\":\"Showtime not found.\"}");
            return;
        }

        String ids = String.join(",", takenSeats.get().stream().map(String::valueOf).toList());
        response.getWriter().write("{\"takenSeatIds\":[" + ids + "]}");
    }
}
