package com.lvwang.osf.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.lvwang.osf.mappers.FollowerMapper;
import com.lvwang.osf.mappers.FollowingMapper;
import org.springframework.stereotype.Service;

import com.lvwang.osf.pojo.Follower;
import com.lvwang.osf.model.Following;
import com.lvwang.osf.pojo.User;
import com.lvwang.osf.util.Property;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Transactional
@Service
public class FollowService extends BaseService {
	
	@Resource
	private FollowerService followerService;
	@Resource
	private FollowingService followingService;
	
	public Map<String, Object> newFollowing(int user_id, String user_name, int following_user_id, String following_user_name) {
		Map<String, Object> map = new HashMap<String, Object>();
		Following following = new Following();
		
		following.setUserId(user_id);
		following.setUserName(user_name);
		following.setFollowingUserId(following_user_id);
		following.setFollowingUserName(following_user_name);
		super.save(following);
		if(following.getId() == 0) {
			map.put("status", Property.ERROR_FOLLOW);
			return map;
		}
		map.put("following", following);
		
		Follower follower = new Follower();
		follower.setUserId(following_user_id);
		follower.setUserName(following_user_name);
		follower.setFollowerUserId(user_id);
		follower.setFollowerUserName(user_name);
		super.save(follower);
		if(follower.getId() == 0) {
			map.put("status", Property.ERROR_FOLLOW);
			return map;
		}
		map.put("follower", follower);
		map.put("status", Property.SUCCESS_FOLLOW);
		return map;
	}
	
	
	public Map<String, Object> undoFollow(int user_id, int following_user_id) {
		Map<String, Object> map = new HashMap<String, Object>();
		Following following = new Following();
		following.setUserId(user_id);
		following.setFollowingUserId(following_user_id);
		if(super.deleteByWhere(following) == 0) {
			map.put("status", Property.ERROR_FOLLOW_UNDO);
			return map;
		}
		map.put("following", following);
		
		Follower follower = new Follower();
		follower.setUserId(following_user_id);
		follower.setFollowerUserId(user_id);
		if(super.deleteByWhere(follower) > 0) {
			map.put("status", Property.SUCCESS_FOLLOW_UNDO);
			map.put("follower", follower);
		} else {
			map.put("status", Property.ERROR_FOLLOW_UNDO);
		}
		return map;
	}
	
	public long followersCount(int user_id) {
		return followDao.getFollowersCount(user_id);
	}
	public long followingsCount(int user_id) {
		return followDao.getFollowingsCount(user_id);
	}
	
	public List<Integer> getFollowerIDs(int user_id) {
		return followDao.getFollowerIDs(user_id);
	}
	
	public List<Integer> getFollowingIDs(int user_id) {
		return followDao.getFollowingIDs(user_id);
	}
	
	public List<Follower> getFollowers(int user_id) {
		return followDao.getFollowers(user_id);
	}
	
	public List<Following> getFollowings(int user_id) {
		return followDao.getFollowings(user_id);
	}
	
	public boolean isFollowing(int user_a, int user_b){
		return followDao.hasFollowing(user_a, user_b);
	}
	
	
	public Map<Integer, Boolean> isFollowing(int user_id, List<User> users){
		if(users == null || users.size() == 0) {
			return null;
		}
		Map<Integer, Boolean> result = new TreeMap<Integer, Boolean>();
		List<Integer> users_id = new ArrayList<Integer>();
		for(User user: users) {
			users_id.add(user.getId());
			result.put(user.getId(), false);
		}
		List<Integer> following_users = followDao.isFollowingUsers(user_id, users_id);
		for(int i=0; i<following_users.size(); i++) {
			result.put(following_users.get(i), true);
		}
		return result;
	}
}
