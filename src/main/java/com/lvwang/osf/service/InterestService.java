package com.lvwang.osf.service;

import java.util.*;

import com.lvwang.osf.mappers.InterestMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lvwang.osf.pojo.Interest;
import com.lvwang.osf.pojo.Tag;

@Service("interestService")
public class InterestService extends BaseService<Interest> {

	private static final String INTEREST_KEY = "TAG_INTEREST_USER_";

	@Autowired
	private InterestMapper interestMapper;

	@Autowired
	private RedisService redisService;

	private int saveInterest(Interest interest) {
		super.save(interest);
		redisService.sadd(INTEREST_KEY + interest.getUserId(), String.valueOf(interest.getTagId()));
		return interest.getId();
	}

	/**
	 * 关注tag
	 * @param user_id
	 * @param tag_id
	 */
	public void interestInTag(int user_id, int tag_id) {
		Interest interest = new Interest(user_id, tag_id);
		saveInterest(interest);
	}

	private int delInterest(Interest interest) {
		int lineCount = super.deleteById(interest.getId());
		redisService.srem(INTEREST_KEY + interest.getUserId(), String.valueOf(interest.getTagId()));
		return lineCount;
	}
	
	/**
	 * 撤销关注tag
	 * @param user_id
	 * @param tag_id
	 */
	public void undoInterestInTag(int user_id, int tag_id){
		Interest interest = new Interest(user_id, tag_id);
		this.delInterest(interest);
	}

	public List<Integer> getUsersInterestInTag(int tagId) {
		return interestMapper.getUsersInterestInTag(tagId);
	}

	/**
	 * 获取关注tag_id的用户列表
	 * @param tag_id
	 * @return
	 */
	public List<Integer> getUsersInterestedInTag(int tag_id) {
		return interestMapper.getUsersInterestInTag(tag_id);
	}

	/**
	 * 判断用户对tag是否已经关注
	 * 
	 * @param user_id
	 * @param tag_id
	 * @return
	 */
	public boolean hasInterestInTag(int user_id, int tag_id) {
		return redisService.sismember(INTEREST_KEY + user_id,String.valueOf(tag_id));
	}

	/**
	 * 判断用户对列表中的tag是否已经关注
	 * 
	 * @param user_id
	 * @param tags
	 * @return
	 */
	public Map<Integer, Boolean> hasInterestInTags(int user_id, List<Tag> tags){
		if(tags == null || tags.size() == 0 ){
			return null;
		}
		Map<Integer, Boolean> result = new TreeMap<Integer, Boolean>();
		List<Integer> integerArrayList = new ArrayList<Integer>();
		for(Tag tag: tags){
			integerArrayList.add(tag.getId());
			result.put(tag.getId(), false);
		}
		
		List<Integer> integerList = converterInterestInTags(user_id, integerArrayList);
		for(int i=0; i<integerList.size(); i++) {
			result.put(integerList.get(i), true);
		}
		return result;
	}
	
	/**
	 * 获取用户关注的tag列表
	 * @param userId
	 * @return
	 */
	public List<Tag> getTagsUserInterestedIn(int userId){
		return interestMapper.getTagsUserInterestedIn(userId);
	}

	public List<Integer> converterInterestInTags(int userId, List<Integer> tagIds){
		List<Integer> result = new ArrayList<Integer>();
		Iterator<Integer> iterator = tagIds.iterator();
		while(iterator.hasNext()) {
			int id = iterator.next();
			if(hasInterestInTag(userId, id )) {
				result.add(id);
			}
		}
		return result;
	}
}
