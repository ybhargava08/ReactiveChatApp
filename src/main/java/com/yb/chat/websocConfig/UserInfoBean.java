package com.yb.chat.websocConfig;

public class UserInfoBean {

	private long uniqueId;
	private String name;
	
	public UserInfoBean (long uniqueId , String name) {
		this.uniqueId = uniqueId;
		this.name = name;
	}
	public long getUniqueId() {
		return uniqueId;
	}
	
	public String getName() {
		return name;
	}
	
	 public String toString() {
		 return this.name+"-"+String.valueOf(this.uniqueId);
	 }
	
}
