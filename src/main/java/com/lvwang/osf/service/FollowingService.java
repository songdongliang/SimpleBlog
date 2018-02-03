package com.lvwang.osf.service;

import com.github.abel533.entity.Example;
import com.lvwang.osf.mappers.FollowingMapper;
import com.lvwang.osf.pojo.Following;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


@Transactional
@Service
public class FollowingService extends BaseService<Following> {

    private static final String FOLLOWING_KEY = "FOLLOWING_USER_";

    @Resource
    private FollowingMapper followingMapper;

    @Resource
    private RedisService redisService;

    public int saveFollowing(Following following) {
        super.save(following);
        redisService.sadd(FOLLOWING_KEY + following.getUserId(),String.valueOf(following.getFollowingUserId()));
        return following.getId();
    }

    public long getFollowingsCount(int user_id) {
        return redisService.smembers(FOLLOWING_KEY + user_id).size();
    }

    public List getFollowingIDs(int user_id) {
        Set<String> followingIds = redisService.smembers(FOLLOWING_KEY + user_id);
        return new ArrayList<>(followingIds);
    }

    public List<Following> getFollowings(int user_id) {
        Example example = new Example(Following.class);
        example.createCriteria().andEqualTo("user_id",user_id);
        return followingMapper.selectByExample(example);
    }

    public boolean hasFollowing(int user_a, int user_b) {
        return redisService.sismember(FOLLOWING_KEY + user_a,String.valueOf(user_b));
    }

    public List<Integer> isFollowingUsers(int user_id, List<Integer> following_ids) {
        List<Integer> result = new ArrayList<Integer>();
        Iterator<Integer> ids_it = following_ids.iterator();
        while(ids_it.hasNext()) {
            int id = ids_it.next();
            if(hasFollowing(user_id, id )) {
                result.add(id);
            }
        }
        return result;
    }
}
