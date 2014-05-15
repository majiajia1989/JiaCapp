package com.e1858.protocol;

import java.io.Serializable;

import com.google.gson.annotations.Expose;

public class LibGroup implements Serializable{

	@Expose
	private long id;
	@Expose
	private String name;
	@Expose
	private long picture;
	@Expose
	private int srcMode;
	@Expose
	private String srcUrl;
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
	
	public long getPicture() {
		return picture;
	}
	public void setPicture(long picture) {
		this.picture = picture;
	}
	public int getSrcMode() {
		return srcMode;
	}
	public void setSrcMode(int srcMode) {
		this.srcMode = srcMode;
	}
	public String getSrcUrl() {
		return srcUrl;
	}
	public void setSrcUrl(String srcUrl) {
		this.srcUrl = srcUrl;
	}
	

	

}
