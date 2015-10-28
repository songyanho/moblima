package sg.edu.ntu.cz2002.moblima.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.json.simple.JSONObject;

import sg.edu.ntu.cz2002.moblima.database.Database;
import sg.edu.ntu.cz2002.moblima.models.*;

public class CinemaDao {
	protected static final String DATABASE_NAME = "cinema";
	
	protected static HashMap<Integer, Cinema> records;
	
	public static HashMap<Integer, Cinema> getAllInHashMap() {
		if(records == null) initialize();
		return records;
	}
	
	public static Cinema findById(int id) {
		if(records == null) initialize();
		if(records.containsKey(id))
			return records.get(id);
		return null;
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
	
	public static HashMap<Integer, Cinema> findByClass(String cinemaClass) {
		if(records == null) initialize();
		HashMap<Integer, Cinema> c = new HashMap<Integer, Cinema>();
		for(Cinema i: records.values()){
			if(i.getCinemaClass().toLowerCase().contains(cinemaClass.toLowerCase()))
				c.put(i.getId(), i);
		}
		return c;
	}
	
	public static HashMap<Integer, Cinema> findByName(String name) {
		if(records == null) initialize();
		HashMap<Integer, Cinema> c = new HashMap<Integer, Cinema>();
		for(Cinema i: records.values()){
			if(i.getName().toLowerCase().contains(name.toLowerCase()))
				c.put(i.getId(), i);
		}
		return c;
	}
	
	public static HashMap<Integer, Cinema> findByCineplex(int cineplexId){
		if(records == null) initialize();
		HashMap<Integer, Cinema> c = new HashMap<Integer, Cinema>();
		for(Cinema i: records.values()){
			if(i.getCineplexId() == cineplexId)
				c.put(i.getId(), i);
		}
		return c;
	}

	public static boolean add(Cinema t){
		if(records == null) initialize();
		if(records.containsKey(t.getId())) return false; 
		records.put(t.getId(), t);
		save();
		return true;
	}
	
	public static boolean save(Cinema t) {
		// TODO Auto-generated method stub
		if(records == null) initialize();
		records.put(t.getId(), t);
		return save();
	}

	public static boolean save() {
		// TODO Auto-generated method stub
		if(records == null) initialize();
		return Database.save(DATABASE_NAME, Cinema.toJSONObjects(records));
	}

	public static void initialize() {
		// TODO Auto-generated method stub
		JSONObject t = Database.getObject(DATABASE_NAME);
		records = Cinema.fromJSONObjects(t);
	}

	@Deprecated
	public static void resetData(){
		records = new HashMap<Integer, Cinema>();
		HashMap<Integer, Cineplex> cp = CineplexDao.getAllInHashMap();
		for(Cineplex cp1: cp.values()){
			for(int i=0; i<cp1.getCinemaNum(); i++){
				Cinema c = new Cinema();
				c.setName("Cinema "+(i+1));
				// TODO set class
				// c.setCinemaClass("Platinum Movie Suites");
				c.setCineplexId(cp1.getId());
				// TODO set seat number
				c.setSeatNum(109);
				records.put(c.getId(), c);
			}
		}
		save();
	}
}
