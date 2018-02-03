package com.lvwang.osf.pojo;

import javax.persistence.*;
import java.util.Date;

@Table(name = "osf_relations")
public class Relation extends BasePojo{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	private int objectType;

	private int objectId;

	private int tagId;

	private Date addTs;
	
	public Relation() {
		
	}
	
	public Relation(int objectType, int objectId, int tagId) {
		this.objectType = objectType;
		this.objectId = objectId;
		this.tagId = tagId;
	}
	
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
	public int getTagId() {
		return tagId;
	}
	public void setTagId(int tagId) {
		this.tagId = tagId;
	}
	public Date getAddTs() {
		return addTs;
	}
	public void setAddTs(Date addTs) {
		this.addTs = addTs;
	}
}
