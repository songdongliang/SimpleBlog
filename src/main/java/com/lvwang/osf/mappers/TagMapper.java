package com.lvwang.osf.mappers;

import com.github.abel533.mapper.Mapper;
import com.lvwang.osf.pojo.Tag;

import java.util.List;

public interface TagMapper extends Mapper<Tag> {

    Integer getTagID(String tag);

    List<Tag> getTagsHasCover();

    List<Tag> getTags(List<String> tagIds);
}
