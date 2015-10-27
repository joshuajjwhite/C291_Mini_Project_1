package project1;

import java.sql.ResultSet;

import java.util.ArrayList;
import java.util.Random;
import java.util.HashMap;
import java.util.Map.Entry;

import textDevicePackage.TextDevice;

public class UserConsoleInterface {
	private final TextDevice io;
	private SqlManager sqlManager;
	
	private String currentUserEmail;
	public String getCurrentUserEmail() {
		return currentUserEmail;
	}

	public void setCurrentUserEmail(String currentUserEmail) {
		this.currentUserEmail = currentUserEmail;
	}

	private String currentUserType;
	
	public String getCurrentUserType() {
		return currentUserType;
	}

	public void setCurrentUserType(String currentUserType) {
		this.currentUserType = currentUserType;
	}

	public UserConsoleInterface(TextDevice io, SqlManager sqlManager){
	    this.io = io;
	    this.sqlManager = sqlManager;
		
//		SqlDB.sendExampleCommands();

	}
	
	public void clearConsole(){
//		for(int i = 0; i <2; i++){
			io.printf("###########################################################%n");
//		}
	}
	
	public void greet(){
		sqlManager.setup();
		
		selectMenuLoginOption();
		mainCLI();
	}

	public void selectMenuLoginOption(){
		boolean loop;
		do {
			loop = false;
			clearConsole();
			io.printf("Welcome to UA Travel.  What would you like to do? (Register(R)/Login(L)/Exit(E)) %n");
			String input = getInput().trim().toLowerCase();
			
			 switch (input) {
			 	 case "r":
		         case "register":
		        	 register();
		        	 loop = true;
		        	 break;
		         case "l":
		         case "login":
		        	 loop = !login();
		             break;
		         case "e":
		         case "exit":
		        	 exitProgram();
		         default:
		        	 io.printf("Invalid Input %n %n");
		        	 loop = true;
		        	 break;
			 }
			} while(loop);
	}
	
	public String getInput(){
		String input = io.readLine().trim();
		return input;
	}
	
	private void exitProgram(){
		io.printf("Exiting System %n");
		sqlManager.closeConnection();
		System.exit(0);
	}
	
	public void register(){
		
		boolean success = false;
		while(!success){
			
			
			io.printf("Please enter an email: %n");
			String email = getInput();
			
			boolean acceptedPass = false;
			String password = "";
			while (!acceptedPass){
				io.printf("Please enter 4 character password: %n");
				password = getInput();
				if (password.length() == 4){
					acceptedPass = true;
				} else {
					io.printf("Invalid password. Must be 4 characters ");
				}
				
			}
			
			if (!sqlManager.checkForUserWithEmail(email)) {//Ensure email not currently in use in tables
				sqlManager.addUser(email, password);
				io.printf("Thanks, You have been succesfully registered with the email %s %n", email);
				success = true;
			} else {
				io.printf("email already exists in system. Please use another %n");
			}
		}
	}
	
	public boolean login(){
		io.printf("To Login: %n");
		
		io.printf("Please enter your email:");
		String email = getInput();
		if ( sqlManager.checkForUserWithEmail(email) ){
			io.printf("Please enter your password:");
			String password = getInput();
			String systemPass = sqlManager.getUserPass(email);
			if (password.equals(systemPass)){
				
				
				String userType = "guest";
				
				if (sqlManager.checkForAgentWithEmail(email)){
					userType  = "agent";
				}
				
				setCurrentUserEmail(email);
				setCurrentUserType(userType);
			
				io.printf("You have successfully logged in with %s with status %s %n", email, userType);
				return true;
			}
			else{
				io.printf("Invaid password. %n");
				return false;
			}
		} else {
			io.printf("email not valid please register. %n");
			return false;
		}
	}
	
	public void mainCLI(){
		boolean loop = true;
		while(loop) {
			clearConsole();
			io.printf("Airport Main Menu %n");
			io.printf("################# %n");
			io.printf("1. Search for flight (& Make Booking) %n"
					 + "2. List existing bookings (& Cancel Booking) %n");
			if (getCurrentUserType().equals("agent")){
				 io.printf("3. Record flight departure %n"
						 + "4. Record flight arrival %n");
			}
			io.printf("L. logout %n");
			 
			String input = getInput().trim().toLowerCase();
			switch (input){
				case "1":
					searchForFlightMenu();
					break;
				case "2":
					listBookings();
					 break;
				 case "l":
					 logout();
					 break;
				 case "3":
					 if(getCurrentUserType().equals("agent")){
						 recordADeparture();
					 }
					 break;
				 case "4":
					 if(getCurrentUserType().equals("agent")){
						 recordAnArrival();
					 }
					 break;
				 default:
					 io.printf("Invalid Input %n %n");
					 break;				
			}
		}
	}
	
