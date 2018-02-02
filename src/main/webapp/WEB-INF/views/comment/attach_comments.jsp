<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.lvwang.osf.pojo.User" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


	 <!-- <div class="comments-attach"> -->
	 		<div class="ui divider"></div>
			<div class="ui middle aligned list">
			  <div class="item" style="margin-bottom: 20px;margin-top: 14px">
			    <div class="content">
					<div class="ui fluid mini action input">
					  <input type="text">
					  <div class="ui blue button reply">评论</div>
					  <div class="ui button cancle" style="margin-left: 10px">取消</div>
					</div>
			    </div>
			  </div>
			  
			  
			  <c:forEach items="${comments }" var="comment">
				  <div class="item">

				    <c:if test="${!empty sessionScope.user }">
				    	<c:if test="${comment.commentAuthor ne sessionScope.user.id }">
						    <div class="right floated content actions">
						      <a class="reply" commentObjectId="${comment.commentObjectId }"
						      reply_to_author="${comment.commentAuthor }"
						      reply_to_authorname="${comment.commentAuthorName }"
						      commentObjectType=${comment.commentObjectType  }
						      commentParent=${comment.id }>回复</a>
						    </div>
				    	</c:if>
				    </c:if>
				    <img class="ui avatar image" src="<c:url value="${img_base_url }${comment.commentAuthorAvatar }"/>">
					<div class="content">
					  	<c:if test="${comment.commentParent == 0 }">
					    	<a class="author"  href="<c:url value="/user/${comment.commentAuthor }" />" >${comment.commentAuthorName }</a>
					    </c:if>
					    <c:if test="${comment.commentParent != 0 }">
					    	<a class="author" href="<c:url value="/user/${comment.commentAuthor }" />">${comment.commentAuthorName }</a> 回复 <a class="author" href="<c:url value="/user/${comment.commentParentAuthor }" />">${comment.commentParentAuthorName }</a>
					    </c:if>
					    ${comment.commentContent }
					 </div>
					 <!-- end content -->
				  </div>
	
				  <div class="ui divider"></div>	
			  </c:forEach>
			</div>   	
<!-- 	   </div> -->
	   <!-- end attach -->  
