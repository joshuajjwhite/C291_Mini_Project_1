package project1;

import java.sql.Date;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Queries {
	
	public static String[] dropTables(){
		
		 String drop[] = { "drop table airline_agents cascade constraints ",
		  "drop table users cascade constraints ",
		  "drop table bookings cascade constraints ",
		  "drop table tickets cascade constraints ",
		  "drop table passengers cascade constraints ",
		  "drop table flight_fares cascade constraints ",
		  "drop table fares cascade constraints ",
		  "drop table sch_flights cascade constraints ",
		  "drop table flights cascade constraints ",
		  "drop table airports cascade constraints ",
		  "drop view good_connections ",
		  "drop view available_flights "} ;
		 
		 return drop;
	}
	
	public static String[] createTables(){
		
		 String tables[] = { "create table airports ( " +
				  "acode		char(3), " +
				  "name		char(30), " +
				  "city		char(15), " +
				  "country	char(15), " +
				  "tzone		int, " +
				  "primary key (acode) ) ",
				 
				 "create table flights (flightno	char(6), src char(3), dst char(3), dep_time	date, est_dur int, "
				 + "primary key (flightno), foreign key (src) references airports, "
				 + "foreign key (dst) references airports) ",
				 
				 "create table sch_flights (flightno	char(6), dep_date	date, act_dep_time	date, act_arr_time	date, "
				+ "primary key (flightno,dep_date), foreign key (flightno) references flights on delete cascade) ",
				
				 "create table fares (fare char(2), descr		char(15), primary key (fare)) ",
				
				 "create table flight_fares (flightno	char(6), fare  char(2), limit int, "
				+ "price		float, bag_allow int, primary key (flightno,fare), "
				+ "foreign key (flightno) references flights, foreign key (fare) references fares) ",
				
				 "create table passengers (email char(20), name	char(20), country char(10), "
				+ "primary key (email, name)) ",
				
				 "create table tickets (tno	int, name char(20), email	char(20), paid_price float, primary key (tno), "
				+ "foreign key (email,name) references passengers) ",
				
				 "create table bookings (tno		int, flightno	char(6), fare		char(2), dep_date	date, "
				+ "seat		char(3), primary key (tno, flightno, dep_date), foreign key (tno) references tickets, "
				+ "foreign key (flightno, dep_date) references sch_flights, foreign key (fare) references fares) ",
				
				 "create table users (email char(20), pass char(4), last_login date, "
				+ "primary key (email)) ",
				
				 "create table airline_agents(email char(20), name char(20), "
				+ "primary key (email), "
				+ "foreign key (email) references users) "};
		 
		 return tables;
	}
	
	public static String[] insertSampleData(){
		String inserts[] = { "insert into airports values ('YEG','Edmonton Internatioanl Airport','Edmonton', 'Canada',-7) ",
				 "insert into airports values ('YYZ','Pearson Internatioanl Airport','Toronto', 'Canada',-5) ",
				 "insert into airports values ('YUL','Trudeau Internatioanl Airport','Montreal', 'Canada',-5) ",
				 "insert into airports values ('YVR','Vancouver Airport','Vancouver', 'Canada',-8) ",
				 "insert into airports values ('LAX','LA Airport','Los Angeles', 'US',-8) ",
				 "insert into airports values ('MOS','Tatooine Airport','Mos Eisley', 'Tatooine',-8) ",
				 "insert into airports values ('HND','Haneda Airport','Tokyo', 'Japan',+9) ",
				 "insert into airports values ('HOB','Hobbiton Airport','Hobbiton', 'Shire',+9) ",

				 "insert into flights values ('AC154','YEG','YYZ',to_date('15:50', 'hh24:mi'),221) ",
				 "insert into flights values ('AC158','YEG','YYZ',to_date('13:55', 'hh24:mi'),221) ",
				 "insert into flights values ('AC104','YEG','YUL',to_date('06:45', 'hh24:mi'),320) ",
				 "insert into flights values ('AC001','YEG','YVR',to_date('12:50', 'hh24:mi'),300) ",
				 "insert into flights values ('AC002','MOS','YEG',to_date('00:50', 'hh24:mi'),480) ",
				 "insert into flights values ('AC003','LAX','YEG',to_date('06:50', 'hh24:mi'),180) ",
				 "insert into flights values ('AC004','YEG','YYZ',to_date('03:50', 'hh24:mi'),221) ",
				 "insert into flights values ('AC005','YYZ','YVR',to_date('17:50', 'hh24:mi'),240) ",
				 "insert into flights values ('AC006','MOS','LAX',to_date('22:30', 'hh24:mi'),480) ",
				 "insert into flights values ('AC007','YUL','MOS',to_date('19:15', 'hh24:mi'),120) ",
				 "insert into flights values ('AC008','YVR','MOS',to_date('11:00', 'hh24:mi'),90) ",
				 "insert into flights values ('AC009','YVR','HND',to_date('00:05', 'hh24:mi'),540) ",
				 "insert into flights values ('AC010','YYZ','HND',to_date('09:05', 'hh24:mi'),340) ",
				 "insert into flights values ('AC011','MOS','HND',to_date('13:15', 'hh24:mi'),400) ",
				 "insert into flights values ('AC012','MOS','HND',to_date('01:30', 'hh24:mi'),400) ",
				 "insert into flights values ('AC013','YEG','HND',to_date('07:45', 'hh24:mi'),360) ",
				 "insert into flights values ('AC014','YEG','LAX',to_date('01:13', 'hh24:mi'),180) ",
				 "insert into flights values ('AC015','YEG','LAX',to_date('11:13', 'hh24:mi'),180) ",
				 "insert into flights values ('AC016','HOB','LAX',to_date('14:00', 'hh24:mi'),60) ",
				 "insert into flights values ('AC017','HND','LAX',to_date('14:00', 'hh24:mi'),120) ",
				 "insert into flights values ('AC018','YVR','LAX',to_date('01:15', 'hh24:mi'),105) ",
				 "insert into flights values ('AC019','YVR','LAX',to_date('11:15', 'hh24:mi'),105) ",
				 "insert into flights values ('AC020','YEG','LAX',to_date('13:00', 'hh24:mi'),180) ",
				 "insert into flights values ('AC021','HOB','LAX',to_date('18:00', 'hh24:mi'),60) ",
				 "insert into flights values ('AC022','YEG','HOB',to_date('09:00', 'hh24:mi'),120) ",
				 "insert into flights values ('AC023','YEG','HOB',to_date('14:00', 'hh24:mi'),120) ",
				 "insert into flights values ('AC024','YEG','YVR',to_date('05:00', 'hh24:mi'),300) ",
				 "insert into flights values ('AC025','YEG','YVR',to_date('20:00', 'hh24:mi'),360) ",
				 "insert into flights values ('AC026','MOS','HOB',to_date('00:15', 'hh24:mi'),200) ",
				 "insert into flights values ('AC027','LAX','YEG',to_date('05:15', 'hh24:mi'),180) ",
				 "insert into flights values ('AC028','HND','YEG',to_date('13:15', 'hh24:mi'),360) ",

				 "insert into sch_flights values ('AC154',to_date('22-Sep-2015','DD-Mon-YYYY'),to_date('15:50', 'hh24:mi'),to_date('21:30','hh24:mi')) ",
				 "insert into sch_flights values ('AC154',to_date('23-Sep-2015','DD-Mon-YYYY'),to_date('15:55', 'hh24:mi'),to_date('21:36','hh24:mi')) ",

				 "insert into sch_flights values ('AC001',to_date('15-Oct-2015','DD-Mon-YYYY'),to_date('12:50', 'hh24:mi'),to_date('17:50','hh24:mi')) ",
				 "insert into sch_flights values ('AC001',to_date('17-Oct-2015','DD-Mon-YYYY'),to_date('12:50', 'hh24:mi'),to_date('17:50','hh24:mi')) ",
				 "insert into sch_flights values ('AC005',to_date('19-Oct-2015','DD-Mon-YYYY'),to_date('17:50', 'hh24:mi'),to_date('21:50','hh24:mi')) ",
				 "insert into sch_flights values ('AC007',to_date('19-Oct-2015','DD-Mon-YYYY'),to_date('19:15', 'hh24:mi'),to_date('21:15','hh24:mi')) ",
				 "insert into sch_flights values ('AC007',to_date('20-Oct-2015','DD-Mon-YYYY'),to_date('19:15', 'hh24:mi'),to_date('19:15','hh24:mi')) ",
				 "insert into sch_flights values ('AC007',to_date('21-Oct-2015','DD-Mon-YYYY'),to_date('19:15', 'hh24:mi'),to_date('19:15','hh24:mi')) ",
				 "insert into sch_flights values ('AC026',to_date('22-Oct-2015','DD-Mon-YYYY'),to_date('00:15', 'hh24:mi'),to_date('3:20','hh24:mi')) ",
				 "insert into sch_flights values ('AC004',to_date('22-Oct-2015','DD-Mon-YYYY'),to_date('03:50', 'hh24:mi'),to_date('7:35','hh24:mi')) ",

				 "insert into sch_flights values ('AC027',to_date('23-Oct-2015','DD-Mon-YYYY'),to_date('05:15', 'hh24:mi'),to_date('08:15','hh24:mi')) ",
				 "insert into sch_flights values ('AC028',to_date('23-Oct-2015','DD-Mon-YYYY'),to_date('13:15', 'hh24:mi'),to_date('19:15','hh24:mi')) ",

				 "insert into sch_flights values ('AC009',to_date('01-Dec-2015','DD-Mon-YYYY'),to_date('00:05', 'hh24:mi'),to_date('09:05','hh24:mi')) ",
				 "insert into sch_flights values ('AC009',to_date('02-Dec-2015','DD-Mon-YYYY'),to_date('00:05', 'hh24:mi'),to_date('09:05','hh24:mi')) ",
				 "insert into sch_flights values ('AC017',to_date('03-Dec-2015','DD-Mon-YYYY'),to_date('14:15', 'hh24:mi'),to_date('16:15','hh24:mi')) ",


				 "insert into sch_flights values ('AC014',to_date('22-Dec-2015','DD-Mon-YYYY'),to_date('01:15', 'hh24:mi'),to_date('04:15','hh24:mi')) ",
				 "insert into sch_flights values ('AC015',to_date('22-Dec-2015','DD-Mon-YYYY'),to_date('11:15', 'hh24:mi'),to_date('14:15','hh24:mi')) ",
				 "insert into sch_flights values ('AC020',to_date('22-Dec-2015','DD-Mon-YYYY'),to_date('13:00', 'hh24:mi'),to_date('16:00','hh24:mi')) ",
				 "insert into sch_flights values ('AC022',to_date('22-Dec-2015','DD-Mon-YYYY'),to_date('09:15', 'hh24:mi'),to_date('11:15','hh24:mi')) ",
				 "insert into sch_flights values ('AC016',to_date('22-Dec-2015','DD-Mon-YYYY'),to_date('14:00', 'hh24:mi'),to_date('15:00','hh24:mi')) ",
				 "insert into sch_flights values ('AC013',to_date('22-Dec-2015','DD-Mon-YYYY'),to_date('07:45', 'hh24:mi'),to_date('13:45','hh24:mi')) ",
				 "insert into sch_flights values ('AC017',to_date('22-Dec-2015','DD-Mon-YYYY'),to_date('14:15', 'hh24:mi'),to_date('16:15','hh24:mi')) ",
				 "insert into sch_flights values ('AC154',to_date('22-Dec-2015','DD-Mon-YYYY'),to_date('15:55', 'hh24:mi'),to_date('21:36','hh24:mi')) ",
				 "insert into sch_flights values ('AC158',to_date('22-Dec-2015','DD-Mon-YYYY'),to_date('13:55', 'hh24:mi'),to_date('17:35','hh24:mi')) ",

				 "insert into fares values ('J', 'Business class') ",
				 "insert into fares values ('Y', 'Economy Lat') ",
				 "insert into fares values ('Q', 'Flex') ",
				 "insert into fares values ('F', 'Tango') ",

				 "insert into flight_fares values ('AC154','J',10,2000,2) ",
				 "insert into flight_fares values ('AC154','Y',20,700,0) ",
				 "insert into flight_fares values ('AC154','Q',10,500,0) ",
				 "insert into flight_fares values ('AC158','J',10,2000,2) ",
				 "insert into flight_fares values ('AC158','Q',10,250,0) ",
				 "insert into flight_fares values ('AC158','F',10,100,0) ",
				 "insert into flight_fares values ('AC001','J',10,2000,2) ",
				 "insert into flight_fares values ('AC001','Q',10,500,0) ",
				 "insert into flight_fares values ('AC005','J',10,2000,2) ",
				 "insert into flight_fares values ('AC007','J',10,2000,2) ",
				 "insert into flight_fares values ('AC007','F',20,200,0) ",
				 "insert into flight_fares values ('AC026','J',10,2000,2) ",
				 "insert into flight_fares values ('AC026','Y',20,700,0) ",
				 "insert into flight_fares values ('AC004','Q',10,500,0) ",
				 "insert into flight_fares values ('AC014','J',10,2000,2) ",
				 "insert into flight_fares values ('AC014','Y',20,700,0) ",
				 "insert into flight_fares values ('AC014','Q',5,500,0) ",
				 "insert into flight_fares values ('AC014','F',20,100,0) ",
				 "insert into flight_fares values ('AC015','J',10,2000,2) ",
				 "insert into flight_fares values ('AC015','Y',10,700,0) ",
				 "insert into flight_fares values ('AC020','Y',10,700,0) ",
				 "insert into flight_fares values ('AC022','Q',15,500,0) ",
				 "insert into flight_fares values ('AC016','J',10,2000,2) ",
				 "insert into flight_fares values ('AC016','Y',20,700,0) ",
				 "insert into flight_fares values ('AC016','Q',10,500,0) ",
				 "insert into flight_fares values ('AC013','Y',10,700,0) ",
				 "insert into flight_fares values ('AC013','F',30,200,0) ",
				 "insert into flight_fares values ('AC017','J',20,2000,2) ",
				 "insert into flight_fares values ('AC001','F',10,200,0) ",
				 "insert into flight_fares values ('AC009','J',10,2000,0) ",
				 "insert into flight_fares values ('AC009','Y',10,200,0) ",
				 "insert into flight_fares values ('AC027','J',10,1500,2) ",
				 "insert into flight_fares values ('AC027','F',10,200,0) ",
				 "insert into flight_fares values ('AC028','Q',10,800,0) ",
				 "insert into flight_fares values ('AC028','Y',10,700,0) ",

				 "insert into passengers values ('davood@ggg.com','Davood Rafiei','Canada') ",
				 "insert into passengers values ('david@ggg.com','David Raft','Canada') ",
				 "insert into passengers values ('gandalf@wizard.com','Gandalf Grey','Canada') ",
				 "insert into passengers values ('ralph@ggg.com','Ralph Rafiei','Canada') ",
				 "insert into passengers values ('uematsu@ff.com','Nobuo Uematsu','Japan') ",
				 "insert into passengers values ('bill@ggg.com','Bill Smith','US') ",
				 "insert into passengers values ('jack@ggg.com','Jack Daniel','Canada') ",
				 "insert into passengers values ('greg@ggg.com','Greg Davis','Canada') ",
				 "insert into passengers values ('thorin@ggg.com','Thorin Oakenshield','Canada') ",
				 "insert into passengers values ('elrond@ggg.com','Elrond Smith','Canada') ",
				 "insert into passengers values ('john@ggg.com','John Smith','US') ",
				 "insert into passengers values ('man@ggg.com','Man Smith','Canada') ",
				 "insert into passengers values ('dude@ggg.com','Dude Smith','Canada') ",
				 "insert into passengers values ('person@ggg.com','Person Smith','Japan') ",

				 "insert into tickets values (111,'Davood Rafiei','davood@ggg.com',700) ",
				 "insert into tickets values (001,'Davood Rafiei','davood@ggg.com',200) ",
				 "insert into tickets values (002,'Gandalf Grey','gandalf@wizard.com',1500) ",
				 "insert into tickets values (003,'Nobuo Uematsu','uematsu@ff.com',800) ",
				 "insert into tickets values (004,'Dude Smith','dude@ggg.com',700) ",
				 "insert into tickets values (005,'Man Smith','man@ggg.com',200) ",
				 "insert into tickets values (006,'Gandalf Grey','gandalf@wizard.com',700) ",
				 "insert into tickets values (007,'Thorin Oakenshield','thorin@ggg.com',2000) ",

				 "insert into bookings values (111,'AC154','Y',to_date('22-Dec-2015','DD-Mon-YYYY'),'20B') ",
				 "insert into bookings values (001,'AC027','F',to_date('23-Oct-2015','DD-Mon-YYYY'),'10B') ",
				 "insert into bookings values (002,'AC027','J',to_date('23-Oct-2015','DD-Mon-YYYY'),'10A') ",
				 "insert into bookings values (003,'AC028','Q',to_date('23-Oct-2015','DD-Mon-YYYY'),'1A') ",
				 "insert into bookings values (004,'AC028','Y',to_date('23-Oct-2015','DD-Mon-YYYY'),'2A') ",
				 "insert into bookings values (005,'AC013','F',to_date('22-Dec-2015','DD-Mon-YYYY'),'20A') ",
				 "insert into bookings values (006,'AC020','Y',to_date('22-Dec-2015','DD-Mon-YYYY'),'20B') ",
				 "insert into bookings values (007,'AC014','J',to_date('22-Dec-2015','DD-Mon-YYYY'),'10B') ",
				
				 "insert into users values ('gandalf@wizard.com', 'lala', to_date('15-Oct-2015','DD-Mon-YYYY'))",
				
				 "insert into airline_agents values ('gandalf@wizard.com','Gandalf Grey')"
		};
		return inserts;
	}
	
	
	public static String getDate(String indate){
		//used to synthesize a date from user and output it in a way we can parse with
		
	SimpleDateFormat df = new SimpleDateFormat( "dd-MMM-yyyy");
		
		Calendar cal = Calendar.getInstance();
		try {
			cal.setTime(df.parse(indate));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			return null;
		}
		
		int month = cal.get(Calendar.MONTH); 
		int day = cal.get(Calendar.DAY_OF_MONTH);
		int year = cal.get(Calendar.YEAR);

		String date = Integer.toString(day) + "-" +
							getMonth(month) + "-" +
							Integer.toString(year);	//DD-Mon-YYYY
		
		return date;
		
		
	}
	
	public static String[] searchFlight(String src, String dst, String indate, Boolean wantthreeflights, Boolean orderbystops){
		//Main function for searching for flights
		//Use wantthreeflights for a user who wants three connecting flights as well as two and direct
		//use orderbystops to output a table sorted by stops (least to greatest) and then price secondary
			String dep_date = indate;
			//String dep_date = getDate(indate);
			String availableflights[] = createAvailableFlights();
			
			if(wantthreeflights){
				String threeflights[] = {availableflights[0], availableflights[1], createGoodConnections3()[0],
						createGoodConnections3()[1], createGoodConnections2()[0], createGoodConnections2()[1], getGoodFlights3(dep_date, src, dst, orderbystops)};
				return threeflights;
			}
			
			else{String twoflights[] = {availableflights[0], availableflights[1], createGoodConnections2()[0],
			
				createGoodConnections2()[1], getGoodFlights2(dep_date, src, dst, orderbystops)};
			
				return twoflights;
			}
	
	}
	
	public static String findAcode(String acode){
		return "SELECT * FROM airports WHERE acode = '" + acode + "'";
	}

	public static String searchAcodeByCity(String city){

			return "SELECT acode "
				+ "FROM airports "
				+ "WHERE city = '" + city + "'";

				

	}

	public static String searchAcodeByName(String name){

		return "SELECT acode " +
				 "FROM airports "
				+ "WHERE name = '" + name + "'";



	}
	
	public static String searchCities(String city){
		//used to narrow down partial user input and will return less and less results as the user specifies input
		return "SELECT DISTINCT city, name "
				+ "FROM airports "
				+ "WHERE city LIKE '%" + city + "%' OR name LIKE '%" + city + "%'";
		
	}
	
	
	
	
	private static String getMonth(int mon){
		
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
	
	
	
	
	public static String[] createAvailableFlights(){
	
	String availableflights[] = {	 "drop view available_flights ",
	
	  "create view available_flights(flightno, dep_date, src , dst, dep_time, arr_time, fare, seats, "
	 + "price) as "
	 + "select f.flightno, sf.dep_date, f.src, f.dst, "
	 + " f.dep_time+(trunc(sf.dep_date)-trunc(f.dep_time)), "
	 + "f.dep_time+(trunc(sf.dep_date)-trunc(f.dep_time))+(f.est_dur/60+a2.tzone-a1.tzone)/24, "
	 + "fa.fare, fa.limit-count(tno), fa.price FROM flights f, "
	 + "flight_fares fa, sch_flights sf, bookings b, airports a1, airports a2 "
	 + "where f.flightno=sf.flightno and f.flightno=fa.flightno and f.src=a1.acode and "
	 + "f.dst=a2.acode and fa.flightno=b.flightno(+) and fa.fare=b.fare(+) and "
	 + "sf.dep_date=b.dep_date(+) "
	 + "group by f.flightno, sf.dep_date, f.src, f.dst, f.dep_time, f.est_dur,a2.tzone, "
	 + "a1.tzone, fa.fare, fa.limit, fa.price having fa.limit-count(tno) > 0 "};
	
	return availableflights;
	 
	}

	public static String[] createGoodConnections2(){

		 String gc[] = { "drop view good_connections2 ",
				 
 				 "create view good_connections2(src,dst,dep_date,flightno1,flightno2, arr_time, layover, price, seats) " +
 				 "as select " +
 				 "a1.src, a2.dst, a1.dep_date, a1.flightno, a2.flightno, to_char(a2.arr_time, 'hh24:mi'), " + 
 				 "a2.dep_time-a1.arr_time, min(a1.price+a2.price), a1.seats || ', ' || a2.seats " +
 				 "from available_flights a1, available_flights a2 " +
 				 "where a1.dst=a2.src and a2.dep_time - a1.arr_time >= 1.5/24 and a2.dep_time - a1.arr_time <= 5/24 " +
 				 "group by a1.src, a2.dst, a1.dep_date, a2.arr_time, a1.seats, a2.seats, a1.flightno, a2.flightno, a2.dep_time, a1.arr_time "};
		 
		 return gc;

	}
	
	private static String[] createGoodConnections3(){
		String gc[] = { "drop view good_connections3 ",
				 
 				 "create view good_connections3(src,dst,dep_date, flightno1, flightno2, flightno3, arr_time, layover1, layover2, price, seats) " +
 				 "as select " +

 				 "a1.src, a3.dst, a1.dep_date, a1.flightno, a2.flightno, a3.flightno, to_char(a3.arr_time, 'hh24:mi'), " +
 				 "a2.dep_time-a1.arr_time, a3.dep_time-a2.arr_time, min(a1.price+a2.price+a3.price), a1.seats || ', ' || a2.seats || ', ' || a3.seats " +

 				 "from available_flights a1, available_flights a2, availableflights a3 " +

 				 "where a1.dst=a2.src and a2.dst = a3.src and a2.dep_time - a1.arr_time >= 1.5/24 and a2.dep_time - a1.arr_time <= 5/24 and a3.dep_time - a2.arr_time >= 1.5/24 and a3.dep_time - a2.arr_time <= 5/24 " +

 				 "group by a1.src, a3.dst, a1.dep_date, a3.arr_time, a1.seats, a2.seats, a3.seats, a1.flightno, a2.flightno, a3.flightno, a3.dep_time, a2.arr_time, a2.dep_time, a1.arr_time "};
		 
		 return gc;

	}
	
	private static String getGoodFlights3(String dep_date, String src, String dst, Boolean orderbystops){ 

		String ggf = "select flightno1, flightno2, flightno3, src, dst, dep_date, arr_time, stops, layover1, layover2, price, seats " + 
		 		 "from ( " +
			 		 "select flightno1, flightno2, flightno3, src, dst, dep_date, arr_time, stops, layover, price, seats " + 
			 		 "from " + 
			 			 "(select gc3.flightno1, gc3.flightno2, gc3.flightno3, gc3.src, gc3.dst, gc3.dep_date, to_char(gc3.arr_time, 'hh24:mi') as arr_time, 2 stops, gc3.layover1, gc3.layover2, gc3.price, gc3.seats " +
			 			 "From good_connections3 gc3 " +
			 			 "where to_char(gc.dep_date,'DD-Mon-YYYY')= '" + dep_date + 
			 			 "' and gc.src='" + src +
			 			 "' and gc.dst='" + dst + "'" +
					 "union " +
			 			 "select gc.flightno1, gc.flightno2, '' flightno3, gc.src, gc.dst, gc.dep_date, to_char(gc.arr_time, 'hh24:mi') as arr_time, 1 stops, gc.layover, gc..price, gc.seats " +
			 			 "from good_connections2 gc " +
			 			 "where to_char(gc.dep_date,'DD-Mon-YYYY')= '" + dep_date + 
			 			 "' and gc.src='" + src +
			 			 "' and gc.dst='" + dst + "'" +
					 "union " +
			 			 "select flightno, '' flightno2, '' flightno3, src, dst, dep_date, to_char(arr_time, 'hh24:mi') as arr_time, 0 stops, 0 layover, price, to_char(seats) " +
			 			 "from available_flights " +
			 			 "where to_char(dep_date,'DD-Mon-YYYY')='" + dep_date + 
			 			 "' and src='" + src + "' " +
			 			 "and dst='" + dst + "')) " ;
		
		if(orderbystops){
			ggf = ggf + "ORDER BY stops, price ";
		}
		
		else{ggf = ggf + "Order BY price ";}
		
		
		return ggf;

	}

	private static String getGoodFlights2(String dep_date, String src, String dst, Boolean orderbystops){


		String ggf = "select flightno1, flightno2, src, dst, dep_date, arr_time , stops, layover, price, seats " + 
		 		 "from ( " +
			 		 "select flightno1, flightno2, src, dst, dep_date, arr_time, stops, layover, price, seats " + 
			 		 "from " + 
			 			 "(select gc.flightno1, gc.flightno2, gc.src, gc.dst, gc.dep_date, to_char(gc.arr_time, 'hh24:mi') as arr_time, 1 stops, gc.layover, gc.price, gc.seats " +
			 			 "from good_connections2 gc " +
			 			 "where to_char(gc.dep_date,'DD-Mon-YYYY')= '" + dep_date + 
			 			 "' and gc.src='" + src +
			 			 "' and gc.dst='" + dst + "' " +
					 "union " +
			 			 "select flightno, '' flightno2, src, dst, dep_date, to_char(arr_time, 'hh24:mi') as arr_time, 0 stops, 0 layover, price, to_char(seats) " +
			 			 "from available_flights " +
			 			 "where to_char(dep_date,'DD-Mon-YYYY')='" + dep_date + 
			 			 "' and src='" + src + "' " +
			 			 "and dst='" + dst + "')) ";
		
		if(orderbystops){
			ggf = ggf + "ORDER BY stops, price ";
		}
		
		else{ggf = ggf + "Order BY price ";}
		
		return ggf;
		
	}

	public static String getFare(String flightNo) {
		return 	"Select fare " +
				"From flight_fares " +
				"Where flightNo = '" + flightNo +"'";
	}
	
	public static String checkPassengers(String email, String name){
		//passengers(email, name, country) 
		 return "SELECT email, name " + 
				 "FROM passengers " +
				 "WHERE email = '" + email + "' and name = '" + name + "' ";
	}

	public static String addPassenger(String email, String name, String country){
		return "INSERT into passengers values('" +
				email + "', ' " + name + "', ' " + country + "') " ;
	}

	public static String addBooking(String tno, String flightno, String fare, String dep_date, String seat){
		//bookings(tno, flightno, fare, dep_date, seat)
			return "INSERT into bookings values(" +
				tno + ", '" + flightno + "', '" + fare + "', " +
				 "TO_DATE('" +dep_date+ "', 'DD-MON-YYYY') " + ", '" + seat + "')" ;
	}
	
	public static String checkTicket(String tno) {
		//used to see if the randomly generated ticketno is already used.
		//should return empty then the ticketno can be used
		return "Select * " +
				 "From tickets " +
				 "Where tno = " + tno;
	}

	public static String checkSeat(String seat) {
		return  "Select seat " +
				"From bookings " +
				"Where seat = '" + seat + "'";
	}
	
	public static String addTicket(String tno,String name,String email,String paid_price){
		//tickets(tno, name, email, paid_price)
			return "INSERT into tickets values(" +
				tno + ", '" + name + "', '" + email + "', " + paid_price + ") " ; 
			
	}
	
	public static String findUser(String email){
		return "select * from users where email = '" + email + "'";
	}
	
	public static String insertUser(String email, String password){
		return "insert into users values ('" + email + "', '" + password + "', SYSDATE) ";
	}
	
	public static String updateLastLogin(String email){
		return "UPDATE users " +
				 "SET last_login = SYSDATE " +
				 "WHERE email = '" + email + "' ";
		}	
	
	public static String findAgent(String email){
		return "SELECT * " +
				 "FROM airline_agents " +
				 "WHERE email = '" + email + "'";
	}
	
	public static String getUserBookings(String email){
		return "SELECT * FROM bookings WHERE tno IN " +
				 "(SELECT tno FROM tickets WHERE email = '" + email + "') ";
	}

	//delete booking
	public static String removeBooking(String tno){
		//bookings(tno, flightno, fare, dep_date, seat)
		//used to remove boookings from a flight
		//remember that the tickets also has to be removed if a booking is deleted
		return "DELETE FROM bookings " +
				 "WHERE tno = " + tno;
	}
	
	public static String removeTicket(String tno){
		return "DELETE FROM tickets " +
				 "WHERE tno = " + tno;
	}

	//Record flight departure
	public static String recordDeparture(String flightno, String dep_date, String time){ //time in 24h:min
			
		if(time == null){
		
		return "UPDATE sch_flights " +
	 		"SET act_dep_time = SYSDATE " +
	 		"WHERE dep_date = (TO_DATE('" + dep_date + "', 'DD-MON-YY')) and flightno = '" + flightno + "'";
		}
		
		else{

			return "UPDATE sch_flights " +
				 "SET act_dep_time = (TO_DATE('" + time + "', 'HH24:MI')) " +
				 "WHERE dep_date = '" + dep_date + "' and flightno = '" + flightno + "'";

		}
	}	


	//record flight arrival
	public static String recordArrival(String flightno, String dep_date, String time){ //time in 24h:min
			
		if(time == null){

		return "UPDATE sch_flights " +
				 "SET act_arr_time = SYSDATE " +
				 "WHERE dep_date = (TO_DATE('" + dep_date + "', 'DD-MON-YY')) and flightno = '" + flightno + "'";
		}

		else{

			return "UPDATE sch_flights " +
				 "SET act_arr_time = (TO_DATE('" + time + "', 'HH24:MI')) " +
				 "WHERE dep_date = '" + dep_date + "' and flightno = " + flightno;

		}
	}

	//round trip query
	public static String[] roundTrip(String src, String dst, String dep_date, String ret_date) {
		String roundTripQuery = makeRoundTrip(src,dst, dep_date, ret_date);
		String availableflights[] = createAvailableFlights();
		String[] queryArray = {availableflights[0], availableflights[1], roundTripQuery};
		
		return queryArray;
	}
	
	private static String makeRoundTrip(String src, String dst, String dep_date, String ret_date) {
		return "Select af1.flightno as flightno1, af2.flightno as flightno2 af1.src as src, af1.dst as dst, af1.dep_date as dep_date, af2.dep_date as ret_date, af1.price+af2.price as price " +
				 "From available_flights af1, available_flights af2 " +
				 "Where to_char(af1.dep_date, 'DD/MM/YYYY')=' " + dep_date + "' And " +
				 "to_char(af2.dep_date, 'DD/MM/YYYY')=' " + ret_date + "' And " +
				 "af1.src = '"+src+ "' And af1.dst = '"+dst+ "' And af2.src='"+dst+ "' And af2.dst='"+src+ "'" +
				 "Order By af1.price+af2.price " ;
	}
	
}
