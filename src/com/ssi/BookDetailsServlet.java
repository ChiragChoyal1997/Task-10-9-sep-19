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

@WebServlet("/BookDetailsServlet")
public class BookDetailsServlet extends HttpServlet {
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
	String sql="SELECT * from books where bcode=?";
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
		
		String code=request.getParameter("code");
		PrintWriter out=response.getWriter();
		String bcode = null;
		try{
		ps.setInt(1, Integer.parseInt(code));
		ResultSet rs=ps.executeQuery();
		out.println("<html>");
		out.println("<html><body>");
		out.println("<h3>Book-Details</h3>");
		out.println("<hr>");
		while(rs.next()){
			bcode=rs.getString(1);
			String title=rs.getString(2);
			String author=rs.getString(3);
			String subject=rs.getString(4);
			String price=rs.getString(5);
			
			boolean isfound = false;
			Cookie ck[]=request.getCookies();
			int s1=0;
			if(ck!=null) {
				for(Cookie c:ck){
					String name=c.getName();
					if(name.equals(bcode)){
						s1 = Integer.parseInt(c.getValue())+1;
						c.setValue(String.valueOf(Integer.parseInt(c.getValue())+1));
						response.addCookie(c);
						isfound = true;
						break;
						}
					}
					if(!isfound) {
						Cookie c1 = new Cookie(bcode,String.valueOf(1));
						c1.setMaxAge(60*60*24*7);
						response.addCookie(c1);
					}
			}
			else {
				Cookie c2 = new Cookie(bcode,String.valueOf(1));
				c2.setMaxAge(60*60*24*7);
				response.addCookie(c2);
			}
			
			out.println("<table border=2>");
			out.println("<tr>");
			out.println("<td>BCode</td>");
			out.println("<td>"+bcode+"</td>");
			out.println("</tr>");
			out.println("<tr>");
			out.println("<td>Title</td>");
			out.println("<td>"+title+"</td>");
			out.println("</tr>");
			out.println("<tr>");
			out.println("<td>Author</td>");
			out.println("<td>"+author+"</td>");
			out.println("</tr>");
			out.println("<tr>");
			out.println("<td>Subject</td>");
			out.println("<td>"+subject+"</td>");
			out.println("</tr>");
			out.println("<tr>");
			out.println("<td>Price</td>");
			
			int newprice = Integer.parseInt(price);
			if(s1<=5) {
				out.println("<td>"+newprice+"</td>");
			}
			else if (s1>5 && s1<10) {
				out.println("<td>"+(newprice+newprice*0.1)+"</td>");
			}
			else {
				out.println("<td>"+(newprice+newprice*0.2)+"</td>");
			}
			out.println("</tr>");
			out.println("</table>");
		}
		out.println("<hr>");
		out.println("<a href=CartManager?code="+code+">Add-To-Cart</a><br>");
		out.println("<a href=SubjectPageServlet>Subject-Page</a><br>");
		out.println("<a href=buyerpage.jsp>Buyer-Page</a><br>");
		out.println("</body></html>");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
