package Project1;
import java.sql.*;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		
		System.out.println("Welcome to UA Travel.  What would you like to do? (Register/Login/Exit)");
		Menu();
		
		
	}
	
	public static void Menu(){
		
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
	
	public static String register(){
		
		//Initialize user names and passwords
		Users user = new Users();
		
		System.out.println("Please create a username:");
		String username = getInput();
		System.out.println("Please Create a password:");
		String password = getInput();
		
		user.getUserInfo().put(username , password);
		
		return "You have been succesfully registered";
		
	}
	
	public static Boolean login(){
		
		Users user = new Users();
		
		System.out.println("Please enter your username:");
		String username = getInput();
		System.out.println("Please enter your password:");
		String password = getInput();
		
		return user.validLogin(username, password);
		
	}
	
	public static void loggedIn(){}
	
	
	public static String getInput(){
		
		Scanner scanner = new Scanner(System.in);
		String input = scanner.next();
		scanner.close();
			
		return input;
		
	}

}
