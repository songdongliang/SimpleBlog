package com.lvwang.osf.mappers;

import com.github.abel533.mapper.Mapper;
import com.lvwang.osf.pojo.Following;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FollowingMapper extends Mapper<Following> {

    boolean hasFollowing(int user_a, int user_b);

    List<Integer> isFollowingUsers(@Param("user_id")int user_id, @Param("following_ids")List<Integer> following_ids);
}
