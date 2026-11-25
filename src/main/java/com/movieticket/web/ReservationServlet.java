package com.movieticket.web;

import com.movieticket.service.BookingService;
import com.movieticket.service.view.ReservationView;
import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Optional;

@WebServlet("/reservation")
public class ReservationServlet extends HttpServlet {

    @Inject
    private BookingService bookingService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String code = request.getParameter("code");
        if (code == null || code.isBlank()) {
            showError(request, response, HttpServletResponse.SC_BAD_REQUEST,
                    "A reservation confirmation code is required.");
            return;
        }

        Optional<ReservationView> reservation = bookingService.findByConfirmationCode(code);
        if (reservation.isEmpty()) {
            showError(request, response, HttpServletResponse.SC_NOT_FOUND,
                    "That reservation could not be found.");
            return;
        }

        request.setAttribute("reservation", reservation.get());
        request.getRequestDispatcher("/WEB-INF/views/reservation.jsp").forward(request, response);
    }

    private void showError(HttpServletRequest request, HttpServletResponse response,
                           int status, String message) throws ServletException, IOException {
        response.setStatus(status);
        request.setAttribute("errorMessage", message);
        request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
    }
}
