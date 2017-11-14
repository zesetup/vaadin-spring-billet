package com.github.zesetup.vaadinspringbillet.service;

import com.github.zesetup.vaadinspringbillet.dao.EmployeeDao;
import com.github.zesetup.vaadinspringbillet.dao.EmployeeRepository;
import com.github.zesetup.vaadinspringbillet.model.Employee;
import com.vaadin.data.provider.QuerySortOrder;
import com.vaadin.shared.data.sort.SortDirection;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;



@Component
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

  @Autowired
  EmployeeRepository employeeRepository;
  @Autowired
  EmployeeDao employeeDao;


  private static final Logger logger = LoggerFactory.getLogger(EmployeeServiceImpl.class);

  @Override
  public void save(Employee employee) {
    employeeRepository.save(employee);
  }


  @Override
  public void update(Employee employee) {
    employeeRepository.save(employee);
  }

  @Override
  public void delete(Employee employee) {
    employeeRepository.delete(employee);
  }

  @Override
  public Employee findOne(Long employeeId) {
    return employeeRepository.findOne(employeeId);
  }

  @Override
  public Stream<Employee> find(List<QuerySortOrder> sortOrders, int offset, int limit) {
    Pageable pageable;
    if (sortOrders.isEmpty()) {
      pageable = new PageRequest(offset / limit, offset % limit + limit);
    } else {
      pageable = new PageRequest(offset / limit, offset % limit + limit, getSort(sortOrders));
      getSort(sortOrders).forEach(v -> logger.info(v.toString()));
    }

    Page<Employee> result = employeeRepository.findAll(pageable);
    logger.info("Fetached: " + result.getSize() + " offset=" + offset + " limit=" + limit 
        + " pageable.getOffset(): " + pageable.getOffset()
        + " pageable.getOffset(): " + pageable.getPageSize()
        );
    return StreamSupport.stream(result.spliterator(), false);
  }

  private Sort getSort(List<QuerySortOrder> sortOrders) {
    return new Sort(sortOrders.stream()
        .map(so -> new Order(
            so.getDirection() == SortDirection.ASCENDING ? Direction.ASC : Direction.DESC,
            so.getSorted()))
        .collect(Collectors.toList()));
  }

  @Override
  public int count() {
    logger.info("Counted: " + employeeRepository.count());
    return (int) employeeRepository.count();
  }

  @Override
  public Stream<Employee> findWithFilter(List<QuerySortOrder> sortOrders, int offset, int limit,
      String filter) {
    Pageable pageable;
    if (sortOrders.isEmpty()) {
      pageable = new PageRequest(offset / limit, limit);      
    } else {
      pageable = new PageRequest(offset / limit, limit, getSort(sortOrders));
      logger.info("Sort with filter: ");
      getSort(sortOrders).forEach(v -> logger.info(v.toString()));
    }
    Page<Employee> result = employeeRepository.findWithFilter(filter, pageable);
    logger.info("Fetached: " + result.getSize() + " offset=" + offset + " limit=" + limit);
    //result.forEach(v -> logger.info(v.getName()));
    return StreamSupport.stream(result.spliterator(), false);
  }

  @Override
  public int countWithFilter(String filter) {
    logger.info("Counted: " + employeeRepository.countWithFilter(filter));
    return (int) employeeRepository.countWithFilter(filter);
  }

  @FunctionalInterface
  public interface Function<Employee> {
    void apply(Stream<Employee> items);
  }

}
