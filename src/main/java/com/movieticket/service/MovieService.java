package com.movieticket.service;

import com.movieticket.entity.Movie;
import com.movieticket.repository.MovieRepository;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

import java.util.List;

@Stateless
public class MovieService {

    @Inject
    private MovieRepository movieRepository;

    public List<Movie> listMovies() {
        return movieRepository.findAll();
    }

    public void seedSampleMoviesIfEmpty() {
        if (!movieRepository.findAll().isEmpty()) {
            return;
        }

        movieRepository.save(createMovie(
                "The Grand Adventure",
                "A family discovers a map to a hidden coastal city.",
                112,
                "PG",
                null
        ));
        movieRepository.save(createMovie(
                "Midnight in Tunis",
                "A musician follows a trail of clues through the old city.",
                104,
                "PG-13",
                null
        ));
        movieRepository.save(createMovie(
                "Journey Beyond Earth",
                "A science crew searches for a new home among the stars.",
                126,
                "PG-13",
                null
        ));
    }

    private Movie createMovie(String title, String description, int durationMinutes,
                              String rating, String posterUrl) {
        Movie movie = new Movie();
        movie.setTitle(title);
        movie.setDescription(description);
        movie.setDurationMinutes(durationMinutes);
        movie.setRating(rating);
        movie.setPosterUrl(posterUrl);
        return movie;
    }
}
