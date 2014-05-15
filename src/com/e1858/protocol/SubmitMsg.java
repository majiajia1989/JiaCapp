package com.e1858.protocol;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.Expose;

public class SubmitMsg implements Serializable{

	@Expose
	private long id;
	@Expose
	private int mode;
	@Expose
	private List<Long> teaDeps;
	@Expose
	private List<Long> teaPros;
	@Expose
	private List<Long> stuDeps;
	@Expose
	private List<Long> stuClasses;
	@Expose
	private List<Long> users;
	@Expose
	private int contentType;
	@Expose
	private int contentCode;
	@Expose
	private String content;
	@Expose
	private long module;
	
	@Expose
	private String time;
	
	
	private String receiver="";
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
	public int getMode() {
		return mode;
	}
	public void setMode(int mode) {
		this.mode = mode;
	}

	
	public List<Long> getTeaPros() {
		return teaPros;
	}
	public List<Long> getUsers() {
		return users;
	}
	public List<Long> getTeaDeps() {
		return teaDeps;
	}
	public void setTeaDeps(List<Long> teaDeps) {
		this.teaDeps = teaDeps;
	}
	public List<Long> getStuDeps() {
		return stuDeps;
	}
	public void setStuDeps(List<Long> stuDeps) {
		this.stuDeps = stuDeps;
	}
	public List<Long> getStuClasses() {
		return stuClasses;
	}
	public void setStuClasses(List<Long> stuClasses) {
		this.stuClasses = stuClasses;
	}
	public void setTeaPros(List<Long> teaPros) {
		this.teaPros = teaPros;
	}
	public void setUsers(List<Long> users) {
		this.users = users;
	}
	public int getContentType() {
		return contentType;
	}
	public void setContentType(int contentType) {
		this.contentType = contentType;
	}
	public int getContentCode() {
		return contentCode;
	}
	public void setContentCode(int contentCode) {
		this.contentCode = contentCode;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public long getModule() {
		return module;
	}
	public void setModule(long module) {
		this.module = module;
	}
	public String getReceiver() {
		return receiver;
	}
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	
	
}
