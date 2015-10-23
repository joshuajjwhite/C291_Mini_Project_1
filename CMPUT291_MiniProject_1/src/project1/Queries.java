package project1;

import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Queries {

	public static String dropTables(){
		
		return "drop table bookings;" +
		  "drop table tickets;" +
		  "drop table passengers;" +
		  "drop table flight_fares;" +
		  "drop table fares;" +
		  "drop table sch_flights;" +
		  "drop table flights;" +
		  "drop table airports;" +
		  "drop view good_connections;" +
		  "drop view available_flights;"
		  + "commit;" ;
	}
	
	public static String createTables(){
		
		return "create table airports (" +
				  "acode		char(3)," +
				  "name		char(30)," +
				  "city		char(15)," +
				  "country	char(15)," +
				  "tzone		int," +
				  "primary key (acode) );" +
				  
				"create table flights (flightno	char(6), src char(3), dst char(3), dep_time	date, est_dur int,"
				  + "primary key (flightno), foreign key (src) references airports,"
				  + "foreign key (dst) references airports);"
				  
				+ "create table sch_flights (flightno	char(6), dep_date	date, act_dep_time	date, act_arr_time	date,"
				+ "primary key (flightno,dep_date), foreign key (flightno) references flights on delete cascade);"
				
				+ "create table fares (fare		char(2), descr		char(15), primary key (fare));"
				
				+ "create table flight_fares (flightno	char(6), fare		char(2), limit		int,"
				+ "price		float, bag_allow	int, primary key (flightno,fare),"
				+ "foreign key (flightno) references flights, foreign key (fare) references fares);"
				
				+ "create table passengers (email		char(20), name		char(20), country	char(10),"
				+ "primary key (email, name));"
				
				+"create table tickets (tno		int, name char(20) email		char(20), paid_price	float, primary key (tno),"
				+ "foreign key (email) references passengers);"
				
				+ "create table bookings (tno		int, flightno	char(6), fare		char(2), dep_date	date,"
				+ "seat		char(3), primary key (tno,flightno,dep_date), foreign key (tno) references tickets,"
				+ "foreign key (flightno,dep_date) references sch_flights,foreign key (fare) references fares);"
				
				+ "create table users (email char(20), pass char(30), last_login date, "
				+ "primary key (email),"
				+ "foreign key (email) references passengers);"
				
				+ "create table airline_agents(email char(20), name char(20),"
				+ "primary key (email), "
				+ "foreign key (email) references passengers,"
				+ "foreign key (name) references passengers);"
				+ "commit;";
	}
	
	public static String searchFlight(String src, String dst, String dep_date){
		
		
		
		SimpleDateFormat df = new SimpleDateFormat();
		
		Calendar cal = Calendar.getInstance();
		try {
			cal.setTime(df.parse(dep_date));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException();
		}
		
		int month = cal.get(Calendar.MONTH); 
		int day = cal.get(Calendar.DAY_OF_MONTH);
		int year = cal.get(Calendar.YEAR);
				
				return "";
		
	}
	
	public static String searchAcode(String acode){
		
		return "SELECT acode"
				+ "FROM airports"
				+ "WHERE acode = '" + acode + "';";
		
	}
	
	public static String searchCities(String city){
		
		return "SELECT DISTINCT city, name"
				+ "FROM airports"
				+ "WHERE city LIKE '%" + city + "%' OR name LIKE '%" + city + "%';";
	}
	
	
	
	
	private String getMonth(int mon){
		
		switch(mon){
		
		case 0: return "Jan";
		case 1: return "Feb";
		case 2: return "Mar";
		case 3: return "Apr";
		case 4: return "May";
		case 5: return "Jun";
		case 6: return "Jul";
		case 7: return "Aug";
		case 8: return "Sep";
		case 9: return "Oct";
		case 10: return "Nov";
		case 11: return "Dec";	
		
		}
		return null;	
		
	}
	
	public static String createAvailableFlights(){
	
	return	"drop table available_flights;" +
	
	  "create view available_flights(flightno, dep_date, src , dst, dep_time, arr_time, fare, seats,"
	  + "price) as"
	  + "select f.flightno, sf.dep_date, f.src, f.dst,"
	  + " f.dep_time+(trunc(sf.dep_date)-trunc(f.dep_time)),"
	  + "f.dep_time+(trunc(sf.dep_date)-trunc(f.dep_time))+(f.est_dur/60+a2.tzone-a1.tzone)/24, "
	  + "fa.fare, fa.limit-count(tno), fa.price from flights f, "
	  + "flight_fares fa, sch_flights sf, bookings b, airports a1, airports a2 "
	  + "where f.flightno=sf.flightno and f.flightno=fa.flightno and f.src=a1.acode and "
	  + "f.dst=a2.acode and fa.flightno=b.flightno(+) and fa.fare=b.fare(+) and "
	  + "sf.dep_date=b.dep_date(+) "
	  + "group by f.flightno, sf.dep_date, f.src, f.dst, f.dep_time, f.est_dur,a2.tzone,"
	  + "a1.tzone, fa.fare, fa.limit, fa.price having fa.limit-count(tno) > 0;";
	  
	}
	
	
	

}
