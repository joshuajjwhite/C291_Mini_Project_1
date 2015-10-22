package project1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.*;

public class JdbcSQL {
	
	String m_url = "jdbc:oracle:thin:@gwynne.cs.ualberta.ca:1521:CRS";
	String m_driverName = "oracle.jdbc.driver.OracleDriver";
	String m_username = "lautisch";
	String m_password = "lalaman123";
	
	Connection m_con;
			
	
	public JdbcSQL(){
		loadTheDriver();
	}
	
	private void loadTheDriver(){
		try{
			Class.forName(m_driverName);
			
		} catch(ClassNotFoundException e) {System.out.println("Could not load driver");}
		try {
			Connection m_con = DriverManager.getConnection(m_url, m_username, m_password);
		} catch (Exception e){System.out.println(e);}
		
	}
	
	public Connection getSQLConnection(){
		return m_con;
	}
		
	public void closeConnection(){
		try{
			m_con.close();
		} catch (Exception e){ System.out.println(e);}
	}
	
	public ResultSet sendCommand(String sql){
		try{
			Statement stmt = m_con.createStatement();
			stmt.executeUpdate(sql);
			ResultSet rs = stmt.executeQuery(sql);
			return rs;
		} catch (Exception e){
			System.out.println(e);
			System.out.println("Could not send commands to sql");
		}
		return null;
	}
	
	
	public void sendExampleCommands(){
		try{
			Statement stmt = m_con.createStatement();
			String sql;
			sql = "DROP TABLE Movie";
			stmt.executeUpdate(sql);
			
			sql = "create table movie(title char(20), movie_number integer, primary key(movie_number))";
			stmt.executeUpdate(sql);
			
			sql = "insert into movie values('Chicago', 1)";
			stmt.executeUpdate(sql);
			
			sql = "select title, movie_number from movie";
			ResultSet rs = stmt.executeQuery(sql);
			
			while(rs.next()){
				System.out.println(rs.getString("title")+"," + rs.getInt("movie_number"));
			}
			
		} catch (Exception e){
			System.out.println(e);
			System.out.println("Could not send commands to sql");
		}

	}
	
}












