package sg.edu.ntu.cz2002.moblima.models;

import java.util.HashMap;
import java.util.Set;

import org.json.simple.JSONObject;

import sg.edu.ntu.cz2002.moblima.dao.CinemaDao;
import sg.edu.ntu.cz2002.moblima.dao.CineplexDao;

public class Cineplex {
	protected int id;
	protected String name;
	
	public Cineplex() {
		this.id = CineplexDao.getLastId() + 1;
	}
	
	/**
	 * Constructor strictly for Database Initialization
	 * @param id Unique ID of Cineplex
	 * @param name Name of Cineplex
	 */
	public Cineplex(int id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public String getCineplexName() {
		return name;
	}
	
	public int getId() {
		return id;
	}
	
	/**
	 * Get a HashMap of Cinemas in this Cineplex
	 * @return HashMap of ID of cinema and the respective cinema
	 */
	public HashMap<Integer, Cinema> getCinemas() {
		return CinemaDao.findByCineplex(this.id);
	}
	
	/**
	 * Count the number of cinemas in this cinplex
	 * @return
	 */
	public int getCinemaNum() {
		return CinemaDao.findByCineplex(this.id).values().size();
	}
	
	public void setCineplexName(String cineplex_Name) {
		this.name = cineplex_Name;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	@SuppressWarnings("unchecked")
	public JSONObject toJSONObject() {
		JSONObject o = new JSONObject();
		o.put("id", this.id);
		o.put("name", this.name);
		return o;
	}
	
	public static Cineplex fromJSONObject(JSONObject o){
		return new Cineplex(Integer.parseInt(o.get("id").toString()), o.get("name").toString());
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
