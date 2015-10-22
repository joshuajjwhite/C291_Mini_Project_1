import java.util.*;
import java.sql.*;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class Asn2Sample extends HttpServlet {
	public void doGet(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException {
		
	        String m_url = "jdbc:oracle:thin:@gwynne.cs.ualberta.ca:1521:CRS";
	        String m_driverName = "oracle.jdbc.driver.OracleDriver";

	        String m_userName = "";
	        String m_password = "";

		String queryString;
		String returnStatement = "";
		Object o;
		Statement stmt;

	        Connection m_con;
		
		String SQLStatement = request.getParameter("SQLStatement");
		response.setContentType("text/html");

		try
		    {

			Class drvClass = Class.forName(m_driverName);
			DriverManager.registerDriver((Driver)
						      drvClass.newInstance());

		    } catch(Exception e)
		    {

			System.err.print("ClassNotFoundException: ");
			System.err.println(e.getMessage());

		    }

		try
		    {

			m_con = DriverManager.getConnection(m_url, m_userName,
							    m_password);
		
			stmt = m_con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
						     ResultSet.CONCUR_READ_ONLY);
			ResultSet rset = stmt.executeQuery(SQLStatement);
			ResultSetMetaData rsetMetaData = rset.getMetaData();
			
			for (int col = 1; col <= rsetMetaData.getColumnCount(); col++) {
			    returnStatement = returnStatement + rsetMetaData.getColumnLabel(col) + " ";
			}
			returnStatement = returnStatement + "\n";

			while (rset.next()) {
			    for (int i = 1; i <= rsetMetaData.getColumnCount(); i++) {
				o = rset.getObject(i);
				returnStatement = returnStatement + o.toString() + " ";
			    }
			    returnStatement = returnStatement + "\n";
			}
			
		    }
		catch(Exception e){
		    returnStatement = "Invalid SQL Statement";
		}

		PrintWriter out = response.getWriter();
		out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 " +
			"Transitional//EN\">\n" +
			"<HTML>\n" +
			"<HEAD><TITLE>Asn2Sample</TITLE></HEAD>\n" +
			"<BODY>\n" +
			"<H1>" +
			returnStatement + 
			"</H1>\n" +
			"</BODY></HTML>");
	}
}
