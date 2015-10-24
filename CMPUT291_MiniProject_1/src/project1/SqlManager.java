package project1;

public class SqlManager {
	JdbcSQL SqlDB;
	
	public SqlManager(){
		SqlDB = new JdbcSQL();
	}
	
	public void closeConnection(){
		SqlDB.closeConnection();
	}
}
