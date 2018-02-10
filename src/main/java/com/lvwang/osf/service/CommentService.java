package com.lvwang.osf.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lvwang.osf.mappers.CommentMapper;
import com.lvwang.osf.pojo.User;
import org.springframework.stereotype.Service;

import com.lvwang.osf.pojo.Comment;
import com.lvwang.osf.util.Dic;
import com.lvwang.osf.util.Property;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Transactional
@Service("commentService")
public class CommentService extends BaseService<Comment>{

	private static final String COUNTER = "counter";

	private static final String TYPE_POST = "post";
	private static final String TYPE_PHOTO = "photo";
	private static final String TYPE_ALBUM = "album";
	private static final String TYPE_SPOST = "spost";

	/**
	 * 默认返回comment条数
	 */
	private static final int COUNT = 10;
	
	@Resource
	private CommentMapper commentMapper;
	
	@Resource
	private UserService userService;

	@Resource
	private RedisService redisService;
	
	public Map<String, String> newComment(Integer commentObjectType, Integer commentObjectId,
										  Integer commentAuthor, String commentAuthorName,
										  String commentContent, Integer commentParent,
										  int commentParentAuthor, String commentParentAuthorName){
		
		Map<String, String> ret = new HashMap<>();
		if(commentContent == null || commentContent.length() == 0) {
			ret.put("status", Property.ERROR_COMMENT_EMPTY);
			return ret;
		}
		//不支持的评论类型
		if(Dic.checkType(commentObjectType) == null){
			ret.put("status", Property.ERROR_COMMENT_TYPE);
			return ret;
		}

		Comment comment = new Comment();
		comment.setCommentObjectType(commentObjectType);
		comment.setCommentObjectId(commentObjectId);
		comment.setCommentAuthor(commentAuthor);
		comment.setCommentAuthorName(commentAuthorName);
		comment.setCommentContent(commentContent);
		comment.setCommentParent(commentParent);
		comment.setCommentParentAuthor(commentParentAuthor);
		comment.setCommentParentAuthorName(commentParentAuthorName);
		super.save(comment);
		ret.put("status", Property.SUCCESS_COMMENT_CREATE);
		ret.put("id", String.valueOf(comment.getId()));
		return ret;
		
	}
	
	public Comment findCommentByID(int id) {
		return queryById(id);
	}
	
	public List<Comment> getComments(String type, int id) {
		return getComments(type, id, 0, COUNT);
	}
	
	public List<Comment> getComments(String type, int id, int offset, int count) {
		if(type == null || type.length() == 0) {
			return null;
		}
		List<Comment> comments = null;
		int commentType = 0;
		if(type.equals(TYPE_PHOTO)) {
			commentType = 1;
		} else if(type.equals(TYPE_ALBUM)){
			commentType = 2;
		} else if(type.equals(TYPE_SPOST)){
			commentType = 4;
		}
		comments = commentMapper.getCommentsByType(id,commentType,offset,count);
		//add avatars;
		if(comments != null && comments.size() !=0) {
			for(Comment comment: comments) {
				comment.setCommentAuthorAvatar(userService.findById(comment.getCommentAuthor()).getUserAvatar());
			}
		}
		return comments;
	}
	
	public int getCommentsCount(int object_type, int object_id) {
		String type = Dic.checkType(object_type);
		if(type == null) {
			return 0;
		}
		String count = redisService.hget(COUNTER, "comment:" + type + ":" + object_id);
		return count == null ? 0 : Integer.parseInt(count);
	}

	public User getCommentAuthor(int comment_id){
		Comment comment = commentMapper.selectByPrimaryKey(comment_id);
		User user = new User();
		user.setId(comment.getCommentAuthor());
		user.setUserName(comment.getCommentAuthorName());
		return user;
	}
}
