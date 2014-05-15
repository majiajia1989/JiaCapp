package com.e1858.protocol;

import java.io.Serializable;

import com.e1858.rssparse.Uri;
import com.google.gson.annotations.Expose;

public class LibBase implements Serializable{

	@Expose
	private long id;
	@Expose
	private long groupID;
	@Expose
	private String title;
	@Expose
	private String issuedDate;
	@Expose
	private String author;
	@Expose
	private long picture;
	
	private String link_url="";
	
	private String content="";
	
	private boolean hasMore=false;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public long getGroupID() {
		return groupID;
	}
	public void setGroupID(long groupID) {
		this.groupID = groupID;
	}
	public String getIssuedDate() {
		return issuedDate;
	}
	public void setIssuedDate(String issuedDate) {
		this.issuedDate = issuedDate;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public long getPicture() {
		return picture;
	}
	public void setPicture(long picture) {
		this.picture = picture;
	}
	public boolean isHasMore() {
		return hasMore;
	}
	public void setHasMore(boolean hasMore) {
		this.hasMore = hasMore;
	}
	public String getLink_url() {
		return link_url;
	}
	public void setLink_url(String link_url) {
		this.link_url = link_url;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	

	
	
}
