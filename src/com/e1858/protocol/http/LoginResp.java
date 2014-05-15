package com.e1858.protocol.http;

import com.e1858.protocol.School;
import com.e1858.protocol.SchoolCnf;
import com.e1858.protocol.Server;
import com.google.gson.annotations.Expose;

public class LoginResp  extends PacketResp{
	
	@Expose
	private long user;
	@Expose 
	private School school;
	@Expose
	private SchoolCnf schoolCnf;
	@Expose
	private Server server;
	
	@Expose
	private int  level;
	
	@Expose
	private long personID;
	
	public LoginResp(){
		setCmd(HttpDefine.LOGIN_RESP);
	}
	
	
	public Server getServer() {
		return server;
	}


	public void setServer(Server server) {
		this.server = server;
	}
	public long getUser() {
		return user;
	}
	public void setUser(long user) {
		this.user = user;
	}
	public School getSchool() {
		return school;
	}
	public void setSchool(School school) {
		this.school = school;
	}
	public SchoolCnf getSchoolCnf() {
		return schoolCnf;
	}
	public void setSchoolCnf(SchoolCnf schoolCnf) {
		this.schoolCnf = schoolCnf;
	}


	public int getLevel() {
		return level;
	}


	public void setLevel(int level) {
		this.level = level;
	}


	public long getPersonID() {
		return personID;
	}


	public void setPersonID(long personID) {
		this.personID = personID;
	}	
	
	
	
	
	
}
