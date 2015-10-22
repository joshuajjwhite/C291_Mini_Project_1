package project1;
import textDevicePackage.TextDevices;

public class c291MiniProject {

	public static void main(String[] args) {
		UserConsoleInterface userConsole = new UserConsoleInterface(TextDevices.defaultTextDevice());
		userConsole.greet();

	}


}
