package com.lvwang.osf.mappers;

import com.github.abel533.mapper.Mapper;
import com.lvwang.osf.pojo.Tag;
import com.lvwang.osf.pojo.Interest;

import java.util.List;

public interface InterestMapper extends Mapper<Interest> {

    List<Integer> getUsersInterestInTag(int tagId);

    List<Tag> getTagsUserInterestedIn(int userId);

}
