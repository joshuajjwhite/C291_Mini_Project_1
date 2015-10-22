package Project1;

import java.util.Scanner;

public class UserConsoleInterface {
	private final TextDevice io;

	public UserConsoleInterface(TextDevice io){
	    this.io = io;
	}
	
	public void greet(){
		io.printf("Welcome to UA Travel.  What would you like to do? (Register/Login/Exit) %n");
		menu();
	}

	public void menu(){
		
		String input = getInput().trim().toLowerCase();
		
			if(input.equals("register")){
				System.out.println(register());
			}
			else if (input.equals("login")){
				Boolean successlog = login();
				if(successlog) {loggedIn();}
				else{System.out.println("Invalid Username or Login");}
			}	
			else if(input.equals("exit")){
				System.out.println("exit");
			}
			else{ 
			}
			
	}
	
	public  String register(){
		
		//Initialize user names and passwords
		Users user = new Users();
		
		System.out.println("Please create a username:");
		String username = getInput();
		System.out.println("Please Create a password:");
		String password = getInput();
		
		user.getUserInfo().put(username , password);
		
		return "You have been succesfully registered";
		
	}
	
	public  Boolean login(){
		
		Users user = new Users();
		
		io.printf("Please enter your username:");
		String username = getInput();
		io.printf("Please enter your password:");
		String password = getInput();
		
		return user.validLogin(username, password);
		
	}
	
	public  void loggedIn(){}
	
	
	public  String getInput(){
		
		Scanner scanner = new Scanner(System.in);
		String input = scanner.next();
		scanner.close();
			
		return input;
		
	}
	
}
