package com.yb.chat.websocConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.StampedLock;
import java.util.stream.Collectors;

public class UserCountInfo {

	private static final StampedLock lock = new StampedLock();
	private static List<UserInfoBean> userList = new ArrayList<UserInfoBean>();
	
	public static MessageBean updateBeanWithCount(MessageBean bean) {
		
		if("Joined".equalsIgnoreCase(bean.getMsgType())) {
			incrementUserList(bean);
		}else if("Left".equalsIgnoreCase(bean.getMsgType())) {
			decrementUserList(bean);
		}
		
		if(("Chat").equalsIgnoreCase(bean.getMsgType())){
			return bean;
		}
		return getModifiedUserBean(bean);
	}
	
	private static void incrementUserList(MessageBean bean) {
		long stamp = lock.writeLock();
		try {
			UserInfoBean uib = new UserInfoBean(bean.getUniqueId(),bean.getUserName());
			userList.add(uib);
		}finally {
			lock.unlockWrite(stamp);
		}
	}
	
	private static void decrementUserList(MessageBean bean) {
		long stamp = lock.writeLock();
		long uniqueID = bean.getUniqueId();
		try {
			userList = userList.stream().
					filter(userinfo -> (uniqueID != userinfo.getUniqueId())).collect(Collectors.toList());
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
		bean.setAllUsers(userList);
		}finally {
			lock.unlockRead(stamp);
		}
		return bean;
	}
	
}
