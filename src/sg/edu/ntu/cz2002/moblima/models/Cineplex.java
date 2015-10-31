package sg.edu.ntu.cz2002.moblima.models;

import java.util.HashMap;
import java.util.Set;

import org.json.simple.JSONObject;

import sg.edu.ntu.cz2002.moblima.dao.CinemaDao;
import sg.edu.ntu.cz2002.moblima.dao.CineplexDao;

public class Cineplex {
	protected int id;
	protected String name;
//	protected HashMap<Integer, Cinema> cinema;
//	protected HashMap<Integer, Movie> movies;
	protected int cinemaNum;
	
	public Cineplex() {
		this.id = CineplexDao.getLastId() + 1;
	}
	
	public Cineplex(int id, String name, int cinemaNum) {
		this.id = id;
		this.name = name;
		this.cinemaNum = cinemaNum;
	}
	
	public String getCineplexName() {
		return name;
	}
	
	public int getId() {
		return id;
	}
	
	public HashMap<Integer, Cinema> getCinemas() {
		return CinemaDao.findByCineplex(this.id);
	}
	
	public int getCinemaNum() {
		return cinemaNum;
	}
	
	public void setCineplexName(String cineplex_Name) {
		this.name = cineplex_Name;
	}
	
	public void setId(int id) {
		this.id = id;
	}

//	public void setCinema(HashMap<Integer, Cinema> cinema) {
//		this.cinema = cinema;
//	}

	public void setCinemaNum(int num) {
		if (num < 3) {
			this.cinemaNum = 3;
			System.out.println("At least three cinemas.");
		}
		else
			this.cinemaNum = num;
	}
	
//	public HashMap<Integer, Movie> getMovies() {
//		return movies;
//	}

//	public void setMovies(HashMap<Integer, Movie> movies) {
//		this.movies = movies;
//	}

	@SuppressWarnings("unchecked")
	public JSONObject toJSONObject() {
		JSONObject o = new JSONObject();
		o.put("id", this.id);
		o.put("name", this.name);
		o.put("cinemaNum", this.cinemaNum);
		return o;
	}
	
	public static Cineplex fromJSONObject(JSONObject o){
		return new Cineplex(Integer.parseInt(o.get("id").toString()), o.get("name").toString(), Integer.parseInt(o.get("cinemaNum").toString()));
	}
	
	public static HashMap<String, JSONObject> toJSONObjects(HashMap<Integer, Cineplex> o){
		HashMap<String, JSONObject> a = new HashMap<String, JSONObject>();
		Set<Integer> s = o.keySet();
		for(Integer i: s){
			a.put(""+i, o.get(i).toJSONObject());
		}
		return a;
	}
	
	@SuppressWarnings("unchecked")
	public static HashMap<Integer, Cineplex> fromJSONObjects(JSONObject o){
		HashMap<Integer, Cineplex> a = new HashMap<Integer, Cineplex>();
		Set<String> s = o.keySet();
		for(String i: s){
			JSONObject n = (JSONObject) o.get(i);
			Cineplex t =  Cineplex.fromJSONObject(n);
			a.put(t.getId(), t);
		}
		return a;
	}
}
