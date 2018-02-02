package com.lvwang.osf.pojo;

import javax.persistence.*;
import java.util.Date;

@Table(name = "osf_followers")
public class Follower extends BasePojo{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
	private int id;

	private int userId;
	private String userName;
	private int followerUserId;
	private String followerUserName;
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
	public Date getTs() {
		return ts;
	}
	public void setTs(Date ts) {
		this.ts = ts;
	}

}
