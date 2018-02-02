package com.lvwang.osf.mappers;

import com.github.abel533.mapper.BaseMapper;
import com.lvwang.osf.pojo.Event;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface EventMapper extends BaseMapper<Event>{

    List<Event> getEvents(@Param("start") int start,@Param("step") int step);

    List<Event> getEventsWithRelations(Map<Integer, List<Integer>> relations);

    void deleteByObject(int object_type, int object_id);

    Event getEvent(int object_type, int object_id);

    List<Event> getEventsHasPhoto(int start, int step);

    List<Event> getEventsOfUser(int user_id, int count);
}
