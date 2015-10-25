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
		Queries.dropTables();
		Queries.createTables();
		
//		Queries.insertDefaultData();
	}
	
	public boolean checkForUserWithEmail(String email){
		ResultSet rs = sqlDB.executeQuery(Queries.findUserByEmail(email));
		try{
			if(!rs.next()){
				return false;
			}
		} catch (Exception e){
			System.out.println(e);
		}
		return true; 
	}
	
	public void addUser(String email, String password){
		
		sqlDB.sendCommand(Queries.insertUser(email, password));
	}
}
