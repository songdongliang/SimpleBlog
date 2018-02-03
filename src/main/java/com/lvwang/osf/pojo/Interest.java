package com.lvwang.osf.pojo;

import javax.persistence.*;

@Table(name = "osf_interests")
public class Interest extends BasePojo{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	private int userId;
	private int tagId;
	
	public Interest(int userId, int tagId) {
		this.userId = userId;
		this.tagId = tagId;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getTagId() {
		return tagId;
	}
	public void setTagId(int tagId) {
		this.tagId = tagId;
	}
}
