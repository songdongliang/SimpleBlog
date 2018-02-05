package com.lvwang.osf.pojo;

import java.util.Date;

import com.lvwang.osf.search.Searchable;

import javax.persistence.*;

@Table(name = "osf_users")
public class User extends BasePojo implements Searchable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	private String userName;
	private String userEmail;
	private String userPwd;

	@Transient
	private String userConfirmPwd;
	private Date userRegisteredDate;
	private Integer userStatus;
	private String userAvatar;

	@Column(name = "user_activationKey")
	private String userActivationKey;
	private String userDesc;

	@Override
	public boolean equals(Object that){
		User user = (User)that;
		return this.id.equals(user.getId());
	}

	@Override
	public int hashCode() {
		return this.id;
	}
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserEmail() {
		return userEmail;
	}
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	public String getUserPwd() {
		return userPwd;
	}
	public void setUserPwd(String userPwd) {
		this.userPwd = userPwd;
	}

	public Date getUserRegisteredDate() {
		return userRegisteredDate;
	}
	public void setUserRegisteredDate(Date userRegisteredDate) {
		this.userRegisteredDate = userRegisteredDate;
	}
	public Integer getUserStatus() {
		return userStatus;
	}
	public void setUserStatus(Integer userStatus) {
		this.userStatus = userStatus;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getUserActivationKey() {
		return userActivationKey;
	}
	public void setUserActivationKey(String userActivationKey) {
		this.userActivationKey = userActivationKey;
	}
	public String getUserAvatar() {
		return userAvatar;
	}
	public void setUserAvatar(String userAvatar) {
		this.userAvatar = userAvatar;
	}
	public String getUserDesc() {
		return userDesc;
	}
	public void setUserDesc(String userDesc) {
		this.userDesc = userDesc;
	}
	public String getUserConfirmPwd() {
		return userConfirmPwd;
	}
	public void setUserConfirmPwd(String userConfirmPwd) {
		this.userConfirmPwd = userConfirmPwd;
	}
}
