package com.lvwang.osf.mappers;

import com.github.abel533.mapper.Mapper;
import com.lvwang.osf.pojo.Photo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PhotoMapper extends Mapper<Photo> {

    List<Photo> getPhotos(int albumId);

    int updatePhotoDesc(@Param("photoId")Integer photoId, @Param("photoDesc")String photoDesc);

    String getKey(int id);
}
