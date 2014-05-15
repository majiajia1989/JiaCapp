package com.e1858.protocol;
import java.io.Serializable;

import com.google.gson.annotations.Expose;

/**
 * 资讯信息
 * @author jia
 *
 */
public class Library implements Serializable{

	
	@Expose
	private long id;
	@Expose
	private long groupID;
	@Expose
	private String title;
	@Expose
	private String content;
	@Expose
	private int contentCode;
	@Expose
	private String author;
	@Expose
	private String publisher;
	@Expose
	private String summary;	
	@Expose
	private long picture;
	@Expose
	private long attach;
	@Expose
	private String isUrl;
	@Expose
	private String issuedDate;


	
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getGroupID() {
		return groupID;
	}
	public void setGroupID(long groupID) {
		this.groupID = groupID;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getIssuedDate() {
		return issuedDate;
	}
	public void setIssuedDate(String issuedDate) {
		this.issuedDate = issuedDate;
	}
	public int getContentCode() {
		return contentCode;
	}
	public void setContentCode(int contentCode) {
		this.contentCode = contentCode;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getPublisher() {
		return publisher;
	}
	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public long getPicture() {
		return picture;
	}
	public void setPicture(long picture) {
		this.picture = picture;
	}

	
	public long getAttach() {
		return attach;
	}
	public void setAttach(long attach) {
		this.attach = attach;
	}
	public String getIsUrl() {
		return isUrl;
	}
	public void setIsUrl(String isUrl) {
		this.isUrl = isUrl;
	}
	
	
	
	
	
}
