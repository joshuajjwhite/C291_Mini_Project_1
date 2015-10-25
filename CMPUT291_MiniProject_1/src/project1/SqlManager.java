package project1;

import java.sql.ResultSet;

import textDevicePackage.TextDevice;

public class SqlManager {
	JdbcSQL sqlDB;
	TextDevice io;
	
	public SqlManager(TextDevice io){
		sqlDB = new JdbcSQL();
		this.io = io;
	}
	
	public ResultSet queryWrapper(String[] stmts){
		ResultSet rs = null;
		for(String i: stmts){
			sqlDB.sendCommand(i);
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
		try {
			if(!rs.isBeforeFirst()){
				return false;
			}
		} catch (Exception e){
			io.printf("checkForAgentWithEmail failed %s", e);
		}
		return true;
	}
	
	public String getUserPass(String email){
		ResultSet rs = sqlDB.executeQuery(Queries.findUser(email));
		String password = "";
		try {
			rs.getString("pass");
		} catch (Exception e){
			io.printf("getUserPass failed %s", e);
		}
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
		try{
			if(!rs.isBeforeFirst()){
				return false;
			}
		} catch (Exception e){
			io.printf("checkForUserWithEmail failed %s", e);
		}
		return true; 
	}
	
	public void addUser(String email, String password){
		sqlDB.sendCommand(Queries.insertUser(email, password));
	}
}
