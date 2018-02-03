package com.lvwang.osf.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.lvwang.osf.pojo.Event;
import com.lvwang.osf.pojo.Relation;
import com.lvwang.osf.pojo.Tag;
import com.lvwang.osf.pojo.User;
import com.lvwang.osf.search.EventIndexService;

@Service("feedService")
public class FeedService {

    private static final String FEED_USER_ = "FEED_USER_";
    private static final String FEED_TAG_ = "FEED_TAG_";

	public static final int FEED_COUNT_PER_PAGE = 10;
	/**
	 * feed缓存量
	 */
	public static final int FEED_COUNT = 200;
	
	@Autowired
	@Qualifier("eventService")
	private EventService eventService;
	
	@Autowired
	@Qualifier("userService")
	private UserService userService;
	
	@Autowired
	@Qualifier("likeService")
	private LikeService likeService;
	
	@Autowired
	@Qualifier("commentService")
	private CommentService commentService;
	
	@Autowired
	@Qualifier("interestService")
	private InterestService interestService;
	
	@Autowired
	@Qualifier("relationService")
	private RelationService relationService;
	
	@Autowired
	@Qualifier("eventIndexService")
	private EventIndexService eventIndexService;

	@Autowired
    private RedisService redisService;
	
	public void push(List<Integer> followers, int event_id) {
		if(followers != null && followers.size() != 0) {
			for(Integer follower: followers) {
				redisService.lpush(FEED_USER_ + follower,String.valueOf(event_id));
			}
		}
	}
	
	/**
	 * 缓存feed到对应标签列表序列中
	 * @param tagId
	 * @param event_id
	 */
	public void cacheFeed2Tag(int tagId, int event_id) {
        redisService.lpush(FEED_TAG_ + tagId,String.valueOf(event_id));
	}
	
	public void cacheFeeds2Tag(int tagId, List<Integer> events_id) {
	    if (events_id == null || events_id.size() == 0) {
	        return;
        }
		String[] strs = new String[events_id.size()];
		for (int i = 0;i < events_id.size();i++) {
		    strs[i] = String.valueOf(events_id.get(i));
        }
		redisService.lpush(FEED_TAG_ + tagId, strs);
	}
	
	private List<Integer> getEventIDs(int userId, int start, int count) {
	    List<String> stringList = redisService.lrange(FEED_USER_ + userId, start, start + count);
	    List<Integer> integers = new ArrayList<>(stringList.size());
	    for (String string : stringList) {
	        integers.add(Integer.parseInt(string));
        }
		return integers;
	}
	
	public List<Event> getFeeds(int userId) {
		return getFeeds(userId, FEED_COUNT_PER_PAGE);
	}
	
	public List<Event> getFeeds(int userId, int count){
		List<Integer> event_ids = getEventIDs(userId, 0, count-1);
		return decorateFeeds(userId, event_ids);
	}
	
	private List<Event> decorateFeeds(int user_id, List<Integer> event_ids){
		List<Event> events = new ArrayList<Event>();
		if(event_ids != null && event_ids.size()!=0 ) {
			events = eventService.getEventsWithIDs(event_ids);
			addUserInfo(events);
			updLikeCount(user_id, events);
			addCommentCount(events);
		}
		System.out.println("events size: " + events.size());
		return events;
	}
	
	public List<Event> getFeedsOfPage(int user_id, int num) {
		long allFeedsCount = redisService.llen(FEED_USER_ + user_id);
		System.out.println("all feeds count :" + allFeedsCount);
		List<Integer> eventIds = getEventIDs(user_id,FEED_COUNT_PER_PAGE * (num - 1),FEED_COUNT_PER_PAGE - 1);
		return decorateFeeds(user_id, eventIds);
		
	}
	
	public List<Event> getFeedsOfPage(int user_id, int num, int feed_id) {
		List<Integer> eventIds = new ArrayList<Integer>();
		int index = -1;
		long allFeedsCount = redisService.llen(FEED_USER_ + user_id);
		System.out.println("all feeds count :" + allFeedsCount);
		while(index == -1) {
			eventIds = getEventIDs(user_id,FEED_COUNT_PER_PAGE * (num - 1) - 1,FEED_COUNT_PER_PAGE);
			if(FEED_COUNT_PER_PAGE * num >= allFeedsCount) {
				break;
			}
			num++;
			index = eventIds.indexOf(feed_id);
			System.out.println("index: " + index);
		}
		if(index != -1) {
			eventIds = eventIds.subList(index+1, eventIds.size());
			System.out.println("len: " + eventIds.size());
			for(int id: eventIds) {
				System.out.println("event id:"+id);
			}
		}
		return decorateFeeds(user_id, eventIds);
	}
	
	
	public List<Event> addUserInfo(List<Event> events) {
		if(events == null || events.size() == 0) {
            return events;
        }
		for(Event event : events) {
			User user = userService.findById(event.getUserId());
			event.setUserName(user.getUserName());
			event.setUserAvatar(user.getUserAvatar());
		}
		return events;
	}
	
