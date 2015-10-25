package project1;

import java.awt.List;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

import textDevicePackage.TextDevice;

public class SqlManager {
	JdbcSQL sqlDB;
	TextDevice io;
	
	public SqlManager(TextDevice io){
		sqlDB = new JdbcSQL();
		this.io = io;
	}
	
	public ResultSet queryWrapper(String[] stmts){
		ResultSet rs = null;
		for(String i: stmts){
			sqlDB.sendCommand(i);
		}
		return rs;
	}
	
	
	public void closeConnection(){
		sqlDB.closeConnection();
	}
	
	public void setup(){
//		this.dropTables();
//		this.createTables();
//		this.insertData();
	}
	
	public HashMap<String, String> getBookings(String email){
		String tno;
		String dep_date;
		String name = "";
		String price = "";
		
		ResultSet rs = sqlDB.executeQuery(Queries.getUserBookings(email));
		HashMap<String, String> bookings = new HashMap<String, String>();
		try{
			while(rs.next()){
				tno = String.valueOf(rs.getInt("tno"));
				dep_date = String.valueOf(rs.getString("dep_date"));
				
				ResultSet rs_tickets = sqlDB.executeQuery(Queries.checkTicket(tno));
				while(rs_tickets.next()){
					name = String.valueOf(rs_tickets.getString("name")).trim();
					price = String.valueOf(rs_tickets.getFloat("paid_price"));
					
				}
				String s = "tno: " + tno + ", passenger: " + name + ", departure date: " + dep_date +
						", price: " + price;
				bookings.put(tno, s);
//				io.printf("tno: %s, passenger: %s, departure date: %s, price: %s ", tno, name, dep_date, price);
				
				
				//+ " " + rs.getString("fare") + String.valueOf(rs.getDate("dep_date")) + " " + rs.getString("seat");
			}
			return bookings;
		} catch (Exception e){
			io.printf("Issue getting bookings %n", e);
			return bookings;
		}
	}
	
	public void cancelBooking(String key){
		try{
			sqlDB.sendCommand(Queries.removeBooking(key));
		} catch (Exception e){
			io.printf("Problem canceling booking %s", e);
		}
	}
	
	public boolean checkForAgentWithEmail(String email){
		ResultSet rs = sqlDB.executeQuery(Queries.findAgent(email));
		try {
			if(!rs.isBeforeFirst()){
				return false;
			}
			return true;
		} catch (Exception e){
			io.printf("checkForAgentWithEmail failed %s %n", e);
			return false;
		}

	}
	
	public String getUserPass(String email){
		ResultSet rs = sqlDB.executeQuery(Queries.findUser(email));
		String password = "";
		try {
			rs.next();
			password = rs.getString("pass");
		} catch (Exception e){
			io.printf("getUserPass failed %s %n", e);
		}
		return password;
	}
	
	
	public void logoutUser(String email){
		sqlDB.sendCommand(Queries.updateLastLogin(email));
	}
	
	
	public void dropTables(){
		queryWrapper(Queries.dropTables());
	}
	
	public void createTables(){
		queryWrapper(Queries.createTables());

	}
	
	public void insertData(){
		queryWrapper(Queries.insertSampleData());
	}
	
	public boolean checkForUserWithEmail(String email){
		ResultSet rs = sqlDB.executeQuery(Queries.findUser(email));
		try{
			if(!rs.isBeforeFirst()){
				return false;
			}
		} catch (Exception e){
			io.printf("checkForUserWithEmail failed %s", e);
		}
		return true; 
	}
	
	public void addUser(String email, String password){
		sqlDB.sendCommand(Queries.insertUser(email, password));
	}
}
