package com.springboot.manage_users.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="profile_img")
public class ProfileImg {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String user_id;
	private String file_name;
	private String file_type;
	private String file_size;
	private byte[] grp_data;
	private LocalDateTime created_at;
	private LocalDateTime updated_at;
	
	public ProfileImg() {
		super();
	}
	public ProfileImg(int id, String user_id, String file_name, String file_type, String file_size, byte[] grp_data,
			LocalDateTime created_at, LocalDateTime updated_at) {
		super();
		this.id = id;
		this.user_id = user_id;
		this.file_name = file_name;
		this.file_type = file_type;
		this.file_size = file_size;
		this.grp_data = grp_data;
		this.created_at = created_at;
		this.updated_at = updated_at;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getFile_name() {
		return file_name;
	}
	public void setFile_name(String file_name) {
		this.file_name = file_name;
	}
	public String getFile_type() {
		return file_type;
	}
	public void setFile_type(String file_type) {
		this.file_type = file_type;
	}
	public String getFile_size() {
		return file_size;
	}
	public void setFile_size(String file_size) {
		this.file_size = file_size;
	}
	public byte[] getGrp_data() {
		return grp_data;
	}
	public void setGrp_data(byte[] grp_data) {
		this.grp_data = grp_data;
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
	
}