	public void updLikeCount(int user_id, List<Event> events){
		if(events == null || events.size() == 0) {
            return;
        }
		for(Event event : events) {
			event.setLikeCount((int)likeService.likersCount(event.getObjectType(),
															 event.getObjectId()));
			event.setLike(likeService.isLike(user_id, event.getObjectType(), event.getObjectId()));
		}
	}
	
	public void addCommentCount(List<Event> events){
		if(events == null || events.size() == 0) {
            return;
        }
		for(Event event : events) {
			event.setCommentCount(commentService.getCommentsCount(event.getObjectType(), event.getObjectId()));
		}
	}
	
	public void delete(int userId, int eventId) {
		redisService.lrem(FEED_USER_ + userId, 0,String.valueOf(eventId));
	}

	/**
	 * 获取tag标签的feed
	 * 
	 * @param userId
	 * @param tagId
	 * @return
	 */
	public List<Event> getFeedsByTag(int userId, int tagId) {
		return getFeedsByTag(userId, tagId, FEED_COUNT_PER_PAGE);
	}
	
	public List<Event> getFeedsByTag(int userId, int tagId, int count){
		List<Integer> event_ids = getEventIDsByTag(tagId, 0, count-1);
		return decorateFeeds(userId, event_ids);
	}
	
	public List<Event> getFeedsByTagOfPage(int userId, int tagId, int num) {
	    List<String> stringList = redisService.lrange(
	            FEED_TAG_ + tagId
                ,FEED_COUNT_PER_PAGE * (num - 1)
                ,FEED_COUNT_PER_PAGE - 1);
		List<Integer> eventIds = new ArrayList<>(stringList.size());
		for (String str : stringList) {
		    eventIds.add(Integer.parseInt(str));
        }
		return decorateFeeds(userId, eventIds);
		
	}
	
	private List<Integer> getEventIDsByTag(int tagId, int start, int count) {
        List<String> stringList = redisService.lrange(FEED_TAG_ + tagId, start, start + count);
        List<Integer> integers = new ArrayList<>(stringList.size());
        for (String string : stringList) {
            integers.add(Integer.parseInt(string));
        }
        return integers;
	}
	
	/**
	 * feeds search
	 */
	public List<Event> getFeedsByTitleOrContentContains(String term) {
		if(term == null || term.length() == 0) return new ArrayList<Event>();
		List<Integer> event_ids = eventIndexService.findByTitleOrContent(term);
		
		return decorateFeeds(0, event_ids);
	}
	public List<Event> getFeedsByTitleOrContentContains(int userId, String term) {
		return getFeedsByTitleOrContentContains(userId, term, 1);
	}
	
	public List<Event> getFeedsByTitleOrContentContains(String term, int page) {
		if(term == null || term.length() == 0) return new ArrayList<Event>();
		List<Integer> eventIds = eventIndexService.findByTitleOrContent(term, (page-1)*FEED_COUNT_PER_PAGE, FEED_COUNT_PER_PAGE);
		
		return decorateFeeds(0, eventIds);
	}
	public List<Event> getFeedsByTitleOrContentContains(int user_id, String term, int page) {
		if(term == null || term.length() == 0) return new ArrayList<Event>();
		List<Integer> event_ids = eventIndexService.findByTitleOrContent(term, (page-1)*FEED_COUNT_PER_PAGE, FEED_COUNT_PER_PAGE);
		
		return decorateFeeds(user_id, event_ids);
	}
	
	/**
	 * feed推荐算法
	 * 这里只是简单实现, 可自己扩充
	 * @param user_id
	 * @return 推荐feed列表 - List<Event>
	 */
	public List<Event> getRecommendFeeds(int user_id) {
		return addUserInfo(eventService.getEventsHasPhoto(0, 20));
	}
	
	public List<Event> getRecommentFeedsOfPage(int user_id, int page) {
		return addUserInfo(eventService.getEventsHasPhoto(FEED_COUNT_PER_PAGE*(page-1), FEED_COUNT_PER_PAGE-1));
	}
	
	public void coldStart(int user_id){
		if(redisService.llen(FEED_USER_ + user_id) != 0){
			return ;
		}
		
		List<Tag> tagsInted = interestService.getTagsUserInterestedIn(user_id);
		List<Relation> relations = relationService.getRelationsInTags(tagsInted);
		List<Event> events = eventService.getEventsWithRelations(relations);
		
		//no choose , fetch latest feeds default
		if(events == null || events.size() == 0){
			events = eventService.getEvents(0, FEED_COUNT_PER_PAGE);
		}
			
		List<String> eventsId = new ArrayList<>();
		for(Event event : events) {
			eventsId.add(String.valueOf(event.getId()));
		}
		redisService.lpush(FEED_USER_ + user_id, eventsId.toArray(new String[eventsId.size()]));
	}
}
