package com.github.zesetup.vaadinspringbillet.dao;

import com.github.zesetup.vaadinspringbillet.model.Employee;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface EmployeeRepository extends JpaRepository<Employee, Long> {

 @Query("select e from Employee e where name like %?1% or surname like %?1%")
 List<Employee> findWithFilter(String filter, Pageable pageable);

  @Query("select count(e) from Employee e where name like %?1% or surname like %?1%")
  long countWithFilter(String  filter);

}