	public void searchForFlightMenu(){
		boolean loop = true;
		while(loop) {
			clearConsole();
			io.printf("Search For A Flight Menu %n");
			io.printf("######################## %n");
			io.printf("Enter Search \"(Source) (Destination) (Departure Date DD-Mon-YYYY)\" %n"
					 + "B. Back %n"
					 + "L. Logout %n");
			 
			String input = getInput().trim();
			switch (input){
				case "b":
					 loop = false;
					 break;
				case "l":
					 logout();
					 break;
				 default:
					 
					 // Search Logic
					 boolean successfulInput = true;
					 String[] splited = input.split("\\s+");
					 if ( splited.length == 3 ){
						 String src = splited[0].trim();
						 String dst = splited[1].trim();
						 String dep_date = splited[2].trim();
						 
						 if (Queries.getDate(dep_date) != null){
							 HashMap<Integer, HashMap<String, String>> flightList = sqlManager.searchFlights(src, dst, dep_date);
							 if (flightList != null){
								 displayFlights(flightList);
							 } else {
								 successfulInput = false;
								 io.printf("No flights returned with given src and dst %n %n");
								 break; 
							 }
						 } else {
							 successfulInput = false;
							 io.printf("Invalid Date%n %n");
							 break;
						 }
						 
					 } else {
						 successfulInput = false;
						 io.printf("Invalid Input, three arguments should be provided %n %n");
						 break;
					 }
							
			}
		}
	}
	
	public void displayFlights(HashMap<Integer, HashMap<String, String>> flightList){

		HashMap<String, String> flight; 

		io.printf("Found Flights %n");
		io.printf("#################### %n");
		
		for(int i=1;i<=flightList.size();i++){
			flight = flightList.get(i);
			
			io.printf("Flight Selection %d: %n", i);
			io.printf("---------------------- %n");
			io.printf("FLIGHTNO1 - %s %n", flight.get("FLIGHTNO1"));
			if (flight.containsKey("FLIGHTNO2") && flight.get("FLIGHTNO2")!="null") { io.printf("FLIGHTNO2 - %s %n", flight.get("FLIGHTNO2")); }
			if (flight.containsKey("FLIGHTNO3") && flight.get("FLIGHTNO3")!="null") { io.printf("FLIGHTNO3 - %s %n", flight.get("FLIGHTNO3")); }
			io.printf("SOURCE - %s %n", flight.get("SRC"));
			io.printf("DESTINATION - %s %n", flight.get("DST"));
			io.printf("DEPARTURE TIME - %s %n", flight.get("DEP_TIME"));
			io.printf("ARRIVAL TIME - %s %n", flight.get("ARR_TIME"));
			io.printf("# OF STOPS - %s %n", flight.get("STOPS"));
			io.printf("LAYOVER TIME - %s %n", flight.get("LAYOVER1")); //DO MATH FOOL AND CONDITIONALS YO
			io.printf("PRICE - %s %n", flight.get("PRICE"));
			io.printf("SEATS - %s %n", flight.get("SEATS"));
			
			io.printf("%n %n");
		}		
		
		boolean loop = true;
		while(loop) {
			io.printf("Choose desried flight number%n"
					 + "B. Back %n"
					 + "L. Logout %n");
			 
			String input = getInput().trim().toLowerCase();
			switch (input){
				case "b":
					 loop = false;
					 break;
				case "l":
					 logout();
					 break;
				 default:
					 if(Integer.valueOf(input) <= flightList.size() && Integer.valueOf(input) > 0){
						makeBooking(flightList.get(Integer.valueOf(input)));
					 }
					 io.printf("Invalid flight selection. Try again? %n");
					 break;
			}
		}
	}
	
	public void makeBooking(HashMap<String, String> flight){
		io.printf("Booking A Flight %n");
		io.printf("####################%n");
		io.printf("Please provide your name %n");
		
		String userName = getInput();
		
		//If the passenger has not been entered before, this will add them.
		if (!sqlManager.checkPassengers(this.getCurrentUserEmail(), userName)) {
			io.printf("%nYou are not registered in the passengers list. %n");
			io.printf("Please provide the country that you are currently living in. %n");
			
			String country = getInput();
			sqlManager.addPassenger(this.getCurrentUserEmail(), userName, country);
		}
		
		//Give them a ticket and add a booking.
		Integer ticketNumber;
		Boolean validTicketNumber = false;
		Random randomGenerator = new Random();
		
		for (int i = 1; i <= 3; i++) {
			while (!validTicketNumber) {
				ticketNumber = randomGenerator.nextInt(2147483647);
				if(sqlManager.checkTicket(ticketNumber)) { validTicketNumber = true; }
			}
			
			//Insert ticket and booking	io.printf("LAYOVER TIME - %s %n", flight.get("LAYOVER1")); //DO MATH FOOL AND CONDITIONALS YO
			//sqlManager.addTicket(ticketNumber, userName, this.getCurrentUserEmail(), flight.get("PRICE"));
			//sqlManager.addBooking(ticketNumber, flightno, fare, flight.get("DEP_DATE"), seat);
		}
	}
	
