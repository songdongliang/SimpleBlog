package com.lvwang.osf.service;

import com.github.abel533.entity.Example;
import com.lvwang.osf.mappers.FollowerMapper;
import com.lvwang.osf.pojo.Follower;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Transactional
@Service
public class FollowerService extends BaseService<Follower> {

    private static final String FOLLOWER_KEY = "FOLLOWER_USER_";

    @Resource
    private FollowerMapper followerMapper;

    @Resource
    private RedisService redisService;

    public int saveFollower(Follower follower) {
        super.save(follower);
        redisService.sadd(FOLLOWER_KEY + follower.getUserId(),String.valueOf(follower.getFollowerUserId()));
        return follower.getId();
    }

    public long getFollowersCount(int user_id) {
        return redisService.smembers(FOLLOWER_KEY + user_id).size();
    }

    public List getFollowerIDs(int user_id) {
        Set<String> followerIds = redisService.smembers(FOLLOWER_KEY + user_id);
        return new ArrayList<>(followerIds);
    }

    public List<Follower> getFollowers(int user_id) {
        Example example = new Example(Follower.class);
        example.createCriteria().andEqualTo("user_id",user_id);
        return followerMapper.selectByExample(example);
    }

    public boolean hasFollower(int user_a, int user_b) {
        return redisService.sismember(FOLLOWER_KEY + user_a,String.valueOf(user_b));
    }
}
