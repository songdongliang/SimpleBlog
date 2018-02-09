package com.lvwang.osf.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lvwang.osf.mappers.PostMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lvwang.osf.pojo.Event;
import com.lvwang.osf.pojo.Post;
import com.lvwang.osf.pojo.Tag;
import com.lvwang.osf.pojo.User;
import com.lvwang.osf.util.Dic;
import com.lvwang.osf.util.Property;

@Transactional
@Service
public class PostService extends BaseService<Post>{

	/**
	 * 公开
	 */
	public static final int POST_STATUS_PUB = 0;
	/**
	 * 私密
	 */
	public static final int POST_STATUS_PRV = 1;
	/**
	 * 保存
	 */
	public static final int POST_STATUS_SAVED = 2;
	/**
	 * 编辑
	 */
	public static final int POST_STATUS_EDIT = 3;
	/**
	 * 允许评论
	 */
	public static final int COMMENT_STATUS_ALLOWED = 0;
	/**
	 * 不允许评论
	 */
	public static final int COMMENT_STATUS_NOTALLOWED = 1;
	
	private static final int POST_SUMMARY_LENGTH = 200;
	
	@Autowired
	@Qualifier("relationService")
	private RelationService relationService;
	
	@Autowired
	@Qualifier("tagService")
	private TagService tagService;
	
	@Autowired
	@Qualifier("userService")
	private UserService userService;

	@Autowired
	@Qualifier("eventService")
	private EventService eventService;

	@Autowired
	@Qualifier("feedService")
	private FeedService feedService;
	
    @Autowired
    private PostMapper postMapper;
	
	public Map<String, Object> newPost(Integer author, String title, String content,
									   Integer postStatus, Integer commentStatus, String paramTags, String postCover) {
		
		Map<String, Object> map = new HashMap<>();
		
		//1 field check
		if(author == null || 
		   title == null || title.length() == 0 ||
		   content == null || content.length() == 0) {
			map.put("status", Property.ERROR_POST_EMPTY);
			return map;
		}

		if(postStatus == null) {
			postStatus = POST_STATUS_PUB;
		}
		if(postStatus < 0 || postStatus > 3) {
			map.put("status", Property.ERROR_POST_STATUS);
			return map;
		}
		if(commentStatus == null) {
			postStatus = COMMENT_STATUS_ALLOWED;
		} else if(commentStatus != 0 && commentStatus != 1) {
			map.put("status", Property.ERROR_COMMENT_STATUS);
		}
		//2 save post
		Post post = new Post();
		post.setPostAuthor(author);
		post.setPostTitle(title);
		post.setPostExcerpt(getSummary(content));
		post.setPostContent(content);
		post.setPostStatus(postStatus);
		post.setCommentStatus(commentStatus);
		post.setLikeCount(0);
		post.setShareCount(0);
		post.setCommentCount(0);
		
		post.setPostCover(postCover);

		//3 save tags
		if(paramTags != null && paramTags.length() != 0) {
			//此处会为tag建立index
			Map<String, Object> tagsmap = tagService.newTags(TagService.toList(paramTags));
			
			post.setPostTagsList((List<Tag>)tagsmap.get("tags"));
			int id = savePost(post);
			post.setId(id);
			
			//4 save post tag relation
			for(Tag tag: (List<Tag>)tagsmap.get("tags")) {
				Map<String, Object> relmap = relationService.newRelation(
											 RelationService.RELATION_TYPE_POST, 
											 post.getId(), 
											 tag.getId());
			}			
			map.put("tags", tagsmap.get("tags"));
		} else {
			int id = savePost(post);
			post.setId(id);
			map.put("tags", new ArrayList<Tag>());
		}
				
		map.put("post", post);
		map.put("status", Property.SUCCESS_POST_CREATE);
		return map;
	}
	
	public int savePost(Post post){
		return super.save(post);
	}
	
	public Post findPostByID(int id) {
		return super.queryById(id);
	}
	
	public List<Post> findPostsOfUser(int id) {
		return postMapper.getPostsByUserID(id);
	}
	
	public static String getSummary(String post_content) {
		if(post_content == null || post_content.length() == 0) {
            return null;
        }
		Document doc = Jsoup.parse(post_content);
		String text = doc.text().replaceAll("<script[^>]*>[\\d\\D]*?</script>", "");
		return text.substring(0, text.length() > POST_SUMMARY_LENGTH ? POST_SUMMARY_LENGTH : text.length());
	}
	
	public User getAuthorOfPost(int id) {
		int user_id = postMapper.getAuthorOfPost(id);
		return userService.findById(user_id);
	}
	
	public long count(int userId){
		return postMapper.count(userId);
	}
	
	public void deletePost(int id){
		super.deleteById(id);
		Event event = eventService.getEvent(Dic.OBJECT_TYPE_POST, id);
		if(event != null){
			eventService.delete(Dic.OBJECT_TYPE_POST, id);
			feedService.delete(event.getObjectType(), event.getObjectId());
		}
	}
}
