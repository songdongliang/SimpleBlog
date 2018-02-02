package com.lvwang.osf.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.lvwang.osf.pojo.User;
import com.lvwang.osf.service.NotificationService;

public class LoginRequiredInterceptor implements HandlerInterceptor{

	@Autowired
	@Qualifier("notificationService")
	private NotificationService notificationService;

	@Override
	public void afterCompletion(HttpServletRequest arg0,
			HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1,
			Object arg2, ModelAndView arg3) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean preHandle(HttpServletRequest req, HttpServletResponse rpo,
			Object arg2) throws Exception {
		
		System.out.println("login required inter:"+req.getRequestURL());
		
		HttpSession session = req.getSession();		
		
		User user = (User) session.getAttribute("user");
		if(user == null){
			String contextPath = req.getContextPath();
			String servletPath = req.getServletPath();
			session.setAttribute("lastvisit", contextPath+servletPath);
			rpo.setCharacterEncoding("UTF-8");  
		    rpo.setContentType("application/json; charset=utf-8"); 
		} else {
			session.setAttribute("notifications", notificationService.getNotificationsCount(user.getId()));
			return true;
		}
		
		
		
		return true;
	}

}
