package com.study.insert_and_select.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/data/addition")
public class DataInsertServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public DataInsertServlet() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		StringBuilder builder = new StringBuilder(); // 문자열 합쳐주는 용도 
		
		String readData = null;
		
		BufferedReader reader = request.getReader();
		
		/** 
		 * "{
		 * 		"name":"심재원",
		 * 		"age":29
		 * }"
		 * */
		
		while((readData = request.getReader().readLine()) != null) { // 각줄이 null이 아니라면 append(더함) 해줌
			builder.append(readData);
		}
		
		System.out.println(builder.toString());
	}

}
