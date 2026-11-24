package com.movieticket.config;

import com.movieticket.service.MovieService;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.inject.Inject;

@Singleton
@Startup
public class SampleMovieInitializer {

    @Inject
    private MovieService movieService;

    @PostConstruct
    public void initialize() {
        movieService.seedSampleMoviesIfEmpty();
    }
}
