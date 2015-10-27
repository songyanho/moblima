package sg.edu.ntu.cz2002.moblima.models;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import sg.edu.ntu.cz2002.moblima.dao.CinemaDao;

public class Cinema {
	protected String name;
	protected String cinemaClass;
	protected int id;
	protected ArrayList<Seat> seat;
	private int seatNum;
	private int numEmptySeat;
	
	public Cinema() {
		this.id = CinemaDao.getLastId() + 1;
	}
	
	public Cinema(int id, String name, String cinemaClass, ArrayList<Seat> seat, int seatNum) {
		this.id = id;
		this.name = name;
		this.cinemaClass = cinemaClass;
		this.seat = seat;
		this.seatNum = seatNum;
	}
	
	public String getCinemaName() {
		return name;
	}
	
	public int getCinemaId() {
		return id;
	}
	
	public String getCinemaClass() {
		return cinemaClass;
	}
	
	public void setCinemaName(String cinema_Name) {
		this.name = cinema_Name;
	}
	
	public void setCinemaId(int cinema_Id) {
		this.id = cinema_Id;
	}
	
	public void setClass(String cinema_Class) {
		this.cinemaClass = cinema_Class;
	}
	
	public void assign(int seatId, int ticketId) {
		if (seat.get(seatId).ticket.getTicketId() == -1) {
			seat.get(seatId).assign(ticketId);
			this.setNumEmptySeat(this.getNumEmptySeat() - 1);
		}
		else
			System.out.println("Seat already assigned to a customer.");
	}
	
	public int getNumEmptySeat() {
		return numEmptySeat;
	}

	public void setNumEmptySeat(int numEmptySeat) {
		this.numEmptySeat = numEmptySeat;
	}

	public int getSeatNum() {
		return seatNum;
	}

	public void setSeatNum(int seatNum) {
		this.seatNum = seatNum;
	}
	
	public JSONObject toJSONObject() {
		JSONObject o = new JSONObject();
		o.put("id", this.id);
		o.put("name", this.name);
		JSONArray a = new JSONArray();
		a.addAll(this.seat);
		o.put("seat", a);
		o.put("cinemaClass", this.cinemaClass);
		o.put("numEmptySeat", this.numEmptySeat);
		return o;
	}
	
	public static Cinema fromJSONObject(JSONObject o){
		ArrayList<Seat> seat = new ArrayList<Seat>();
		JSONArray seatInJSON = (JSONArray) o.get("seat");
		seat.addAll(seatInJSON);
		return new Cinema(Integer.parseInt(o.get("id").toString()), o.get("name").toString(), o.get("cinemaClass").toString(), seat, Integer.parseInt(o.get("seatNum").toString()));
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
			a.put(t.getCinemaId(), t);
		}
		return a;
	}
}
