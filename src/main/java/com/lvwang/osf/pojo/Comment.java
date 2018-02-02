package com.lvwang.osf.pojo;

import javax.persistence.*;
import java.util.Date;

@Table(name = "osf_comments")
public class Comment extends BasePojo{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
	private int id;

	private int commentObjectType;

	private int commentObjectId;

	private int commentAuthor;

	private String commentAuthorName;

	private String commentAuthorAvatar;

	private Date commentTs;

	private String commentContent;

	private int commentParent;

	private int commentParentAuthor;

	private String commentParentAuthorName;

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getCommentObjectType() {
		return commentObjectType;
	}
	public void setCommentObjectType(int commentObjectType) {
		this.commentObjectType = commentObjectType;
	}
	public int getCommentObjectId() {
		return commentObjectId;
	}
	public void setCommentObjectId(int commentObjectId) {
		this.commentObjectId = commentObjectId;
	}
	public int getCommentAuthor() {
		return commentAuthor;
	}
	public void setCommentAuthor(int commentAuthor) {
		this.commentAuthor = commentAuthor;
	}

	public Date getCommentTs() {
		return commentTs;
	}
	public void setCommentTs(Date commentTs) {
		this.commentTs = commentTs;
	}
	public String getCommentContent() {
		return commentContent;
	}
	public void setCommentContent(String commentContent) {
		this.commentContent = commentContent;
	}
	public int getCommentParent() {
		return commentParent;
	}
	public void setCommentParent(int commentParent) {
		this.commentParent = commentParent;
	}
	public String getCommentAuthorName() {
		return commentAuthorName;
	}
	public void setCommentAuthorName(String commentAuthorName) {
		this.commentAuthorName = commentAuthorName;
	}

	public String getCommentAuthorAvatar() {
		return commentAuthorAvatar;
	}
	public void setCommentAuthorAvatar(String commentAuthorAvatar) {
		this.commentAuthorAvatar = commentAuthorAvatar;
	}
	public String getCommentParentAuthorName() {
		return commentParentAuthorName;
	}
	public void setCommentParentAuthorName(String commentParentAuthorName) {
		this.commentParentAuthorName = commentParentAuthorName;
	}
	public int getCommentParentAuthor() {
		return commentParentAuthor;
	}
	public void setCommentParentAuthor(int commentParentAuthor) {
		this.commentParentAuthor = commentParentAuthor;
	}
}
