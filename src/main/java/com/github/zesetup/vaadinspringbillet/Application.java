package com.github.zesetup.vaadinspringbillet;

import com.github.zesetup.vaadinspringbillet.dao.EmployeeRepository;
import com.github.zesetup.vaadinspringbillet.model.Employee;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {
  private static final Logger log = LoggerFactory.getLogger(Application.class);
  public Application() {}


  public static void main(String[] args) {
    SpringApplication.run(Application.class);
  }

  @Bean
  public CommandLineRunner loadData(final EmployeeRepository repository) {
    return (args) -> {
      // save a couple of Employees
      repository.save(new Employee("ivanov", "Ivan", "Ivanovich", "Engeneer"));
      repository.save(new Employee("johnson", "John", "Johnson", "Project Manager"));
      repository.save(new Employee("jonauskas", "Jonas", "Jonauskas", "Officer"));
      for (int i = 0; i < 2256; i++) {
        repository.save(new Employee("l" + UUID.randomUUID().toString().substring(0, 8),
            "name" + UUID.randomUUID().toString().substring(0, 5),
            "surname-" + i,
            "posit" + UUID.randomUUID().toString().substring(0, 5)));
      }

      // fetch all Employees
      log.info("Employees found with findAll():");
      log.info("-------------------------------");
      for (Employee employee : repository.findAll()) {
        log.info(employee.toString());
      }
      log.info("");

      // fetch an individual Employee by ID
      Employee employee = repository.getOne(1);
      log.info("Employee found with findOne(1L):");
      log.info("--------------------------------");
      log.info(employee.toString());
      log.info("");

      // fetch Employees by last name
      log.info("Employee found with findByLastNameStartsWithIgnoreCase('Bauer'):");
      log.info("--------------------------------------------");
      for (Employee bauer : repository.findAll()) {
        log.info(bauer.toString());
      }
      log.info("");
    };
  }

}
