package com.springboot.manage_users.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "documents")
public class Documents {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private String id;
	private String userid;
	private String filename;
	private String filetype;
	private String filesize;
	private byte[] filedata;
	private LocalDateTime createdat;
	
	public Documents() {
		super();
	}
	public Documents(String id, String userid, String filename, String filetype, String filesize, byte[] filedata,
			LocalDateTime createdat) {
		super();
		this.id = id;
		this.userid = userid;
		this.filename = filename;
		this.filetype = filetype;
		this.filesize = filesize;
		this.filedata = filedata;
		this.createdat = createdat;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public String getFiletype() {
		return filetype;
	}
	public void setFiletype(String filetype) {
		this.filetype = filetype;
	}
	public String getFilesize() {
		return filesize;
	}
	public void setFilesize(String filesize) {
		this.filesize = filesize;
	}
	public byte[] getFiledata() {
		return filedata;
	}
	public void setFiledata(byte[] filedata) {
		this.filedata = filedata;
	}
	public LocalDateTime getCreatedat() {
		return createdat;
	}
	public void setCreatedat(LocalDateTime createdat) {
		this.createdat = createdat;
	}
}
