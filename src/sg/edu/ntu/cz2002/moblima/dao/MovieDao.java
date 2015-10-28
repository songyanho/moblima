package sg.edu.ntu.cz2002.moblima.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.json.simple.JSONObject;

import sg.edu.ntu.cz2002.moblima.database.Database;
import sg.edu.ntu.cz2002.moblima.models.Movie;
import sg.edu.ntu.cz2002.moblima.models.Movie.MovieStatus;

public class MovieDao {
	
	protected static final String DATABASE_NAME = "movie";
	
	protected static HashMap<Integer, Movie> records;
	
	public static HashMap<Integer, Movie> getAllInHashMap() {
		if(records == null) initialize();
		return records;
	}
	
	public static int getLastId(){
		if(records == null) initialize();
		Set<Integer> ids = records.keySet();
		if(ids.size() == 0) return 0;
		List<Integer> a = new ArrayList<Integer>();
		for(Integer i: ids)
			a.add(i);
		Collections.sort(a);
		return a.get(a.size()-1);
	}

	public static Movie findById(int id) {
		if(records == null) initialize();
		if(records.containsKey(id))
			return records.get(id);
		return null;
	}
	
	public static boolean deleteById(int id){
		if(records == null) initialize();
		if(records.containsKey(id)){
			records.remove(id);
			return save();
		}
		return false;
	}
	
	public static HashMap<Integer, Movie> findActiveMovie(){
		if(records == null) initialize();
		HashMap<Integer, Movie> m = new HashMap<Integer, Movie>();
		for(Movie i: records.values()){
			if(i.getStatus() != MovieStatus.ENDOFSHOWING)
				m.put(i.getId(), i);
		}
		return m;
	}
	
	public static HashMap<Integer, Movie> findByTitle(String title){
		if(records == null) initialize();
		HashMap<Integer, Movie> m = new HashMap<Integer, Movie>();
		for(Movie i: records.values()){
			if(i.getTitle().toLowerCase().contains(title.toLowerCase()))
				m.put(i.getId(), i);
		}
		return m;
	}
	
	public static HashMap<Integer, Movie> findByDirector(String director){
		if(records == null) initialize();
		HashMap<Integer, Movie> m = new HashMap<Integer, Movie>();
		for(Movie i: records.values()){
			if(i.getDirector().toLowerCase().contains(director.toLowerCase()))
				m.put(i.getId(), i);
		}
		return m;
	}
	
	public static HashMap<Integer, Movie> findByStatus(int status){
		if(records == null) initialize();
		MovieStatus ms = Movie.getStatusEnumFromChoice(status);
		HashMap<Integer, Movie> m = new HashMap<Integer, Movie>();
		for(Movie i: records.values()){
			if(i.getStatus() == ms)
				m.put(i.getId(), i);
		}
		return m;
	}

	public static boolean save(Movie t) {
		if(records == null) initialize();
		records.put(t.getId(), t);
		return save();
	}
	
	public static boolean save(){
		if(records == null) initialize();
		return Database.save(DATABASE_NAME, Movie.toJSONObjects(records));
	}
	
	public static boolean add(Movie t){
		if(records == null) initialize();
		if(records.containsKey(t.getId())) return false; // Records exists
		records.put(t.getId(), t);
		save();
		return true;
	}

	public static void initialize() {
		JSONObject t = Database.getObject(DATABASE_NAME);
		records = Movie.fromJSONObjects(t);
	}

}
