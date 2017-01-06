package com.github.zesetup.vaadinspringbillet.dao;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import com.github.zesetup.vaadinspringbillet.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
	List<Employee> findBySurnameStartsWithIgnoreCase(String surname);
}

