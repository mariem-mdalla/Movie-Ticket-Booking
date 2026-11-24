package com.movieticket.config;

import com.movieticket.service.MovieService;
import com.movieticket.service.ShowtimeService;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.inject.Inject;

@Singleton
@Startup
public class SampleMovieInitializer {

    @Inject
    private MovieService movieService;

    @Inject
    private ShowtimeService showtimeService;

    @PostConstruct
    public void initialize() {
        movieService.seedSampleMoviesIfEmpty();
        showtimeService.seedShowtimesIfEmpty();
    }
}
