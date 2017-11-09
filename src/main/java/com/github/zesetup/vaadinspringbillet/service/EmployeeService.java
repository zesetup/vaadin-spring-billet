package com.github.zesetup.vaadinspringbillet.service;

import java.util.List;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;
import com.github.zesetup.vaadinspringbillet.model.*;
import com.vaadin.data.provider.QuerySortOrder;

@Service
public interface EmployeeService {
  void save(Employee employee);

  Employee findOne(Long employeeId);

  void update(Employee employee);

  void delete(Employee employee);

  Stream<Employee> find(List<QuerySortOrder> sortOrder, int offset, int limit);

  int count();

  Stream<Employee> findWithFilter(List<QuerySortOrder> sortOrder, int offset, int limit,
      String filter);

  int countWithFilter(String value);

}

