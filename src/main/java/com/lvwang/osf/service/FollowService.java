package com.lvwang.osf.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.stereotype.Service;

import com.lvwang.osf.pojo.Follower;
import com.lvwang.osf.pojo.Following;
import com.lvwang.osf.pojo.User;
import com.lvwang.osf.util.Property;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Transactional
@Service
public class FollowService {
	
	@Resource
	private FollowerService followerService;
	@Resource
	private FollowingService followingService;
	
	public Map<String, Object> newFollowing(int user_id, String user_name, int following_user_id, String following_user_name) {
		Map<String, Object> map = new HashMap<>();
		Following following = new Following();
		
		following.setUserId(user_id);
		following.setUserName(user_name);
		following.setFollowingUserId(following_user_id);
		following.setFollowingUserName(following_user_name);
		int followingId = followingService.saveFollowing(following);
		if(followingId == 0) {
			map.put("status", Property.ERROR_FOLLOW);
			return map;
		}
		map.put("following", following);
		
		Follower follower = new Follower();
		follower.setUserId(following_user_id);
		follower.setUserName(following_user_name);
		follower.setFollowerUserId(user_id);
		follower.setFollowerUserName(user_name);
		int followerId = followerService.saveFollower(follower);
		if(followerId == 0) {
			map.put("status", Property.ERROR_FOLLOW);
			return map;
		}
		map.put("follower", follower);
		map.put("status", Property.SUCCESS_FOLLOW);
		return map;
	}
	
	
	public Map<String, Object> undoFollow(int user_id, int following_user_id) {
		Map<String, Object> map = new HashMap<>();
		Following following = new Following();
		following.setUserId(user_id);
		following.setFollowingUserId(following_user_id);
		if(followingService.deleteByWhere(following) == 0) {
			map.put("status", Property.ERROR_FOLLOW_UNDO);
			return map;
		}
		map.put("following", following);
		
		Follower follower = new Follower();
		follower.setUserId(following_user_id);
		follower.setFollowerUserId(user_id);
		if(followerService.deleteByWhere(follower) > 0) {
			map.put("status", Property.SUCCESS_FOLLOW_UNDO);
			map.put("follower", follower);
		} else {
			map.put("status", Property.ERROR_FOLLOW_UNDO);
		}
		return map;
	}
	
	public long followersCount(int user_id) {
		return followerService.getFollowersCount(user_id);
	}
	public long followingsCount(int user_id) {
		return followingService.getFollowingsCount(user_id);
	}
	
	public List<Integer> getFollowerIDs(int user_id) {
		return followerService.getFollowerIDs(user_id);
	}
	
	public List<Integer> getFollowingIDs(int user_id) {
		return followingService.getFollowingIDs(user_id);
	}
	
	public List<Follower> getFollowers(int user_id) {
		return followerService.getFollowers(user_id);
	}
	
	public List<Following> getFollowings(int user_id) {
		return followingService.getFollowings(user_id);
	}
	
	public boolean isFollowing(int user_a, int user_b){
		return followingService.hasFollowing(user_a, user_b);
	}
	
	
	public Map<Integer, Boolean> isFollowing(int user_id, List<User> users){
		if(users == null || users.size() == 0) {
			return null;
		}
		Map<Integer, Boolean> result = new TreeMap<>();
		List<Integer> users_id = new ArrayList<>();
		for(User user: users) {
			users_id.add(user.getId());
			result.put(user.getId(), false);
		}
		List<Integer> following_users = followingService.isFollowingUsers(user_id, users_id);
		for(int i=0; i<following_users.size(); i++) {
			result.put(following_users.get(i), true);
		}
		return result;
	}
}
