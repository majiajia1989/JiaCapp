package com.e1858.protocol.http;

import com.google.gson.annotations.Expose;

public class CheckVersionResp extends PacketResp {

	
	@Expose
	private boolean exists;
	@Expose
	private String url;
	
	public CheckVersionResp(){
		setCmd(HttpDefine.CHECK_VERSION_RESP);
	}

	public boolean isExists() {
		return exists;
	}

	public void setExists(boolean exists) {
		this.exists = exists;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	
	
	
}
