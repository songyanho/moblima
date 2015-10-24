package sg.edu.ntu.cz2002.moblima.models;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import sg.edu.ntu.cz2002.moblima.dao.MovieDao;

public class Movie implements StandardData{
	protected int id;
	protected String title;
	protected int status;
	protected String synopsis;
	protected String director;
	protected ArrayList<String> casts;
	protected double rating;
	protected ArrayList<Review> reviews;
	
	public Movie(){
		this.id = MovieDao.getLastId()+1;
	}

	public Movie(int id, String title, int status, String director, String synopsis, ArrayList<String> casts) {
		this.id = id;
		this.title = title;
		this.status = status;
		this.director = director;
		this.synopsis = synopsis;
		this.casts = casts;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getSynopsis() {
		return synopsis;
	}

	public void setSynopsis(String synopsis) {
		this.synopsis = synopsis;
	}

	public String getDirector() {
		return director;
	}

	public void setDirector(String director) {
		this.director = director;
	}

	public ArrayList<String> getCasts() {
		return casts;
	}
	
	public void setCasts(ArrayList<String> casts) {
		this.casts = casts;
	}

	public void addCast(String name) {
		this.casts.add(name);
	}
	
	public void removeCast(int index){
		this.casts.remove(index);
	}

	public double getRating() {
		return rating;
	}

	public void setRating(double rating) {
		this.rating = rating;
	}

	public ArrayList<Review> getReviews() {
		return reviews;
	}

	public void setReviews(ArrayList<Review> reviews) {
		this.reviews = reviews;
	}
	
	public void addReview(Review review){
		this.reviews.add(review);
	}
	
	@Override
	public JSONObject toJSONObject() {
		JSONObject o = new JSONObject();
		o.put("id", this.id);
		o.put("title", this.title);
		o.put("status", this.status);
		o.put("synopsis", this.synopsis);
		o.put("director", this.director);
		JSONArray a = new JSONArray();
		a.addAll(this.casts);
		o.put("casts", a);
		return o;
	}
	
	public static Movie fromJSONObject(JSONObject o){
		ArrayList<String> casts = new ArrayList<String>();
		JSONArray castsInJSON = (JSONArray) o.get("casts");
		casts.addAll(castsInJSON);
		return new Movie(Integer.parseInt(o.get("id").toString()), o.get("title").toString(), Integer.parseInt(o.get("status").toString()), o.get("director").toString(), o.get("synopsis").toString(), casts);
	}
	
	public static HashMap<String, JSONObject> toJSONObjects(HashMap<Integer, Movie> o){
		HashMap<String, JSONObject> a = new HashMap<String, JSONObject>();
		Set<Integer> s = o.keySet();
		for(Integer i: s){
			a.put(""+i, o.get(i).toJSONObject());
		}
		return a;
	}
	
	public static HashMap<Integer, Movie> fromJSONObjects(JSONObject o){
		HashMap<Integer, Movie> a = new HashMap<Integer, Movie>();
		Set<String> s = o.keySet();
		for(String i: s){
			JSONObject n = (JSONObject) o.get(i);
			Movie t =  Movie.fromJSONObject(n);
			a.put(t.getId(), t);
		}
		return a;
	}
}
