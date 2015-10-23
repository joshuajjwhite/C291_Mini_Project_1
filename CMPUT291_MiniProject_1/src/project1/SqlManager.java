package project1;

public class SqlManager {
	JdbcSQL SqlDB;
	
	public SqlManager(){
		JdbcSQL SqlDB = new JdbcSQL();
	}
	
	public void closeConnection(){
		SqlDB.closeConnection();
	}
}
