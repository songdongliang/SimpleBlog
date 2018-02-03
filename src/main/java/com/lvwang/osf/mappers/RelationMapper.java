package com.lvwang.osf.mappers;

import com.github.abel533.mapper.Mapper;
import com.lvwang.osf.pojo.Relation;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RelationMapper extends Mapper<Relation> {

    List<Relation> getRelationsInTags(@Param("tag_ids")List<String> tagIds);

}
