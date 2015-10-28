package sg.edu.ntu.cz2002.moblima.models;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import sg.edu.ntu.cz2002.moblima.dao.CinemaDao;

public class Cinema {
	protected int id;
	protected String name;
	protected String cinemaClass;
	protected int cineplexId;
	protected ArrayList<Seat> seat;
	protected int seatNum;
	protected int numEmptySeat;

	public Cinema() {
		this.id = CinemaDao.getLastId() + 1;
	}
	
	public Cinema(int id, String name, String cinemaClass, int cineplexId) {
		this.id = id;
		this.name = name;
		this.cinemaClass = cinemaClass;
		this.cineplexId = cineplexId;
		this.seat = seat;
		this.seatNum = seatNum;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCineplexId() {
		return cineplexId;
	}

	public void setCineplexId(int cineplexId) {
		this.cineplexId = cineplexId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public ArrayList<Seat> getSeat() {
		return seat;
	}

	public void setSeat(ArrayList<Seat> seat) {
		this.seat = seat;
	}

	public void setCinemaClass(String cinemaClass) {
		this.cinemaClass = cinemaClass;
	}
	
	public int getSeatNum() {
		return seatNum;
	}

	public void setSeatNum(int seatNum) {
		this.seatNum = seatNum;
	}

	public int getNumEmptySeat() {
		return numEmptySeat;
	}

	public void setNumEmptySeat(int numEmptySeat) {
		this.numEmptySeat = numEmptySeat;
	}

	public String getCinemaClass() {
		return cinemaClass;
	}

	public void assign(int seatId, int ticketId) {
		if (seat.get(seatId).ticket.getTicketId() == -1) {
			seat.get(seatId).assign(ticketId);
			this.setNumEmptySeat(this.getNumEmptySeat() - 1);
		}
		else
			System.out.println("Seat already assigned to a customer.");
	}
	
	public JSONObject toJSONObject() {
		JSONObject o = new JSONObject();
		o.put("id", this.id);
		o.put("name", this.name);
		o.put("cinemaClass", this.cinemaClass);
		o.put("cineplexid", this.cineplexId);
//		JSONArray a = new JSONArray();
//		a.addAll(this.seat);
//		o.put("seat", a);
		o.put("numEmptySeat", this.numEmptySeat);
		return o;
	}
	
	public static Cinema fromJSONObject(JSONObject o){
		ArrayList<Seat> seat = new ArrayList<Seat>();
//		JSONArray seatInJSON = (JSONArray) o.get("seat");
//		seat.addAll(seatInJSON);
		return new Cinema(
				Integer.parseInt(o.get("id").toString()), 
				o.get("name").toString(), 
				o.get("cinemaClass").toString(), 
				Integer.parseInt(o.get("cineplexid").toString())//, 
//				Integer.parseInt(o.get("seatNum").toString())
				);
	}
	
	public static HashMap<String, JSONObject> toJSONObjects(HashMap<Integer, Cinema> o){
		HashMap<String, JSONObject> a = new HashMap<String, JSONObject>();
		Set<Integer> s = o.keySet();
		for(Integer i: s){
			a.put(""+i, o.get(i).toJSONObject());
		}
		return a;
	}
	
	public static HashMap<Integer, Cinema> fromJSONObjects(JSONObject o){
		HashMap<Integer, Cinema> a = new HashMap<Integer, Cinema>();
		Set<String> s = o.keySet();
		for(String i: s){
			JSONObject n = (JSONObject) o.get(i);
			Cinema t =  Cinema.fromJSONObject(n);
			a.put(t.getId(), t);
		}
		return a;
	}
}
