package Project1;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class Users {
	
	private String FILENAME = "userinfo.txt";
	
	private HashMap<String, String> userinfo = new HashMap<String, String>();
	
	public Users(){

		loadFromFile();
		
	}
	
	public void setUserInfo(HashMap<String,String> hashmap){
		this.userinfo = hashmap;
	}
	
	public HashMap<String,String> getUserInfo(){
		return this.userinfo;
	}
	
	public Boolean validLogin(String username, String password){
		
		if(!getUserInfo().containsKey(username)){
			return Boolean.FALSE;
		}
		else if(getUserInfo().get(username).equals(password)){
			return Boolean.TRUE;
		}
		else{return Boolean.FALSE;}
		
	}
	


private void loadFromFile() {
    try {
        Reader reader = new InputStreamReader(
        		new FileInputStream(FILENAME));
        Gson gson = new GsonBuilder().create();
        //Following line based on gson
        Type Dicttype = new TypeToken<HashMap<String,String>>() {}.getType();
        HashMap<String,String> dictionary  = gson.fromJson(reader, Dicttype);
        setUserInfo(dictionary);

    } catch (FileNotFoundException e) {
        // TODO Auto-generated catch block
        setUserInfo(new HashMap<String,String>());
    } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
        throw new RuntimeException();
    }

} 

public void saveToFile(){
    try {
        Writer writer = new OutputStreamWriter(
        		new FileOutputStream(FILENAME));
    
        Gson gson = new GsonBuilder().create();
        gson.toJson(getUserInfo(), writer);
        writer.flush();
        writer.close();
    } catch (FileNotFoundException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
        throw new RuntimeException(e);
    } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
        throw new RuntimeException(e);
    }
}

}