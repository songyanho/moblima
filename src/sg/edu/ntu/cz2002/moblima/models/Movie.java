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
	protected String rating;
	protected ArrayList<Review> reviews;
	
	public enum MovieType{
		// TODO implement MovieType
		BLOCKBUSTER, THREED
	}
	
	public enum MovieRating{
		// TODO implement MovieRating
		GENERAL, PG, PG13, M18, R21
	}
	
	public enum MovieStatus{
		COMINGSOON, PREVIEW, NOWSHOWING, ENDOFSHOWING
	}
	
	public Movie(){
		this.id = MovieDao.getLastId()+1;
	}

	public Movie(int id, String title, int status, String director, String synopsis, ArrayList<String> casts, String rating) {
		this.id = id;
		this.title = title;
		this.status = status;
		this.director = director;
		this.synopsis = synopsis;
		this.casts = casts;
		this.rating = rating;
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
	
	public String getStatusString(){
		return this.status == Movie.MovieStatus.COMINGSOON.ordinal() ? 		"Coming soon" : 
			   this.status == Movie.MovieStatus.PREVIEW.ordinal() ? 		"Preview" : 
			   this.status == Movie.MovieStatus.NOWSHOWING.ordinal() ? 		"Now Showing" : 
			   																"End of Showing";
	}

	@Deprecated
	public void setStatus(int status) {
		this.status = status;
	}
	
	public void setStatusFromChoice(int choice){
		switch(choice){
		case 1:
			this.status = MovieStatus.COMINGSOON.ordinal();
			break;
		case 2:
			this.status = MovieStatus.PREVIEW.ordinal();
			break;
		case 3:
			this.status = MovieStatus.NOWSHOWING.ordinal();
			break;
		default:
			this.status = MovieStatus.ENDOFSHOWING.ordinal();
			break;
		}
	}
	
	public static String getStatusStringFromChoice(int choice){
		return choice==1 ? 	"Coming soon" : 
			   choice==2 ? 	"Preview" : 
			   choice==3 ? 	"Now Showing" : 
			   				"End of Showing";
	}
	
	public static MovieStatus getStatusEnumFromChoice(int choice){
		return choice==1 ? 	MovieStatus.COMINGSOON : 
			   choice==2 ? 	MovieStatus.PREVIEW : 
			   choice==3 ? 	MovieStatus.NOWSHOWING : 
			   				MovieStatus.ENDOFSHOWING;
	}
	
	public static void printMovieStatusChoice(){
		System.out.println("\t1. Coming soon");
		System.out.println("\t2. Preview");
		System.out.println("\t3. Now Showing");
		System.out.println("\t4. End of Showing");
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

	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
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
	
	@SuppressWarnings("unchecked")
	@Override
	public JSONObject toJSONObject() {
		JSONObject o = new JSONObject();
		o.put("id", this.id);
		o.put("title", this.title);
		o.put("status", this.status);
		o.put("synopsis", this.synopsis);
		o.put("director", this.director);
		o.put("rating", this.rating);
		JSONArray a = new JSONArray();
		a.addAll(this.casts);
		o.put("casts", a);
		return o;
	}
	
	public static Movie fromJSONObject(JSONObject o){
		ArrayList<String> casts = new ArrayList<String>();
		JSONArray castsInJSON = (JSONArray) o.get("casts");
		casts.addAll(castsInJSON);
		return new Movie(Integer.parseInt(o.get("id").toString()), o.get("title").toString(), Integer.parseInt(o.get("status").toString()), o.get("director").toString(), o.get("synopsis").toString(), casts, o.get("rating").toString());
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
