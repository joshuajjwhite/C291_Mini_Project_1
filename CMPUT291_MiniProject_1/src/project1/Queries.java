package project1;

import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Queries {

	public static String[] dropTables(){
		
		 String drop[] =  {"drop table airline_agents cascade constraints",
		  "drop table users cascade constraints",
		  "drop table bookings cascade constraints",
		  "drop table tickets cascade constraints",
		  "drop table passengers cascade constraints",
		  "drop table flight_fares cascade constraints",
		  "drop table fares cascade constraints",
		  "drop table sch_flights cascade constraints",
		  "drop table flights cascade constraints",
		  "drop table airports cascade constraints",
		  "drop view good_connections ",
		  "drop view available_flights "} ;
		 
		 return drop;
	}
	
	public static String[] createTables(){
		
		 String tables[] = {"create table airports (" +
				  "acode		char(3)," +
				  "name		char(30)," +
				  "city		char(15)," +
				  "country	char(15)," +
				  "tzone		int," +
				  "primary key (acode) )",
				  
				"create table flights (flightno	char(6), src char(3), dst char(3), dep_time	date, est_dur int,"
				  + "primary key (flightno), foreign key (src) references airports,"
				  + "foreign key (dst) references airports)",
				  
				"create table sch_flights (flightno	char(6), dep_date	date, act_dep_time	date, act_arr_time	date,"
				+ "primary key (flightno,dep_date), foreign key (flightno) references flights on delete cascade)",
				
				"create table fares (fare		char(2), descr		char(15), primary key (fare))",
				
				 "create table flight_fares (flightno	char(6), fare		char(2), limit		int,"
				+ "price		float, bag_allow	int, primary key (flightno,fare),"
				+ "foreign key (flightno) references flights, foreign key (fare) references fares)",
				
				 "create table passengers (email		char(20), name		char(20), country	char(10),"
				+ "primary key (email, name))",
				
				"create table tickets (tno	int, name char(20), email	char(20), paid_price float, primary key (tno),"
				+ "foreign key (email) references passengers)",
				
				"create table bookings (tno		int, flightno	char(6), fare		char(2), dep_date	date,"
				+ "seat		char(3), primary key (tno,flightno,dep_date), foreign key (tno) references tickets,"
				+ "foreign key (flightno, dep_date) references sch_flights,foreign key (fare) references fares)",
				
				"create table users (email char(20), pass char(4), last_login date, "
				+ "primary key (email))",
				
				"create table airline_agents(email char(20), name char(20),"
				+ "primary key (email), "
				+ "foreign key (email) references users)"};
		 
		 return tables;
	}
	
	
	private String getDate(String indate){
		
	SimpleDateFormat df = new SimpleDateFormat();
		
		Calendar cal = Calendar.getInstance();
		try {
			cal.setTime(df.parse(indate));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException("Not a valid date");
		}
		
		int month = cal.get(Calendar.MONTH); 
		int day = cal.get(Calendar.DAY_OF_MONTH);
		int year = cal.get(Calendar.YEAR);

		String date = Integer.toString(day) + "-" +
							getMonth(month) + "-" +
							Integer.toString(year);	//DD-Mon-YYYY
		
		return date;
		
		
	}
	
	public static String searchFlight(String src, String dst, String dep_date){
		
		
		
	
				
				return Queries.getGoodFlights(dep_date, src, dst);
		
	}
	


	public static String searchAcodeByCity(String city){

			return "SELECT acode"
				+ "FROM airports"
				+ "WHERE city = '" + city + "'";

				

	}

	public static String searchAcodeByName(String name){

		return "SELECT acode" +
				 "FROM airports"
				+ "WHERE name = '" + name + "'";



	}
	
	public static String searchCities(String city){
		
		return "SELECT DISTINCT city, name"
				+ "FROM airports"
				+ "WHERE city LIKE '%" + city + "%' OR name LIKE '%" + city + "%'";
		
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
	
	//existing bookings
	
	//delete booking
	
	//check available flights
	
	
	public static String createAvailableFlights(){
	
	return	"drop view available_flights" +
	
	  "create view available_flights(flightno, dep_date, src , dst, dep_time, arr_time, fare, seats,"
	  + "price) as"
	  + "select f.flightno, sf.dep_date, f.src, f.dst,"
	  + " f.dep_time+(trunc(sf.dep_date)-trunc(f.dep_time)),"
	  + "f.dep_time+(trunc(sf.dep_date)-trunc(f.dep_time))+(f.est_dur/60+a2.tzone-a1.tzone)/24, "
	  + "fa.fare, fa.limit-count(tno), fa.price FROM flights f, "
	  + "flight_fares fa, sch_flights sf, bookings b, airports a1, airports a2 "
	  + "where f.flightno=sf.flightno and f.flightno=fa.flightno and f.src=a1.acode and "
	  + "f.dst=a2.acode and fa.flightno=b.flightno(+) and fa.fare=b.fare(+) and "
	  + "sf.dep_date=b.dep_date(+) "
	  + "group by f.flightno, sf.dep_date, f.src, f.dst, f.dep_time, f.est_dur,a2.tzone,"
	  + "a1.tzone, fa.fare, fa.limit, fa.price having fa.limit-count(tno) > 0;";
	  
	}

	public static String  createGoodConnections2(){

		  return "drop view good_connections;" +
 				"create view good_connections(src,dst,dep_date,flightno1,flightno2, layover,price)" +
 				"as select" +
 				"a1.src, a2.dst, a1.dep_date, a1.flightno, a2.flightno," + 
 				"a2.dep_time-a1.arr_time, min(a1.price+a2.price)" +
  				"from available_flights a1, available_flights a2" +
  				"where a1.dst=a2.src and a1.arr_time +1.5/24 <=a2.dep_time and a1.arr_time +5/24 >=a2.dep_time" +
  				"group by a1.src, a2.dst, a1.dep_date, a1.flightno, a2.flightno, a2.dep_time, a1.arr_time;" ;

	}

	private static String getGoodFlights2(String dep_date, String src, String dst, Boolean orderbystops){


		 /* DO NOT REMOVE! return "select flightno1, flightno2, layover, price" + 
  					"from (" +
  					"select flightno1, flightno2, layover, price, row_number() over (order by price asc) rn" + 
  					"from" + 
  						"(select flightno1, flightno2, layover, price" +
  						"from good_connections" +
  						"where to_char(dep_date,'DD/MM/YYYY')='" + 
  						dep_date + 
  						"' and src='" + src +
  						"' and dst='" + dst + "'" +
  						"union" +
  						"select flightno flightno1, '' flightno2, 0 layover, price" +
  						"from available_flights" +
  						"where to_char(dep_date,'DD/MM/YYYY')='" +
  						dep_date + 
  						"' and src=' " + src + "'" +
  						"and dst='" + dst + "'));"; */

		String ggf = "select flightno1, flightno2, src, dst, dep_date, to_char(arr_time, 'HH2:MI') , stops, layover, price, seats" + 
		  		"from (" +
			  		"select flightno1, flightno2, src, dst, dep_date, arr_time, stops, layover, price, seats" + 
			 		"from" + 
			  			"(select gc.flightno1, gc.flightno2, gc.src, gc.dst, gc.dep_date, af.arr_time, 1 stops, gc.layover, af.price, af.seats" +
			  			"from good_connections gc, available_flights af" +
			  			"where to_char(gc.dep_date,'DD-Mon-YYYY')= '" + dep_date + 
			  			"' and gc.src='" + src +
			  			"' and gc.dst='" + dst + "'" +
					"union" +
			  			"select flightno, '' flightno2, src, dst, dep_date, arr_time, 0 stops, 0 layover, price, seats" +
			  			"from available_flights" +
			  			"where to_char(dep_date,'DD-Mon-YYYY')='" + dep_date + 
			  			"' and src='" + src + "'" +
			  			"and dst='" + dst + "'))";
		
		if(orderbystops){
			ggf = ggf + "ORDER BY stops, price";
		}
		
		else{ggf = ggf + "Order BY price";}
		
		
		return ggf;
		
	}


	public static String checkPassengers(String email, String name){
		//passengers(email, name, country) 
		 return "SELECT email, name" + 
				"FROM passengers" +
				"WHERE email = '" + email + "' and name = '" + name + "'";

		

	}

	public static String addPassenger(String email, String name, String country){

		return"INSERT into passengers values(," +
				email + "', '" + name + "', '" + country + "')" ;
		
	
	}

	public static String addBooking(String tno, String flightno, String fare, String dep_date, String seat){
		//bookings(tno, flightno, fare, dep_date, seat)
			return "INSERT into bookings values(" +
				tno + ", " + flightno + ", '" + fare + "'," +
				"to_date('" +dep_date+"', 'DD-Mon-YYYY')" + ", " + seat + ")" ;
			
		
	}
	
	//create check ticket

	public static String addTicket(String tno,String name,String email,String paid_price){
		//tickets(tno, name, email, paid_price)
			return "INSERT into tickets values(" +
				tno + ", '" + name + "', '" + email + "', " + paid_price + ")" ; 
			
	}
	
	public static String findUser(String email){
		return "select * from users where email = '" + email + "'";
		
	}
	
	public static String insertUser(String email, String password){
		return "insert into users values ('" + email + "', '" + password + "', SYSDATE)";
		
	}
	
	public static String updateLastLogin(String email){
		return "UPDATE users" +
				"SET last_login = SYSDATE" +
				"WHERE email = '" + email + "'";
		
		
		}	
	
	public static String findAgent(String email){
		return "SELECT *" +
							"FROM airline_agents" +
							"WHERE email = '" + email + "'";
		
		}
	
}
