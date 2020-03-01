package filter;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginFilter implements Filter{

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request1 = (HttpServletRequest) request;
		HttpServletResponse response1 =(HttpServletResponse) response;
		HttpSession session = request1.getSession();
		String requestUrl = request1.getRequestURI();
		//通过访问的不是后台页面可以直接放行
		if(requestUrl.contains("add") || requestUrl.contains("update") ||
				requestUrl.contains("Manage")) {
			//访问后台页面的话已登录的也放行
			if(session.getAttribute("user") != null) {
				chain.doFilter(request, response);
				return;
			}
			//未登录
			else {
				response1.setContentType("text/html; charset=UTF-8"); 
				PrintWriter out = response1.getWriter();
				out.println("<script> alert('请先登录');window.location.href = '/project/login.html'; </script>");		
				return;
			}			
		}
		else {
			chain.doFilter(request, response);
			return;
		}
		
	}

	
}
