package com.github.zesetup.vaadinspringbillet;

import com.github.zesetup.vaadinspringbillet.dao.EmployeeRepository;
import com.github.zesetup.vaadinspringbillet.model.Employee;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application  implements ApplicationRunner {
  private static final Logger log = LoggerFactory.getLogger(Application.class);

  @Autowired
  EmployeeRepository repository;

  public Application() {}

  public static void main(String[] args) {
      SpringApplication.run(Application.class, args);
  }

  @Override
  public void run(ApplicationArguments args) throws Exception {
    repository.save(new Employee("ivanov", "Ivan", "Ivanovich", "Engeneer"));
    repository.save(new Employee("johnson", "John", "Johnson", "Project Manager"));
    repository.save(new Employee("jonauskas", "Jonas", "Jonauskas", "Officer"));
    for (int i = 0; i < 99; i++) {
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
  }
}
