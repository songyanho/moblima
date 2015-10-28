package sg.edu.ntu.cz2002.moblima.models;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;

import org.json.simple.JSONObject;

import sg.edu.ntu.cz2002.moblima.dao.CinemaDao;
import sg.edu.ntu.cz2002.moblima.dao.MovieDao;
import sg.edu.ntu.cz2002.moblima.dao.TicketDao;

public class Ticket implements StandardData {
	protected int id; //timeslot
	protected int movieId;
	protected int cinemaId;
	protected String seatId;
	protected String dayOfWeek;

	public Ticket() {
		this.id = TicketDao.getLastId()+1;
		Date now = new Date();
		SimpleDateFormat day = new SimpleDateFormat("E");
		this.dayOfWeek = day.format(now);
	}
	
	public Ticket(int id, int movieId, int cinemaId, String seatId, String dayOfWeek) {
		this.id = id;
		this.movieId = movieId;
		this.cinemaId = cinemaId;
		this.seatId = seatId;
		this.dayOfWeek = dayOfWeek;
	}
	
	public int getTicketId() {
		return id;
	}
	
	public String getDayOfWeek() {
		return dayOfWeek;
	}
	
	public void setTicketId(int ticket_Id) {
		this.id = ticket_Id;
	}
	
	public void setDayOfWeek (String day) {
		this.dayOfWeek = day;
	}
	
	public int getMovieId() {
		return movieId;
	}

	public void setMovieId(int movieId) {
		this.movieId = movieId;
	}

	public int getCinemaId() {
		return cinemaId;
	}

	public void setCinemaId(int cinemaId) {
		this.cinemaId = cinemaId;
	}

	public String getSeatId() {
		return seatId;
	}

	public void setSeatId(String seatId) {
		this.seatId = seatId;
	}

	public double calculatePrice() {
		double typePrice, classPrice, discount = 0;
		double basePrice = 0;;
		Movie movie = MovieDao.findById(this.movieId);
		Cinema cinema = CinemaDao.findById(this.cinemaId);
		int mType = movie.getType();
		if (mType == 1)
			typePrice = 3.0;
		else if (mType == 2)
			typePrice = 5.0;
		else
			typePrice = 1.0;
		
		int mClass = cinema.getCinemaClass();
		if (mClass == 1)
			classPrice = 5.0;
		else if (mClass == 2)
			classPrice = 3.0;
		else
			classPrice = 0;
		
		return basePrice + typePrice + classPrice + discount;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public JSONObject toJSONObject() {
		JSONObject o = new JSONObject();
		o.put("id", this.id);
		o.put("movieId", this.movieId);
		o.put("cinemaId", this.cinemaId);
		o.put("seatId", this.seatId);
		o.put("dayOfWeek", this.dayOfWeek);
		return o;
	}
	
	public static Ticket fromJSONObject(JSONObject o){
		return new Ticket(Integer.parseInt(o.get("id").toString()), Integer.parseInt(o.get("movieId").toString()), Integer.parseInt(o.get("cinemaId").toString()), o.get("seatId").toString(), o.get("dayOfWeek").toString());
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
			a.put(t.getTicketId(), t);
		}
		return a;
	}
}
