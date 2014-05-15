package com.e1858.protocol.http;

import java.util.List;

import com.e1858.protocol.LibBase;
import com.google.gson.annotations.Expose;

public class GetLibraryListResp extends PacketResp {

	@Expose
	private List<LibBase> librarys;
	public GetLibraryListResp(){
		setCmd(HttpDefine.GET_LIBRARY_LIST_RESP);
	}
	public List<LibBase> getLibrarys() {
		return librarys;
	}
	public void setLibrarys(List<LibBase> librarys) {
		this.librarys = librarys;
	}
	
}
