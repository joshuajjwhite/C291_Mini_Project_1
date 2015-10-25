package project1;

import textDevicePackage.TextDevice;

public class UserConsoleInterface {
	private final TextDevice io;
	private SqlManager sqlManager;
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
			io.printf("Welcome to UA Travel.  What would you like to do? (Register/Login/Exit) %n");
			String input = getInput().trim().toLowerCase();
			
			 switch (input) {
		         case "register":
		        	 register();
		        	 loop = true;
		        	 break;
		         case "login":
		        	 login();
		             break;
		         case "exit":
		        	 exitProgram();
		         default:
		        	 io.printf("Invalid Input %n %n");
		        	 loop = true;
		        	 break;
			 }
			} while(loop);
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
	
	public void login(){
		io.printf("To Login: %n");
		io.printf("Please enter your username:");
		String username = getInput();
		io.printf("Please enter your password:");
		String password = getInput();
		
		String userType = "agent";
		setCurrentUserType("agent");
	
		io.printf("You have successfully logged in as %s with status %s %n", username, userType);
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
		io.printf("Logout %n");
		exitProgram();
	}
	
	public void recordADeparture(){
		io.printf("Record a Departure %n");
	}
	
	public void recordAnArrival(){
		io.printf("Record an Arrival %n");
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
	
}
