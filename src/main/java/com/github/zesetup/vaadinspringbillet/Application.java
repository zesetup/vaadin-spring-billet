package com.github.zesetup.vaadinspringbillet;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;

import com.github.zesetup.vaadinspringbillet.dao.EmployeeRepository;
import com.github.zesetup.vaadinspringbillet.model.Employee;


@SpringBootApplication
public class Application extends SpringBootServletInitializer {
  private static final Logger log = LoggerFactory.getLogger(Application.class);

  public Application() {
  }

  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
    return application.sources(Application.class);
  }

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
      for(int i=0;i<150;i++) {
        repository.save(new Employee(
            "login"+UUID.randomUUID().toString().substring(0,5), 
            "name"+UUID.randomUUID().toString().substring(0,5),
            "surname"+UUID.randomUUID().toString().substring(0,5), 
            "posit"+UUID.randomUUID().toString().substring(0,5)));
      }

      // fetch all Employees
      log.info("Employees found with findAll():");
      log.info("-------------------------------");
      for (Employee Employee : repository.findAll()) {
        log.info(Employee.toString());
      }
      log.info("");

      // fetch an individual Employee by ID
      Employee Employee = repository.findOne(1L);
      log.info("Employee found with findOne(1L):");
      log.info("--------------------------------");
      log.info(Employee.toString());
      log.info("");

      // fetch Employees by last name
      log.info("Employee found with findByLastNameStartsWithIgnoreCase('Bauer'):");
      log.info("--------------------------------------------");
      for (Employee bauer : repository
          .findByNameIgnoringCaseContainingOrSurnameIgnoringCaseContaining("John","Connor")) {
        log.info(bauer.toString());
      }
      log.info("");
    };
  }

}
