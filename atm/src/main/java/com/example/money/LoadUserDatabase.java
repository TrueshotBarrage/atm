package com.example.money;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
public class LoadUserDatabase implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(LoadUserDatabase.class);

    @Override
    public void run(String... args) throws Exception {

        userRepository.save(new AtmUser("Bilbo", "Baggins", 0.0));
        userRepository.save(new AtmUser("Frodo", "Baggins", 20.20));

        userRepository.findAll().forEach(user -> log.info("Preloaded " + user));
    }

    @Autowired
    UserRepository userRepository;
}