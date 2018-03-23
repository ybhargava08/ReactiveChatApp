package com.yb.chat.websocConfig;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.StampedLock;
import java.util.stream.Collectors;

import reactor.core.publisher.UnicastProcessor;

public class UserStats {

	private static final StampedLock lock = new StampedLock();
	private static List<UserStatBean> userList = new ArrayList<UserStatBean>();
	
	public static MessageBean updateUserStats(MessageBean bean) {
		
		if(MsgType.Joined == bean.getMsgType()) {
			incrementUserList(bean);
		}else if(MsgType.Left==bean.getMsgType()) {
			decrementUserList(bean);
		}
		
		/*if(MsgType.Chat == bean.getMsgType()){
			return bean;
		}*/
		return getModifiedUserBean(bean);
	}
	
	private static void incrementUserList(MessageBean bean) {
		long stamp = lock.writeLock();
		try {
			UserStatBean usb = new UserStatBean(bean.getUserName(),bean.getUniqueId(),bean.getUserAvatarColor());
			userList.add(usb);
		}finally {
			lock.unlockWrite(stamp);
		}
	}
	
	private static void decrementUserList(MessageBean bean) {
		long stamp = lock.writeLock();
		long uniqueID = bean.getUniqueId();
		
		try {
			userList = userList.stream().
					filter(userstatbean -> (uniqueID != userstatbean.getUniqueId())).collect(Collectors.toList());
		}finally {
			lock.unlockWrite(stamp);
		}
	}
	
	private static MessageBean getModifiedUserBean(MessageBean bean) {
		/*long stamp = lock.tryOptimisticRead();
		if(!lock.validate(stamp)) {
		*/	long stamp = lock.readLock();
		//	System.out.println("got stamp after read: "+stamp);
		//}
		try {
		/*String allUsers = userList.stream().map(msg->msg.getUserName()).collect(Collectors.joining(","));*/
			//userList = userList.stream().sorted(Comparator.comparing(UserInfoBean::getName)).collect(Collectors.toList());
		bean.setUserstats(userList);
		}finally {
			lock.unlockRead(stamp);
		}
		return bean;
	}
	
	public static void sendChatBotWelcomeMsg(MessageBean msgbean,UnicastProcessor<MessageBean> messagePub) {
		  if(null!=msgbean && msgbean.getUserstats().size() == 1 && (MsgType.Joined == msgbean.getMsgType() 
				  || MsgType.Left == msgbean.getMsgType())) {
			  String chatBotMsg = null;
			  if(MsgType.Joined == msgbean.getMsgType()) {
				  chatBotMsg = "Welcome "+msgbean.getUserName()+" to Basic Group Chat . "
					  		+ "Seems you're the one only here . Hangon someone might join shortly";
			  }else if(MsgType.Left == msgbean.getMsgType()){
				  chatBotMsg = "Hey "+msgbean.getUserName()+" . "
					  		+ "Seems like all left . Hangon someone might join shortly";
			  }
			  
			  MessageBean chatbotbean = new MessageBean();
			  chatbotbean.setUniqueId(System.currentTimeMillis());
			  chatbotbean.setChatDate(System.currentTimeMillis());
			  
			  if(MsgType.Joined == msgbean.getMsgType()) {
				  chatbotbean.setMsgType(MsgType.ChatBotJoin);
			  }else if(MsgType.Left == msgbean.getMsgType()){
				  chatbotbean.setMsgType(MsgType.ChatBotLeave);
			  }  
			  chatbotbean.setUserName("ChatBot");
			  chatbotbean.setUserstats(userList);
			  chatbotbean.setIsChatBot(Boolean.TRUE);
			  messagePub.onNext(chatbotbean);
		  }
	}
	
	
	
}
