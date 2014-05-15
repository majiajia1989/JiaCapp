package com.e1858.protocol.http;

import com.e1858.protocol.Library;
import com.google.gson.annotations.Expose;

public class GetLibraryResp extends PacketResp {

	@Expose
	private Library library;
	public GetLibraryResp(){
		setCmd(HttpDefine.GET_LIBRARY_RESP);
	}
	public Library getLibrary() {
		return library;
	}
	public void setLibrary(Library library) {
		this.library = library;
	}
	
}
