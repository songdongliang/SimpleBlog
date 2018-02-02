package com.lvwang.osf.mappers;

import com.github.abel533.mapper.BaseMapper;
import com.lvwang.osf.pojo.Post;

import java.util.List;

public interface PostMapper extends BaseMapper<Post>{

    List<Post> getPostsByUserID(int userId);

    Long count(int userId);

    int getAuthorOfPost(int id);
}
