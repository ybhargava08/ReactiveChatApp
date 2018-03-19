package com.yb.chat.websocConfig;

import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class MessageBean {

	private String userName;
	private String chat;
	private String msgType;
	private Long uniqueId;
	private Long chatDate;
	private List<UserInfoBean> allUsers;
	
	public List<UserInfoBean> getAllUsers() {
		return allUsers;
	}
	public void setAllUsers(List<UserInfoBean> allUsers) {
		this.allUsers = allUsers;
	}
	
	public Long getChatDate() {
		return chatDate;
	}
	public void setChatDate(Long chatDate) {
		this.chatDate = chatDate;
	}
	public Long getUniqueId() {
		return uniqueId;
	}
	public void setUniqueId(Long uniqueId) {
		this.uniqueId = uniqueId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getChat() {
		return chat;
	}
	public void setChat(String chat) {
		this.chat = chat;
	};
	
	public String toString() {
		return "bean of user: "+this.getUserName();
	}
	public String getMsgType() {
		return msgType;
	}
	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}
	
}
