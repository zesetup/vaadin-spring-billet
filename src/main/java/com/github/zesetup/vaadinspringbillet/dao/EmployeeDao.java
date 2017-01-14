package com.github.zesetup.vaadinspringbillet.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.github.zesetup.vaadinspringbillet.model.Employee;
import com.github.zesetup.vaadinspringbillet.ui.VaadinUI;

@Repository
public class EmployeeDao {
	private static final Logger logger = LoggerFactory.getLogger(EmployeeDao.class);

	@PersistenceContext
	private EntityManager entityManager;
	 
	public EntityManager getEntityManager() {
		return entityManager;
	}
	 
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	@Transactional(readOnly = true)
	public List<Employee> load(
			String sortField,
			Boolean isAsc,
			Integer recordsOffset,
			Integer recordsLimit,
			String fullSearch) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Employee> criteriaQuery = criteriaBuilder.createQuery( Employee.class );							
		Root<Employee> employeeRoot = criteriaQuery.from( Employee.class );
		if(fullSearch!=null) {
			criteriaQuery.where(fullSearchToPredicates(fullSearch, criteriaBuilder, criteriaQuery, employeeRoot));
		}
		if(isAsc == null) isAsc = true;
		if(sortField==null){
			if(isAsc){
				criteriaQuery.orderBy(criteriaBuilder.asc(employeeRoot.get("id")));	
			}else{
				criteriaQuery.orderBy(criteriaBuilder.desc(employeeRoot.get("id")));
			}
		} else {
			if(sortField.equals("name")){
				logger.info("name, asc="+isAsc);
				if(isAsc){
					criteriaQuery.orderBy(criteriaBuilder.asc(employeeRoot.get("name")));	
				}else{
					criteriaQuery.orderBy(criteriaBuilder.desc(employeeRoot.get("name")));
				}
			}
			if(sortField.equals("surname")){
				if(isAsc){
					criteriaQuery.orderBy(criteriaBuilder.asc(employeeRoot.get("surname")));	
				}else{
					criteriaQuery.orderBy(criteriaBuilder.desc(employeeRoot.get("surname")));
				}
			}
		}
		TypedQuery<Employee> typedQuery = entityManager.createQuery( criteriaQuery );
		if((recordsOffset!=null) && (recordsLimit!=null)) {
			typedQuery.setFirstResult(recordsOffset);
			typedQuery.setMaxResults(recordsLimit);
		}
		List<Employee> result = typedQuery.getResultList();		
		logger.info("** DEV result size:"+result.size()+" offset="+recordsOffset+" limit="+recordsLimit
				+" sort="+sortField);
		return result;
	}
	private Predicate[]  fullSearchToPredicates(
			String fullSearch,
			CriteriaBuilder criteriaBuilder,
			CriteriaQuery<?> criteriaQuery,
			Root<Employee> employeeRoot
			){									
		List<Predicate> predicateList = new ArrayList<Predicate>();	
		Predicate predicate = criteriaBuilder.like(
				criteriaBuilder.upper(employeeRoot.<String>get("name")), 
				"%"+fullSearch.toUpperCase()+"%");
		Predicate  predicate2 = criteriaBuilder.like(
				criteriaBuilder.upper(employeeRoot.<String>get("surname")), 
				"%"+fullSearch.toUpperCase()+"%");
		predicate = criteriaBuilder.or(predicate, predicate2);
		predicateList.add(predicate);
		Predicate[] predicates = new Predicate[predicateList.size()];
	    predicateList.toArray(predicates);
	    return predicates;
	}

}
