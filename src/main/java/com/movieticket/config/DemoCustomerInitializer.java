package com.movieticket.config;

import com.movieticket.entity.Role;
import com.movieticket.entity.User;
import com.movieticket.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.inject.Inject;

@Singleton
@Startup
public class DemoCustomerInitializer {

    private static final String DEMO_USERNAME = "demo-customer";

    @Inject
    private UserRepository userRepository;

    @PostConstruct
    public void initialize() {
        if (userRepository.findByUsername(DEMO_USERNAME).isPresent()) {
            return;
        }

        User demoCustomer = new User();
        demoCustomer.setUsername(DEMO_USERNAME);
        // TEMPORARY: no login exists yet; replace this placeholder when password hashing is implemented.
        demoCustomer.setPasswordHash("TEMPORARY_NO_LOGIN_HASH");
        demoCustomer.setRole(Role.CUSTOMER);
        userRepository.save(demoCustomer);
    }
}
