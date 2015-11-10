package sg.edu.ntu.cz2002.moblima.models;
import java.util.HashMap;
import java.util.Set;

import org.json.simple.JSONObject;

import sg.edu.ntu.cz2002.moblima.dao.CinemaDao;
import sg.edu.ntu.cz2002.moblima.dao.SeatPlaneDao;

public class Cinema {
	protected int id;
	protected String name;
	protected CinemaClass cinemaClass;
	protected int cineplexId;
	@Deprecated
	protected int seatNum;
	protected int seatPlaneId;

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
		return choice == 1? "Premium":
			   choice == 2? "Platinum":
			   choice == 3? "Gold":
				   			"Normal";
	}
	
	public static String getCinemaClassStringFromCinemaClass(CinemaClass choice) {
		return choice == CinemaClass.PREMIUM? "Premium":
			   choice == CinemaClass.PLATINUM? "Platinum":
			   choice == CinemaClass.GOLD? "Gold":
				   			"Normal";
	}
	
	public static void printCinemaClassChoice() {
		for (CinemaClass cc: CinemaClass.values())
			System.out.print("\t" + (cc.ordinal()+1) + ". " + cc.name());
	}
	
	public Cinema(int id, String name, int cinemaClass, int cineplexId, int seatNum, int seatPlaneId) {
		this.id = id;
		this.name = name;
		this.cinemaClass = Cinema.getCinemaClassEnumFromOrdinal(cinemaClass);
		this.cineplexId = cineplexId;
		this.seatNum = seatNum;
		this.seatPlaneId = seatPlaneId;
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

	public CinemaClass getCinemaClass() {
		return cinemaClass;
	}
	
	public String getCinemaClassString() {
		return this.cinemaClass == CinemaClass.PREMIUM? "PREMIUM":
			   this.cinemaClass == CinemaClass.PLATINUM? "PLATINUM":
			   this.cinemaClass == CinemaClass.GOLD? "GOLD":
				   	"NORMAL";
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
	
	public int getSeatPlaneId() {
		return seatPlaneId;
	}
	
	public SeatPlane getSeatPlane(){
		return SeatPlaneDao.findById(this.seatPlaneId);
	}

	public void setSeatPlaneId(int seatPlaneId) {
		this.seatPlaneId = seatPlaneId;
	}

	@SuppressWarnings("unchecked")
	public JSONObject toJSONObject() {
		JSONObject o = new JSONObject();
		o.put("id", this.id);
		o.put("name", this.name);
		o.put("cinemaClass", this.cinemaClass.ordinal());
		o.put("cineplexid", this.cineplexId);
		o.put("seatNum", this.seatNum);
		o.put("seatplaneid", this.seatPlaneId);
		return o;
	}
	
	public static Cinema fromJSONObject(JSONObject o){
		return new Cinema(
				Integer.parseInt(o.get("id").toString()), 
				o.get("name").toString(), 
				Integer.parseInt(o.get("cinemaClass").toString()), 
				Integer.parseInt(o.get("cineplexid").toString()),
				Integer.parseInt(o.get("seatNum").toString()),
				Integer.parseInt(o.get("seatplaneid").toString())
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
