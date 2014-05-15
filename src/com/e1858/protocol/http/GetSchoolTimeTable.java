package com.e1858.protocol.http;

import com.google.gson.annotations.Expose;

public class GetSchoolTimeTable extends PacketRequest {


	@Expose
	private long classe;

	public GetSchoolTimeTable(){
		setCmd(HttpDefine.GET_SCHOOLTIMETABLE);
	}

	public long getClasse() {
		return classe;
	}

	public void setClasse(long classe) {
		this.classe = classe;
	}

	

	
	
}
