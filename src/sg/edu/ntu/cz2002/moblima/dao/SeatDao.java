package sg.edu.ntu.cz2002.moblima.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.json.simple.JSONObject;

import sg.edu.ntu.cz2002.moblima.database.Database;
import sg.edu.ntu.cz2002.moblima.models.Movie;
import sg.edu.ntu.cz2002.moblima.models.Seat;

public class SeatDao {

protected static final String DATABASE_NAME = "seat";
	
	protected static HashMap<Integer, Seat> records;
	
	public static HashMap<Integer, Seat> getAllInHashMap() {
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
	
	public static HashMap<Integer, Seat> getSeatsWithPlane(int seatPlaneId){
		if(records == null) initialize();
		HashMap<Integer, Seat> s = new HashMap<Integer, Seat>();
		for(Seat seat: records.values()){
			if(seat.getSeatPlaneId() == seatPlaneId)
				s.put(seat.getId(), seat);
		}
		return s;
	}

	public static Seat findById(int id) {
		if(records == null) initialize();
		if(records.containsKey(id))
			return records.get(id);
		return null;
	}
	
	public static boolean save(Seat t) {
		if(records == null) initialize();
		records.put(t.getId(), t);
		return save();
	}
	
	public static boolean save(){
		if(records == null) initialize();
		return Database.save(DATABASE_NAME, Seat.toJSONObjects(records));
	}
	
	public static boolean add(Seat t){
		if(records == null) initialize();
		if(records.containsKey(t.getId())) return false; // Records exists
		records.put(t.getId(), t);
		save();
		return true;
	}
	
	public static void initialize() {
		JSONObject t = Database.getObject(DATABASE_NAME);
		records = Seat.fromJSONObjects(t);
	}
	
	@Deprecated
	public static void reset(){
		records = new HashMap<Integer, Seat>();
	}

}
