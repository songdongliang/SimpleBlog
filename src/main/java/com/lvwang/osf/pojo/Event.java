package com.lvwang.osf.pojo;

import java.util.Date;
import java.util.List;

import com.lvwang.osf.search.Searchable;
import com.lvwang.osf.service.TagService;

import javax.persistence.*;

@Table(name = "osf_events")
public class Event extends BasePojo implements Searchable{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	private int objectType;

	private int objectId;

	private Date ts;

	private int userId;

	private String userName;

	private String userAvatar;

	private int likeCount;

	private int shareCount;

	private int commentCount;
	private String title;
	private String summary;
	private String content;

	@Transient
	private List<Tag> tags;
	@Transient
	private String tagsStr;
	private int followingUserId;
	private String followingUserName;
	private int followerUserId;
	private String followerUserName;

	@Transient
	private boolean isLike;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getObjectType() {
		return objectType;
	}
	public void setObjectType(int objectType) {
		this.objectType = objectType;
	}
	public int getObjectId() {
		return objectId;
	}
	public void setObjectId(int objectId) {
		this.objectId = objectId;
	}
	public Date getTs() {
		return ts;
	}
	public void setTs(Date ts) {
		this.ts = ts;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserAvatar() {
		return userAvatar;
	}
	public void setUserAvatar(String userAvatar) {
		this.userAvatar = userAvatar;
	}
	public int getLikeCount() {
		return likeCount;
	}
	public void setLikeCount(int likeCount) {
		this.likeCount = likeCount;
	}
	public int getShareCount() {
		return shareCount;
	}
	public void setShareCount(int shareCount) {
		this.shareCount = shareCount;
	}
	public int getCommentCount() {
		return commentCount;
	}
	public void setCommentCount(int commentCount) {
		this.commentCount = commentCount;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public List<Tag> getTags() {
		return tags;
	}
	public String getTagsStr(){
		return TagService.toString(tags);
	}
	public void setTags(String tags) {
		this.tags = TagService.toList(tags);
	}
	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}
	public int getFollowingUserId() {
		return followingUserId;
	}
	public void setFollowingUserId(int followingUserId) {
		this.followingUserId = followingUserId;
	}
	public String getFollowingUserName() {
		return followingUserName;
	}
	public void setFollowingUserName(String followingUserName) {
		this.followingUserName = followingUserName;
	}
	public int getFollowerUserId() {
		return followerUserId;
	}
	public void setFollowerUserId(int followerUserId) {
		this.followerUserId = followerUserId;
	}
	public String getFollowerUserName() {
		return followerUserName;
	}
	public void setFollowerUserName(String followerUserName) {
		this.followerUserName = followerUserName;
	}
	public boolean isLike() {
		return isLike;
	}
	public void setLike(boolean like) {
		this.isLike = like;
	}
}
