package sg.edu.ntu.cz2002.moblima.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.json.simple.JSONObject;

import sg.edu.ntu.cz2002.moblima.database.Database;
import sg.edu.ntu.cz2002.moblima.models.Review;

public class ReviewDao {
	protected static final String DATABASE_NAME = "review";
	
	protected static HashMap<Integer, Review> records;
	
	public static HashMap<Integer, Review> getAllInHashMap() {
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

	public static Review findById(int id) {
		if(records == null) initialize();
		if(records.containsKey(id))
			return records.get(id);
		return null;
	}
	
	public static HashMap<Integer, Review> findByMovieId(int movieId){
		if(records == null) initialize();
		HashMap<Integer, Review> m = new HashMap<Integer, Review>();
		for (Review r: records.values()) {
			if(r.getMovieId() == movieId)
				m.put(r.getId(), r);
		}
		return m;
	}

	public static boolean save(Review t) {
		if(records == null) initialize();
		records.put(t.getId(), t);
		return save();
	}
	
	public static boolean save(){
		if(records == null) initialize();
		return Database.save(DATABASE_NAME, Review.toJSONObjects(records));
	}
	
	public static boolean add(Review t){
		if(records == null) initialize();
		if(records.containsKey(t.getId())) return false; // Records exists
		records.put(t.getId(), t);
		save();
		return true;
	}

	public static void initialize() {
		JSONObject t = Database.getObject(DATABASE_NAME);
		records = Review.fromJSONObjects(t);
	}
}
