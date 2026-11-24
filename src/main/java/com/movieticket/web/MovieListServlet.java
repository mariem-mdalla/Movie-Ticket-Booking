package com.movieticket.web;

import com.movieticket.service.MovieService;
import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/movies")
public class MovieListServlet extends HttpServlet {

    @Inject
    private MovieService movieService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("movies", movieService.listMovies());
        request.getRequestDispatcher("/WEB-INF/views/movies.jsp").forward(request, response);
    }
}
