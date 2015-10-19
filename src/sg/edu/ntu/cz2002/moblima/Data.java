package sg.edu.ntu.cz2002.moblima;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Data {
	
	protected int panel;
	protected int adminId;
	
	protected HashMap<Integer, Admin> admins;
	protected ArrayList<String> holidays;
	
	public Data() throws FileNotFoundException, IOException, ParseException{
		JSONParser parser = new JSONParser();
		File f = new File("data/data.json");
		Object obj = parser.parse(new FileReader(f.getAbsolutePath()));
        JSONObject database = (JSONObject) obj;

        admins = new HashMap<Integer, Admin>();
        JSONArray jAdmins = (JSONArray)database.get("admins");
        for(int i=0; i<jAdmins.size(); i++){
        	JSONObject t = (JSONObject)jAdmins.get(i);
        	admins.put(new Integer(Integer.parseInt(t.get("id").toString())), new Admin(Integer.parseInt(t.get("id").toString()), t.get("username").toString(), t.get("password").toString()));
        }
        
        holidays = new ArrayList<String>();
        JSONArray jHolidays = (JSONArray)database.get("holidays");
        for(int i=0; i<jHolidays.size(); i++){
        	String h = (String) jHolidays.get(i);
        	holidays.add(h);
        }
	}
	
	public boolean saveData(){
		
		return true;
	}
	
	public Admin getAdmin(){
		return admins.get(adminId);
	}

	public int getPanel() {
		return panel;
	}

	public void setPanel(int panel) {
		this.panel = panel;
	}

	public int getAdminId() {
		return adminId;
	}

	public void setAdminId(int adminId) {
		this.adminId = adminId;
	}

	public HashMap<Integer, Admin> getAdmins() {
		return admins;
	}

	public void setAdmins(HashMap<Integer, Admin> admins) {
		this.admins = admins;
	}

	public ArrayList<String> getHolidays() {
		return holidays;
	}

	public void setHolidays(ArrayList<String> holidays) {
		this.holidays = holidays;
	}
	
	
	
	
}
