package com.github.zesetup.vaadinspringbillet.model;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "employee")
public class Employee {		
	public void setSurname(String surname) {
		this.surname = surname;
	}
	
	@Id
	@GeneratedValue
	private Long id;

	@Column(name = "login", unique = true, nullable = true, length = 32)
	private String login;

	@Column(name = "name", nullable = false, length = 32)
	private String name;

	@Column(name = "surname", nullable = false, length = 32)
	private String surname;

	@Column(name = "position", nullable = true, length = 64)
	private String position; 

	@Column(name = "isActive")
	private Boolean isActive = true;

	@Column(name = "notes", unique = false,  nullable = true, length = 256)
	private String notes;
	

	public Employee(){
	}

	public Employee(String login, String name, String surname, String position){
		this.login = login;
		this.name = name;
		this.surname = surname;
		this.position = position;
	}

	public Long getId() {
		return id;
	}

	public String getLogin() {
		return login+"";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}
	@Override
	public String  toString() {
		return "login: "+login+"; name: " +name+"; surname: "+surname+"; notes: "+notes;
	}
	
	public Boolean equals(Employee e) {
	  if(e.getId().equals(id)) {
	    return true;
	  } else {
	    return false;
	  }
	}
}
