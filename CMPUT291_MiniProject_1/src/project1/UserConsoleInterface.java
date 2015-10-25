package project1;

import java.sql.ResultSet;

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
	
	public void greet(){
		sqlManager.setup();
		
		selectMenuLoginOption();
		mainCLI();
	}

	public void selectMenuLoginOption(){
		boolean loop;
		do {
			loop = false;
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
			io.printf("1. Search for flight %n"
			 		 + "2. Make a booking %n"
					 + "3. List existing bookings %n"
			 		 + "4. Cancle a booking %n"
					 + "5. Logout %n");
			if (getCurrentUserType().equals("agent")){
				 io.printf("6. Record flight departure %n"
						 + "7. Record flight arrival %n");
			}
			 
			String input = getInput().trim().toLowerCase();
			switch (input){
				case "1":
					searchForFlight();
					break;
				case "2":
					 makeBooking();
					 break;
				 case "3":
					 listBookings();
					 break;
				 case "4":
					 cancleBooking();
					 break;
				 case "5":
					 logout();
					 break;
				 case "6":
					 if(getCurrentUserType().equals("agent")){
						 recordADeparture();
					 }
					 break;
				 case "7":
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
	
	public void searchForFlight(){
		io.printf("Search For A Flight %n");
	}
	
	public void makeBooking(){
		io.printf("Book A Flight %n");
	}
	
	public void listBookings(){
		io.printf("List Bookings %n");
	}
	
	public void cancleBooking(){
		io.printf("Cancle Booking %n");
	}
	
	public void logout(){
		sqlManager.logoutUser(this.getCurrentUserEmail());
		io.printf("Logout %n");
		exitProgram();
	}
	
	public void recordADeparture(){
		io.printf("Record a Departure %n");
	}
	
	public void recordAnArrival(){
		io.printf("Record an Arrival %n");
	}

	
}