	public void listBookings(){
		io.printf("List Bookings %n");
		
		boolean loop = true;
		while(loop) {
			clearConsole();
			io.printf("Listed Bookings %n");
			io.printf("############### %n");
			
			//content
			HashMap<String, String> bookings = sqlManager.getBookings(getCurrentUserEmail());
			for(Entry<String, String> entry: bookings.entrySet()){
				io.printf(entry.getValue() + "%n");
			}
			
			io.printf("%n%nType \"(tno)\" to select Booking %n"
					 + "B. Back %n"
					 + "L. Logout %n");
			 
			String input = getInput().trim().toLowerCase();
			switch (input){
				case "b":
					 loop = false;
					 break;
				case "l":
					 logout();
					 break;
				 default:
					if ( bookings.containsKey(input) ) {
					 String value = bookings.get(input);
					 selectBooking(input, value);
					} else {
					 io.printf("Invalid Input %n %n");
					 break;
					}
			}
		}
	}
	
	public void selectBooking(String key, String value){
		boolean loop = true;
		while(loop){
			io.printf("Selected Booking with tno %s%n", key);
			io.printf("###############################%n");
			io.printf("Info: %s%n", value);
			
			io.printf("Type \"D\" to delete Booking %n"
					 + "B. Back %n"
					 + "L. Logout %n");
			 
			String input = getInput().trim().toLowerCase();
			switch (input){
				case "d":
					cancleBooking(key);
					loop = false;
					break;
				case "b":
					 loop = false;
					 break;
				case "l":
					 logout();
					 break;
				 default:
					 io.printf("Invalid Input");
					 break;
			}
		}
	}
	
	public void cancleBooking(String key){
		io.printf("Canceling Booking %s %n", key);
		sqlManager.cancelBooking(key);
	}
	
	public void logout(){
		sqlManager.logoutUser(this.getCurrentUserEmail());
		io.printf("Logout %n");
		exitProgram();
	}
	
	public void recordADeparture(){
		boolean loop = true;
		while(loop){
			io.printf("Record a Departure %n");
			io.printf("###############################%n");
			
			io.printf("Type \"(FlightNum) (Scheduled Departure Date DD-MON-YYYY) \" to Record a Departure %n"
					 + "B. Back %n"
					 + "L. Logout %n");
			 
			String input = getInput().trim().toLowerCase();
			switch (input){
				case "b":
					 loop = false;
					 break;
				case "l":
					 logout();
					 break;
				default:
					//do work
					boolean successfulInput = true;
					String[] splited = input.split("\\s+");
					if (splited.length == 2 ){
						 String act_dep_time = null;
						 if (splited.length == 3){
							 act_dep_time = splited[2];
						 }
						 String flightNum = splited[0];
						 String dep_date = splited[1];
						 if (Queries.getDate(dep_date) != null){
							 try {
								 sqlManager.updateDeparture(flightNum, dep_date, act_dep_time);
								 io.printf("Departure time has been updated %n %n");
								 break;
							 } catch (Exception e) {
								 successfulInput = false;
								 io.printf("Issue Updating Departure Time %n %n");
								 break; 
							 }
						 } else {
							 successfulInput = false;
							 io.printf("Invalid Departure Date%n %n");
							 break;
						 }
						 
					 } else {
						 successfulInput = false;
						 io.printf("Invalid Input, three arguments should not be provided %n %n");
						 break;
					 }
			}
		}
	}
	
	public void recordAnArrival(){
		boolean loop = true;
		while(loop){
			io.printf("Record an Arrival %n");
			io.printf("###############################%n");
			
			io.printf("Type \"(FlightNum) (Scheduled Departure Date DD-MON-YYYY) \" to Record an Arrival %n"
					 + "B. Back %n"
					 + "L. Logout %n");
			 
			String input = getInput().trim().toLowerCase();
			switch (input){
				case "b":
					 loop = false;
					 break;
				case "l":
					 logout();
					 break;
				default:
					//do work
					boolean successfulInput = true;
					String[] splited = input.split("\\s+");
					if (splited.length == 2 ){
						 String act_arr_time = null;
						 if (splited.length == 3){
							 act_arr_time = splited[2];
						 }
						 String flightNum = splited[0];
						 String dep_date = splited[1];
						 if (Queries.getDate(dep_date) != null){
							 try {
								 sqlManager.updateArrival(flightNum, dep_date, act_arr_time);
								 io.printf("Arrival time has been updated %n %n");
								 break;
							 } catch (Exception e) {
								 successfulInput = false;
								 io.printf("Issue Updating Arrival Time %n %n");
								 break; 
							 }
						 } else {
							 successfulInput = false;
							 io.printf("Invalid Arrival Date%n %n");
							 break;
						 }
						 
					 } else {
						 successfulInput = false;
						 io.printf("Invalid Input, three arguments should not be provided %n %n");
						 break;
					 }
			}
		}
	}

	
}
