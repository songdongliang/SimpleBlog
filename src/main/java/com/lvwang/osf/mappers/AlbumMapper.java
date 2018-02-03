package com.lvwang.osf.mappers;

import com.github.abel533.mapper.Mapper;
import com.lvwang.osf.pojo.Album;
import org.apache.ibatis.annotations.Param;

public interface AlbumMapper extends Mapper<Album> {

    int getAlbumUser(int id);

    Integer getAlbumID(@Param("userId")int userId, @Param("status") int status);

    int getAuthorOfAlbum(int id);

    Album getAlbumContainPhoto(int photoId);
}
