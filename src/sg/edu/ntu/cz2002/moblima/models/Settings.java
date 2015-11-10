package sg.edu.ntu.cz2002.moblima.models;

import java.util.ArrayList;
import java.util.HashMap;
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
	protected HashMap<AgeGroup,Double> ageGroup;
	protected HashMap<CinemaClass, Double> cinemaClass;
	protected HashMap<MovieType, Double> movieType;
	protected HashMap<Day, Double> day;
	protected HashMap<SeatType, Double> seatType;
	protected double basePrice;

	public Settings() {}
	
	public Settings(ArrayList<String> holidays, double basePrice, HashMap<AgeGroup, Double> ageGroup,
			HashMap<CinemaClass, Double> cinemaClass, HashMap<MovieType, Double> movieType, HashMap<Day, Double> day,
			HashMap<SeatType, Double> seatType) {
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
	
	public HashMap<AgeGroup, Double> getAgeGroupCharges() {
		return ageGroup;
	}
	
	public void setAgeGroupCharges(int index, Double value) {
		AgeGroup a = Ticket.getAgeGroupEnumFromChoice(index);
		this.ageGroup.replace(a, value);
	}
	
	public HashMap<MovieType, Double> getMovieTypeCharges() {
		return movieType;
	}
	
	public void setMovieTypeCharges(int index, Double value) {
		MovieType m = Movie.getTypeEnumFromChoice(index);
		this.movieType.replace(m, value);
	}
	
	public HashMap<CinemaClass, Double> getCinemaClassCharges() {
		return cinemaClass;
	}
	
	public void setCinemaClassCharges(int index, Double value) {
		CinemaClass c = Cinema.getCinemaClassEnumFromChoice(index);
		this.cinemaClass.replace(c, value);
	}
	
	public HashMap<Day, Double> getDayCharges() {
		return day;
	}
	
	public void setDayCharges(int index, Double value) {
		Day d = Showtime.getDayEnumFromChoice(index);
		this.day.replace(d, value);
	}
	
	public double getBasePrice() {
		return basePrice;
	}

	public void setBasePrice(double basePrice) {
		this.basePrice = basePrice;
	}

	public HashMap<SeatType, Double> getSeatTypeCharges() {
		return seatType;
	}
	
	public void setSeatTypeCharges(int index, Double value) {
		SeatType s = Seat.getSeatTypeEnumFromChoice(index);
		this.seatType.replace(s, value);
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

	public static Settings fromJSONObject(JSONObject o) {
		// Holiday
		ArrayList<String> holidays = new ArrayList<String>();
		JSONArray h = (JSONArray) o.get("holidays");
		for(int i=0; i<h.size(); i++)
			holidays.add(h.get(i).toString());
		
		// AgeGroup
//		Object obj = o;
//		JSONObject jsonObj = (JSONObject) obj;
		JSONObject ageGroup = (JSONObject) o.get("AgeGroup");
		HashMap<AgeGroup, Double> ag = new HashMap<AgeGroup, Double>();
//		Map<Integer, Double> map = ageGroup;
//		Set set = map.entrySet();
//		Iterator it = set.iterator();
//		while (it.hasNext()) {
//			Map.Entry entry = (Map.Entry)it.next();
//		AgeGroup agKeyValue[] = AgeGroup.values();
		for(Object i: ageGroup.keySet()){
			int key = Integer.parseInt(i.toString());
		    ag.put(AgeGroup.values()[key], Double.parseDouble(ageGroup.get(i).toString()));
		}
//		}
		
		// Movie Type
		JSONObject movieType = (JSONObject) o.get("MovieType");
		HashMap<MovieType, Double> mt = new HashMap<MovieType, Double>();
//		map = movieType;
//		set = map.entrySet();
//		it = set.iterator();
//		while (it.hasNext()) {
//			Map.Entry entry = (Map.Entry)it.next();
		for(Object i: movieType.keySet()){
			int key = Integer.parseInt(i.toString());
			mt.put(MovieType.values()[key], Double.parseDouble(movieType.get(i).toString()));
		}
		    
//		}
		JSONObject cinemaClass = (JSONObject) o.get("CinemaClass");
		HashMap<CinemaClass, Double> cc = new HashMap<CinemaClass, Double>();
//		map = cinemaClass;
//		set = map.entrySet();
//		it = set.iterator();
//		while (it.hasNext()) {
//			Map.Entry entry = (Map.Entry)it.next();
		for(Object i: cinemaClass.keySet()){
			int key = Integer.parseInt(i.toString());
			cc.put(CinemaClass.values()[key], Double.parseDouble(cinemaClass.get(i).toString()));
		}
//		}
		JSONObject dayType = (JSONObject) o.get("Day");
		HashMap<Day, Double> dt = new HashMap<Day, Double>();
//		map = dayType;
//		set = map.entrySet();
//		it = set.iterator();
//		while (it.hasNext()) {
//			Map.Entry entry = (Map.Entry)it.next();
		for(Object i: dayType.keySet()){
			int key = Integer.parseInt(i.toString());
			dt.put(Day.values()[key], Double.parseDouble(dayType.get(i).toString()));
		}
//		    dt.put(Integer.parseInt(entry.getKey().toString()), Double.parseDouble(entry.getValue().toString()));
//		}
		JSONObject seatType = (JSONObject) o.get("SeatType");
		HashMap<SeatType, Double> st = new HashMap<SeatType, Double>();
//		map = seatType;
//		set = map.entrySet();
//		it = set.iterator();
//		while (it.hasNext()) {
//			Map.Entry entry = (Map.Entry)it.next();
		for(Object i: seatType.keySet()){
			int key = Integer.parseInt(i.toString());
			st.put(SeatType.values()[key], Double.parseDouble(seatType.get(i).toString()));
		//JSONObject seatType = (JSONObject) jsonObj.get("SeatType");
		//HashMap<Integer, Double> st = new HashMap<Integer, Double>();
		//map = seatType;
		//set = map.entrySet();
		//it = set.iterator();
		//while (it.hasNext()) {
		//	Map.Entry entry = (Map.Entry)it.next();
		//    st.put(Integer.parseInt(entry.getKey().toString()), Double.parseDouble(entry.getValue().toString()));
		}
//		    dt.put(Integer.parseInt(entry.getKey().toString()), Double.parseDouble(entry.getValue().toString()));
//		}
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
