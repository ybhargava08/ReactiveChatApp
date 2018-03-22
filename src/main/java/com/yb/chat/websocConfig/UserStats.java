package com.yb.chat.websocConfig;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.StampedLock;
import java.util.stream.Collectors;

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
			System.out.println("after incrementing list: "+userList);
		}finally {
			lock.unlockWrite(stamp);
		}
	}
	
	private static void decrementUserList(MessageBean bean) {
		long stamp = lock.writeLock();
		long uniqueID = bean.getUniqueId();
		System.out.println("unique id to remove: "+uniqueID);
		
		try {
			userList = userList.stream().
					filter(userstatbean -> (uniqueID != userstatbean.getUniqueId())).collect(Collectors.toList());
			System.out.println("after decrementing list: "+userList);
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
		System.out.println("setting list in bean : "+bean.getUserstats());
		}finally {
			lock.unlockRead(stamp);
		}
		return bean;
	}
	
}
