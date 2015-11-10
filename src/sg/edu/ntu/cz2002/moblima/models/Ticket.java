package sg.edu.ntu.cz2002.moblima.models;

import java.util.HashMap;
import java.util.Set;

import org.json.simple.JSONObject;

import sg.edu.ntu.cz2002.moblima.dao.*;
import sg.edu.ntu.cz2002.moblima.models.Cinema.CinemaClass;
import sg.edu.ntu.cz2002.moblima.models.Movie.MovieType;
import sg.edu.ntu.cz2002.moblima.models.Seat.SeatType;
import sg.edu.ntu.cz2002.moblima.models.Showtime.Day;

public class Ticket implements StandardData{
	protected int id;
	protected int showtimeId;
	protected AgeGroup ageGroup;
	protected double price;
	protected int seatId;
	protected int transactionId;

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
	
	public static String getAgeGroupStringFromAgeGroup(AgeGroup choice) {
		return choice==AgeGroup.CHILD ? 	"CHILD" : 
			   choice==AgeGroup.ADULT ? 	"ADULT" : 
				   							"SENIOR";
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
	
	public Ticket(int transactionId) {
		super();
		this.id = TicketDao.getLastId()+1;
		this.transactionId = transactionId;
	}
	
	public Ticket(int id, int showtimeId, int ageGroup, double price, int seatId, int transactionId) {
		this.id = id;
		this.showtimeId = showtimeId;
		this.ageGroup = getAgeGroupEnumFromOrdinal(ageGroup);
		this.price = price;
		this.seatId = seatId;
		this.transactionId = transactionId;
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


	public int getSeatId() {
		return seatId;
	}

	public Seat getSeat() {
		return SeatDao.findById(this.seatId);
	}

	public void setSeatId(int seatId) {
		this.seatId = seatId;
	}
	
	public double calculatePrice() {
		double classCharge, dayCharge, typeCharge, ageCharge, basePrice, seatPrice;
		Showtime st = ShowtimeDao.findById(showtimeId);
		Settings settings = SettingsDao.getSettings();
		MovieType mt = st.getMovie().getType();
		CinemaClass cc = st.getCinema().getCinemaClass();
		Day day = st.getDayType();
		AgeGroup ag = this.ageGroup;
		SeatType seatType = this.getSeat().getSeatType();
		classCharge = settings.getCinemaClassCharges().get(cc);
		dayCharge = settings.getDayCharges().get(day);
		typeCharge = settings.getMovieTypeCharges().get(mt);
		ageCharge = settings.getAgeGroupCharges().get(ag);
		basePrice = settings.getBasePrice();
		seatPrice = settings.getSeatTypeCharges().get(seatType);
		return basePrice + classCharge + dayCharge + typeCharge + ageCharge + seatPrice;
	} 
	
	public int getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(int transactionId) {
		this.transactionId = transactionId;
	}

	public void printTicket() {
		Cineplex cineplex = ShowtimeDao.findById(this.showtimeId).getCineplex();
		Cinema cinema = ShowtimeDao.findById(this.showtimeId).getCinema();
		System.out.println("Cineplex: " + cineplex.getCineplexName());
		System.out.println("Cinema: " + cinema.getName() + ", Class: " + cinema.getCinemaClassString());
		System.out.println("Seat ID: " + this.getSeat().getSeatName() +" <"+Seat.getSeatTypeStringFromSeatType(this.getSeat().getSeatType())+">");
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
		o.put("transactionid", this.transactionId);
		return o;
	}
	
	public static Ticket fromJSONObject(JSONObject o){
		return new Ticket(Integer.parseInt(o.get("id").toString()), 
				Integer.parseInt(o.get("showtime").toString()), 
				Integer.parseInt(o.get("ageGroup").toString()),
				Double.parseDouble(o.get("price").toString()), 
				Integer.parseInt(o.get("seatId").toString()),
				Integer.parseInt(o.get("transactionid").toString()));
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
