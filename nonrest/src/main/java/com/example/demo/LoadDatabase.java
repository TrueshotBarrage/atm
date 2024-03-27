package com.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
public class LoadDatabase implements CommandLineRunner {

  private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

  @Override
  public void run(String... args) throws Exception {

    employeeRepository.save(new Employee("Bilbo", "Baggins", "burglar"));
    employeeRepository.save(new Employee("Frodo", "Baggins", "thief"));

    employeeRepository.findAll().forEach(employee -> log.info("Preloaded " + employee));
  }

  @Autowired
  EmployeeRepository employeeRepository;
}