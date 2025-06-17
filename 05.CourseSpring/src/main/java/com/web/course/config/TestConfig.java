package com.web.course.config;

import java.time.Instant;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.web.course.entities.Order;
import com.web.course.entities.User;
import com.web.course.repositories.OrderRepository;
import com.web.course.repositories.UserRepository;

@Configuration
@Profile("test")
public class TestConfig implements CommandLineRunner {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrderRepository orderRepository;

    @Override
    public void run(String... args) throws Exception {
        User u1 = new User(null, "Maria Brown", "maria@gmail.com", "988888888", "123456");
        User u2 = new User(null, "Alex Green", "alex@gmail.com", "977777777", "123456");
        userRepository.saveAll(List.of(u1, u2));

        Order o1 = new Order(null, Instant.parse("2023-10-01T10:00:00Z"), u1);
        Order o2 = new Order(null, Instant.parse("2023-10-02T11:30:00Z"), u1);
        Order o3 = new Order(null, Instant.parse("2023-10-03T15:45:00Z"), u1);
        orderRepository.saveAll(List.of(o1, o2, o3));
    }
}
