package com.lvwang.osf.control;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

import com.lvwang.osf.pojo.Post;
import com.lvwang.osf.pojo.Tag;
import com.lvwang.osf.pojo.User;
import com.lvwang.osf.service.EventService;
import com.lvwang.osf.service.FeedService;
import com.lvwang.osf.service.FollowService;
import com.lvwang.osf.service.InterestService;
import com.lvwang.osf.service.LikeService;
import com.lvwang.osf.service.PostService;
import com.lvwang.osf.util.Dic;
import com.lvwang.osf.util.Property;

@Controller
@RequestMapping("/post")
public class PostController {
	
	@Autowired
	@Qualifier("postService")
	private PostService postService;
	
	@Autowired
	@Qualifier("eventService")
	private EventService eventService;
	
	@Autowired
	@Qualifier("feedService")
	private FeedService feedService;
	
	@Autowired
	@Qualifier("interestService")
	private InterestService interestService;
	
	@Autowired
	@Qualifier("followService")
	private FollowService followService;
	
	@Autowired
	@Qualifier("likeService")
	private LikeService likeService;
	
	@RequestMapping("/{id}")
	public ModelAndView post(@PathVariable("id") int id, HttpSession session) {
		User me = (User) session.getAttribute("user");
		
		ModelAndView mav = new ModelAndView();
		User author = postService.getAuthorOfPost(id);
		mav.addObject("u", author);
		mav.addObject("follow", followService.isFollowing(me==null?0:me.getId(), author.getId()));
		mav.addObject("is_like", likeService.isLike(me==null?0:me.getId(), Dic.OBJECT_TYPE_POST, id));
		mav.addObject("post", postService.findPostByID(id));
		mav.setViewName("post/index");
		return mav;
	}
	
	@RequestMapping(value="/create", method=RequestMethod.GET)
	public String createPost() {
		return "post/create";
	}
	
	@ResponseBody
	@RequestMapping(value="/create", method=RequestMethod.POST)
	public Map<String, Object> createPost(					
						@RequestParam("title") String title,
						@RequestParam("content") String content,
						@RequestParam("postStatus") int postStatus,
						@RequestParam("commentStatus") int commentStatus,
						@RequestParam("tags") String paramTags,
						HttpSession session) {
				
		User user = (User)session.getAttribute("user");
		String postCover = (String) session.getAttribute("post_cover");
		session.removeAttribute("post_cover");
		//1 save post
		Map<String, Object> map = postService
				.newPost(user.getId(), title, content, postStatus, commentStatus, paramTags, postCover);
		String status = (String)map.get("status");
		Post post = (Post)map.get("post");

		//2 add event 
		if(Property.SUCCESS_POST_CREATE.equals(status)) {
			int eventId = eventService.newEvent(Dic.OBJECT_TYPE_POST, post);
			
			//3 push to followers
			List<Integer> followers = followService.getFollowerIDs(user.getId());
			followers.add(user.getId());
			feedService.push(followers, eventId);
			
			//4 push to users who follow the tags in the post
			List<Tag> tags = (ArrayList<Tag>)map.get("tags");
			//push to users who follow the tags
			Set<Integer> followersSet = new HashSet<>();
			for(Tag tag : tags) {
				List<Integer> userIds = interestService.getUsersInterestedInTag(tag.getId());
				for(int u: userIds) {
					if(u != user.getId())
						followersSet.add(u);
				}
				//cache feeds to tag list
				feedService.cacheFeed2Tag(tag.getId(), eventId);
			}
			feedService.push(new ArrayList<>(followersSet), eventId);
			
		}
		return map;
		
	}
	
	@ResponseBody
	@RequestMapping("/delete/{id}")
	public Map<String, Object> deletePost(@PathVariable("id") int id){
		Map<String, Object> map = new HashMap<>();
		postService.deletePost(id);
		map.put("status", Property.SUCCESS_POST_DELETE);
		return map;
	}
	
	
}
