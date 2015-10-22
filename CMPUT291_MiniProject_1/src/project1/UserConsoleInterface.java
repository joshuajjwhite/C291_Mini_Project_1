package project1;

import java.util.Scanner;

import textDevicePackage.TextDevice;

public class UserConsoleInterface {
	private final TextDevice io;
	private JdbcSQL SqlDB;

	public UserConsoleInterface(TextDevice io, JdbcSQL SqlDB){
	    this.io = io;
	    this.SqlDB = SqlDB;
		
//		SqlDB.sendExampleCommands();

	}
	
	public void greet(){
		io.printf("Welcome to UA Travel.  What would you like to do? (Register/Login/Exit) %n");
		String input = getInput().trim().toLowerCase();
		selectMenuLoginOption(input);
	}

	public void selectMenuLoginOption(String input){
		
		 switch (input) {
	         case "register":
	        	 register();
	        	 break;
	         case "login":
	        	 login();
	             break;
	         case "exit":
	        	 System.exit(0);
	         default:
	        	 break;
		 }
	}
	
	public String register(){
		
		//Initialize user names and passwords
		Users user = new Users();
		
		System.out.println("Please create a username:");
		String username = getInput();
		System.out.println("Please Create a password:");
		String password = getInput();
		
		user.getUserInfo().put(username , password);
		
		return "You have been succesfully registered";
		
	}
	
	public Boolean login(){
		
		Users user = new Users();
		
		io.printf("Please enter your username:");
		String username = getInput();
		io.printf("Please enter your password:");
		String password = getInput();
		
		return user.validLogin(username, password);
		
	}
	
	public void loggedIn(){}
	
	
	public String getInput(){
		String input = io.readLine().trim().toLowerCase();;
		return input;
		
	}
	
}
