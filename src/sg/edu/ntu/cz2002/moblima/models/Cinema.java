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
	protected CinemaClass cinemaClass;
	protected int cineplexId;
	protected ArrayList<String> seat;
	protected int seatNum;
	protected int numEmptySeat;

	public Cinema() {
		this.id = CinemaDao.getLastId() + 1;
	}
	
	public enum CinemaClass {
		PREMIUM, PLATINUM, GOLD, NORMAL;
	}
	
	public void setCinemaClassFromChoice(int choice) {
		switch (choice) {
		case 1: 
			this.cinemaClass = CinemaClass.PREMIUM;
			break;
		case 2:
			this.cinemaClass = CinemaClass.PLATINUM;
			break;
		case 3:
			this.cinemaClass = CinemaClass.GOLD;
			break;
		default:
			this.cinemaClass = CinemaClass.NORMAL;
		}
	}
	
	public static CinemaClass getCinemaClassEnumFromChoice(int choice) {
		return choice == 1? CinemaClass.PREMIUM:
			   choice == 2? CinemaClass.PLATINUM:
			   choice == 3? CinemaClass.GOLD:
				   			CinemaClass.NORMAL;
	}
	
	public static CinemaClass getCinemaClassEnumFromOrdinal(int ordinal) {
		return ordinal == CinemaClass.PREMIUM.ordinal() ? CinemaClass.PREMIUM:
			   ordinal == CinemaClass.PLATINUM.ordinal() ? CinemaClass.PLATINUM:
			   ordinal == CinemaClass.GOLD.ordinal() ? CinemaClass.GOLD:
				   CinemaClass.NORMAL;
	}
	
	public static String getCinemaClassStringFromChoice(int choice) {
		return choice == 1? "PREMIUM":
			   choice == 2? "PLATINUM":
			   choice == 3? "GOLD":
				   			"NORMAL";
	}
	
	public static void printCinemaClassChoice() {
		for (CinemaClass cc: CinemaClass.values())
			System.out.print("\t" + (cc.ordinal()+1) + ". " + cc.name());
	}
	
	public Cinema(int id, String name, int cinemaClass, int cineplexId, ArrayList<String> seat) {
		this.id = id;
		this.name = name;
		this.cinemaClass = Cinema.getCinemaClassEnumFromOrdinal(cinemaClass);
		this.cineplexId = cineplexId;
		this.seat = seat;
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

	public ArrayList<String> getSeat() {
		return seat;
	}

	public void setSeat(ArrayList<String> seat) {
		this.seat = seat;
	}
	
	@Deprecated
	public void setCinemaClass(CinemaClass cinemaClass) {
		this.cinemaClass = cinemaClass;
	}
	
	public int getSeatNum() {
		return seatNum;
	}

	public void setSeatNum(int seatNum) {
		this.seatNum = seatNum;
	}

	public int getNumEmptySeat() {
		return seatNum - seat.size();
	}

	public void setNumEmptySeat(int numEmptySeat) {
		this.numEmptySeat = numEmptySeat;
	}

	public CinemaClass getCinemaClass() {
		return cinemaClass;
	}
	
	/*
	public void assign(int seatId, int ticketId) {
		if (seat.get(seatId).ticket.getTicketId() == -1) {
			seat.get(seatId).assign(ticketId);
			this.setNumEmptySeat(this.getNumEmptySeat() - 1);
		}
		else
			System.out.println("Seat already assigned to a customer.");
	}
	*/
	
	@SuppressWarnings("unchecked")
	public JSONObject toJSONObject() {
		JSONObject o = new JSONObject();
		o.put("id", this.id);
		o.put("name", this.name);
		o.put("cinemaClass", this.cinemaClass.ordinal());
		o.put("cineplexid", this.cineplexId);
		JSONArray a = new JSONArray();
		a.addAll(this.seat);
		o.put("seat", a);
		o.put("numEmptySeat", this.numEmptySeat);
		return o;
	}
	
	@SuppressWarnings("unchecked")
	public static Cinema fromJSONObject(JSONObject o){
		ArrayList<String> seat = new ArrayList<String>();
		JSONArray seatInJSON = (JSONArray) o.get("seat");
		seat.addAll(seatInJSON);
		return new Cinema(
				Integer.parseInt(o.get("id").toString()), 
				o.get("name").toString(), 
				Integer.parseInt(o.get("cinemaClass").toString()), 
				Integer.parseInt(o.get("cineplexid").toString()),
				seat
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
	
	@SuppressWarnings("unchecked")
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
