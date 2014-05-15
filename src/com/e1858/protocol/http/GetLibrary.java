package com.e1858.protocol.http;

import com.google.gson.annotations.Expose;

public class GetLibrary extends PacketRequest {

	@Expose
	private long library;
	public GetLibrary(){
		setCmd(HttpDefine.GET_LIBRARY);
	}
	public long getLibrary() {
		return library;
	}
	public void setLibrary(long library) {
		this.library = library;
	}
	
}
