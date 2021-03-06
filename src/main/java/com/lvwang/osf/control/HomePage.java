package com.lvwang.osf.control;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.lvwang.osf.pojo.Event;
import com.lvwang.osf.pojo.Tag;
import com.lvwang.osf.pojo.User;
import com.lvwang.osf.service.EventService;
import com.lvwang.osf.service.FeedService;
import com.lvwang.osf.service.FollowService;
import com.lvwang.osf.service.InterestService;
import com.lvwang.osf.service.TagService;
import com.lvwang.osf.service.UserService;
import com.lvwang.osf.util.Dic;


@Controller
public class HomePage {

	@Resource
	private EventService eventService;
	
	@Autowired
	@Qualifier("feedService")
	private FeedService feedService;
	
	@Autowired
	@Qualifier("userService")
	private UserService userService;
	
	@Autowired
	@Qualifier("followService")
	private FollowService followService;
	
	@Autowired
	@Qualifier("tagService")
	private TagService tagService;
	
	@Autowired
	@Qualifier("interestService")
	private InterestService interestService;
	
	@RequestMapping("/")
	public ModelAndView showHomePage(HttpSession session) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("index");
        List<Event> feeds = eventService.queryAll();
        for(Event event : feeds) {
            User user = userService.findById(event.getUserId());
            event.setUserName(user.getUserName());
            event.setUserAvatar(user.getUserAvatar());
        }
        mav.addObject("feeds", feeds);
        mav.addObject("dic", new Dic());
		User user = (User)session.getAttribute("user");
		if(user == null) {
			return mav;
		}
		mav.addObject("counter", userService.getCounterOfFollowAndShortPost(user.getId()));
//		List<Event> feeds = feedService.getFeeds(user.getId());

		return mav;
		
	}
	
	@RequestMapping("/popup_usercard/{user_id}")
	public ModelAndView getPopupUserCard(@PathVariable("user_id") String userId){
		ModelAndView mav = new ModelAndView();
		mav.setViewName("popup_usercard");
		User user = userService.findById(Integer.valueOf(userId));
		if(user != null) {
			mav.addObject("u", user);
			mav.addObject("counter", userService.getCounterOfFollowAndShortPost(Integer.valueOf(userId)));
		}
		
		return mav;
	}

	@RequestMapping("/page/{num}")
	public ModelAndView nextPage(@PathVariable("num") String num_str, HttpSession session) {
		System.out.println(num_str);
		
		User user = (User)session.getAttribute("user");
		if(user == null) {
			return null;
		}
		
		ModelAndView mav = new ModelAndView();
		mav.setViewName("nextpage");
		
		int num = Integer.parseInt(num_str);
		List<Event> feeds = feedService.getFeedsOfPage(user.getId(), num);
		mav.addObject("feeds", feeds);
		mav.addObject("dic", new Dic());
		return mav;
	}
	
	@RequestMapping("/welcome")
	public ModelAndView welcome(HttpSession session) {		
		ModelAndView mav = new ModelAndView();
		if(session.getAttribute("user") != null){
			mav.setViewName("redirect:/");
			return mav;
		}
		
		mav.setViewName("welcome");
		List<Tag> recommendTags = tagService.getRecommendTags();
		mav.addObject("tags", recommendTags);
		mav.addObject("dic", new Dic());
		return mav;
	}
	
	@RequestMapping("/sidebar")
	public ModelAndView sideBar(HttpSession session){
		ModelAndView mav = new ModelAndView();
		mav.setViewName("sidebar");
		User user = (User)session.getAttribute("user");

		List<User> recommendUsers = userService.getRecommendUsers(user == null ? 0 : user.getId(), 4);
		mav.addObject("isFollowings", followService.isFollowing(user == null ? 0:user.getId(), recommendUsers));
		mav.addObject("popusers", recommendUsers);
				
		List<Tag> recommendTags = tagService.getRecommendTags();
		mav.addObject("poptags", recommendTags);
		
		return mav;
	}
	
	@RequestMapping("/guide")
	public ModelAndView guide(HttpSession session){
		ModelAndView mav = new ModelAndView();
		mav.setViewName("guide");
		
		User user = (User) session.getAttribute("user");
		
		List<Tag> tags_recommend = tagService.getRecommendTags();
		mav.addObject("tags", tags_recommend);
		mav.addObject("isInterests", interestService.hasInterestInTags(user==null?0:user.getId(), tags_recommend));
		
		mav.addObject("dic", new Dic());
		return mav;
	}
	
	/**
	 * 新用户兴趣选择之后 feed初始化
	 * @param session
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/guide/ok")
	public Map<String, Object> guideOk(HttpSession session){
		Map<String, Object> map = new HashMap<String, Object>();
		User user = (User) session.getAttribute("user");
		feedService.coldStart(user.getId());
		
		return map;
	}
	
	@RequestMapping("/followers")
	public ModelAndView getFollowers(HttpSession session){
		ModelAndView mav = new ModelAndView();
		mav.addObject("followers", userService.findByIDs(
								   followService.getFollowerIDs(
										   ((User)session.getAttribute("user")).getId())
								   ));
		
		mav.setViewName("follower");
		return mav;
	}
	
	@RequestMapping("/followings")
	public ModelAndView getFollowings(HttpSession session){
		ModelAndView mav = new ModelAndView();
		mav.addObject("followings", userService.findByIDs(
									followService.getFollowingIDs(
											((User)session.getAttribute("user")).getId())
									));
		
		mav.setViewName("following");
		return mav;
	}
	
	@RequestMapping("/404")
	public ModelAndView pageNotFound() {
		return new ModelAndView("404");
	}
	
	@RequestMapping("/500")
	public ModelAndView error500() {
		return new ModelAndView("500");
	}
}
