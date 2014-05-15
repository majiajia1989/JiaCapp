package com.e1858.protocol;

import java.io.Serializable;

import com.google.gson.annotations.Expose;

public class EjournalBase implements Serializable{

	
	@Expose
	private long id;
	@Expose
	private String title;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}

	
}
