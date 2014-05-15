package com.e1858.protocol;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;

public class DepBase implements Serializable{

	@Expose
	private long id;
	
	@Expose
	private String name;
	
	private boolean isSelected=false;
	
	private String  users="";
	
	private List<Long>   class_ids=new ArrayList<Long>();
	
	private List<Long>   tea_ids=new ArrayList<Long>();
	
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
	public String getUsers() {
		return users;
	}
	public void setUsers(String users) {
		this.users = users;
	}
	public List<Long> getClass_ids() {
		return class_ids;
	}
	public void setClass_ids(List<Long> class_ids) {
		this.class_ids = class_ids;
	}
	public List<Long> getTea_ids() {
		return tea_ids;
	}
	public void setTea_ids(List<Long> tea_ids) {
		this.tea_ids = tea_ids;
	}

	

	
	
}
