package com.e1858.protocol;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.Expose;

public class Ejournal implements Serializable{

	@Expose
	private  long id;
	@Expose
	private  String title;
	@Expose
	private List<MMSAttach> attachs;
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
	public List<MMSAttach> getAttachs() {
		return attachs;
	}
	public void setAttachs(List<MMSAttach> attachs) {
		this.attachs = attachs;
	}
	
	
}
