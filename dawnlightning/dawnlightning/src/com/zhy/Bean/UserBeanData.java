package com.zhy.Bean;

import java.io.Serializable;

public class UserBeanData implements Serializable   {
	String space;
	String m_auth;
	public String getSpace() {
		return space;
	}
	public void setSpace(String space) {
		this.space = space;
	}
	public String getM_auth() {
		return m_auth;
	}
	public void setM_auth(String m_auth) {
		this.m_auth = m_auth;
	}
}
