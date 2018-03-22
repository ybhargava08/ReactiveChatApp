package com.yb.chat.websocConfig;

public class UserStatBean {

	private String name;
	private Long uniqueId;
	private String avatarColor;
	
	public UserStatBean(String _name,Long _uniqueId,String _avatarColor) {
		this.name = _name;
		this.uniqueId = _uniqueId;
		this.avatarColor = _avatarColor;
	}
	
	public String getName() {
		return name;
	}
	
	public Long getUniqueId() {
		return uniqueId;
	}

	public String getAvatarColor() {
		return avatarColor;
	}	
	
	public String toString() {
		return this.name + " "+this.getUniqueId();
	}
}
