package com.e1858.protocol;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.Expose;

public class Course implements Serializable{


	@Expose
	private long id;
	@Expose
	private String name;
	@Expose
	private String teacher;
	@Expose
	private List<CourseTimes> courseTimes;
	
	private String  times="";
	
	private boolean  addornot=false;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTeacher() {
		return teacher;
	}
	public void setTeacher(String teacher) {
		this.teacher = teacher;
	}
	public List<CourseTimes> getCourseTimes() {
		return courseTimes;
	}
	public void setCourseTimes(List<CourseTimes> coursesTimes) {
		this.courseTimes = coursesTimes;
	}
	public boolean isAddornot() {
		return addornot;
	}
	public void setAddornot(boolean addornot) {
		this.addornot = addornot;
	}
	public String getTimes() {
		return times;
	}
	public void setTimes(String times) {
		this.times = times;
	}
	
	
	
}
