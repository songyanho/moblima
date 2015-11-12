package sg.edu.ntu.cz2002.moblima.models;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import sg.edu.ntu.cz2002.moblima.dao.*;

public class Movie implements StandardData{
	protected int id;
	protected String title;
	protected MovieStatus status;
	protected String synopsis;
	protected String director;
	protected ArrayList<String> casts;
	protected MovieRating rating;
	protected HashMap<Integer, Review> reviews;
	protected int duration;
	protected MovieType type;
	
	public enum MovieType{
		BLOCKBUSTER, THREED, NORMAL;
	}
	
	public enum MovieRating{
		GENERAL, PG, PG13, M18, R21, UNRATED
	}
	
	public enum MovieStatus{
		COMINGSOON, PREVIEW, NOWSHOWING, ENDOFSHOWING
	}

	public Movie(){
		this.id = MovieDao.getLastId()+1;
	}

	/**
	 * Constructor strictly for Database Initialization
	 * @param id Unique ID of Movie
	 * @param title Title of Movie
	 * @param status Status of ID in ordinal
	 * @param director Director of Movie
	 * @param synopsis Synopsis of Movie
	 * @param casts Casts in ArrayList<String>
	 * @param rating Movie Rating in ordinal
	 * @param duration Duration of Movie
	 * @param type Movie Type in ordinal
	 */
	public Movie(int id, String title, int status, String director, String synopsis, ArrayList<String> casts, int rating, int duration, int type) {
		this.id = id;
		this.title = title;
		this.status = Movie.getStatusEnumFromOrdinal(status);
		this.director = director;
		this.synopsis = synopsis;
		this.casts = casts;
		this.rating = Movie.getRatingEnumFromOrdinal(rating);
		this.duration = duration;
		this.type = Movie.getTypeEnumFromOrdinal(type);
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

	public MovieStatus getStatus() {
		return status;
	}
	
	/**
	 * Get Presentable String of Screening Status of this movie
	 * @return MovieStatus of this movie
	 */
	public String getStatusString(){
		return this.status == MovieStatus.COMINGSOON ? 	"Coming soon" : 
			   this.status == MovieStatus.PREVIEW ? 	"Preview" : 
			   this.status == MovieStatus.NOWSHOWING ? 	"Now Showing" : 
			   											"End of Showing";
	}

	/**
	 * Set the Movie Status based on user input
	 * @param choice Integer from 1 to 4
	 */
	public void setStatusFromChoice(int choice){
		switch(choice){
		case 1:
			this.status = MovieStatus.COMINGSOON;
			break;
		case 2:
			this.status = MovieStatus.PREVIEW;
			break;
		case 3:
			this.status = MovieStatus.NOWSHOWING;
			break;
		default:
			this.status = MovieStatus.ENDOFSHOWING;
			break;
		}
	}

	/**
	 * Get the MovieStatus enumeration based on user input
	 * @param choice Integer from 1 to 4
	 * @return MovieStatus based on user input
	 */
	public static MovieStatus getStatusEnumFromChoice(int choice){
		return choice==1 ? 	MovieStatus.COMINGSOON : 
			   choice==2 ? 	MovieStatus.PREVIEW : 
			   choice==3 ? 	MovieStatus.NOWSHOWING : 
			   				MovieStatus.ENDOFSHOWING;
	}
	
	/**
	 * Get the MovieStatus enumeration based on enumeration ordinal
	 * @param ordinal Integer from 0 to 3
	 * @return MovieStatus based on its ordinal
	 */
	public static MovieStatus getStatusEnumFromOrdinal(int ordinal){
		return ordinal == MovieStatus.COMINGSOON.ordinal() ? MovieStatus.COMINGSOON :
			   ordinal == MovieStatus.PREVIEW.ordinal() ? MovieStatus.PREVIEW :
			   ordinal == MovieStatus.NOWSHOWING.ordinal() ? MovieStatus.NOWSHOWING :
				   	MovieStatus.ENDOFSHOWING;
	}
	
	/**
	 * Get presentable string of MovieStatus based on user input
	 * @param choice Integer from 1 to 4
	 * @return Presentable string of MovieStatus based on user input
	 */
	public static String getStatusStringFromChoice(int choice){
		return choice==1 ? 	"Coming soon" : 
			   choice==2 ? 	"Preview" : 
			   choice==3 ? 	"Now Showing" : 
			   				"End of Showing";
	}
	
	/**
	 * Print the name of each Enum MovieStatus
	 */
	public static void printMovieStatusChoice(){
		for (MovieStatus m: MovieStatus.values()) {
			System.out.println("\t" + (m.ordinal()+1) + ". " + m.name());
		}
	}
	
	public MovieType getType() {
		return type;
	}
	
	/**
	 * Set the MovieType based on user input
	 * @param choice Integer from 1 to 3
	 */
	public void setTypeFromChoice(int choice) {
		switch(choice){
		case 1:
			this.type = MovieType.BLOCKBUSTER;
			break;
		case 2:
			this.type = MovieType.THREED;
			break;
		default:
			this.type = MovieType.NORMAL;
			break;
		}
	}
	
	/**
	 * Get name of Enum MovieType from user choice
	 * @param choice
	 * @return
	 */
	public static String getTypeStringFromChoice(int choice){
		return choice==1 ? 	"BLOCKBUSTER" : 
			   choice==2 ? 	"THREED" : 
			   				"NORMAL";
	}
	
	/**
	 * Get name of Enum MovieType from the class itself
	 * @param choice
	 * @return
	 */
	public static String getTypeStringFromMovieType(MovieType choice){
		return choice==MovieType.BLOCKBUSTER ? 	"BLOCKBUSTER" : 
			   choice==MovieType.THREED ? 	"THREED" : 
			   				"NORMAL";
	}
	
	/**
	 * Get Enum MovieType from user choice
	 * @param choice
	 * @return
	 */
	public static MovieType getTypeEnumFromChoice(int choice){
		return choice==1 ? 	MovieType.BLOCKBUSTER: 
			   choice==2 ? 	MovieType.THREED : 
				   			MovieType.NORMAL ; 
	}

	/**
	 * Get Enum MovieType from its ordinal value
	 * @param ordinal
	 * @return
	 */
	public static MovieType getTypeEnumFromOrdinal(int ordinal){
		return ordinal == MovieType.BLOCKBUSTER.ordinal() ? MovieType.BLOCKBUSTER :
			   ordinal == MovieType.THREED.ordinal() ? MovieType.THREED :
				   MovieType.NORMAL;
	}
	
	/**
	 * Print  the name for each of Enum MovieType
	 */
	public static void printMovieTypeChoice(){
		for (MovieType m: MovieType.values()) {
			System.out.println("\t" + (m.ordinal()+1) + ". " + m.name());
		}
	}
	
	public MovieRating getRating() {
		return rating;
	}
	
	/**
	 * Get the name of Enum MovieRating of this movie
	 * @return
	 */
	public String getRatingString(){
		return this.rating == MovieRating.GENERAL ? "General" : 
			   this.rating == MovieRating.PG ? 		"PG" : 
			   this.rating == MovieRating.PG13 ? 	"PG13" : 
			   this.rating == MovieRating.M18 ? 	"M18" : 
			   this.rating == MovieRating.R21 ?		"R21" :
			   										"Unrated";
	}

	public void setRating(MovieRating rating) {
		this.rating = rating;
	}
	
	/**
	 * Set the Enum MovieRating from user choice
	 * @param choice
	 */
	public void setRatingFromChoice(int choice){
		switch(choice){
		case 1:
			this.rating = MovieRating.GENERAL;
			break;
		case 2:
			this.rating = MovieRating.PG;
			break;
		case 3:
			this.rating = MovieRating.PG13;
			break;
		case 4:
			this.rating = MovieRating.M18;
			break;
		case 5:
			this.rating = MovieRating.R21;
			break;
		default:
			this.rating = MovieRating.UNRATED;
			break;
		}
	}
	
	/**
	 * Get the name of Enum MovieRating from user choice
	 * @param choice
	 * @return
	 */
	public static String getRatingStringFromChoice(int choice){
		return choice==1 ? 	"General" : 
			   choice==2 ? 	"PG" : 
			   choice==3 ? 	"PG13" : 
			   choice==4 ?	"M18" :
			   choice==5 ?	"R21" :
			   				"Unrated";
	}
	
	/**
	 * Get the ordinal value of Enum MovieRating from user choice
	 * @param choice
	 * @return
	 */
	public static MovieRating getRatingEnumFromChoice(int choice){
		return choice==1 ? 	MovieRating.GENERAL : 
			   choice==2 ? 	MovieRating.PG : 
			   choice==3 ? 	MovieRating.PG13 : 
			   choice==4 ?	MovieRating.M18 : 
			   choice==5 ?	MovieRating.R21 :
				   			MovieRating.UNRATED;
	}
	
	/**
	 * Get Enum MovieRating from its ordinal value
	 * @param ordinal
	 * @return
	 */
	public static MovieRating getRatingEnumFromOrdinal(int ordinal){
		return ordinal == MovieRating.GENERAL.ordinal() ? MovieRating.GENERAL :
			   ordinal == MovieRating.PG.ordinal() ? MovieRating.PG :
			   ordinal == MovieRating.PG13.ordinal() ? MovieRating.PG13 :
			   ordinal == MovieRating.M18.ordinal() ? MovieRating.M18 :
			   ordinal == MovieRating.R21.ordinal() ? MovieRating.R21 :
				   			MovieRating.UNRATED;
	}
	
	/**
	 * Print the name for each of Enum MovieRating 
	 */
	public static void printMovieRatingChoice(){
		for (MovieRating m: MovieRating.values()) {
		System.out.println("\t" + (m.ordinal()+1) + ". " + m.name());
		}
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

	public HashMap<Integer, Review> getReviews() {
		return ReviewDao.findByMovieId(this.id);
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}
	
	public void setReviews(HashMap<Integer, Review> reviews) {
		this.reviews = reviews;
	}
	
	public void addReview(Review review){
		ReviewDao.add(review);
	}

	@SuppressWarnings("unchecked")
	@Override
	public JSONObject toJSONObject() {
		JSONObject o = new JSONObject();
		o.put("id", this.id);
		o.put("title", this.title);
		o.put("status", this.status.ordinal());
		o.put("synopsis", this.synopsis);
		o.put("director", this.director);
		o.put("rating", this.rating.ordinal());
		JSONArray a = new JSONArray();
		a.addAll(this.casts);
		o.put("casts", a);
		o.put("duration", this.duration);
		o.put("type", this.type.ordinal());
		return o;
	}
	
	@SuppressWarnings("unchecked")
	public static Movie fromJSONObject(JSONObject o){
		ArrayList<String> casts = new ArrayList<String>();
		JSONArray castsInJSON = (JSONArray) o.get("casts");
		casts.addAll(castsInJSON);
		return new Movie(
				Integer.parseInt(o.get("id").toString()), 
				o.get("title").toString(), 
				Integer.parseInt(o.get("status").toString()), 
				o.get("director").toString(), 
				o.get("synopsis").toString(), 
				casts, 
				Integer.parseInt(o.get("rating").toString()), 
				Integer.parseInt(o.get("duration").toString()),
				Integer.parseInt(o.get("type").toString()));
	}
	
	public static HashMap<String, JSONObject> toJSONObjects(HashMap<Integer, Movie> o){
		HashMap<String, JSONObject> a = new HashMap<String, JSONObject>();
		Set<Integer> s = o.keySet();
		for(Integer i: s){
			a.put(""+i, o.get(i).toJSONObject());
		}
		return a;
	}
	
	@SuppressWarnings("unchecked")
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
