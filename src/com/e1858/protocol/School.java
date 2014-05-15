package com.e1858.protocol;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.Expose;

public class School implements Serializable{

	@Expose
	private long id;
	@Expose
	private String name;
	@Expose
	private List<Long> pics;
	
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
	public List<Long> getPics() {
		return pics;
	}
	public void setPics(List<Long> pics) {
		this.pics = pics;
	}

	
	
}
