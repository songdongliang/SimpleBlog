package com.lvwang.osf.pojo;

import javax.persistence.*;
import java.sql.Timestamp;

@Table(name = "osf_notifications")
public class Notification extends BasePojo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
    /**
     * 通知类型 Dic里有定义
     */
	private int notifyType;
	/**
     * 通告对象ID,如评论的ID
	 */
	private int notifyId;
    /**
     * 被通告的对象类型 Dic里有定义
     */
	private int objectType;
    /**
     * 被通告对象的ID
     */
	private int objectId;
	/**
	 * 被通告的用户
	 */
	private int notifiedUser;
	/**
	 * 通告者
	 */
	private int notifier;
	/**
	 * 通知时间戳
	 */
	private Timestamp ts;
	/**
	 * 状态 0未读 1已读
	 */
	private int status;

	/**
	 * 以下属性用于通知展现
	 */
	@Transient
	private String notifierName;
	@Transient
	private String notifierAvatar;
	@Transient
	private String objectTitle;
	
	
	public Notification(){
		
	}
	
	public Notification(int notifyType, int notifyId, int objectType, int objectId, int notifiedUser, int notifier){
		this.notifiedUser = notifiedUser;
		this.notifyType = notifyType;
		this.notifyId = notifyId;
		this.objectId = objectId;
		this.objectType = objectType;
		this.notifier = notifier;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getNotifyType() {
		return notifyType;
	}
	public void setNotifyType(int notifyType) {
		this.notifyType = notifyType;
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
	public int getNotifiedUser() {
		return notifiedUser;
	}
	public void setNotifiedUser(int notifiedUser) {
		this.notifiedUser = notifiedUser;
	}
	public int getNotifier() {
		return notifier;
	}
	public void setNotifier(int notifier) {
		this.notifier = notifier;
	}

	public int getNotifyId() {
		return notifyId;
	}

	public void setNotifyId(int notifyId) {
		this.notifyId = notifyId;
	}

	public Timestamp getTs() {
		return ts;
	}

	public void setTs(Timestamp ts) {
		this.ts = ts;
	}

	public String getNotifierName() {
		return notifierName;
	}

	public void setNotifierName(String notifierName) {
		this.notifierName = notifierName;
	}

	public String getNotifierAvatar() {
		return notifierAvatar;
	}

	public void setNotifierAvatar(String notifierAvatar) {
		this.notifierAvatar = notifierAvatar;
	}

	public String getObjectTitle() {
		return objectTitle;
	}

	public void setObjectTitle(String objectTitle) {
		this.objectTitle = objectTitle;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
}
