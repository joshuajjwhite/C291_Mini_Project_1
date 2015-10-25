package project1;

import java.sql.ResultSet;

public class SqlManager {
	JdbcSQL sqlDB;
	
	public SqlManager(){
		sqlDB = new JdbcSQL();
		
	}
	
	public ResultSet queryWrapper(String[] stmts){
		ResultSet rs = null;
		for(String i: stmts){
			rs = sqlDB.executeQuery(i);
		}
		return rs;
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
		sqlDB.sendCommand(Queries.updateLastLogin(email));
	}
	
	
	public void dropTables(){
		queryWrapper(Queries.dropTables());
	}
	
	public void createTables(){
		queryWrapper(Queries.createTables());

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
