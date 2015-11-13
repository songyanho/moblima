package sg.edu.ntu.cz2002.moblima.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import org.json.simple.JSONObject;

import sg.edu.ntu.cz2002.moblima.database.Database;
import sg.edu.ntu.cz2002.moblima.models.Settings;

public class SettingsDao {

	protected static final String DATABASE_NAME = "settings";
	
	protected static Settings settings;
	
	public static Settings getSettings() {
		// TODO Auto-generated method stub
		if(settings == null) initialize();
		return settings;
	}
	
	public static ArrayList<String> getHolidaysInString(){
		if(settings == null) initialize();
		ArrayList<String> holidays = new ArrayList<String>();
		SimpleDateFormat formatter = new SimpleDateFormat("d/M/yyyy");
		for(Calendar c: settings.getHolidays()){
			holidays.add(formatter.format(c.getTime()));
		}
		return holidays;
	}
	
	public static ArrayList<Calendar> getHolidays(){
		if(settings == null) initialize();
		return settings.getHolidays();
	}

	public static boolean save() {
		// TODO Auto-generated method stub
		if(settings == null) initialize();
		return Database.save(DATABASE_NAME, settings.toJSONObject());
	}
	
	public static void initialize() {
		// TODO Auto-generated method stub
		JSONObject t = Database.getObject(DATABASE_NAME);
		settings = Settings.fromJSONObjects(t);
	}
}
