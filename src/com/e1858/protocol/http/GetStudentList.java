package com.e1858.protocol.http;

import com.google.gson.annotations.Expose;

public class GetStudentList extends PacketRequest {

	
	@Expose
	private long classe;

	public GetStudentList(){
		setCmd(HttpDefine.GET_STUDENT_LIST);
	}

	public long getClasse() {
		return classe;
	}

	public void setClasse(long classe) {
		this.classe = classe;
	}
	
	

}
