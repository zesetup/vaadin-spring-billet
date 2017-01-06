package com.github.zesetup.vaadinspringbillet.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.github.zesetup.vaadinspringbillet.model.*;

@Service
public interface EmployeeService {
	void save(Employee employee);
	Employee get(String employeeId);
	List<Employee>  find(String surname);
	void update(Employee employee);
	void remove(String employeeId);
}
