package com.movieticket.web;

import com.movieticket.service.BookingService;
import com.movieticket.service.SeatAlreadyTakenException;
import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/book")
public class BookingServlet extends HttpServlet {

    @Inject
    private BookingService bookingService;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Long showtimeId;
        try {
            showtimeId = Long.parseLong(request.getParameter("showtimeId"));
            if (showtimeId < 1) {
                throw new NumberFormatException("showtime id must be positive");
            }
        } catch (NumberFormatException | NullPointerException exception) {
            showError(request, response, HttpServletResponse.SC_BAD_REQUEST,
                    "Choose a valid showtime before booking.");
            return;
        }

        List<Long> seatIds = new ArrayList<>();
        String[] submittedSeatIds = request.getParameterValues("seatIds");
        if (submittedSeatIds != null) {
            try {
                for (String submittedSeatId : submittedSeatIds) {
                    seatIds.add(Long.parseLong(submittedSeatId));
                }
            } catch (NumberFormatException exception) {
                redirectBackToSeats(request, response, showtimeId,
                        "The selected seat list is invalid.");
                return;
            }
        }

        try {
            Long userId = bookingService.temporaryDemoCustomerId();
            String confirmationCode = bookingService.reserveSeats(showtimeId, seatIds, userId);
            response.sendRedirect(response.encodeRedirectURL(
                    request.getContextPath() + "/reservation?code=" + confirmationCode));
        } catch (SeatAlreadyTakenException exception) {
            redirectBackToSeats(request, response, showtimeId,
                    "that seat was just taken, please choose another");
        } catch (IllegalArgumentException exception) {
            redirectBackToSeats(request, response, showtimeId, exception.getMessage());
        }
    }

    private void redirectBackToSeats(HttpServletRequest request, HttpServletResponse response,
                                     Long showtimeId, String message) throws IOException {
        String encodedMessage = URLEncoder.encode(message, StandardCharsets.UTF_8);
        response.sendRedirect(response.encodeRedirectURL(request.getContextPath()
                + "/showtimes/seats?id=" + showtimeId + "&message=" + encodedMessage));
    }

    private void showError(HttpServletRequest request, HttpServletResponse response,
                           int status, String message) throws ServletException, IOException {
        response.setStatus(status);
        request.setAttribute("errorMessage", message);
        request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
    }
}
