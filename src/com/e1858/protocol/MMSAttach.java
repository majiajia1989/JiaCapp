package com.e1858.protocol;

import java.io.Serializable;

import com.google.gson.annotations.Expose;

public class MMSAttach implements Serializable {
	@Expose
	private long id;
	@Expose
	private int type;
	@Expose
	private int sort;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getSort() {
		return sort;
	}
	public void setSort(int sort) {
		this.sort = sort;
	}
	
	
	
	
}
