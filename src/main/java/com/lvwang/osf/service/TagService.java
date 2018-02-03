package com.lvwang.osf.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lvwang.osf.mappers.TagMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lvwang.osf.pojo.Event;
import com.lvwang.osf.pojo.Tag;
import com.lvwang.osf.search.TagIndexService;
import com.lvwang.osf.util.Property;

import javax.annotation.Resource;

@Service("tagService")
public class TagService extends BaseService<Tag> {
	
	@Autowired
	@Qualifier("eventService")
	private EventService eventService;
	
	@Autowired
	@Qualifier("relationService")
	private RelationService relationService;

	@Autowired
	@Qualifier("feedService")
	private FeedService feedService;
	
	@Autowired
	@Qualifier("tagIndexService")
	private TagIndexService tagIndexService;

	@Resource
    private TagMapper tagMapper;
	
	private String check(String tag) {
		if(tag == null || tag.length() == 0) {
			return Property.ERROR_TAG_EMPTY;
		}
		return Property.SUCCESS_TAG_FORMAT;
	}
	
	public static List<Tag> toList(String tags) {
		if(tags == null || tags.length() == 0) {
            return new ArrayList<Tag>();
        }
		String[] tagAndIdStrs = tags.split(" ");
		List<Tag> tagList = new ArrayList<Tag>();
		for(String tag : tagAndIdStrs) {
			String[] tagAndId = tag.split(":");
			Tag t = new Tag();
			if(tagAndId.length > 1) {
				t.setId(Integer.valueOf(tag.split(":")[1]) );
			}
			t.setTag(tag.split(":")[0]);
			
			tagList.add(t);
		}
		
		return tagList;
	}

	public static String toString(List<Tag> tags) {
		if(tags == null || tags.size() == 0)
			return null;
		StringBuffer buffer = new StringBuffer();
		for(Tag tag: tags) {
			buffer.append(tag.getTag()+":"+tag.getId()+" ");
		}
		return buffer.toString();
	}
	
	public Map<String, Object> newTag(String tag){
		Map<String, Object> ret = new HashMap<String, Object>();
		String status = check(tag);
		ret.put("status", status);
		if(!status.equals(Property.SUCCESS_TAG_FORMAT)) {
			return ret;
		}
		
		int id = tagMapper.getTagID(tag);
		if(id != 0) {
			Tag tg = new Tag();
			tg.setId(id);
			tg.setTag(tag);
			ret.put("tag", tg);
			return ret;
		}
		
		Tag newTag = new Tag(tag);
		int newTagId = super.save(newTag);
		if(newTagId != 0) {
			ret.put("tag", newTag);
		}
		return ret;
	}
	
	@Transactional
	public Map<String, Object> newTags(List<Tag> tags) {
				
		Map<String, Object> ret = new HashMap<String, Object>();
		List<Tag> taglist = new ArrayList<Tag>();
		ret.put("tags", taglist);
		
		if(tags == null || tags.size() == 0) {
			ret.put("status", Property.SUCCESS_TAG_CREATE);
			return ret;
		}
		
		for(Tag tag: tags) {
			String status = check(tag.getTag());
			if(!status.equals(Property.SUCCESS_TAG_FORMAT)) {
				return ret;
			}
			
			Integer id = tagMapper.getTagID(tag.getTag());
			if(id != null) {
				Tag tg = new Tag();				
				tg.setId(id);
				tg.setTag(tag.getTag());
				taglist.add(tg);
				continue;
			}
						
			Integer newTagId = super.save(tag);
			if(newTagId != null && newTagId != 0) {
				taglist.add(tag);
				//index tag
				tagIndexService.add(tag);
			}
		}
		ret.put("status", Property.SUCCESS_TAG_CREATE);
		return ret;
	}
	
	public int getID(String tag){
		return tagMapper.getTagID(tag);
	}
	
	public List<Tag> searchTag(String term) {
		List<Integer> tag_ids = tagIndexService.findTag(term);
		return getTagsByIDs(tag_ids);
	}
	
	/**
	 * 获取有tag的event
	 * 需重构，迁移到feed或event
	 * @param tag
	 * @return
	 */
	public List<Event> getWithTag(String tag) {
		List<Event> events = eventService.getEventsWithRelations(relationService.getRelationsWithTag(tag));
		feedService.addUserInfo(events);
		return events;
	}
	
	
	/**
	 * 获取推荐tag
	 * 简单实现，获取有cover的tag
	 * @return
	 */
	public List<Tag> getRecommendTags(){
		return tagMapper.getTagsHasCover();
	}
	
	public Tag getTagByID(int id) {
		return super.queryById(id);
	}
	
	private List<Tag> getTagsByIDs(List<Integer> ids) {
		List<String> idsStr = new ArrayList<>();
		for(int i = 0; i < ids.size(); i++) {
			idsStr.add(String.valueOf(ids.get(i)));
		}
		return tagMapper.getTags(idsStr);
	}

}
