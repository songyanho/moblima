package sg.edu.ntu.cz2002.moblima.models;
import java.util.HashMap;
import java.util.Set;

import org.json.simple.JSONObject;

import sg.edu.ntu.cz2002.moblima.dao.ReviewDao;

public class Review implements StandardData {
	protected int id;
	protected String name;
	protected double rating;
	protected String comment;
	protected int movieId;
	
	public Review(){
		this.id = ReviewDao.getLastId()+1;
		this.rating = 0;
	}
	
	public Review(int id, int movieId, String name, String comment, double rating) {
		this.id = id;
		this.movieId = movieId;
		this.name = name;
		this.rating = rating;
		this.comment = comment;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public int getMovieId(){
		return this.movieId;
	}
	
	public void setMovieId(int movieId){
		this.movieId = movieId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public double getRating() {
		return rating;
	}
	
	public void setRating (double rating) {
		if (rating >= 1.0 && rating <= 5.0)
			this.rating = rating;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public JSONObject toJSONObject() {
		JSONObject o = new JSONObject();
		o.put("id", this.id);
		o.put("movieid", this.movieId);
		o.put("name", this.name);
		o.put("comment", this.comment);
		o.put("rating", this.rating);
		return o;
	}
	
	public static Review fromJSONObject(JSONObject o){
		return new Review(Integer.parseInt(o.get("id").toString()), Integer.parseInt(o.get("movieid").toString()), o.get("name").toString(), o.get("comment").toString(), Double.parseDouble(o.get("rating").toString()));
	}
	
	public static HashMap<String, JSONObject> toJSONObjects(HashMap<Integer, Review> o){
		HashMap<String, JSONObject> a = new HashMap<String, JSONObject>();
		Set<Integer> s = o.keySet();
		for(Integer i: s){
			a.put(""+i, o.get(i).toJSONObject());
		}
		return a;
	}
	
	@SuppressWarnings("unchecked")
	public static HashMap<Integer, Review> fromJSONObjects(JSONObject o){
		HashMap<Integer, Review> a = new HashMap<Integer, Review>();
		Set<String> s = o.keySet();
		for(String i: s){
			JSONObject n = (JSONObject) o.get(i);
			Review t =  Review.fromJSONObject(n);
			a.put(t.getId(), t);
		}
		return a;
	}
}