package project1;

import java.sql.ResultSet;
import java.util.ArrayList;
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
		String input = io.readLine().trim().toLowerCase();;
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
			io.printf("Enter Search \"(source) (destination) (DD-Mon-YYYY)\" %n"
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
					 
					 // Search Logic
					 boolean successfulInput = true;
					 String[] splited = input.split("\\s+");
					 if ( splited.length == 3 ){
						 String src = splited[0];
						 String dst = splited[1];
						 String dep_date = splited[2];
						 
						 if (Queries.getDate(dep_date) != null){
							 HashMap<String, String> flightList = sqlManager.searchFlights(src, dst, dep_date);
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
	
	public void displayFlights(HashMap<String, String> flightList){
		
		boolean loop = true;
		while(loop) {
	
			io.printf("Found Flights %n");
			io.printf("#################### %n");
			
			for(Entry<String, String> entry: flightList.entrySet()){
				io.printf(entry.getValue() + "%n");
			}
			
			io.printf("%n%n Enter Search \"(source) (destination) (DD-Mon-YYYY)\" %n"
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
					 break;
			}
		}
	}
	
	public void makeBooking(){
		io.printf("Book A Flight %n");
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
		io.printf("Record a Departure %n");
		boolean loop = true;
		while(loop){
			io.printf("Record a Departure %n");
			io.printf("###############################%n");
			
			io.printf("Type \"(FlightNum) (Scheduled Departure Time) (Actual Departure Time)\" to Record departure %n"
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
					 io.printf("Invalid Input");
					 break;
			}
		}
	}
	
	public void recordAnArrival(){
		io.printf("Record an Arrival %n");
	}

	
}
