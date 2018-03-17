package com.lvwang.osf.service;

import java.util.List;

import com.github.abel533.entity.Example;
import com.lvwang.osf.mappers.LikeMapper;
import com.lvwang.osf.pojo.Like;
import com.lvwang.osf.util.Dic;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("likeService")
public class LikeService extends BaseService<Like> {

	/**
	 * 缓存喜欢object的用户id
	 */
	private static final String LIKE_ = "LIKE_";

	@Resource
	private LikeMapper likeMapper;

	@Resource
	private RedisService redisService;

	@Resource
	private PostService postService;
	@Resource
	private EventService eventService;
	
	public void like(int userId, int objectType, int objectId){
		Like like = new Like();
		like.setUserId(userId);
		like.setObjectId(objectId);
		like.setObjectType(objectType);
		super.save(like);
		postService.likeCountAdd(objectId);
		eventService.likeCountAdd(objectId);
		redisService.sadd(LIKE_ + Dic.checkType(objectType) + "_" + objectId,String.valueOf(userId));
	}
	
	public void undoLike(int userId, int objectType, int objectId){
		Example example = new Example(Like.class);
		example.createCriteria()
				.andEqualTo("userId", userId)
				.andEqualTo("objectType", objectType)
				.andEqualTo("objectId", objectId);
		likeMapper.deleteByExample(example);
		redisService.srem(LIKE_ + Dic.checkType(objectType) + "_" + objectId,String.valueOf(userId));
	}
	
	/**
	 * 判断用户是否喜欢某个对象
	 */
	public boolean isLike(int userId, int objectType, int objectId) {
		return redisService.sismember(LIKE_ + Dic.checkType(objectType) + "_" + objectId,String.valueOf(userId));
	}
	
	/**
	 * 返回喜欢某个对象的用户数量
	 * @param objectType
	 * @param objectId
	 * @return
	 */
	public long likersCount(int objectType, int objectId) {
		return redisService.strlen(LIKE_ + Dic.checkType(objectType) + "_" + objectId);
	}
	
	/**
	 * 返回喜欢某个对象的用户ID列表
	 */
	public List<Integer> likers(int objectType, int objectId){
		final String key = LIKE_ + Dic.checkType(objectType) + "_" + objectId;
		List<Integer> likers = null;
		if( redisService.smembers(key) == null ){
			likers = likeMapper.getLikers(objectType, objectId);
			String[] members = new String[likers.size()];
			for (int i = 0;i < likers.size();i++) {
				members[i] = String.valueOf(likers.get(i));
			}
			redisService.sadd(key,members);
		}
		return likers;
	}
	
	/**
	 * 用户喜欢的对象数量(部分对象类型)
	 * @param user_id
	 * @return
	 */
	public int likeCount(int user_id){
		return 0;
	}
}
