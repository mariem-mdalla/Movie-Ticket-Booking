package com.movieticket.repository;

import com.movieticket.entity.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.Optional;

@ApplicationScoped
public class UserRepository {

    @PersistenceContext(unitName = "MovieTicketBookingDevPU")
    private EntityManager entityManager;

    public Optional<User> findById(Long id) {
        return Optional.ofNullable(entityManager.find(User.class, id));
    }

    public Optional<User> findByUsername(String username) {
        return entityManager.createQuery(
                        "SELECT user FROM User user WHERE user.username = :username",
                        User.class
                )
                .setParameter("username", username)
                .getResultStream()
                .findFirst();
    }

    public void save(User user) {
        entityManager.persist(user);
    }
}
