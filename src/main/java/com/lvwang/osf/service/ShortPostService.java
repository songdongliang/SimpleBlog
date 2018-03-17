package com.lvwang.osf.service;

import java.util.HashMap;
import java.util.Map;

import com.lvwang.osf.mappers.PostMapper;
import com.lvwang.osf.mappers.ShortPostMapper;
import com.lvwang.osf.pojo.User;
import org.springframework.stereotype.Service;

import com.lvwang.osf.pojo.ShortPost;
import com.lvwang.osf.util.Property;

import javax.annotation.Resource;

@Service("shortPostService")
public class ShortPostService extends BaseService<ShortPost> {

	@Resource
	private PostService postService;

	public Map<String, Object> newPost(Integer author, String content){
		Map<String, Object> map = new HashMap<>();
		if(content == null || content.length() == 0){
			map.put("status", Property.ERROR_POST_EMPTY);
			return map;
		}
		ShortPost spost = new ShortPost();
		spost.setPostAuthor(author);
		spost.setPostContent(content);
		postService.save(spost);
		map.put("spost", spost);
		map.put("status", Property.SUCCESS_POST_CREATE);
		return map;
	}

	public User getAuthorOfPost(int id) {
		return postService.getAuthorOfPost(id);
	}
	
	public long count(int userId){
		return postService.count(userId);
	}

}
