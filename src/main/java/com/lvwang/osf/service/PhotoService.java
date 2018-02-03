package com.lvwang.osf.service;

import com.lvwang.osf.mappers.PhotoMapper;
import com.lvwang.osf.pojo.Photo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PhotoService extends BaseService<Photo> {

    @Autowired
    private PhotoMapper photoMapper;

    public List<Photo> getPhotos(int albumId) {
        return photoMapper.getPhotos(albumId);
    }

    public int updatePhotoDesc(Integer photoId, String photoDesc) {
        return photoMapper.updatePhotoDesc(photoId, photoDesc);
    }

    public String getKey(int id) {
        return photoMapper.getKey(id);
    }
}
