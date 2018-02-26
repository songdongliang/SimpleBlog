package com.lvwang.osf.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lvwang.osf.mappers.NotificationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.lvwang.osf.pojo.Event;
import com.lvwang.osf.pojo.Notification;
import com.lvwang.osf.pojo.User;
import com.lvwang.osf.util.Dic;
import org.springframework.util.StringUtils;

@Service("notificationService")
public class NotificationService extends BaseService<Notification> {

	private static final String NOTIFY_KEY = "NOTIFICATION_";

	@Autowired
	@Qualifier("userService")
	private UserService userService;
	
	@Autowired
	@Qualifier("eventService")
	private EventService eventService;
	@Autowired
	private RedisService redisService;

	@Autowired
	private NotificationMapper notificationMapper;
	
	/**
	 * save notification
	 * @param notification
	 * @return notification id
	 */
	public int doNotify(Notification notification){
		int id = super.save(notification);
		refreshNotification(notification);
		return id;
	}
	
	public Map<String, Long> getNotificationsCount(int userId){
		final Map<String, Long> notifications = new HashMap<>();
		if (!redisService.containsKey(NOTIFY_KEY + userId)){
			initNotification(notifications);
			refreshNotifications(userId, notifications);
		} else{
			for(String key: redisService.hkeys(NOTIFY_KEY + userId)){
				notifications.put(key, Long.parseLong(redisService.hget(NOTIFY_KEY + userId, key)));
			}
		}
		return notifications;
	}

	private void refreshNotifications(int userId, Map<String, Long> notifications){
		List<Map<String, Number>> notificationsCount = notificationMapper.getNotificationsCount(userId);
		if(notificationsCount != null && notificationsCount.size() != 0) {
			for(Map<String, Number> notifyTypeCount: notificationsCount) {
				notifications.put(Dic.toNotifyTypeDesc((Integer)notifyTypeCount.get("notifyType")), (Long)notifyTypeCount.get("count"));
			}
		}
		for (Map.Entry<String,Long> entry : notifications.entrySet()) {
			redisService.hset(NOTIFY_KEY + userId,entry.getKey(),String.valueOf(entry.getValue()));
		}
	}

	private void initNotification(Map<String, Long> notifications){
		notifications.put("comment", 0L);
		notifications.put("comment_reply", 0L);
		notifications.put("follow", 0L);
		notifications.put("like", 0L);
		notifications.put("system", 0L);
	}
	
	public void refreshNotification(Notification notification){
		String count = redisService.hget(NOTIFY_KEY + notification.getNotifiedUser()
				, Dic.toNotifyTypeDesc(notification.getNotifyType()));
		if (StringUtils.isEmpty(count)) {
			count = "0";
		}
		redisService.hset(NOTIFY_KEY + notification.getNotifiedUser()
				, Dic.toNotifyTypeDesc(notification.getNotifyType())
				, String.valueOf(Long.parseLong(count) + 1));
	}
	
	public List<Notification> getNotifications(int userId, int notifyType){
		
		List<Notification> notifications  = null;
		
		if(notifyType == Dic.NOTIFY_TYPE_COMMENT) {
			notifications = notificationMapper.getNotificationsOfTypes(userId
					, Arrays.asList(Dic.NOTIFY_TYPE_COMMENT,Dic.NOTIFY_TYPE_COMMENT_REPLY));
		} else {
			notifications = notificationMapper.getNotificationsOfType(userId, notifyType);
		}
		
		if(notifications != null) {
			for(Notification notification : notifications){
				User user = userService.findById(notification.getNotifier());
				notification.setNotifierName(user.getUserName());
				notification.setNotifierAvatar(user.getUserAvatar());
				
				Event event = eventService.getEvent(notification.getObjectType(), notification.getObjectId());
				
				String objectTitle = null;
				if(Dic.OBJECT_TYPE_POST == notification.getObjectType()) {
					objectTitle = event.getTitle();
				} else if(Dic.OBJECT_TYPE_ALBUM == notification.getObjectType()){
					objectTitle = event.getSummary();
				} else if(Dic.OBJECT_TYPE_SHORT_POST == notification.getObjectType()) {
					objectTitle = event.getSummary();
				} 
				if(objectTitle != null){
					int len = objectTitle.length();
					if(len > 20) {
						objectTitle = objectTitle.substring(0, 20);
					}
				}
				notification.setObjectTitle(objectTitle);
			}
		}
		return notifications;
	}
}
