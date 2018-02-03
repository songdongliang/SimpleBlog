package com.lvwang.osf.mappers;

import com.github.abel533.mapper.Mapper;
import com.lvwang.osf.pojo.Notification;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface NotificationMapper extends Mapper<Notification>{

    List<Notification> getNotificationsOfType(int userId, int notifyType);

    List<Notification> getNotificationsOfTypes(int userId, @Param("notify_types")List<Integer> notifyTypes);

    List<Map<String, Number>> getNotificationsCount(int userId);

}

