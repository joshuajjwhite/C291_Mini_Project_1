package project1;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
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
		this.dropTables();
		this.createTables();
		this.insertData();
	}
	
	public HashMap<Integer, HashMap<String, String>> searchFlights(String src, String dst, String dep_date, boolean roundTrip, String ret_date, boolean threeConn, boolean orderByConn){
		// good_connections(src,dst,dep_date,flightno1,flightno2, layover,price)
		//available_flights(flightno, dep_date, src , dst, dep_time, arr_time, fare, seats,price)

		HashMap<Integer, HashMap<String, String>> flights = new HashMap<Integer, HashMap<String, String>>();
		HashMap<String, String> flight = new HashMap<String, String>();
		
//		String roundTrip(String src, String dst, String dep_date, String ret_date)
		
		String[] stmts = null;
		if(roundTrip){
			stmts = Queries.roundTrip(src.toUpperCase(), dst.toUpperCase(), dep_date, ret_date);
		} else {
			stmts = Queries.searchFlight(src.toUpperCase(), dst.toUpperCase(), dep_date, threeConn, orderByConn);
		}
		
		Integer flightCounter = 0 ;
		ResultSet rs = null;
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
	
	
	public HashMap<Integer, HashMap<String, String>> getBookings(String email){
		String tno;
		String dep_date;
		String price = "";
		Integer ticketCounter = 0;
		
		ResultSet rs = sqlDB.executeQuery(Queries.getUserBookings(email));
		HashMap<Integer, HashMap<String, String>> tickets = new HashMap<Integer, HashMap<String, String>>();
		HashMap<String, String> ticketInfo = new HashMap<String, String>();
		try{
			while(rs.next()){
				tno = String.valueOf(rs.getInt("tno"));
				dep_date = String.valueOf(rs.getString("dep_date"));
				
				ResultSet rs_tickets = sqlDB.executeQuery(Queries.checkTicket(tno));
			    ticketCounter ++;
				while (rs_tickets.next()){
					
					ResultSetMetaData rsetMD = rs.getMetaData();
				    int columnCount = rsetMD.getColumnCount();


		      		for (int c=1; c<=columnCount; c++){
						String name = rsetMD.getColumnLabel(c); // get column name
						Object o = rs.getObject(c); // get content at that index
						String value="null";
						if (o!=null) {
							value = o.toString();
		     		 	}
						
						io.printf("%s %s ", name, value);
						
						ticketInfo.put(name, value);
		      		}
	

				}
	      		tickets.put(ticketCounter, ticketInfo);
			}
			return tickets;
		} catch (Exception e){
			io.printf("Issue getting bookings %n", e);
			return null;
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
	
	public boolean searchACODE(String s){
		
		ResultSet rs = sqlDB.executeQuery(Queries.findAcode(s));
		if(getRowCount(rs) == 1){
			return true;
		}
		
		else return false;
		
		
	}
	
	private int getRowCount(ResultSet resultSet) {
		    if (resultSet == null) {
		        return 0;
		    }
		    try {
		    	int counter = 0;
		        while(resultSet.next()){
		        	counter ++;
		        }
		        return counter;
		    } catch (SQLException exp) {
		        exp.printStackTrace();
		    } 
		    return 0;
		}
	
	public ArrayList<String> searchCity(String s){
		ResultSet rs = sqlDB.executeQuery(Queries.searchCities(s));
		ArrayList<String> ar = new ArrayList<String>();
		try {
			while(rs.next()){
				
				try {
					ar.add(rs.getString("CITY"));

				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		return ar;
	}
	
	public ArrayList<String> searchName(String s){
		ResultSet rs = sqlDB.executeQuery(Queries.searchCities(s));
		ArrayList<String> ar = new ArrayList<String>();
		try {
			while(rs.next()){
				try {
					ar.add(rs.getString("NAME"));


				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ar;
	}
	
	public String isThisWhatYouWanted(String s){
		ResultSet rs = sqlDB.executeQuery(Queries.searchCities(s));
		ArrayList<String> ar = new ArrayList<String>();
		try {
			while(rs.next()){
				try {
					return rs.getString("CITY") + " - " + rs.getString("NAME");
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	
	public String getAcodeByCity(String city){
		
		ResultSet rs = sqlDB.executeQuery(Queries.searchAcodeByCity(city));
		try {
			rs.next();
			return rs.getString("ACODE");
		} catch (SQLException e) {
			io.printf("Error getting AcodeByCity %s", e);
//			e.printStackTrace();
		}
		
		return null;
	}
	
	public String getAcodeByName(String name){
		
		ResultSet rs = sqlDB.executeQuery(Queries.searchAcodeByName(name));
		try {
			return rs.getString("ACODE");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
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
	
	public boolean checkPassengers(String email, String name) {
		ResultSet rs = sqlDB.executeQuery(Queries.checkPassengers(email, name));
		try{
			if(!rs.isBeforeFirst()){
				return false;
			}
		} catch (Exception e){
			io.printf("checkPassengers failed %s", e);
		}
		return true; 
	}
	
	public boolean checkTicket(Integer tno) {
		ResultSet rs = sqlDB.executeQuery(Queries.checkTicket(tno.toString()));
		try{
			if(!rs.isBeforeFirst()){
				return false;
			}
		} catch (Exception e){
			io.printf("checkTicket failed %s", e);
		}
		return true; 
	}
	
	public void addPassenger(String email, String name, String country) {
		sqlDB.sendCommand(Queries.addPassenger(email, name, country));
	}
	
	public void addTicket(Integer ticketNumber,String name,String email,String paid_price) {
		sqlDB.sendCommand(Queries.addTicket(ticketNumber.toString(), name, email, paid_price));
	}
	
	public void addBooking(Integer ticketNumber, String flightno, String fare, String dep_date, String seat) {
		sqlDB.sendCommand(Queries.addBooking(ticketNumber.toString(), flightno, fare, dep_date, seat));
	}
}
