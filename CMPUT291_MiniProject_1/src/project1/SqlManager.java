package project1;

import java.sql.ResultSet;

public class SqlManager {
	JdbcSQL sqlDB;
	
	public SqlManager(){
		sqlDB = new JdbcSQL();
		
	}
	
	public void closeConnection(){
		sqlDB.closeConnection();
	}
	
	public void setup(){
		this.dropTables();
		this.createTables();
		
		
	}
	
	public boolean checkForAgentWithEmail(String email){
		ResultSet rs = sqlDB.executeQuery(Queries.findAgent(email));
		if(!rs.isBeforeFirst()){
			return false;
		}
		return true;
	}
	
	public String getUserPass(String email){
		ResultSet rs = sqlDB.executeQuery(Queries.findUser(email));
		String password = rs.getString("pass");
		return password;
	}
	
	
	public void logoutUser(String email){
		sqlDB.sendCommand(Queries.updateLastLogin());
	}
	
	
	public void dropTables(){
		String[] tables = Queries.dropTables();
		for(String i: tables){
			sqlDB.sendCommand(i);
		}
	}
	
	public void createTables(){
		String[] tables  = Queries.createTables();
		for(String i: tables){
			sqlDB.sendCommand(i);
		}

	}
	
	public boolean checkForUserWithEmail(String email){
		ResultSet rs = sqlDB.executeQuery(Queries.findUser(email));
		if(!rs.isBeforeFirst()){
			return false;
		}
		return true; 
	}
	
	public void addUser(String email, String password){
		sqlDB.sendCommand(Queries.insertUser(email, password));
	}
}
