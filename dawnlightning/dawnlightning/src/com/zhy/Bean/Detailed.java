package com.zhy.Bean;

import java.util.ArrayList;
import java.util.List;

public class Detailed {
	private String uid;
	private String age;
	private String usename;
	private String datetime;
	private String subject;
	private String content;
	private List<Pics> pics;
	private List<Comment> comment;
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public String getUsename() {
		return usename;
	}
	public void setUsename(String usename) {
		this.usename = usename;
	}
	public String getDatetime() {
		return datetime;
	}
	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public List<Pics> getPics() {
		return pics;
	}
	public void setPics(List<Pics> list) {
		this.pics = list;
	}
	public List<Comment> getComment() {
		return comment;
	}
	public void setComment(List<Comment> list) {
		this.comment = list;
	}
	public Detailed(String uid, String age, String usename, String datetime,
			String subject, String content, List<Pics> pics,
			ArrayList<Comment> comment) {
		super();
		this.uid = uid;
		this.age = age;
		this.usename = usename;
		this.datetime = datetime;
		this.subject = subject;
		this.content = content;
		this.pics = pics;
		this.comment = comment;
	}
	public Detailed() {
		// TODO 自动生成的构造函数存根
	}
	@Override
	public String toString() {
		return "Detailed [uid=" + uid + ", age=" + age + ", usename=" + usename
				+ ", datetime=" + datetime + ", subject=" + subject
				+ ", content=" + content + ", pics=" + pics + ", comment="
				+ comment + "]";
	}
	
}
