package com.github.zesetup.vaadinspringbillet.dao;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;
import com.github.zesetup.vaadinspringbillet.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
	List<Employee> findByNameIgnoringCaseContainingOrSurnameIgnoringCaseContaining(String name, String surname);
	 // For lazy loading and filtering
    List<Employee> findByNameIgnoringCaseContainingOrSurnameIgnoringCaseContaining(String name, String surname, Pageable pageable);
    long countByNameIgnoringCaseContainingOrSurnameIgnoringCaseContaining(String name, String surname);
}

