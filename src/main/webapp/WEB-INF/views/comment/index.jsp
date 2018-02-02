<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.lvwang.osf.pojo.User" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="header">
	评论
</div>
<div class="ui divider"></div>
<div id="commentList">
<c:forEach items="${comments }" var="comment">
	<div class="comment" id="comment${comment.id }" author="${comment.commentAuthor }">
	  <a class="avatar" href="<c:url value="/user/${comment.commentAuthor }" />">
	  	<img src="<c:url value="${img_base_url }${comment.commentAuthorAvatar }?imageView2/1/w/48/h/48"/>" alt="" />
	  </a>
	  <div class="content">
	  	<c:if test="${comment.commentParent == 0 }">
	    	<a class="author" href="<c:url value="/user/${comment.commentAuthor }" />" >${comment.commentAuthorName }</a>
	    </c:if>
	    <c:if test="${comment.commentParent != 0 }">
	    	<a class="author" href="<c:url value="/user/${comment.commentAuthor }" />">${comment.commentAuthorName }</a> 回复 <a class="author" href="<c:url value="/user/${comment.commentParentAuthor }" />">${comment.commentParentAuthorName }</a>
	    </c:if>
	    <div class="metadata">
	      <span class="date">${comment.commentTs }</span>
	    </div>
	    <div class="text commentContent">
	      <p>${comment.commentContent }</p>
	    </div>
	    <c:if test="${!empty sessionScope.user }">
	    	<c:if test="${comment.commentAuthor ne sessionScope.user.id }">
			    <div class="actions" >
			      <a class="reply" ref="${comment.id }">回复</a>
			    </div>	    		
	    	</c:if>
	    </c:if>
	    <c:if test="${empty sessionScope.user }">
		    <div class="actions" >
		      <a class="reply" ref="${comment.id }">Reply</a>
		    </div>
	    </c:if>
	  </div>
	</div>
</c:forEach>
</div>