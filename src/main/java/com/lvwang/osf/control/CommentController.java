package com.lvwang.osf.control;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.lvwang.osf.pojo.Comment;
import com.lvwang.osf.pojo.Notification;
import com.lvwang.osf.pojo.User;
import com.lvwang.osf.service.CommentService;
import com.lvwang.osf.service.NotificationService;
import com.lvwang.osf.service.PostService;
import com.lvwang.osf.service.UserService;
import com.lvwang.osf.util.Dic;
import com.lvwang.osf.util.Property;

@Controller
@RequestMapping("/comment")
public class CommentController {
	
	@Autowired
	@Qualifier("commentService")
	private CommentService commentService;
	
	@Autowired
	@Qualifier("userService")
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
	public Map<String, String> createComment(@RequestParam("comment_object_type") int comment_object_type,
											 @RequestParam("comment_object_id") int comment_object_id,
											 @RequestParam("comment_content") String comment_content,
											 @RequestParam("comment_parent") int comment_parent,
											 HttpSession session) {
		User user = (User)session.getAttribute("user");
		User commentParentAuthor = new User();
		if(comment_parent != 0 ){
			commentParentAuthor = commentService.getCommentAuthor(comment_parent);
		}
		
		Map<String, String> ret = commentService.newComment(comment_object_type, 
															comment_object_id, 
															user.getId(), 
															user.getUserName(),
															comment_content, 
															comment_parent,
															commentParentAuthor.getId(),
															commentParentAuthor.getUserName());
		Notification notification =  new Notification(Dic.NOTIFY_TYPE_COMMENT,
													  Integer.parseInt(ret.get("id")),
													  comment_object_type,
													  comment_object_id,
													  userService.getAuthor(comment_object_type, comment_object_id).getId(),
													  user.getId()
													  );
		
		
		if(comment_parent!=0) {
			//reply notification
			notification.setNotifyType(Dic.NOTIFY_TYPE_COMMENT_REPLY);
			notification.setNotifiedUser(commentParentAuthor.getId());
			notificationService.doNotify(notification);
		} else {
			//comment notification
			notificationService.doNotify(notification);
		}
		
		
		ret.put("avatar", userService.findById(user.getId()).getUserAvatar());
		ret.put("author_id", String.valueOf(user.getId()));
		ret.put("author_name", user.getUserName());
		ret.put("reply_to_author", String.valueOf(commentParentAuthor.getId()));
		ret.put("reply_to_authorname", commentParentAuthor.getUserName());
		return ret;
	}
	
	@RequestMapping(value="/{type}/{id}")
	public ModelAndView getComments(@PathVariable("type") String type, @PathVariable("id") int id) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("comment/index");
		mav.addObject("comments", commentService.getComments(type, id));
		return mav;
	}
	
	/**
	 * feed附属的comments
	 * @param type
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/attach/{type}/{id}")
	public ModelAndView getAttachComments(@PathVariable("type") String type, @PathVariable("id") int id) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("comment/attach_comments");
		mav.addObject("comments", commentService.getComments(type, id, 0, 5));
		return mav;
	}
	
}
