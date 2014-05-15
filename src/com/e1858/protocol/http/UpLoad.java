package com.e1858.protocol.http;

import com.google.gson.annotations.Expose;

public class UpLoad extends PacketRequest {
	
	@Expose
	private int    type;
	@Expose
	private long   id;
	@Expose
	private String fileName;
	
	public UpLoad(){
		setCmd(HttpDefine.UPLOAD);
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	
	
}
