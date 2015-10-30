package sg.edu.ntu.cz2002.moblima.models;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Set;
import java.util.TimeZone;

import org.json.simple.JSONObject;

import sg.edu.ntu.cz2002.moblima.dao.CinemaDao;
import sg.edu.ntu.cz2002.moblima.dao.CineplexDao;
import sg.edu.ntu.cz2002.moblima.dao.MovieDao;
import sg.edu.ntu.cz2002.moblima.dao.ShowtimeDao;

public class Showtime implements StandardData {
	protected int id;
	protected MovieType type;
	protected Calendar date;
	protected int cineplexId;
	protected int cinemaId;
	protected int movieId;
	protected double price;
	
	public enum MovieType{
		// TODO implement MovieType
		BLOCKBUSTER, THREED;
		public static final MovieType values[] = values();
	}
	
	public Showtime(){
		this.id = ShowtimeDao.getLastId()+1;
	}
	
	public Showtime(int id, int type, String date, int cineplexId, int cinemaId, int movieId){
		this.id = id;
		this.type = MovieType.values[type];
		String[] dateComponents = date.split(":");
		this.date = new GregorianCalendar(Integer.parseInt(dateComponents[0]), Integer.parseInt(dateComponents[1]), Integer.parseInt(dateComponents[2]), Integer.parseInt(dateComponents[3]), Integer.parseInt(dateComponents[4]));
		this.date.setTimeZone(TimeZone.getTimeZone("Asia/Singapore"));
		this.cineplexId = cineplexId;
		this.cinemaId = cinemaId;
		this.movieId = movieId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public MovieType getType() {
		return type;
	}

	public void setType(MovieType type) {
		this.type = type;
	}
	
	public static String getMovieTypeString(MovieType m){
		return m==MovieType.BLOCKBUSTER ? "BlockBuster" :
			m==MovieType.THREED ? "3D" : "Normal";
	}

	public Calendar getDate() {
		return date;
	}

	public void setDate(Calendar date) {
		this.date = date;
	}

	public int getCineplexId() {
		return cineplexId;
	}
	
	public Cineplex getCineplex(){
		return CineplexDao.findById(this.cineplexId);
	}

	public void setCineplexId(int cineplexId) {
		this.cineplexId = cineplexId;
	}

	public int getCinemaId() {
		return cinemaId;
	}
	
	public Cinema getCinema(){
		return CinemaDao.findById(this.cinemaId);
	}

	public void setCinemaId(int cinemaId) {
		this.cinemaId = cinemaId;
	}
	
	public Movie getMovie(){
		return MovieDao.findById(this.movieId);
	}

	public int getMovieId() {
		return movieId;
	}

	public void setMovieId(int movieId) {
		this.movieId = movieId;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public JSONObject toJSONObject() {
		JSONObject o = new JSONObject();
		o.put("id", this.id);
		o.put("type", this.type.ordinal());
		o.put("date", ""+this.date.get(Calendar.YEAR)+":"+date.get(Calendar.MONTH)+":"+date.get(Calendar.DAY_OF_MONTH)+":"+date.get(Calendar.HOUR_OF_DAY)+":"+date.get(Calendar.MINUTE));
		o.put("cineplexid", this.cineplexId);
		o.put("cinemaid", this.cinemaId);
		o.put("movieid", this.movieId);
		return o;
	}
	
	public static Showtime fromJSONObject(JSONObject o){
		return new Showtime(
				Integer.parseInt(o.get("id").toString()), 
				Integer.parseInt(o.get("type").toString()), 
				o.get("date").toString(), 
				Integer.parseInt(o.get("cineplexid").toString()), 
				Integer.parseInt(o.get("cinemaid").toString()), 
				Integer.parseInt(o.get("movieid").toString()));
	}
	
	public static HashMap<String, JSONObject> toJSONObjects(HashMap<Integer, Showtime> o){
		HashMap<String, JSONObject> a = new HashMap<String, JSONObject>();
		Set<Integer> s = o.keySet();
		for(Integer i: s){
			a.put(""+i, o.get(i).toJSONObject());
		}
		return a;
	}
	
	@SuppressWarnings("unchecked")
	public static HashMap<Integer, Showtime> fromJSONObjects(JSONObject o){
		HashMap<Integer, Showtime> a = new HashMap<Integer, Showtime>();
		Set<String> s = o.keySet();
		for(String i: s){
			JSONObject n = (JSONObject) o.get(i);
			Showtime t =  Showtime.fromJSONObject(n);
			a.put(t.getId(), t);
		}
		return a;
	}
}
