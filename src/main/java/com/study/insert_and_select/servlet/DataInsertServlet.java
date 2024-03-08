package com.study.insert_and_select.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
import com.study.insert_and_select.dao.StudentDao;
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
		
		/* 함수로 빼기 dao로
		// DB 대입
		Connection con = null; 			// DB연결
		String sql = null; 				// sql 쿼리문 작성
		PreparedStatement pstmt = null; // sql 쿼리문 입력
		int successCount = 0; 			// sql insert, Update, delete 실행결과 성공 횟수
		ResultSet rs = null;			// sql 쿼리문 출력값 담기
		Student findStudent = null;		// 동일 이름 값 있는지 확인 하는 용도 
		*/
		
		/* // 일일이 복사하기 보다는 객체로 만들어서 빼기 DBconfig로 
		String url = "jdbc:mysql://mysql-db.c9ouoiu00n8i.ap-northeast-2.rds.amazonaws.com/db_study"; // mysql hostname
		String username = "aws";
		String password = "1q2w3e4r!!";
		*/
		
		/* // 함수로 빼기 dao로
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			
			con = DriverManager.getConnection(url, username, password);
			sql = "select * from student_tb where student_name = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1,  student.getName());
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
			findStudent = Student.builder()
					.name(rs.getString(2))
					.age(rs.getInt(3))
					.build();
			// 2 와 3은 각각 컬럼 번호 2 -> Student_name, 3 -> Student_age
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally { // 마지막으로 다하고 접속 끊어주기 
			try {
				if(rs != null) {
					rs.close();
				}
				if(pstmt != null) { 
					pstmt.close();
				}
				if(con != null) {
					con.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		// 동일 이름 확인 후 그 다음 코드 이행하게 함
		*/
		
		// 뺀 함수 호출
		//StudentDao studentDao = new StudentDao(); // -> 코드는 간략해 지나 서블렛 호출시마다 StudentDao 호출됨 -> 싱글톤으로 해결
		StudentDao studentDao = StudentDao.getInstance();
		
		Student findStudent = studentDao.findStudentByName(student.getName()); // StudentDao에서 return 값에 student 객체 값이라면 아래 코드 실행 
		
		if(findStudent != null) { // null 이 아니라면 동일 값 존재함
			response.setStatus(400);
			Map<String, Object> errorMap = new HashMap<>();
			errorMap.put("errorMessage", "이미 등록된 이름입니다.");
			response.setContentType("application/json");
			response.getWriter().println(gson.toJson(errorMap));
			return; // -> 여기서 코드 종료 
		}
		
		int successCount = studentDao.saveStudnet(student);
		
		/* // 함수로 빼기 dao로
		try {// 데이터베이스 커넥터 드라이브 클래스 이름, 예외처리
			Class.forName("com.mysql.cj.jdbc.Driver");
			
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
		*/
		
		Map<String, Object> responseMap = new HashMap<>();
		responseMap.put("status", 201);
		responseMap.put("data", "응답데이터");
		responseMap.put("successCount", successCount);
		
		response.setStatus(201);
		response.setContentType("application/json");
		
		PrintWriter writer = response.getWriter();
		writer.println(gson.toJson(responseMap));
	}

}
