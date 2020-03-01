package filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import pojo.View;
import until.TimeUntil;

public class ViewFilter implements Filter{
	
	private TimeUntil timeUntil = new TimeUntil();

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request1 = (HttpServletRequest) request;
		HttpServletResponse response1 =(HttpServletResponse) response;
		HttpSession session = request1.getSession();
		String requestUrl = request1.getRequestURI();
		//如果是后台页面访问量不增加
		if( requestUrl.contains("add") || requestUrl.contains("update") ||
			requestUrl.contains("Manage")|| requestUrl.contains("index2") ||
			requestUrl.contains("login") || requestUrl.contains("forgotPassword")){
				chain.doFilter(request1, response1);
				}
		else {
			//如果是打开浏览器后的第一次则访问+1
			if(session.getAttribute("view") == null) {
				ServletContext servletContext = request1.getServletContext();
				View view = (View) servletContext.getAttribute("view");
				view.setPageView(view.getPageView()+1);
				servletContext.setAttribute("view", view);
				//记录正在打开的浏览器已经访问过该网站了
				session.setAttribute("view", view);
				chain.doFilter(request1, response1);
			}
			else {
				chain.doFilter(request1, response1);
				}
			}
		}
	}
