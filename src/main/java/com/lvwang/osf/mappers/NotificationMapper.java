package com.lvwang.osf.mappers;

import com.lvwang.osf.dao.NotificationDAO;
import com.lvwang.osf.model.Notification;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface NotificationMapper {
    int save(Notification notification);

    void delete(int id);

    Notification get(int notification_id);

    List<Notification> getAllOfUser(int user_id);

    List<Notification> getNotificationsOfType(int user_id, int notify_type);

    List<Notification> getNotificationsOfTypes(int user_id, @Param("notify_types")List<Integer> notify_types);

    List<Map<String, Number>> getNotificationsCount(int user_id);

    void refreshNotification(Notification notification);
}

