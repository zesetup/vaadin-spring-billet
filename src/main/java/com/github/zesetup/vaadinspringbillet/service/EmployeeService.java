package com.github.zesetup.vaadinspringbillet.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.github.zesetup.vaadinspringbillet.model.*;

@Service
public interface EmployeeService {
	void save(Employee employee);
	Employee findOne(Long employeeId);
	List<Employee> find(String sortField,
			Boolean isAsc,
			Integer recordsOffset,
			Integer recordsLimit,
			String fullSearch);
	List<Employee> findAll();
	List<Employee> findByNameOrSurnameContainingIgnoringCase(String name, String surname);
	void update(Employee employee);
	void delete(Employee employee);
}
