package com.yb.chat.websocConfig;

import org.springframework.stereotype.Component;

@Component
public class MessageBean {

	String userName;
	String chat;
	String msgType;
	
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
