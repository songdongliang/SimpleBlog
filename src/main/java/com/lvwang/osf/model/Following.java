package com.lvwang.osf.model;

import com.lvwang.osf.pojo.BasePojo;

import javax.persistence.*;
import java.util.Date;


@Table(name = "osf_follows")
public class Following extends BasePojo {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	private int userId;
	private String userName;
	private int followingUserId;
	private String followingUserName;

	private Date ts;
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
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
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
	public Date getTs() {
		return ts;
	}
	public void setTs(Date ts) {
		this.ts = ts;
	}
}
