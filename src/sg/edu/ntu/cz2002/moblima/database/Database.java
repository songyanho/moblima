package sg.edu.ntu.cz2002.moblima.database;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import sg.edu.ntu.cz2002.moblima.models.Admin;

public class Database {
	public static JSONObject getObject(String databaseName){
		JSONParser parser = new JSONParser();
		File f = new File("data/"+databaseName+".json");
		
		try {
			Object obj = parser.parse(new FileReader(f.getAbsolutePath()));
			JSONObject database = (JSONObject) obj;
			return database;
		} catch (IOException | ParseException e) {
			// TODO Auto-generated catch block
			System.err.println("Database file: "+databaseName+" was not found.");
			e.printStackTrace();
		}
		System.out.println("Commit Test 1");
		return null;
	}
	
	public static JSONArray getArray(String databaseName){
		JSONParser parser = new JSONParser();
		File f = new File("data/"+databaseName+".json");
		
		try {
			Object obj = parser.parse(new FileReader(f.getAbsolutePath()));
			JSONArray database = (JSONArray) obj;
			return database;
		} catch (IOException | ParseException e) {
			// TODO Auto-generated catch block
			System.err.println("Database file: "+databaseName+" was not found.");
			e.printStackTrace();
		}
		System.out.println("Commit Test 1");
		return null;
	}
	
	public static boolean save(String databaseName, HashMap m){
		JSONObject j = new JSONObject();
		j.putAll(m);
		File f = new File("data/"+databaseName+".json");
		FileWriter file;
		try {
			file = new FileWriter(f.getAbsolutePath());
			file.write(j.toJSONString());
            file.flush();
            file.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		System.out.println("Commit Test 1");
		return true;
	}
}
