package com.e1858.protocol;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.Expose;

public class SchoolCnf implements Serializable{

	@Expose
	private List<Module> modules;
	@Expose
	private TermCnf termCnf;
	public List<Module> getModules() {
		return modules;
	}
	public void setModules(List<Module> modules) {
		this.modules = modules;
	}
	public TermCnf getTermCnf() {
		return termCnf;
	}
	public void setTermCnf(TermCnf termCnf) {
		this.termCnf = termCnf;
	}
	

}
