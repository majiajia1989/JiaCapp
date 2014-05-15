package com.e1858.protocol;

import java.io.Serializable;
import java.util.Date;

import com.google.gson.annotations.Expose;

public class Notice implements Serializable{

	@Expose
	private long id;
	@Expose
	private String title;
	@Expose
	private String content;
	@Expose
	private String url;
	@Expose
	private String begin;
	@Expose
	private String end;

	
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
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getBegin() {
		return begin;
	}
	public void setBegin(String begin) {
		this.begin = begin;
	}
	public String getEnd() {
		return end;
	}
	public void setEnd(String end) {
		this.end = end;
	}

}
