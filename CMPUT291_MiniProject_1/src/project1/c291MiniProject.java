package project1;
import textDevicePackage.TextDevices;

public class c291MiniProject {

	public static void main(String[] args) {
		SqlManager sqlManager = new SqlManager(TextDevices.defaultTextDevice());
		
		UserConsoleInterface userConsole = new UserConsoleInterface(TextDevices.defaultTextDevice(), sqlManager);
		userConsole.greet();
		
//		SqlDB.closeConnection();

	}


}
