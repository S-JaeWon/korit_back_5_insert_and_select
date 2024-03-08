package com.study.insert_and_select.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.study.insert_and_select.config.DBConfig;
import com.study.insert_and_select.entity.Student;

public class StudentDao {
	
	private static StudentDao instance;
	
	private StudentDao() {}
	
	public static StudentDao getInstance() {
		if(instance == null) {
			instance = new StudentDao();
		}
		return instance;
	}
	
	
	// DataInsertServlet.java 속 try 문 함수로 빼기, null 이면 못 찾음
	public Student findStudentByName(String name) { // select 문 ,select 할때는 ResultSet 무조건 있어야 됨.
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Student student = null; // 이름으로 찾아야 하니 객체 필요
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			
			con = DriverManager.getConnection(DBConfig.URL, DBConfig.USERNAME, DBConfig.PASSWORD);
			String sql = "select * from student_tb where student_name = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, name);
			rs = pstmt.executeQuery(); // 실행한 결과 rs 에 담기
			
			if(rs.next()) {
				student = Student.builder()
						.studentId(rs.getInt(1))
						.name(rs.getString(2))
						.age(rs.getInt(3))
						.build();
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally { // 마지막으로 다하고 접속 끊어주기 
			try {
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
		
		return student;
	}
	
	public int saveStudnet(Student student) { // insert 문 
		Connection con = null;
		PreparedStatement pstmt = null;
		int successCount = 0;
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			
			con = DriverManager.getConnection(DBConfig.URL, DBConfig.USERNAME, DBConfig.PASSWORD);
			
			String sql = "insert into student_tb(student_name, student_age) values(?, ?)"; // ? -> 어떤 값 들어올지 모름 
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, student.getName()); // (?, ?) -> (1, student.getName())
			pstmt.setInt(2, student.getAge());
			successCount = pstmt.executeUpdate();
			
		} catch(Exception e) {
			e.printStackTrace();
		}  finally { 
			try {
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
		
		return successCount;
	}
	
	// 조회 하려는 값들을 리스트로 담아 호출
	public List<Student> getStudentListAll() {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<Student> students = new ArrayList<>();
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			
			con = DriverManager.getConnection(DBConfig.URL, DBConfig.USERNAME, DBConfig.PASSWORD);
			String sql = "select * from student_tb";
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			while (rs.next()) { // next 가 false 될때 까지 반복을 통해 값 담기
				Student student = Student.builder()
						.studentId(rs.getInt(1))
						.name(rs.getString(2))
						.age(rs.getInt(3))
						.build();
				
				students.add(student);
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally { 
			try {
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
				
		return students;
	}
	
}
