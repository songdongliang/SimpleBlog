package com.lvwang.osf.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.lvwang.osf.mappers.NotificationMapper;
import com.lvwang.osf.model.Notification;
import com.lvwang.osf.util.Dic;

@Repository("notificationDao")
public class NotificationDAOImpl{

	private static final String TABLE = "osf_notifications";
	
	private static final String NOTIFY_KEY = "notification:";
	
	@Autowired
	private NotificationMapper notificationMapper;
	
	@Autowired
	@Qualifier("redisTemplate")
	private RedisTemplate<String, String> redisTemplate; 
	
	@Resource(name="redisTemplate")
	private HashOperations<String, String, Long> hashOps;
	

	public int save(final Notification notification) {
		notificationMapper.save(notification);
		return notification.getId();
	}

	public List<Notification> getNotificationsOfType(int user_id,
			int notify_type) {
		return notificationMapper.getNotificationsOfType(user_id, notify_type);
	}

	public List<Notification> getNotificationsOfTypes(int user_id,
			List<Integer> notify_types) {
		return notificationMapper.getNotificationsOfTypes(user_id, notify_types);
	}
	
	private void initNotification(Map<String, Long> notifications){
		notifications.put("comment", 0L);
		notifications.put("comment_reply", 0L);
		notifications.put("follow", 0L);
		notifications.put("like", 0L);
		notifications.put("system", 0L);
	}
	
	//刷新所有类型通知
	private void refreshNotifications(int user_id, Map<String, Long> notifications){
		List<Map<String, Number>> notify_type_counts = notificationMapper.getNotificationsCount(user_id);
		if(notify_type_counts != null && notify_type_counts.size() != 0) {
			for(Map<String, Number> notify_type_count: notify_type_counts) {
				notifications.put(Dic.toNotifyTypeDesc((Integer)notify_type_count.get("notify_type")), (Long)notify_type_count.get("count"));
			}
		}
		hashOps.putAll(NOTIFY_KEY + user_id, notifications);
	}
	
	public void refreshNotification(Notification notification) {
		long count = hashOps.get(NOTIFY_KEY+notification.getNotified_user(), Dic.toNotifyTypeDesc(notification.getNotify_type()));
		hashOps.put(NOTIFY_KEY+notification.getNotified_user(), Dic.toNotifyTypeDesc(notification.getNotify_type()), count+1);
	}
	
	public Map<String, Long> getNotificationsCount(int user_id) {
		final Map<String, Long> notifications = new HashMap<String, Long>();
		
		if(!redisTemplate.hasKey(NOTIFY_KEY+user_id)){
			initNotification(notifications);
			refreshNotifications(user_id, notifications);
		} else{
			for(String key: hashOps.keys(NOTIFY_KEY + user_id)){
				notifications.put(key, hashOps.get(NOTIFY_KEY + user_id, key));
			}
		}
		return notifications;
	}

}
