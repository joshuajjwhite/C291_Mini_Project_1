package project1;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
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
		this.dropTables();
		this.createTables();
		this.insertData();
	}
	
	public HashMap<Integer, HashMap<String, String>> searchFlights(String src, String dst, String dep_date){
		// good_connections(src,dst,dep_date,flightno1,flightno2, layover,price)
		//available_flights(flightno, dep_date, src , dst, dep_time, arr_time, fare, seats,price)
		//io.printf("%d %n", src.length());
		//io.printf("%d %n", dst.length());
		
		//io.printf("%s %n", src);
		//io.printf("%s %n", dst);
		//io.printf("%s %n", dep_date);
		HashMap<Integer, HashMap<String, String>> flights = new HashMap<Integer, HashMap<String, String>>();
		HashMap<String, String> flight = new HashMap<String, String>();
		String[] stmts = Queries.searchFlight(src.toUpperCase(), dst.toUpperCase(), dep_date, false, false);
		String flightNo = "Oops";
		String sourceAcode = "Oops";
		String dstAcode = "Oops";
		String dep_time = "Oops";
		String arr_time = "Oops";
		String numStops = "Oops";
		String price = "Oops";
		String seats = "Oops";
		Integer flightCounter = 0 ;
		
		//heres the error (--Joshua)
		ResultSet rs = null;
//		for (String stmt: stmts[0:stmts.length-1]){
		for (int i = 0; i < stmts.length-1; i++){
			rs = sqlDB.executeQuery(stmts[i]);
		}
		rs = sqlDB.executeQuery(stmts[stmts.length-1]);
		try{
		
			while (rs.next()){
				
				ResultSetMetaData rsetMD = rs.getMetaData();
			    int columnCount = rsetMD.getColumnCount();
			    flight.clear();
			    flightCounter ++;
			    
	      		for (int c=1; c<=columnCount; c++){
					String name = rsetMD.getColumnLabel(c); // get column name
					Object o = rs.getObject(c); // get content at that index
					String value="null";
					if (o!=null) {
						value = o.toString();
	     		 	}
					flight.put(name, value);
	      		}
	      		
	      		flights.put(flightCounter, flight);
	      		
			}
		} catch (Exception e) {
			io.printf("Error getting boat names %s %n", e);
		}
		
		return flights;
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
			sqlDB.sendCommand(Queries.removeTicket(key));
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
	
	public boolean checkValidSource(String src){
		ResultSet rs = sqlDB.executeQuery(Queries.findAcode(src));
		try{
			if(!rs.isBeforeFirst()){
				//Find with name
				
			} 
			
		} catch (Exception e){
			io.printf("checkValidSource failed %s", e);
		}
		return true; 
	}
	
	public boolean checkValidDestination(String dst){
		ResultSet rs = sqlDB.executeQuery(Queries.findUser(dst));
		try{
			if(!rs.isBeforeFirst()){
				return false;
			}
		} catch (Exception e){
			io.printf("checkForUserWithEmail failed %s", e);
		}
		return true; 
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
	
	public void updateDeparture(String flightNum, String dep_time, String act_dep_time){
		sqlDB.sendCommand(Queries.recordDeparture(flightNum.toUpperCase(), Queries.getDate(dep_time), act_dep_time));
	}
	
	public void updateArrival(String flightNum, String dep_time, String act_arr_time){
		sqlDB.sendCommand(Queries.recordArrival(flightNum.toUpperCase(), Queries.getDate(dep_time), act_arr_time));
	}
}
