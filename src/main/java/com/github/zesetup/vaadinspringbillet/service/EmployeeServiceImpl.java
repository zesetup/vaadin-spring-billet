package com.github.zesetup.vaadinspringbillet.service;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.github.zesetup.vaadinspringbillet.dao.EmployeeDao;
import com.github.zesetup.vaadinspringbillet.dao.EmployeeRepository;
import com.github.zesetup.vaadinspringbillet.model.Employee;


@Component
@Transactional
public class EmployeeServiceImpl implements EmployeeService{

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
    public List<Employee> find(
            String sortField,
            Boolean isAsc,
            Integer recordsOffset,
            Integer recordsLimit,
            String fullSearch
            ) {
        return employeeDao.load(sortField, isAsc, recordsOffset, recordsLimit, fullSearch);
    }

    @Override
    public List<Employee> findAll(){
        return employeeRepository.findAll();
    }
    
    @Override
    public List<Employee> findViaJpaRepository(String name, String surname){
        List<Employee> empls = employeeRepository.findByNameIgnoringCaseContainingOrSurnameIgnoringCaseContaining(name, surname);
        logger.info(" name:"+name+" surname:"+surname+" empls size:"+empls.size());
        return empls;
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
}
