package project1;
import textDevicePackage.TextDevices;

public class c291MiniProject {

	public static void main(String[] args) {
		
		JdbcSQL SqlDB = new JdbcSQL();
		
		UserConsoleInterface userConsole = new UserConsoleInterface(TextDevices.defaultTextDevice(), SqlDB);
		userConsole.greet();
		
		SqlDB.closeConnection();
		

	}


}
