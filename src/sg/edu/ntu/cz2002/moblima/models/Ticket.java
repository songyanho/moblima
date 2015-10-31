package sg.edu.ntu.cz2002.moblima.models;

import java.util.HashMap;
import java.util.Set;

import org.json.simple.JSONObject;

import sg.edu.ntu.cz2002.moblima.dao.*;
import sg.edu.ntu.cz2002.moblima.models.Cinema.CinemaClass;
import sg.edu.ntu.cz2002.moblima.models.Ticket.AgeGroup;

public class Ticket implements StandardData{
	protected int id;
	protected int showtimeId;
	protected AgeGroup ageGroup;
	protected double price;
	protected String seatId;

	public enum AgeGroup {
		CHILD, ADULT, SENIOR;
	}
	
	public static AgeGroup getAgeGroupEnumFromOrdinal(int ordinal) {
		return ordinal == AgeGroup.CHILD.ordinal()? AgeGroup.CHILD:
				ordinal == AgeGroup.ADULT.ordinal()? AgeGroup.ADULT:
							AgeGroup.SENIOR;
	}		
	
	public static AgeGroup getAgeGroupEnumFromChoice(int choice) {
		return choice==1 ? 	AgeGroup.CHILD : 
			   choice==2 ? 	AgeGroup.ADULT : 
				   			AgeGroup.SENIOR;
	}
	
	public static String getAgeGroupStringFromChoice(int choice) {
		return choice==1 ? 	"CHILD" : 
			   choice==2 ? 	"ADULT" : 
				   			"SENIOR" ;
	}
	
	public void setAgeGroupFromChoice(int choice) {
		switch (choice) {
		case 1:
			this.ageGroup = AgeGroup.CHILD;
			break;
		case 2:
			this.ageGroup = AgeGroup.ADULT;
			break;
		default:
			this.ageGroup = AgeGroup.SENIOR;
		}
	}
	
	public static void printAgeGroupChoice() {
		System.out.println("\n<< Age Group >>");
		for (AgeGroup a: AgeGroup.values())
			System.out.println("\t" + (a.ordinal()+1) + ". " + a.name());
	}
	
	public Ticket() {
		this.id = TicketDao.getLastId()+1;
	}
	
	public Ticket(int id, int showtimeId, int ageGroup, double price, String seatId) {
		this.id = id;
		this.showtimeId = showtimeId;
		this.ageGroup = getAgeGroupEnumFromOrdinal(ageGroup);
		this.price = price;
		this.seatId = seatId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getShowtime() {
		return showtimeId;
	}

	public void setShowtime(int showtimeId) {
		this.showtimeId = showtimeId;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}


	public String getSeatId() {
		return seatId;
	}

	public void setSeatId(String seatId) {
		this.seatId = seatId;
	}

	public double calculatePrice() {
		double base = 6.0;
		CinemaClass cc = ShowtimeDao.findById(showtimeId).getCinema().getCinemaClass();
		double classCharge = cc == CinemaClass.PREMIUM? 7.0:
				  			 cc == CinemaClass.PLATINUM? 5.0:
				  			 cc == CinemaClass.GOLD? 3.0:
				  			 0;
		AgeGroup ag = ageGroup;
		double ageGroupCharge = ag == AgeGroup.CHILD? 0.7:
								ag == AgeGroup.ADULT? 1.0:
								0.7;
		return (base + classCharge) * ageGroupCharge;
	} 
	
	@SuppressWarnings("unchecked")
	@Override
	public JSONObject toJSONObject() {
		JSONObject o = new JSONObject();
		o.put("id", this.id);
		o.put("showtime", this.showtimeId);
		o.put("ageGroup", this.ageGroup.ordinal());
		o.put("price", this.price);
		o.put("seatId", this.seatId);
		return o;
	}
	
	public static Ticket fromJSONObject(JSONObject o){
		return new Ticket(Integer.parseInt(o.get("id").toString()), 
				Integer.parseInt(o.get("showtime").toString()), 
				Integer.parseInt(o.get("ageGroup").toString()),
				Double.parseDouble(o.get("price").toString()), 
				(o.get("seatId").toString()));
	}
	
	public static HashMap<String, JSONObject> toJSONObjects(HashMap<Integer, Ticket> o){
		HashMap<String, JSONObject> a = new HashMap<String, JSONObject>();
		Set<Integer> s = o.keySet();
		for(Integer i: s){
			a.put(""+i, o.get(i).toJSONObject());
		}
		return a;
	}
	
	@SuppressWarnings("unchecked")
	public static HashMap<Integer, Ticket> fromJSONObjects(JSONObject o){
		HashMap<Integer, Ticket> a = new HashMap<Integer, Ticket>();
		Set<String> s = o.keySet();
		for(String i: s){
			JSONObject n = (JSONObject) o.get(i);
			Ticket t =  Ticket.fromJSONObject(n);
			a.put(t.getId(), t);
		}
		return a;
	}
}
