package com.lvwang.osf.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lvwang.osf.mappers.EventMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.lvwang.osf.pojo.Album;
import com.lvwang.osf.pojo.Event;
import com.lvwang.osf.pojo.Photo;
import com.lvwang.osf.pojo.Post;
import com.lvwang.osf.pojo.Relation;
import com.lvwang.osf.pojo.ShortPost;
import com.lvwang.osf.search.EventIndexService;
import com.lvwang.osf.util.Dic;

import javax.annotation.Resource;

@Service("eventService")
public class EventService extends BaseService<Event>{

	@Resource
	private EventMapper eventMapper;

	@Autowired
	@Qualifier("eventIndexService")
	private EventIndexService eventIndexService;
	
	private Event toEvent(int objectType, Object obj) {
		Event event = new Event();
		if(Dic.OBJECT_TYPE_POST == objectType) {
			Post post = (Post)obj;
			event.setObjectType(Dic.OBJECT_TYPE_POST);
			event.setObjectId(post.getId());
			event.setUserId(post.getPostAuthor());
			event.setTitle(post.getPostTitle());
			event.setSummary(post.getPostExcerpt());
			event.setContent(post.getPostCover());
			event.setLikeCount(post.getLikeCount());
			event.setShareCount(post.getShareCount());
			event.setCommentCount(post.getCommentCount());
			event.setTags(post.getPostTagsList());
		} else if(Dic.OBJECT_TYPE_ALBUM == objectType) {
			Album album = (Album)obj;
			event.setObjectType(Dic.OBJECT_TYPE_ALBUM);
			event.setObjectId(album.getId());
			event.setUserId(album.getUserId());
			event.setTitle(album.getCover());
			event.setSummary(album.getAlbumDesc());
			
			List<Photo> photos = album.getPhotos();
			StringBuilder keys = new StringBuilder();
			for(Photo photo:photos) {
				keys.append(photo.getKey());
				keys.append(":");
			}
			event.setContent(keys.toString());
			event.setLikeCount(0);
			event.setShareCount(0);
			event.setCommentCount(0);
			event.setTags(album.getAlbumTagsList());
			
		} else if(Dic.OBJECT_TYPE_PHOTO == objectType) {
			//event_id = eventDao.savePhotoEvent((Photo)obj);
		} else if(Dic.OBJECT_TYPE_SHORT_POST == objectType){
			ShortPost spost = (ShortPost) obj;
			event.setObjectType(Dic.OBJECT_TYPE_SHORT_POST);
			event.setObjectId(spost.getId());
			event.setSummary(spost.getPostContent());
			event.setUserId(spost.getPostAuthor());
			event.setLikeCount(spost.getLikeCount());
			event.setShareCount(spost.getShareCount());
			event.setCommentCount(spost.getCommentCount());
		}
		return event;
	}
	
	/**
	 * 保存event，并索引
	 * @param objectType
	 * @param obj
	 * @return event_id
	 */
	public int newEvent(int objectType, Object obj) {
		Event event = toEvent(objectType, obj);
		save(event);
		eventIndexService.add(event, obj);
		return event.getId();
	}
	
	/**
	 * 
	 * @param start
	 * @param step
	 * @return
	 */
	public List<Event> getEvents(int start, int step) {
		return eventMapper.getEvents(start, step);
	}
	
	
	/**
	 * 根据relation关系(objectType, objectId)查询event
	 */
	public List<Event> getEventsWithRelations(List<Relation> relations) {
		List<Event> events = new ArrayList<Event>();
		if(relations != null && relations.size() != 0) {
			Map<Integer, List<Integer>> category = new HashMap<>();
			for(Relation relation : relations) {
				if(!category.containsKey(relation.getObjectType())) {
					category.put(relation.getObjectType(), new ArrayList<Integer>());
				}
				category.get(relation.getObjectType()).add(relation.getObjectId());
			}
			events = getEventsWithRelations(category);
		}
		return events;
	}

	private List<Event> getEventsWithRelations(Map<Integer, List<Integer>> relationsCategory) {
		if(relationsCategory == null || relationsCategory.size() == 0) {
			return new ArrayList<>();
		}
		return eventMapper.getEventsWithRelations(relationsCategory);
	}

	/**
	 * 获取含有图片的Event
	 * @param start 
	 * @param step
	 * @return 
	 */
	public List<Event> getEventsHasPhoto(int start, int step) {
		return eventMapper.getEventsHasPhoto(start, step);
	}
	
	public Event getEvent(int objectType, int objectId){
		return eventMapper.getEvent(objectType, objectId);
	}
	
	/**
	 * 根据event id查询event
	 */
	public List<Event> getEventsWithIDs(List<Integer> eventIds) {
		return queryByIds(Event.class, eventIds,"id");
	}
	
	public List<Event> getEventsOfUser(int userId, int count){
		return eventMapper.getEventsOfUser(userId, count);
	}

	public void delete(int id){
		deleteById(id);
	}
	
	public void delete(int objectType, int objectId){
		eventMapper.deleteByObject(objectType, objectId);
	}

	public void commentCountAdd(int objectId) {
		eventMapper.commentCountAdd(objectId);
	}

	public void likeCountAdd(int objectId) {
		eventMapper.likeCountAdd(objectId);
	}
}
