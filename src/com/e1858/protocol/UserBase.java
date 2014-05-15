package com.e1858.protocol;

import java.io.Serializable;

import com.google.gson.annotations.Expose;

public class UserBase implements Serializable {

	@Expose
	private long id;
	@Expose
	private String realName;
	@Expose
	private String nickname;
	@Expose
	private String userName;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	
}
