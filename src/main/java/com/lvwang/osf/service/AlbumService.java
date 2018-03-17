package com.lvwang.osf.service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.imageio.ImageIO;

import com.github.abel533.entity.Example;
import com.lvwang.osf.mappers.AlbumMapper;
import com.lvwang.osf.model.*;
import com.lvwang.osf.pojo.Album;
import com.lvwang.osf.pojo.Photo;
import com.lvwang.osf.pojo.Tag;
import com.lvwang.osf.pojo.User;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.lvwang.osf.util.Property;

@Transactional
@Service("albumService")
public class AlbumService extends BaseService<Album> {
	
	public static final int ALBUM_STAUS_NORMAL = 0;
    //待发布
	public static final int ALBUM_STAUS_TOBERELEASED = 1;


	private static String IMG_BASE_URL = Property.IMG_BASE_URL;

	@Autowired
	@Qualifier("userService")
	private UserService userService;
	
	@Autowired
	@Qualifier("tagService")
	private TagService tagService;
	
	@Autowired
	@Qualifier("relationService")
	private RelationService relationService;

	@Autowired
    private ApiService apiService;
	@Resource
	private PhotoService photoService;

	@Resource
	private AlbumMapper albumMapper;
	
	public String getImgType(MultipartFile img) {
		String contentType = img.getContentType();
		return contentType.substring(contentType.indexOf('/') + 1);
	}
	
	public Map<String, Object> newAlbum(int user_id, String title, String desc, int status, String cover) {
		Map<String, Object> map = new HashMap<String, Object>();
		Album album = new Album();
		album.setUserId(user_id);
		album.setAlbumTitle(title);
		album.setAlbumDesc(desc);
		album.setStatus(status);
		album.setCover(cover);
		int albumId = super.save(album);
		if(albumId != 0){
			map.put("album", album);
			map.put("status", Property.SUCCESS_ALBUM_CREATE);
		} else {
			map.put("status", Property.ERROR_ALBUM_CREATE);
		}
		return map;
	}
	
