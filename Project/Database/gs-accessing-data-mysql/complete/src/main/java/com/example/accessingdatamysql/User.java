package com.example.accessingdatamysql;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

// A class to define the users
@Entity // This tells Hibernate to make a table out of this class
public class User {
	@Id // this bean tells spring it is an id (useful for next bean)
	@GeneratedValue(strategy=GenerationType.AUTO) // this bean automatically generates an id; should be unnecesary once
												  // the openid is passed to the database from the login app
	private Integer id; 

	private String email;

	private Roles role;

	protected User() {}

	// constructor (not used at the moment)
	public User(String email, Roles role) {
		this.email = email; 
		this.role = role;
	}
	// a method to read the id
	public Integer getId() {
		return id;
	}
	// a method to set the id
	public void setId(Integer id) {
		this.id = id;
	}
	// a method to read the email
	public String getEmail() {
		return email;
	}
	// a method to set the email
	public void setEmail(String email) {
		this.email = email;
	}
	// a method to get the role
	public Roles getRole() {
		return role;
	}
	// a method to set the role
	public void setRole(Roles role) {
		this.role = role;
	}
}
