package com.springboot.manage_users.dto;

import java.time.LocalDateTime;

public class UserInformationDto {
	private String userid;
	private String first_name;
	private String last_name;
	private String email;
	private String phone;
	private String username;
	private String password;
	private String employee_id;
	private String role;
	private String organization_unit;
	private String department;
	private String designation;
	private LocalDateTime created_at;
	private LocalDateTime last_login;
	private String status;
	private byte[] file;
	
	
	public UserInformationDto(String userid, String first_name, String last_name, String email, String phone,
			String username, String password, String employee_id, String role, String organization_unit,
			String department, String designation, LocalDateTime created_at, LocalDateTime last_login, String status,
			byte[] file) {
		super();
		this.userid = userid;
		this.first_name = first_name;
		this.last_name = last_name;
		this.email = email;
		this.phone = phone;
		this.username = username;
		this.password = password;
		this.employee_id = employee_id;
		this.role = role;
		this.organization_unit = organization_unit;
		this.department = department;
		this.designation = designation;
		this.created_at = created_at;
		this.last_login = last_login;
		this.status = status;
		this.file = file;
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
	public String getLast_name() {
		return last_name;
	}
	public void setLast_name(String last_name) {
		this.last_name = last_name;
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
	public String getOrganization_unit() {
		return organization_unit;
	}
	public void setOrganization_unit(String organization_unit) {
		this.organization_unit = organization_unit;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getDesignation() {
		return designation;
	}
	public void setDesignation(String designation) {
		this.designation = designation;
	}
	public LocalDateTime getCreated_at() {
		return created_at;
	}
	public void setCreated_at(LocalDateTime created_at) {
		this.created_at = created_at;
	}
	public LocalDateTime getLast_login() {
		return last_login;
	}
	public void setLast_login(LocalDateTime last_login) {
		this.last_login = last_login;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public byte[] getFile() {
		return file;
	}
	public void setFile(byte[] file) {
		this.file = file;
	}
	
	
}
