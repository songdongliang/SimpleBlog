package com.lvwang.osf.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.lvwang.osf.dao.EventDAO;
import com.lvwang.osf.mappers.EventMapper;
import com.lvwang.osf.model.Event;

@Repository("eventDao")
public class EventDAOImpl implements EventDAO{

	private static final String TABLE = "osf_events";
	
	@Autowired
	private EventMapper eventMapper;

	@Override
	public int save(final Event event) {
		eventMapper.save(event);
		return event.getId();
	}

	@Override
	public List<Event> getEventsWithIDs(List<Integer> event_ids) {
		return eventMapper.getEventsWithIDs(event_ids);
	}

	@Override
	public List<Event> getEventsWithRelations(Map<Integer, List<Integer>> relationsCategory) {
		if(relationsCategory == null || relationsCategory.size() == 0) {
			return new ArrayList<>();
		}
		return eventMapper.getEventsWithRelations(relationsCategory);
	}

	@Override
	public List<Event> getEventsOfUser(int user_id, int count) {
		return eventMapper.getEventsOfUser(user_id, count);
	}

	@Override
	public List<Event> getEventsHasPhoto(int start, int step){
		return eventMapper.getEventsHasPhoto(start, step);
	}

	@Override
	public void delete(int id) {
		eventMapper.delete(id);
	}

	@Override
	public void deleteByObject(int object_type, int object_id) {
		eventMapper.deleteByObject(object_type, object_id);
	}

	@Override
	public Event getEvent(final int object_type, final int object_id) {
		return eventMapper.getEvent(object_type, object_id);
	}

	@Override
	public List<Event> getEvents(int start, int step) {
		return  eventMapper.getEvents(start, step);
	}
}
