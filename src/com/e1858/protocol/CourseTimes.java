package com.e1858.protocol;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.Expose;

public class CourseTimes implements Serializable{


	@Expose
	private long id;
	
	@Expose
	private String classroom;
	
	@Expose
	private List<Long> weeks;
	
	@Expose
	private int weekday;
	
	@Expose
	private int start;
	
	
	@Expose
	private int end;
	
	

	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public List<Long> getWeeks() {
		return weeks;
	}
	public void setWeeks(List<Long> weeks) {
		this.weeks = weeks;
	}
	public String getClassroom() {
		return classroom;
	}
	public void setClassroom(String classroom) {
		this.classroom = classroom;
	}
	public int getWeekday() {
		return weekday;
	}
	public void setWeekday(int weekday) {
		this.weekday = weekday;
	}
	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	public int getEnd() {
		return end;
	}
	public void setEnd(int end) {
		this.end = end;
	}
	
	
	
	
	
}
