package com.yb.chat.websocConfig;

import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class MessageBean {

	private String userName;
	private String chat;
	private MsgType msgType;
	private Long uniqueId;
	private Long chatDate;
	private Long typedTime;
	private List<UserStatBean> userstats;
	private String userAvatarColor;
	
	public String getUserAvatarColor() {
		return userAvatarColor;
	}
	public void setUserAvatarColor(String userAvatarColor) {
		this.userAvatarColor = userAvatarColor;
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
	public MsgType getMsgType() {
		return msgType;
	}
	public void setMsgType(MsgType msgType) {
		this.msgType = msgType;
	}
	public Long getTypedTime() {
		return typedTime;
	}
	public void setTypedTime(Long typedTime) {
		this.typedTime = typedTime;
	}
	public List<UserStatBean> getUserstats() {
		return userstats;
	}
	public void setUserstats(List<UserStatBean> userstats) {
		this.userstats = userstats;
	}
	
}

enum MsgType {
	Chat("Chat"),
	Joined("Joined"),
	Left("Left"),
	TypedInd("TypedInd");
	
	private String value;
	
	MsgType(String value) {
		this.value = value;
	}
	
	public String toString() {
		return this.value;
	}
}
