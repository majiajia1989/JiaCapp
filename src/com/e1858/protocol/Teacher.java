package com.e1858.protocol;

import java.io.Serializable;

import com.google.gson.annotations.Expose;
public class Teacher implements Serializable{
	@Expose
	private long id;
	@Expose
	private long school;
	@Expose
	private String schoolName;
	@Expose
	private long depID;
	@Expose
	private String depName;
	@Expose
	private String code;
	@Expose
	private String name;
	@Expose
	private String gender;
	@Expose
	private String mobile;
	@Expose
	private String email;
	@Expose
	private String jobTitle;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
	public long getSchool() {
		return school;
	}
	public void setSchool(long school) {
		this.school = school;
	}
	public String getSchoolName() {
		return schoolName;
	}
	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}
	public long getDepID() {
		return depID;
	}
	public void setDepID(long depID) {
		this.depID = depID;
	}
	public String getDepName() {
		return depName;
	}
	public void setDepName(String depName) {
		this.depName = depName;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getJobTitle() {
		return jobTitle;
	}
	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}
	
	
	
	
	
	
	
}
