package com.lvwang.osf.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.abel533.entity.Example;
import com.lvwang.osf.mappers.RelationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lvwang.osf.pojo.Relation;
import com.lvwang.osf.pojo.Tag;
import com.lvwang.osf.util.Property;

@Service("relationService")
public class RelationService extends BaseService<Relation> {
	
	public static final int RELATION_TYPE_POST = 0;
	public static final int RELATION_TYPE_PHOTO = 1;
	public static final int RELATION_TYPE_ALBUM = 2;
	
	@Autowired
	@Qualifier("tagService")
	private TagService tagService;
	
	@Autowired
	private RelationMapper relationMapper;
	
	@Transactional
	public Map<String, Object> newRelation(int objectType, int objectId, int tagId) {
		Map<String, Object> ret = new HashMap<>();
		int relationId = super.save(new Relation(objectType, objectId, tagId));
		if(relationId != 0){
			Relation relation = new Relation();
			relation.setId(relationId);
			relation.setObjectType(objectType);
			relation.setObjectId(objectId);
			relation.setTagId(tagId);
			ret.put("relation", relation);
			ret.put("status", Property.SUCCESS_RELATION_CREATE);
		} else {
			ret.put("status", Property.ERROR_RELATION_CREATE);
		}
		return ret;
	}
	
	/**
	 * @param tag
	 * @return
	 */
	public List<Relation> getRelationsWithTag(String tag){
		List<Relation> relations = new ArrayList<Relation>();
		int tagId = tagService.getID(tag);
		if(tagId != 0) {
			Example example = new Example(Relation.class);
			example.createCriteria().andEqualTo("tag_id",tagId);
			example.setOrderByClause("add_ts");
			List<Relation> rels = relationMapper.selectByExample(example);
			if(rels != null) {
				relations = rels;
			}
		}
		return relations;
	}
	
	/**
	 * 获取有列表中tag的关联关系
	 * 
	 * @param tags
	 * @return
	 */
	public List<Relation> getRelationsInTags(List<Tag> tags){
		if(tags == null || tags.size() == 0) {
			return new ArrayList<>();
		}
		List<String> tagIds = new ArrayList<>();
		for(Tag tag : tags) {
			tagIds.add(String.valueOf(tag.getId()));
		}
		return relationMapper.getRelationsInTags(tagIds);
	}
	
}
