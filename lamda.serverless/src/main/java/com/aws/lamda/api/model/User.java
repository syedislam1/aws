package com.aws.lamda.api.model;

import com.google.gson.Gson;

public class User {
	private String name;
	private String email;
	private String password;

	public User(String name, String email, String password) {
		this.name=  name;
		this.email =email;
		this.password=password;
		
	}
	
	public User(String json) {
		Gson gson = new Gson();
		User tempUser = gson.fromJson(json, User.class);
		this.name=tempUser.name;
		this.email =tempUser.email;
		this.password = tempUser.password;
		
	}
	
	
	public String toString() {
		return new Gson().toJson(this);
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}
	public void setEamil(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	
}
