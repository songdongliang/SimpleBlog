package com.lvwang.osf.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lvwang.osf.mappers.EventMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.lvwang.osf.dao.AlbumDAO;
import com.lvwang.osf.model.Album;
import com.lvwang.osf.pojo.Event;
import com.lvwang.osf.model.Photo;
import com.lvwang.osf.pojo.Post;
import com.lvwang.osf.model.Relation;
import com.lvwang.osf.pojo.ShortPost;
import com.lvwang.osf.search.EventIndexService;
import com.lvwang.osf.util.Dic;

import javax.annotation.Resource;

@Service("eventService")
public class EventService extends BaseService<Event>{

	@Resource
	private EventMapper eventMapper;

	@Autowired
	@Qualifier("albumDao")
	private AlbumDAO albumDao;
		
	@Autowired
	@Qualifier("eventIndexService")
	private EventIndexService eventIndexService;
	
	private Event toEvent(int object_type, Object obj) {
		Event event = new Event();
		if(Dic.OBJECT_TYPE_POST == object_type) {
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
			event.setTags_list(post.getPostTagsList());
			
		} else if(Dic.OBJECT_TYPE_ALBUM == object_type) {
			Album album = (Album)obj;
			event.setObjectType(Dic.OBJECT_TYPE_ALBUM);
			event.setObjectId(album.getId());
			event.setUserId(album.getUser_id());
			event.setTitle(album.getCover());
			event.setSummary(album.getAlbum_desc());
			
			List<Photo> photos = album.getPhotos();
			StringBuffer keys = new StringBuffer();
			for(Photo photo:photos) {
				keys.append(photo.getKey()+":");
			}
			event.setContent(keys.toString());
			event.setLikeCount(0);
			event.setShareCount(0);
			event.setCommentCount(0);
			event.setTags_list(album.getAlbum_tags_list());
			
		} else if(Dic.OBJECT_TYPE_PHOTO == object_type) {
			//event_id = eventDao.savePhotoEvent((Photo)obj);
		} else if(Dic.OBJECT_TYPE_SHORTPOST == object_type){
			ShortPost spost = (ShortPost) obj;
			event.setObjectType(Dic.OBJECT_TYPE_SHORTPOST);
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
		event.setId(event.getId());
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
	 * 根据relation关系(objectType, object_id)查询event
	 */
	public List<Event> getEventsWithRelations(List<Relation> relations) {
		List<Event> events = new ArrayList<Event>();
		if(relations != null && relations.size() != 0) {
			Map<Integer, List<Integer>> category = new HashMap<Integer, List<Integer>>();
			for(Relation relation : relations) {
				if(!category.containsKey(relation.getObject_type())) {
					category.put(relation.getObject_type(), new ArrayList<Integer>());
				}
				category.get(relation.getObject_type()).add(relation.getObject_id());
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
	
	public Event getEvent(int object_type, int object_id){
		return eventMapper.getEvent(object_type, object_id);
	}
	
	/**
	 * 根据event id查询event
	 */
	public List<Event> getEventsWithIDs(List<Integer> event_ids) {
		return queryByIds(Event.class,event_ids,"id");
	}
	
	public List<Event> getEventsOfUser(int user_id, int count){
		return eventMapper.getEventsOfUser(user_id, count);
	}

	public void delete(int id){
		deleteById(id);
	}
	
	public void delete(int object_type, int object_id){
		eventMapper.deleteByObject(object_type, object_id);
	}

}
