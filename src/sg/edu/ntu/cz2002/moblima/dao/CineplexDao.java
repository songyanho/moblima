package sg.edu.ntu.cz2002.moblima.dao;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.json.simple.JSONObject;

import sg.edu.ntu.cz2002.moblima.database.Database;
import sg.edu.ntu.cz2002.moblima.models.Cineplex;

public class CineplexDao {
	protected static final String DATABASE_NAME = "cineplex";
	
	protected static HashMap<Integer, Cineplex> records;
	
	public static HashMap<Integer, Cineplex> getAllInHashMap() {
		if(records == null) initialize();
		return records;
	}
	
	public static Cineplex findById(int id) {
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
	
	public static HashMap<Integer, Cineplex> findByName(String name) {
		if(records == null) initialize();
		HashMap<Integer, Cineplex> c = new HashMap<Integer, Cineplex>();
		for(Cineplex i: records.values()){
			if(i.getCineplexName().toLowerCase().contains(name.toLowerCase()))
				c.put(i.getCineplexId(), i);
		}
		return c;
	}
	
	public static boolean add(Cineplex t){
		if(records == null) initialize();
		if(records.containsKey(t.getCineplexId())) return false; 
		records.put(t.getCineplexId(), t);
		save();
		return true;
	}
	
	public static boolean save(Cineplex t) {
		// TODO Auto-generated method stub
		if(records == null) initialize();
		records.put(t.getCineplexId(), t);
		return save();
	}

	public static boolean save() {
		// TODO Auto-generated method stub
		if(records == null) initialize();
		return Database.save(DATABASE_NAME, Cineplex.toJSONObjects(records));
	}

	public static void initialize() {
		// TODO Auto-generated method stub
		JSONObject t = Database.getObject(DATABASE_NAME);
		records = Cineplex.fromJSONObjects(t);
	}
}