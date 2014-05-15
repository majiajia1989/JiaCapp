package com.e1858.protocol;

import java.io.Serializable;

import com.google.gson.annotations.Expose;

public class StuBase implements Serializable{

	@Expose
	private long id;
	
	@Expose
	private String name;

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
	
}
