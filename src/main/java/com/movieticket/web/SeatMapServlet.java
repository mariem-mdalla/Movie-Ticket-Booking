package com.movieticket.web;

import com.movieticket.service.ShowtimeService;
import com.movieticket.service.view.SeatMapView;
import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Optional;

@WebServlet("/showtimes/seats")
public class SeatMapServlet extends HttpServlet {

    @Inject
    private ShowtimeService showtimeService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idValue = request.getParameter("id");
        if (idValue == null || idValue.isBlank()) {
            showError(request, response, HttpServletResponse.SC_BAD_REQUEST,
                    "A showtime id is required.");
            return;
        }

        long id;
        try {
            id = Long.parseLong(idValue);
            if (id < 1) {
                throw new NumberFormatException("id must be positive");
            }
        } catch (NumberFormatException exception) {
            showError(request, response, HttpServletResponse.SC_BAD_REQUEST,
                    "The showtime id must be a positive number.");
            return;
        }

        Optional<SeatMapView> seatMap = showtimeService.findSeatMap(id);
        if (seatMap.isEmpty()) {
            showError(request, response, HttpServletResponse.SC_NOT_FOUND,
                    "That showtime could not be found.");
            return;
        }

        request.setAttribute("seatMap", seatMap.get());
        request.getRequestDispatcher("/WEB-INF/views/seats.jsp").forward(request, response);
    }

    private void showError(HttpServletRequest request, HttpServletResponse response,
                           int status, String message) throws ServletException, IOException {
        response.setStatus(status);
        request.setAttribute("errorMessage", message);
        request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
    }
}
