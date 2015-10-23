package sg.edu.ntu.cz2002.moblima.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Settings implements StandardData {
	
	protected ArrayList<String> holidays;
	
	public Settings(ArrayList<String> holidays){
		this.holidays = holidays;
	}

	public ArrayList<String> getHolidays() {
		return holidays;
	}

	public void setHolidays(ArrayList<String> holidays) {
		this.holidays = holidays;
	}
	
	@Override
	public JSONObject toJSONObject() {
		JSONObject o = new JSONObject();
		JSONArray holidaysArray = new JSONArray();
		holidaysArray.addAll(holidays);
		o.put("holidays", holidaysArray);
		return o;
	}

	public Settings fromJSONObject(JSONObject o) {
		ArrayList<String> holidays = new ArrayList<String>();
		JSONArray h = (JSONArray) o.get("holidays");
		for(int i=0; i<h.size(); i++)
			holidays.add(h.get(i).toString());
		return new Settings(holidays);
	}
	
	public static Settings fromJSONObjects(JSONObject o){
		return Settings.fromJSONObjects(o);
	}
}
