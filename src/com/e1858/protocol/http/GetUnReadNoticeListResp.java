package com.e1858.protocol.http;

import java.util.List;

import com.e1858.protocol.NoticeBase;
import com.google.gson.annotations.Expose;

public class GetUnReadNoticeListResp extends PacketResp {

	@Expose
	private List<NoticeBase> notices;
	public GetUnReadNoticeListResp(){
		setCmd(HttpDefine.GET_UNREADNOTICE_LIST_RESP);
	}
	public List<NoticeBase> getNotices() {
		return notices;
	}
	public void setNotices(List<NoticeBase> notices) {
		this.notices = notices;
	}
	

}
