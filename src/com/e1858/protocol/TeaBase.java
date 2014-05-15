package com.e1858.protocol;

import java.io.Serializable;

import com.google.gson.annotations.Expose;

public class TeaBase implements Serializable{

	@Expose
	private long id;
	
	@Expose
	private String name;

	private boolean isSelected=false;
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

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}
	
	
}
