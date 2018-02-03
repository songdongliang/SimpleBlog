package com.lvwang.osf.control;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lvwang.osf.pojo.Comment;
import com.lvwang.osf.pojo.Notification;
import com.lvwang.osf.pojo.User;
import com.lvwang.osf.service.CommentService;
import com.lvwang.osf.service.NotificationService;
import com.lvwang.osf.service.PostService;
import com.lvwang.osf.service.UserService;
import com.lvwang.osf.util.Dic;
import com.lvwang.osf.util.Property;
import com.lvwang.osf.web.RequestAttribute;

@Controller
@RequestMapping("/api/v1/comment") 
public class CommentAPI {
	
	@Autowired
	@Qualifier("commentService")
	private CommentService commentService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	@Qualifier("notificationService")
	private NotificationService notificationService;
	 
	@Autowired
	@Qualifier("postService")
	private PostService postService;
	
	@ResponseBody
	@RequestMapping("/{id}")
	public Map<String, Object> comment(@PathVariable("id") int id) {
		Comment comment = commentService.findCommentByID(id);
		Map<String, Object> ret = new HashMap<String, Object>();
		if(comment == null) {
			ret.put("status", Property.ERROR);
		}else {
			ret.put("status", Property.SUCCESS);
			ret.put("comment", comment);
		}
		return ret;
	}
	
	@ResponseBody
	@RequestMapping(value="/create", method=RequestMethod.POST)	
	public Map<String, String> createComment(@RequestBody Comment comment,
											 @RequestAttribute("uid") Integer id) {
		User user = (User)userService.findById(id);
		User comment_parent_author = new User();
		if(comment.getCommentParent() !=0 ){
			comment_parent_author = commentService.getCommentAuthor(comment.getCommentParent());
		}
		
		Map<String, String> ret = commentService.newComment(comment.getCommentObjectType(),
															comment.getCommentObjectId(),
															user.getId(), 
															user.getUserName(),
															comment.getCommentContent(),
															comment.getCommentParent(),
															comment_parent_author.getId(),
															comment_parent_author.getUserName());
		Notification notification =  new Notification(Dic.NOTIFY_TYPE_COMMENT,
													  Integer.parseInt(ret.get("id")),
													  comment.getCommentObjectType(),
													  comment.getCommentObjectId(),
													  userService.getAuthor(comment.getCommentObjectType(),
															  				comment.getCommentObjectId()).getId(),
													  user.getId()
													  );
		
		
		if(comment.getCommentParent()!=0) {
			//reply notification
			notification.setNotifyType(Dic.NOTIFY_TYPE_COMMENT_REPLY);
			notification.setNotifiedUser(comment_parent_author.getId());
			notificationService.doNotify(notification);
		} else {
			//comment notification
			notificationService.doNotify(notification);
		}
		
		
		ret.put("avatar", userService.findById(user.getId()).getUserAvatar());
		ret.put("author_id", String.valueOf(user.getId()));
		ret.put("author_name", user.getUserName());
		ret.put("reply_to_author", String.valueOf(comment_parent_author.getId()));
		ret.put("reply_to_authorname", comment_parent_author.getUserName());
		return ret;
	}

	/**
	 * feed附属的comments
	 * @param type
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/{type}/{id}")
	public Map<String, List<Comment>> getComments(@PathVariable("type") String type, @PathVariable("id") int id) {
		System.out.println("type:"+type + " id:"+id);
		Map<String, List<Comment>> comments = new TreeMap<String, List<Comment>>();
		comments.put("comments", commentService.getComments(type, id));
		return comments;
	}
	

}
