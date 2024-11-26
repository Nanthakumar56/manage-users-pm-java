package com.springboot.manage_users.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="users")
public class Users {
	
	@Id
	private String userid;
	private String name;
	private String first_name;
	private String middle_name;
	private String last_name;
	private String email;
	private String phone;
	private String gender;
	private String username;
	private String password;
	private String employee_id;
	private String role;
	private String address;
	private String state;
	private String country;
	private LocalDateTime created_at;
	private LocalDateTime updated_at;
	private String status;
	private String profile;
	
	public Users() {
		super();
	}
	public Users(String userid, String first_name, String middle_name, String last_name, String name, String email,
			String phone, String gender, String username, String password, String employee_id, String role,
			String address, String state, String country, LocalDateTime created_at, LocalDateTime updated_at,
			String status, String profile) {
		super();
		this.userid = userid;
		this.first_name = first_name;
		this.middle_name = middle_name;
		this.last_name = last_name;
		this.name = name;
		this.email = email;
		this.phone = phone;
		this.gender = gender;
		this.username = username;
		this.password = password;
		this.employee_id = employee_id;
		this.role = role;
		this.address = address;
		this.state = state;
		this.country = country;
		this.created_at = created_at;
		this.updated_at = updated_at;
		this.status = status;
		this.profile = profile;
	}
	
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getFirst_name() {
		return first_name;
	}
	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}
	public String getMiddle_name() {
		return middle_name;
	}
	public void setMiddle_name(String middle_name) {
		this.middle_name = middle_name;
	}
	public String getLast_name() {
		return last_name;
	}
	public void setLast_name(String last_name) {
		this.last_name = last_name;
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
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmployee_id() {
		return employee_id;
	}
	public void setEmployee_id(String employee_id) {
		this.employee_id = employee_id;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public LocalDateTime getCreated_at() {
		return created_at;
	}
	public void setCreated_at(LocalDateTime created_at) {
		this.created_at = created_at;
	}
	public LocalDateTime getUpdated_at() {
		return updated_at;
	}
	public void setUpdated_at(LocalDateTime updated_at) {
		this.updated_at = updated_at;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getProfile() {
		return profile;
	}
	public void setProfile(String profile) {
		this.profile = profile;
	}
}
