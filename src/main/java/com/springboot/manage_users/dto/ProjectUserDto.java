package com.springboot.manage_users.dto;


public class ProjectUserDto {
	 private String userid;
	    private String first_name;
	    private String last_name;
	    private String role;
	    private String status;
	    private byte[] file;
		
	    public ProjectUserDto(String userid, String first_name, String last_name, String role, String status,
				byte[] file) {
			super();
			this.userid = userid;
			this.first_name = first_name;
			this.last_name = last_name;
			this.role = role;
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

		public String getRole() {
			return role;
		}

		public void setRole(String role) {
			this.role = role;
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
