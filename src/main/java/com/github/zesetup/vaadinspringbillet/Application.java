package com.github.zesetup.vaadinspringbillet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.github.zesetup.vaadinspringbillet.dao.EmployeeRepository;
import com.github.zesetup.vaadinspringbillet.model.Employee;

@SpringBootApplication
public class Application {
	private static final Logger log = LoggerFactory.getLogger(Application.class);

		public static void main(String[] args) {
			SpringApplication.run(Application.class);
		}

		@Bean
		public CommandLineRunner loadData(final EmployeeRepository repository) {
			return (args) -> {
				// save a couple of Employees
				repository.save(new Employee("John", "Reech", "South", "manager"));
				repository.save(new Employee("John2", "Reech2", "South2", "manager2"));
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
						.findBySurnameStartsWithIgnoreCase("John")) {
					log.info(bauer.toString());
				}
				log.info("");
			};
		}

}
