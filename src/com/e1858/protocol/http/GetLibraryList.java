package com.e1858.protocol.http;

import com.google.gson.annotations.Expose;

public class GetLibraryList extends PacketRequest {

	@Expose
	private long group;
	@Expose
	private int direction;
	@Expose
	private long id;
	@Expose
	private int cnt;

	public GetLibraryList(){
		setCmd(HttpDefine.GET_LIBRARY_LIST);
	}

	public long getGroup() {
		return group;
	}

	public void setGroup(long group) {
		this.group = group;
	}

	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getCnt() {
		return cnt;
	}

	public void setCnt(int cnt) {
		this.cnt = cnt;
	}

	
	
	
}
