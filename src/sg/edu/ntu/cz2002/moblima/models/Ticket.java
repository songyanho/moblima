package sg.edu.ntu.cz2002.moblima.models;

import java.util.HashMap;
import java.util.Set;

import org.json.simple.JSONObject;

import sg.edu.ntu.cz2002.moblima.dao.*;
import sg.edu.ntu.cz2002.moblima.models.Cinema.CinemaClass;
import sg.edu.ntu.cz2002.moblima.models.Movie.MovieType;
import sg.edu.ntu.cz2002.moblima.models.Showtime.Day;

public class Ticket implements StandardData{
	protected int id;
	protected int showtimeId;
	protected AgeGroup ageGroup;
	protected double price;
	protected String seatId;

	public enum AgeGroup {
		CHILD, ADULT, SENIOR;
	}
	
	public AgeGroup getAgeGroup() {
		return this.ageGroup;
	}
	
	public String getAgeGroupString() {
		return this.ageGroup == AgeGroup.CHILD? "Child":
			   this.ageGroup == AgeGroup.ADULT? "Adult":
							 			 		"Senior";
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
		super();
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
		return Math.round(price);
	}

	public void setPrice(double price) {
		this.price = Math.round(price);
	}


	public String getSeatId() {
		return seatId;
	}

	public void setSeatId(String seatId) {
		this.seatId = seatId;
	}
	
	public double calculatePrice() {
		double classCharge, dayCharge, typeCharge, ageCharge, basePrice;
		Showtime st = ShowtimeDao.findById(showtimeId);
		Settings setting = SettingsDao.getSettings();
		MovieType mt = st.getMovie().getType();
		CinemaClass cc = st.getCinema().getCinemaClass();
		Day day = st.getDayType();
		AgeGroup ag = this.ageGroup;
		classCharge = setting.getCinemaClassCharges().get(cc.ordinal());
		dayCharge = setting.getDayCharges().get(day.ordinal());
		typeCharge = setting.getMovieTypeCharges().get(mt.ordinal());
		ageCharge = setting.getAgeGroupCharges().get(ag.ordinal());
		basePrice = setting.getBasePrice();
		return basePrice + classCharge + dayCharge + typeCharge + ageCharge;
	} 
	
	public void printTicket() {
		Cineplex cineplex = ShowtimeDao.findById(this.showtimeId).getCineplex();
		Cinema cinema = ShowtimeDao.findById(this.showtimeId).getCinema();
		System.out.println("Cineplex: " + cineplex.getCineplexName());
		System.out.println("Cinema: " + cinema.getName() + ", Class: " + cinema.getCinemaClassString());
		System.out.println("Seat ID: " + this.seatId);
		System.out.println("Age group: " + this.getAgeGroupString());
		System.out.println("Ticket price: " + Math.round(this.price));
		System.out.print("\n");
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
