package com.lvwang.osf.mappers;


import com.github.abel533.mapper.BaseMapper;
import com.lvwang.osf.pojo.Comment;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CommentMapper extends BaseMapper<Comment>{

    /**
     * 通过内容类型获取列表
     * @param id
     * @param type
     * @param offset
     * @param count
     * @return
     */
    List<Comment> getCommentsByType(
            @Param("id") int id
            , @Param("type") int type
            , @Param("offset") int offset
            , @Param("count")int count);



}
