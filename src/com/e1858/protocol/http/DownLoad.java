package com.e1858.protocol.http;

import com.google.gson.annotations.Expose;

public class DownLoad extends PacketRequest {

	@Expose
	private long	id;

	public DownLoad(){
		setCmd(HttpDefine.DOWNLOAD);
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
}
