package com.e1858.protocol.http;

public class EditTeacherInfoResp extends PacketResp {

	public EditTeacherInfoResp(){
		setCmd(HttpDefine.EDIT_TEACHERINFO_RESP);
	}
}
