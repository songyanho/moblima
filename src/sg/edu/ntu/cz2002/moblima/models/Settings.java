package sg.edu.ntu.cz2002.moblima.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import sg.edu.ntu.cz2002.moblima.models.Cinema.CinemaClass;
import sg.edu.ntu.cz2002.moblima.models.Movie.MovieType;
import sg.edu.ntu.cz2002.moblima.models.Seat.SeatType;
import sg.edu.ntu.cz2002.moblima.models.Showtime.Day;
import sg.edu.ntu.cz2002.moblima.models.Ticket.AgeGroup;

public class Settings implements StandardData {
	
	protected ArrayList<String> holidays;
	protected HashMap<Integer,Double> ageGroup;
	protected HashMap<Integer, Double> cinemaClass;
	protected HashMap<Integer, Double> movieType;
	protected HashMap<Integer, Double> day;
	protected HashMap<Integer, Double> seatType;
	protected double basePrice;

	public Settings() {}
	
	public Settings(ArrayList<String> holidays, double basePrice, HashMap<Integer, Double> ageGroup,
			HashMap<Integer, Double> cinemaClass, HashMap<Integer, Double> movieType, HashMap<Integer, Double> day,
			HashMap<Integer, Double> seatType) {
		this.holidays = holidays;
		this.basePrice = basePrice;
		this.ageGroup = ageGroup;
		this.cinemaClass = cinemaClass;
		this.movieType = movieType;
		this.day = day;
		this.seatType = seatType;
	}
	
	public ArrayList<String> getHolidays() {
		return holidays;
	}

	public void setHolidays(ArrayList<String> holidays) {
		this.holidays = holidays;
	}
	
	public HashMap<Integer, Double> getAgeGroupCharges() {
		return ageGroup;
	}
	
	public void setAgeGroupCharges(int index, Double value) {
		AgeGroup a = Ticket.getAgeGroupEnumFromChoice(index);
		this.ageGroup.replace(a.ordinal(), value);
	}
	
	public HashMap<Integer, Double> getMovieTypeCharges() {
		return movieType;
	}
	
	public void setMovieTypeCharges(int index, Double value) {
		MovieType m = Movie.getTypeEnumFromChoice(index);
		this.movieType.replace(m.ordinal(), value);
	}
	
	public HashMap<Integer, Double> getCinemaClassCharges() {
		return cinemaClass;
	}
	
	public void setCinemaClassCharges(int index, Double value) {
		CinemaClass c = Cinema.getCinemaClassEnumFromChoice(index);
		this.cinemaClass.replace(c.ordinal(), value);
	}
	
	public HashMap<Integer, Double> getDayCharges() {
		return day;
	}
	
	public void setDayCharges(int index, Double value) {
		Day d = Showtime.getDayEnumFromChoice(index);
		this.day.replace(d.ordinal(), value);
	}
	
	public double getBasePrice() {
		return basePrice;
	}

	public void setBasePrice(double basePrice) {
		this.basePrice = basePrice;
	}

	public HashMap<Integer, Double> getSeatTypeCharges() {
		return seatType;
	}
	
	public void setSeatTypeCharges(int index, Double value) {
		SeatType s = Seat.getSeatTypeEnumFromChoice(index);
		this.seatType.replace(s.ordinal(), value);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public JSONObject toJSONObject() {
		JSONObject o = new JSONObject();
		JSONArray holidaysArray = new JSONArray();
		holidaysArray.addAll(holidays);
		o.put("holidays", holidaysArray);
		o.put("basePrice", this.basePrice);
		o.put("AgeGroup", this.ageGroup);
		o.put("CinemaClass", this.cinemaClass);
		o.put("MovieType", this.movieType);
		o.put("Day", this.day);
		o.put("SeatType", this.seatType);
		return o;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Settings fromJSONObject(JSONObject o) {
		ArrayList<String> holidays = new ArrayList<String>();
		JSONArray h = (JSONArray) o.get("holidays");
		for(int i=0; i<h.size(); i++)
			holidays.add(h.get(i).toString());
		Object obj = o;
		JSONObject jsonObj = (JSONObject) obj;
		JSONObject ageGroup = (JSONObject) jsonObj.get("AgeGroup");
		HashMap<Integer, Double> ag = new HashMap<Integer, Double>();
		Map<Integer, Double> map = ageGroup;
		Set set = map.entrySet();
		Iterator it = set.iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry)it.next();
		    ag.put(Integer.parseInt(entry.getKey().toString()), Double.parseDouble(entry.getValue().toString()));
		}
		JSONObject movieType = (JSONObject) jsonObj.get("MovieType");
		HashMap<Integer, Double> mt = new HashMap<Integer, Double>();
		map = movieType;
		set = map.entrySet();
		it = set.iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry)it.next();
		    mt.put(Integer.parseInt(entry.getKey().toString()), Double.parseDouble(entry.getValue().toString()));
		}
		JSONObject cinemaClass = (JSONObject) jsonObj.get("CinemaClass");
		HashMap<Integer, Double> cc = new HashMap<Integer, Double>();
		map = cinemaClass;
		set = map.entrySet();
		it = set.iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry)it.next();
		    cc.put(Integer.parseInt(entry.getKey().toString()), Double.parseDouble(entry.getValue().toString()));
		}
		JSONObject dayType = (JSONObject) jsonObj.get("Day");
		HashMap<Integer, Double> dt = new HashMap<Integer, Double>();
		map = dayType;
		set = map.entrySet();
		it = set.iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry)it.next();
		    dt.put(Integer.parseInt(entry.getKey().toString()), Double.parseDouble(entry.getValue().toString()));
		}
		JSONObject seatType = (JSONObject) jsonObj.get("SeatType");
		HashMap<Integer, Double> st = new HashMap<Integer, Double>();
		map = seatType;
		set = map.entrySet();
		it = set.iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry)it.next();
		    st.put(Integer.parseInt(entry.getKey().toString()), Double.parseDouble(entry.getValue().toString()));
		}
		/*
		HashMap<Integer, Double> a = (HashMap<Integer, Double>) o.get("AgeGroup");
		HashMap<Integer, Double> m = (HashMap<Integer, Double>) o.get("MovieType");
		HashMap<Integer, Double> c = (HashMap<Integer, Double>) o.get("CinemaClass");
		HashMap<Integer, Double> d = (HashMap<Integer, Double>) o.get("Day");
		*/
		return new Settings(holidays,
				Double.parseDouble(o.get("basePrice").toString()),
				ag, cc, mt, dt, st);
	}
	
	
	public static HashMap<String, JSONObject> toJSONObjects(HashMap<Integer, Settings> o){
		HashMap<String, JSONObject> a = new HashMap<String, JSONObject>();
		Set<Integer> s = o.keySet();
		for(Integer i: s){
			a.put(""+i, o.get(i).toJSONObject());
		}
		return a;
	}
	
	
	public static Settings fromJSONObjects(JSONObject o){
		return Settings.fromJSONObject(o);
	}

}
