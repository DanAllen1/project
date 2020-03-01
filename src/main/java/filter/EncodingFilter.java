package filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class EncodingFilter implements Filter {

	@Override
	public void doFilter(ServletRequest res, ServletResponse rps, FilterChain chain)
			throws IOException, ServletException {
		res.setCharacterEncoding("UTF-8");
		rps.setContentType("text/html; charset=UTF-8");
		chain.doFilter(res, rps);
	}

}
