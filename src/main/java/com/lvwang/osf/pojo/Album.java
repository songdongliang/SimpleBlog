package com.lvwang.osf.pojo;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Table(name = "osf_albums")
public class Album extends BasePojo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
	private Integer id;

	private Integer userId;
	private Date createTs;
	private String albumTitle;
	private String albumDesc;
	private Date lastAddTs;
	private Integer photosCount;
	private Integer status;
	private String cover;
	private Integer likeCount;
	private Integer shareCount;
	private Integer commentCount;
	private List<Photo> photos;
	private List<Tag> albumTagsList;
	private String albumTags;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public Date getCreateTs() {
		return createTs;
	}
	public void setCreateTs(Date createTs) {
		this.createTs = createTs;
	}
	public String getAlbumTitle() {
		return albumTitle;
	}
	public void setAlbumTitle(String albumTitle) {
		this.albumTitle = albumTitle;
	}
	public String getAlbumDesc() {
		return albumDesc;
	}
	public void setAlbumDesc(String albumDesc) {
		this.albumDesc = albumDesc;
	}
	public Date getLastAddTs() {
		return lastAddTs;
	}
	public void setLastAddTs(Date lastAddTs) {
		this.lastAddTs = lastAddTs;
	}
	public Integer getPhotosCount() {
		return photosCount;
	}
	public void setPhotosCount(Integer photosCount) {
		this.photosCount = photosCount;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getCover() {
		return cover;
	}
	public void setCover(String cover) {
		this.cover = cover;
	}
	public List<Photo> getPhotos() {
		return photos;
	}
	public void setPhotos(List<Photo> photos) {
		this.photos = photos;
	}
	public Integer getLikeCount() {
		return likeCount;
	}
	public void setLikeCount(Integer likeCount) {
		this.likeCount = likeCount;
	}
	public Integer getShareCount() {
		return shareCount;
	}
	public void setShareCount(Integer shareCount) {
		this.shareCount = shareCount;
	}
	public Integer getCommentCount() {
		return commentCount;
	}
	public void setCommentCount(Integer commentCount) {
		this.commentCount = commentCount;
	}
	public List<Tag> getAlbumTagsList() {
		return albumTagsList;
	}
	public void setAlbumTagsList(List<Tag> albumTagsList) {
		this.albumTagsList = albumTagsList;
	}
	public String getAlbumTags() {
		return albumTags;
	}
	public void setAlbumTags(String albumTags) {
		this.albumTags = albumTags;
	}
}
