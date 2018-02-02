package com.lvwang.osf.pojo;

import com.github.abel533.mapper.BaseMapper;
import com.lvwang.osf.model.Tag;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Table(name = "osf_posts")
public class Post extends BasePojo {

	private static final long serialVersionUID = 1041319130842178407L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	private int postAuthor;
	private Date postTs;
	private String postContent;
	private String postTitle;
	private String postExcerpt;
	private int postStatus;
	private int commentStatus;
	private String postPwd;
	private Date postLastts;
	private int likeCount;
	private int shareCount;
	private int commentCount;
	private String postUrl;
	private String postTags;
	private List<Tag> postTagsList;
	private int postAlbum;
	private String postCover;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getPostAuthor() {
		return postAuthor;
	}
	public void setPostAuthor(int postAuthor) {
		this.postAuthor = postAuthor;
	}
	public Date getPostTs() {
		return postTs;
	}
	public void setPostTs(Date postTs) {
		this.postTs = postTs;
	}
	public String getPostContent() {
		return postContent;
	}
	public void setPostContent(String postContent) {
		this.postContent = postContent;
	}
	public String getPostTitle() {
		return postTitle;
	}
	public void setPostTitle(String postTitle) {
		this.postTitle = postTitle;
	}
	public String getPostExcerpt() {
		return postExcerpt;
	}
	public void setPostExcerpt(String postExcerpt) {
		this.postExcerpt = postExcerpt;
	}
	public int getPostStatus() {
		return postStatus;
	}
	public void setPostStatus(int postStatus) {
		this.postStatus = postStatus;
	}
	public int getCommentStatus() {
		return commentStatus;
	}
	public void setCommentStatus(int commentStatus) {
		this.commentStatus = commentStatus;
	}
	public String getPostPwd() {
		return postPwd;
	}
	public void setPostPwd(String postPwd) {
		this.postPwd = postPwd;
	}
	public Date getPostLastts() {
		return postLastts;
	}
	public void setPostLastts(Date postLastts) {
		this.postLastts = postLastts;
	}
	public int getCommentCount() {
		return commentCount;
	}
	public void setCommentCount(int commentCount) {
		this.commentCount = commentCount;
	}
	public String getPostUrl() {
		return postUrl;
	}
	public void setPostUrl(String postUrl) {
		this.postUrl = postUrl;
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
	public List<Tag> getPostTagsList() {
		return postTagsList;
	}
	public void setPostTagsList(List<Tag> postTagsList) {
		this.postTagsList = postTagsList;
	}
	public int getPostAlbum() {
		return postAlbum;
	}
	public void setPostAlbum(int postAlbum) {
		this.postAlbum = postAlbum;
	}
	public String getPostCover() {
		return postCover;
	}
	public void setPostCover(String postCover) {
		this.postCover = postCover;
	}
	public String getPostTags() {
		return postTags;
	}
	public void setPostTags(String postTags) {
		this.postTags = postTags;
	}

}
