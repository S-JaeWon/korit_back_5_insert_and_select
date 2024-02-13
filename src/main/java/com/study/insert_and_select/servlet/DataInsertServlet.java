package com.study.insert_and_select.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.mysql.cj.jdbc.Driver;
import com.study.insert_and_select.entity.Student;

@WebServlet("/data/addition")
public class DataInsertServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public DataInsertServlet() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		StringBuilder builder = new StringBuilder(); // 문자열 합쳐주는 용도 / 쓰레드를 쓰면 StringBuffer
		
		String readData = null;
		// String builder2 = "";
		
		BufferedReader reader = request.getReader();
		
		/** 
		 * "{
		 * 		"name":"심재원",
		 * 		"age":29
		 * }"
		 * */
		
		while((readData = request.getReader().readLine()) != null) { // 각줄이 null이 아니라면 append(더함) 해줌
			builder.append(readData); // 할당된 메모리에 문자열이 추가
			// builder2 += readData; // 문자열 추가 될때마다 메모리 할당
		}
		
//		System.out.println(builder.toString());
//		
//		Student student = Student.builder()
//				.name("심재원")
//				.age(29)
//				.build();
//		
//		Gson gson = new Gson(); //Gson gson = new GsonBuilder().setPrettyPrinting().create();
//		
//		String studentJson = gson.toJson(student);
//		
//		System.out.println(studentJson);
		
		// Json -> Map : 키값을 하나하나 외워야 함 
		Gson gson = new Gson();
		Map<String, Object> map = gson.fromJson(builder.toString(), Map.class);
		
		System.out.println(map);
		System.out.println(map.get("name"));
		System.out.println(map.get("age"));
		
		
		// json -> Entity객체 : 빈 값이면 빈 값 고대로 보내줌 
		Student student = gson.fromJson(builder.toString(), Student.class);
		
		System.out.println(student);
		System.out.println(student.getName());
		System.out.println(student.getAge());
		
		// DB 대입
		Connection con = null; 			// DB연결
		String sql = null; 				// sql 쿼리문 작성
		PreparedStatement pstmt = null; // sql 쿼리문 입력
		int successCount = 0; 			// sql insert, Update, delete 실행결과 성공 횟수
		
		try {// 데이터베이스 커넥터 드라이브 클래스 이름, 예외처리
			Class.forName("com.mysql.cj.jdbc.Driver");
			
			String url = "jdbc:mysql://mysql-db.c9ouoiu00n8i.ap-northeast-2.rds.amazonaws.com/db_study"; // mysql hostname
			String username = "aws";
			String password = "1q2w3e4r!!";
			
			con = DriverManager.getConnection(url, username, password);
			
			sql = "insert into student_tb(student_name, student_age) values(?, ?)"; // ? -> 어떤 값 들어올지 모름 
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, student.getName()); // (?, ?) -> (1, student.getName())
			pstmt.setInt(2, student.getAge());
			successCount = pstmt.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally { // 마지막으로 다하고 접속 끊어주기 
			try {
				if(pstmt != null) { // 위에서 예외가 되면 pstmt와 con 값은 null이 되므로 (위에 null로 기본값 정함) null 처리를 해줌!
					pstmt.close();
				}
				if(con != null) {
					con.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		Map<String, Object> responseMap = new HashMap<>();
		responseMap.put("status", 20);
		responseMap.put("data", "응답데이터");
		responseMap.put("successCount", successCount);
		
		response.setContentType("application/json");
		
		PrintWriter writer = response.getWriter();
		writer.println(gson.toJson(responseMap));
	}

}
