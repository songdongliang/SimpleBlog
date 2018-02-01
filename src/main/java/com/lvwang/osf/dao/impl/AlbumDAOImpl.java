package com.lvwang.osf.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.lvwang.osf.dao.AlbumDAO;
import com.lvwang.osf.mappers.AlbumMapper;
import com.lvwang.osf.model.Album;
import com.lvwang.osf.model.Photo;
import com.qiniu.common.QiniuException;
import com.qiniu.storage.BucketManager;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;

@Repository("albumDao")
public class AlbumDAOImpl implements AlbumDAO{
	
	private static final String AK = "fpkPpZD69KlXKlz8xR6jxEG6UtQ8hxtlnonGDhYY";
	private static final String SK = "kCXacra1ds3DJmTuX8Adv1hklgzeuI15T7AX1Gcy";
	private static final String bucket = "osfimgs2";
	private Auth auth = Auth.create(AK, SK);
	private BucketManager bucketManager = new BucketManager(auth);
	
	
	@Autowired
	private AlbumMapper albumMapper;
	
	public int saveAlbum(final Album album) {
		albumMapper.saveAlbum(album);
		return album.getId();
	}
	
	public int savePhoto(final Photo photo) {
		albumMapper.savePhoto(photo);
		return photo.getId();
	}

    @Override
    public String uploadPhoto(byte[] img, String key) {
        return null;
    }

    private String getUpToken(){
	    return auth.uploadToken(bucket, null, 3600, new StringMap().
	    		putNotEmpty("returnBody", "{\"key\": $(key), \"hash\": $(etag), \"width\": $(imageInfo.width), \"height\": $(imageInfo.height)}"));
	}
	
	public void delPhotoInBucket(String key){
		//client.deleteObject(bucket, key);
		try {
			bucketManager.delete(bucket, key);
		} catch (QiniuException e) {
			e.printStackTrace();
		}
	}
	

	public int getAlbumUser(int id) {
		return albumMapper.getAlbumUser(id);
	}

	public Integer getAlbumID(int user_id, int status) {
		return albumMapper.getAlbumID(user_id, status);
	}

	public List<Photo> getPhotos(int album_id) {
		return albumMapper.getPhotos(album_id);
	}

	public int updateAlbumDesc(int album_id, String album_desc, int album_status) {
		return albumMapper.updateAlbumDesc(album_id, album_desc, album_status);
	}

	public int updatePhotoDesc(int photo_id, String photo_desc) {
		return albumMapper.updatePhotoDesc(photo_id, photo_desc);
	}

	public Album getAlbum(final int id) {
		return albumMapper.getAlbum(id);
	}

	public int updateAlbumCover(int album_id, String cover) {
		return albumMapper.updateAlbumCover(album_id, cover);
	}

	public String getKey(int id) {
		return albumMapper.getKey(id);
		
	}
	public List<String> getKeys(List<Integer> ids) {
		return albumMapper.getKeys(ids);
	}
	
	
	public int updatePhotosCount(int album_id, int count) {
		return albumMapper.updatePhotosCount(album_id, count);
	}

	public List<Album> getAlbumsOfUser(final int id) {
		return albumMapper.getAlbumsOfUser(id);
	}

	public int getAuthorOfAlbum(int id) {
		return albumMapper.getAuthorOfAlbum(id);
	}

	public int updateAlbumInfo(Album album) {
		return albumMapper.updateAlbumInfo(album);
	}

	public void delPhoto(int id) {
		albumMapper.delPhoto(id);
	}

	public Album getAlbumContainPhoto(int photo_id) {
		return albumMapper.getAlbumContainPhoto(photo_id);
	}
}
