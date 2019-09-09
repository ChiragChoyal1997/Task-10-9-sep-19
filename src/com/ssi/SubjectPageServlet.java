package com.ssi;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/SubjectPageServlet")
public class SubjectPageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	String url = "jdbc:oracle:thin:@localhost:1521:xe";
	String username = "abc123";
	String password = "abc123";
	String drivername = "oracle.jdbc.driver.OracleDriver";
	private Connection con;
	private PreparedStatement ps;
	
	public void init(){
		try{
		Class.forName(drivername);
		con=DriverManager.getConnection(url,username,password);
		String sql="SELECT distinct subject from books";
		ps=con.prepareStatement(sql);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void destroy(){
		try{
			con.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out=response.getWriter();
		try{
		ResultSet rs=ps.executeQuery();
		String s1="All Books";
		Cookie ck[]=request.getCookies();
		if(ck!=null) {
			for(Cookie c:ck){
				String name=c.getName();
				if(name.equals("subjectchoice")){
					s1=c.getValue().replace("_", " ");
				}
			}
		}
		out.println("<html>");
		out.println("<html><body>");
		out.println("<marquee><h4>Attractive Offers On "+s1+"</h4></marquee>");
		out.println("<h3>Select The Desired Subject</h3>");
		out.println("<hr>");
		while(rs.next()){
			String sub=rs.getString(1);
			String querystring = sub.replace(' ', '+');
			out.println("<a href=BookListServlet?subject="+querystring+">");
			out.println(sub);
			out.println("</a><br>");
		}
		out.println("<hr>");
		out.println("<a href=buyerpage.jsp>Buyer-Page</a>");
		out.println("</body></html>");
		}catch(Exception e){
			out.println(e);
		}
	}

}
