package com.lvwang.osf.pojo;

import com.lvwang.osf.search.Searchable;

import java.util.Date;

import javax.persistence.*;

@Table(name = "osf_tags")
public class Tag extends BasePojo implements Searchable{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	private String tag;

	private Date addTs;

	private String cover;
	
	public Tag() {
		
	}
	
	public Tag(String tag) {
		this.tag = tag;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public Date getAddTs() {
		return addTs;
	}
	public void setAddTs(Date addTs) {
		this.addTs = addTs;
	}
	public String getCover() {
		return cover;
	}
	public void setCover(String cover) {
		this.cover = cover;
	}
}
