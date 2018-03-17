package com.lvwang.osf.mappers;

import com.github.abel533.mapper.Mapper;
import com.lvwang.osf.pojo.Post;

import java.util.List;

public interface PostMapper extends Mapper<Post> {

    List<Post> getPostsByUserID(int userId);

    Long count(int userId);

    int getAuthorOfPost(int id);

    void commentCountAdd(int id);

    void likeCountAdd(int id);
}
