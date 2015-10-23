package project1;

import textDevicePackage.TextDevice;

public class UserConsoleInterface {
	private final TextDevice io;
	private SqlManager sqlManager;

	public UserConsoleInterface(TextDevice io, SqlManager sqlManager){
	    this.io = io;
	    this.sqlManager = sqlManager;
		
//		SqlDB.sendExampleCommands();

	}
	
	public void greet(){
		selectMenuLoginOption();
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
		        	 loop = true;
		        	 break;
			 }
			} while(loop);
	}
	
	public void register(){
		
		io.printf("Please create a username: %n");
		String username = getInput();
		io.printf("Please create a password: %n");
		String password = getInput();
		
		String userType = "guest";
		
		io.printf("Thanks %s, You have been succesfully registered as %s %n", username, userType);
		
		
	}
	
	public void login(){
		io.printf("To Login: %n");
		io.printf("Please enter your username:");
		String username = getInput();
		io.printf("Please enter your password:");
		String password = getInput();
		
		String userType = "admin";
	
		io.printf("You have successfully logged in as %s with status %s %n", username, userType);
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
