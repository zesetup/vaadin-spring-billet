package com.github.zesetup.vaadinspringbillet.service;
import com.vaadin.flow.data.provider.SortDirection;
import com.github.zesetup.vaadinspringbillet.dao.EmployeeDao;
import com.github.zesetup.vaadinspringbillet.dao.EmployeeRepository;
import com.github.zesetup.vaadinspringbillet.model.Employee;
import com.vaadin.flow.data.provider.QuerySortOrder;
import java.util.ArrayList;
import java.util.Collections;
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
  public Employee findOne(Integer employeeId) {
    return employeeRepository.findById(employeeId).get();
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
      String filterText) {
    List<Employee> result = new ArrayList<>();
    if (sortOrders.isEmpty()) {
      PageRequest pageable = new PageRequest(offset / limit, limit );
      result =
          employeeRepository.findWithFilter(filterText, pageable);
    } else {
      final int pageSize = limit;
      int startPage = (int) Math.floor((double) offset / pageSize);
      int endPage = (int) Math.floor((double) (offset + pageSize - 1) / pageSize);
      if (startPage != endPage) {
        PageRequest pageable = new PageRequest(offset / limit, limit, getSort(sortOrders));
        List<Employee> page0 =  employeeRepository.findWithFilter(filterText, pageable);
        page0 = page0.subList(offset % pageSize, page0.size());
        List<Employee> page1 = employeeRepository.findWithFilter(filterText,
            new PageRequest(endPage, pageSize, getSort(sortOrders)));
        page1 = page1.subList(0, limit - page0.size());
        result = new ArrayList<Employee>(page0);
        result.addAll(page1);
      } else {
        result = employeeRepository.findWithFilter(filterText,
            new PageRequest(endPage, pageSize, getSort(sortOrders)));
      }
    }
    result = Collections.unmodifiableList(result);
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
