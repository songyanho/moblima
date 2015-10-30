package sg.edu.ntu.cz2002.moblima.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import sg.edu.ntu.cz2002.moblima.dao.*;

public class Ticket implements StandardData{
	protected int id;
	protected int showTime;
	protected AgeGroup ageGroup;
	protected double price;
	protected ArrayList<String> seat;

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
	
	public void printAgeGroupChoice() {
		for (AgeGroup a: AgeGroup.values())
			System.out.print("\t" + (a.ordinal()+1) + ". " + a.name());
	}
	
	public Ticket() {
		this.id = TicketDao.getLastId()+1;
	}
	
	public Ticket(int id, int showTime, int ageGroup, double price, ArrayList<String> seat) {
		this.id = id;
		this.showTime = showTime;
		this.ageGroup = getAgeGroupEnumFromOrdinal(ageGroup);
		this.price = price;
		this.seat = seat;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getShowTime() {
		return showTime;
	}

	public void setShowTime(int showTime) {
		this.showTime = showTime;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public ArrayList<String> getSeat() {
		return seat;
	}

	public void setSeat(ArrayList<String> seat) {
		this.seat = seat;
	}

	@Deprecated
	public double calculatePrice() {
		double typePrice, classPrice, discount = 0;
		double basePrice = 0;;
		//Movie movie = MovieDao.findById(this.movieId);
		//Cinema cinema = CinemaDao.findById(this.cinemaId);
		//MovieType mType = movie.getType();
		//if (mType.ordinal() == 1)
			typePrice = 3.0;
		//else if (mType.ordinal() == 2)
			typePrice = 5.0;
		//else
			typePrice = 1.0;
		
		//String mClass = cinema.getCinemaClass();
		classPrice = 0;
		/* 
		if (mClass == 1)
			classPrice = 5.0;
		else if (mClass == 2)
			classPrice = 3.0;
		else
			classPrice = 0;
		*/
		return basePrice + typePrice + classPrice + discount;
	} 
	
	
	@SuppressWarnings("unchecked")
	@Override
	public JSONObject toJSONObject() {
		JSONObject o = new JSONObject();
		o.put("id", this.id);
		o.put("showTime", this.showTime);
		o.put("age", this.ageGroup);
		o.put("price", this.price);
		o.put("seat", this.seat);
		return o;
	}
	
	@SuppressWarnings("unchecked")
	public static Ticket fromJSONObject(JSONObject o){
		ArrayList<String> seat = new ArrayList<String>();
		JSONArray seatsInJSON = (JSONArray) o.get("seat");
		seat.addAll(seatsInJSON);
		return new Ticket(Integer.parseInt(o.get("id").toString()), 
				Integer.parseInt(o.get("showTime").toString()), 
				Integer.parseInt(o.get("ageGroup").toString()),
				Double.parseDouble(o.get("price").toString()), 
				seat);
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
