package com.springboot.manage_users.dto;

import java.time.LocalDateTime;

public class UserDTO {
    private String userid;
    private String first_name;
    private String last_name;
    private String role;
    private String email;
    private LocalDateTime created_at;
    private String status;
    
    
	public UserDTO(String userid, String first_name, String last_name, String role, String email,
			LocalDateTime created_at, String status) {
		super();
		this.userid = userid;
		this.first_name = first_name;
		this.last_name = last_name;
		this.role = role;
		this.email = email;
		this.created_at = created_at;
		this.status = status;
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
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public LocalDateTime getCreated_at() {
		return created_at;
	}
	public void setCreated_at(LocalDateTime created_at) {
		this.created_at = created_at;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
    
    
}

