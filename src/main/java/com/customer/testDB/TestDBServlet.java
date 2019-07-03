package com.customer.testDB;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.*;
/**
 * Servlet implementation class TestDBServlet
 */
@WebServlet("/TestDBServlet")
public class TestDBServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//setup connection variables
		
		String user = "springuser";
		String pass = "springuser";
		
		String jdbcUrl = "jdbc:mysql://localhost:3306/customer_base?useSSL=false&allowPublicKeyRetrieval=true";
		String driver = "com.mysql.jdbc.Driver";
		
		//get connection to database
		try {
		PrintWriter out = response.getWriter();
		
		out.println("Connecting to DB " + jdbcUrl);
		
			Class.forName(driver);
			
			Connection myConn = DriverManager.getConnection(jdbcUrl, user, pass);
			
			out.println("Connection is ok!");
			
			myConn.close();
			
		} catch (Exception exc) {
			
			exc.printStackTrace();
			throw new ServletException(exc);
		}
		
		
		
	}

}
