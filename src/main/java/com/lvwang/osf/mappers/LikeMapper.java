package com.lvwang.osf.mappers;

import com.github.abel533.mapper.Mapper;
import com.lvwang.osf.pojo.Like;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface LikeMapper extends Mapper<Like> {

    List<Integer> getLikers(@Param("objectType")int objectType, @Param("objectId")int objectId);

}
