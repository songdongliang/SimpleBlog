<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<c:forEach items="${feeds }" var="feed"> 
	<c:if test="${feed.objectType == dic.object_type_shortpost }">
		<div class="event row" type="spost" object_type="${feed.objectType }" object_id="${feed.objectId }" author="${feed.userId}">
		   <div class="label span2">
		     <a href="<c:url value="/user/${feed.userId }" />"><img src="${img_base_url}${feed.userAvatar }"></a>
		   </div>
		   <div class="content span6">
		     <div class="summary">
		       <a href="<c:url value="/user/${feed.userId }" />">${feed.userName }</a> 说
		       <div class="date">
		         ${feed.ts }
		       </div>
		     </div>
		     <!-- end summary -->
		     
		     <div class="extra">                         
		       ${feed.summary }
		     </div>
		     <div class="meta">							                     	
					<div class="actions">
						<a class="comment">
					    	<i class="comment outline icon"></i> ${feed.commentCount }
						</a>                           
				        <a class="like">
					       	<c:if test="${feed.like }">
					       		<i class="red heart icon" author="${feed.userId }" object_type=${feed.objectType } object_id=${feed.objectId }></i>
					       		<span>${feed.likeCount }</span>
					       	</c:if>
					       	<c:if test="${!feed.like }">
					       		<i class="heart icon" author="${feed.userId }" object_type=${feed.objectType } object_id=${feed.objectId }></i>
					       		<span>${feed.likeCount }</span>
					       	</c:if>	                          	
				    	</a>                         
			   		</div>
		 	</div>
		 	<!-- end meta -->   
		    <div class="comments-attach"></div>
		    <!-- end comments-attach -->   
		   </div>
		   <!-- end content -->
		 </div>   
		 <!-- end event -->                 		
	</c:if>
	
<!-- new post -->
 <c:if test="${feed.objectType == dic.object_type_post}">
 <div class="event row" type="post" object_type="${feed.objectType }" object_id="${feed.objectId }">
   <div class="label span2">
     <a href="<c:url value="/user/${feed.userId }" />"><img src="${img_base_url}${feed.userAvatar }"></a>
   </div>
   <div class="content span6">
     <div class="summary">
       <a href="<c:url value="/user/${feed.userId }" />">${feed.userName }</a> 发表了日志
       <div class="date">
         ${feed.ts }
       </div>
     </div>
     <div class="extra">
     	<div class="postheader">
        		<a href="<c:url value="/post/${feed.objectId }" />">${feed.title }</a>
         </div>
         <c:if test="${not empty feed.content }">
   		 	<img src="<c:url value="${img_base_url}${feed.content }${post_cover_thumbnail}"/>" alt="" />
   		 </c:if>
   	</div>
     <div class="extra">
       
       ${feed.summary }
     </div>
     <div class="meta">
       <c:if test="${not empty feed.tags }">	
	       <div class="tags">
		     	<i class="tag icon"></i>
		     	<c:forEach items="${feed.tags }" var="tag">
		     		<a href="<c:url value="/tag/${tag.id }"/>">${tag.tag }</a>
		     	</c:forEach>
		   </div>
	   </c:if>							                     	
       <div class="actions">
	 		<a class="comment">
	            <i class="comment outline icon"></i> ${feed.commentCount }
	        </a>                           
	        <a class="like">
	        	<c:if test="${feed.like }">
	        		<i class="red heart icon" author="${feed.userId }" object_type=${feed.objectType } object_id=${feed.objectId }></i>
	        		<span>${feed.likeCount }</span>
	        	</c:if>
	        	<c:if test="${!feed.like }">
	        		<i class="heart icon" author="${feed.userId }" object_type=${feed.objectType } object_id=${feed.objectId }></i>
	        		<span>${feed.likeCount }</span>
	        	</c:if>	                          	
	        </a>                       
       </div>

     </div>  
     <!-- end meta --> 
     <div class="comments-attach"></div>
     <!-- end comments-attach -->                                            
   </div>
   <!-- end content -->

 </div>                    	
 </c:if>
 
 <!-- new album -->
 <c:if test="${feed.objectType == dic.object_type_album }">
 <div class="event row" type="album" object_type="${feed.objectType }" object_id="${feed.objectId }">
   <div class="label span2">
     <a href="<c:url value="/user/${feed.userId }" />"><img src="${img_base_url}${feed.userAvatar }"></a>
   </div>
   <div class="content span6">
     <div class="summary">
       <a>${feed.userName }</a> 上传了相册 <a href="<c:url value="/album/${feed.objectId }" /> "></a>
    <div class="date">
      ${feed.ts }
    </div>                          
     </div>
     <div class="extra images">
       <c:forTokens items="${feed.content }" delims=":" var="img" begin="0" end="2">
       	<a href="<c:url value="/album/${feed.objectId }/photos" />"><img alt="" src="${img_base_url }${img }${album_thumbnail}"></a>
       </c:forTokens>
     </div>
     <div class="extra">${feed.summary }</div>
     <div class="meta">
        <c:if test="${not empty feed.tags }">	
        <div class="tags">
	      	<i class="tag icon"></i>
	      	<c:forEach items="${feed.tags }" var="tag">
	      		<a href="<c:url value="/tag/${tag.id }"/>">${tag.tag }</a>
	      	</c:forEach>
		</div> 
		</c:if>                       
        <div class="actions">
			<a class="comment">
                    <i class="comment outline icon"></i> ${feed.commentCount }
        	</a>                           
	        <a class="like">
	        	<c:if test="${feed.like }">
	        		<i class="red heart icon" author="${feed.userId }" object_type=${feed.objectType } object_id=${feed.objectId }></i>
	        		<span>${feed.likeCount }</span>
	        	</c:if>
	        	<c:if test="${!feed.like }">
	        		<i class="heart icon" author="${feed.userId }" object_type=${feed.objectType } object_id=${feed.objectId }></i>
	        		<span>${feed.likeCount }</span>
	        	</c:if>	                          	
	        </a>                          
       </div>
       <!-- end actions -->
     </div>
     <!-- end meta -->
     <div class="comments-attach"></div>
     <!-- end comments-attach -->   
   </div>
 </div>                    
 </c:if>
 <!-- end album  -->    
 
 <c:if test="">
 	
 </c:if>
             
</c:forEach>   