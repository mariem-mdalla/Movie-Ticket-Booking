package com.movieticket.repository;

import com.movieticket.entity.Movie;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;

@ApplicationScoped
public class MovieRepository {

    @PersistenceContext(unitName = "MovieTicketBookingDevPU")
    private EntityManager entityManager;

    public List<Movie> findAll() {
        return entityManager.createQuery(
                "SELECT movie FROM Movie movie ORDER BY movie.title ASC",
                Movie.class
        ).getResultList();
    }

    public void save(Movie movie) {
        entityManager.persist(movie);
    }
}
