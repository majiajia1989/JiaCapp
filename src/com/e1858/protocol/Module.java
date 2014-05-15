package com.e1858.protocol;

import java.io.Serializable;

import com.e1858.common.ModuleInteger;
import com.e1858.common.SelectorType;
import com.google.gson.annotations.Expose;

public class Module implements Serializable{

	@Expose
	private long id;
	
	@Expose
	private String name;
	
	@Expose
	private long icon;
	
	private long home=ModuleInteger.MESSAGE;
	private int select=SelectorType.UNSELECTED;
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
	public long getIcon() {
		return icon;
	}
	public void setIcon(long icon) {
		this.icon = icon;
	}
	public long getHome() {
		return home;
	}
	public void setHome(long home) {
		this.home = home;
	}
	public int getSelect() {
		return select;
	}
	public void setSelect(int select) {
		this.select = select;
	}
	
	
	
	
}