	public void saveImgToLocal(MultipartFile img, String key){
		try {
			BufferedImage imgBuf = ImageIO.read(img.getInputStream());
			String classpath = AlbumService.class.getClassLoader().getResource("").getPath();
			ImageIO.write(imgBuf, getImgType(img), new File(classpath+"/tmp/"+key));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Map<String, Object> uploadPhoto(MultipartFile img) {
		Map<String, Object> map = new HashMap<String, Object>();
        try {
            HttpResult httpResult = apiService.upload(Property.UPLOAD_URL,img);
            if (200 != httpResult.getCode()) {
                map.put("status", Property.ERROR_PHOTO_CREATE);
                return map;
            } else {
                String key = UUID.randomUUID().toString() + "." + getImgType(img);
                map.put("key",key);
                map.put("link", IMG_BASE_URL + httpResult.getData());
                map.put("status", Property.SUCCESS_PHOTO_CREATE);
                map.put("simpleLink",httpResult.getData());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
		return map;
	}
	
	public Map<String, Object> newPhoto(int album_id, MultipartFile img, String desc) {
		Map<String, Object> map = new HashMap<String, Object>();
		Photo details = new Photo();
		String key = UUID.randomUUID().toString()+"."+getImgType(img);
		details.setKey(key);
		details.setAlbumId(album_id);
		details.setDesc(desc);
//		String etag = albumDao.uploadPhoto(img, details);
        String etag = "";
		if(etag == null || etag.length() ==0) {
			map.put("status", Property.ERROR_PHOTO_CREATE);
			return map;
		} else {		
			map.put("status", Property.SUCCESS_PHOTO_CREATE);			
		}
		int photoId = photoService.save(details);
		if(photoId == 0) {
			map.put("status", Property.ERROR_PHOTO_CREATE);
			return map;
		} 
		details.setId(photoId);
		map.put("photo", details);		
		return map;
	}
	
	public List<Tag> newPhotos(Album album) {
		//save tag
		Map<String, Object> tagsmap = tagService.newTags(album.getAlbumTagsList());
		album.setAlbumTagsList((List<Tag>)tagsmap.get("tags"));
		
		updateAlbumInfo(album);
		
		//save relation 
		for(Tag tag: (List<Tag>)tagsmap.get("tags")) {
			relationService.newRelation(
					 		RelationService.RELATION_TYPE_ALBUM, 
					 		album.getId(), 
					 		tag.getId()
					 		);
		}
		List<Photo> photos = album.getPhotos();
		for(Photo photo : photos) {
			photo.setAlbumId(album.getId());
			photo.setId(photoService.save(photo));
		}
		
		return (List<Tag>)tagsmap.get("tags");
		
	}
	
	
	public String checkUserOfAlbum(int album_id, int user_id) {
		int id = albumMapper.getAlbumUser(album_id);
		if(id != user_id) {
			return Property.ERROR_ALBUM_PERMISSIONDENIED;
		} else {
			return Property.SUCCESS_ALBUM_ALLOWED;
		}
	}

	/**
	 * 获取用户的待发布相册
	 */
	public Integer getToBeReleasedAlbum(int userId) {
		return albumMapper.getAlbumID(userId, ALBUM_STAUS_TOBERELEASED);
	}

	public List<Photo> getPhotosOfAlbum(int albumId) {
		return photoService.getPhotos(albumId);
	}
	
//	public String updateAlbumDesc(int albumId, String album_desc) {
//		int effRows = albumDao.updateAlbumDesc(albumId, album_desc, ALBUM_STAUS_NORMAL);
//		if(effRows==1) {
//			return Property.SUCCESS_ALBUM_UPDATE;
//		} else {
//			return Property.ERROR_ALBUM_UPDDESC;
//		}
//	}
	
	public String updatePhotoDesc(List<Photo> photos) {
		for(Photo photo: photos) {
			photoService.updatePhotoDesc(photo.getId(), photo.getDesc());
		}
		return Property.SUCCESS_ALBUM_UPDATE;
	}
	
	public String updateAlbumCover(int album_id, String cover) {
		Album album = new Album();
		album.setId(album_id);
		album.setCover(cover);
		int effRows = super.updateSelective(album);
		if(effRows == 1) {
			return Property.SUCCESS_ALBUM_UPDATE;
		} else {
			return Property.ERROR_ALBUM_UPDCOVER;
		}
	}
	
	public String updatePhotosCount(Integer albumId, Integer count) {
		Album album = new Album();
		album.setId(albumId);
		album.setPhotosCount(count);
		int effRows = super.updateSelective(album);
		if(effRows == 1) {
			return Property.SUCCESS_ALBUM_UPDATE;
		} else {
			return Property.ERROR_ALBUM_UPDCOVER;
		}
	}
	
	public String updateAlbumInfo(Album album) {
		super.updateSelective(album);
		return Property.SUCCESS_ALBUM_UPDATE; 
	}
	
	public List<Tag> updateAlbum(Album album) {
		//save tag
		Map<String, Object> tagsmap = tagService.newTags(album.getAlbumTagsList());
		album.setAlbumTagsList((List<Tag>)tagsmap.get("tags"));
		
		updateAlbumInfo(album);
		updatePhotoDesc(album.getPhotos());
		
		//save relation 
		for(Tag tag: (List<Tag>)tagsmap.get("tags")) {
			relationService.newRelation(
					 		RelationService.RELATION_TYPE_ALBUM, 
					 		album.getId(), 
					 		tag.getId()
					 		);
		}
		return (List<Tag>)tagsmap.get("tags");
	}
	
	public List<Album> getAlbumsOfUser(int userId) {
		Example example = new Example(Album.class);
		example.createCriteria().andEqualTo("user_id", userId);
		return albumMapper.selectByExample(example);
	}
	
	public Album getAlbum(int id) {
		Album album = super.queryById(id);
		List<Photo> photos = getPhotosOfAlbum(id);
		album.setPhotos(photos);
		return album;
	}
	
	public String getKeyofPhoto(int id) {
		return photoService.getKey(id);
	}
	
	public User getAuthorOfALbum(int id) {
		int userId = albumMapper.getAuthorOfAlbum(id);
		return userService.findById(userId);
	}
	public User getAuthorOfPhoto(int id){
		Album album = albumMapper.getAlbumContainPhoto(id);
		User user = new User();
		if(album != null){
			user.setId(album.getUserId());
		}
		return user;
	}
	
	public String cropAvatar(String key, int x, int y, int width, int height) {
		String classpath = AlbumService.class.getClassLoader().getResource("").getPath();
		try {
			File ori_img = new File(classpath + "/tmp/" + key);
			
			BufferedImage croped_img = Thumbnails.of(ImageIO.read(ori_img))
									  .sourceRegion(x, y, width, height)
									  .size(200, 200).asBufferedImage();
			String img_type = key.split("\\.")[1];
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ImageIO.write(croped_img, img_type, bos);  
			
			HttpResult httpResult = apiService.upload(Property.UPLOAD_URL,bos.toByteArray(),key);
			if( httpResult.getCode() != null && httpResult.getCode() == 200){
				if(ori_img.exists()){
					ori_img.delete();
				}
				return httpResult.getData();
			} else {
				return key;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}		
		return key;
	}
	
	public void deletePhoto(int id){
		photoService.deleteById(id);
	}

}
