package com.github.zesetup.vaadinspringbillet.service;

import java.util.List;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;
import com.github.zesetup.vaadinspringbillet.model.*;
import com.vaadin.flow.data.provider.QuerySortOrder;

@Service
public interface EmployeeService {

  public class EmployeeSort {
    String propertyName;
    Boolean descending;
    public EmployeeSort(String propertyName, Boolean descending) {
      this.propertyName = propertyName;
      this.descending = descending;
    }
    String getProperty() {
      return propertyName;
    }
    Boolean getDescending() {
      return descending;
    }
  }
  void save(Employee employee);

  Employee findOne(Integer integer);

  void update(Employee employee);

  void delete(Employee employee);

  Stream<Employee> find(List<EmployeeSort> sortOrder, int offset, int limit);

  int count();

  Stream<Employee> findWithFilter(List<EmployeeSort> sortOrder, int offset, int limit,
      String filter);

  int countWithFilter(String value);

  EmployeeSort createSort(String propertyName, boolean descending);
}
