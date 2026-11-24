package com.movieticket.web;

import com.movieticket.service.ShowtimeService;
import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.DateTimeException;
import java.time.LocalDate;

@WebServlet("/showtimes")
public class ShowtimeListServlet extends HttpServlet {

    @Inject
    private ShowtimeService showtimeService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String dateValue = request.getParameter("date");
        LocalDate date = null;
        if (dateValue != null) {
            try {
                date = LocalDate.parse(dateValue);
                date.plusDays(1);
            } catch (DateTimeException exception) {
                showError(request, response, HttpServletResponse.SC_BAD_REQUEST,
                        "The date must use YYYY-MM-DD format.");
                return;
            }
        }

        LocalDate selectedDate = date == null ? LocalDate.now() : date;
        request.setAttribute("selectedDate", selectedDate);
        request.setAttribute("showtimes", showtimeService.listShowtimes(date));
        request.getRequestDispatcher("/WEB-INF/views/showtimes.jsp").forward(request, response);
    }

    private void showError(HttpServletRequest request, HttpServletResponse response,
                           int status, String message) throws ServletException, IOException {
        response.setStatus(status);
        request.setAttribute("errorMessage", message);
        request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
    }
}
