package com.study.insert_and_select.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletResponse;

@WebFilter("/*")
public class CommonFilter extends HttpFilter implements Filter {
       
    public CommonFilter() {
        super();
    }

	public void destroy() {
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletResponse httpServletResponse = (HttpServletResponse) response;
		
		// cors
		httpServletResponse.setHeader("Access-Control-Allow-Headers", "Content-Type"); // 주소가 같으면 cros X -> 다르면 cors O, "Content-Type" ->  컨텐츠 타입(텍스트 등)변경 허용  
		httpServletResponse.setHeader("Access-Control-Allow-Origin", "*"); // * -> 모든 주소를 요청 허가 
		// 인코딩
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		
		chain.doFilter(request, response);
		
	}

	public void init(FilterConfig fConfig) throws ServletException {
	}

}
