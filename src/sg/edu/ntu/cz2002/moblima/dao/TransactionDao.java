package sg.edu.ntu.cz2002.moblima.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.json.simple.JSONObject;

import sg.edu.ntu.cz2002.moblima.database.Database;
import sg.edu.ntu.cz2002.moblima.models.*;

public class TransactionDao {
	protected static final String DATABASE_NAME = "transaction";
	
	protected static HashMap<Integer, Transaction> records;
	
	public static HashMap<Integer, Transaction> getAllInHashMap() {
		if(records == null) initialize();
		return records;
	}
	
	public static Transaction findById(int id) {
		if(records == null) initialize();
		if(records.containsKey(id))
			return records.get(id);
		return null;
	}
	
	public static HashMap<Integer, Transaction> findByName(String name){
		if(records == null) initialize();
		HashMap<Integer, Transaction> m = new HashMap<Integer, Transaction>();
		for(Transaction i: records.values()){
			if(i.getName().toLowerCase().contains(name.toLowerCase()))
				m.put(i.getId(), i);
		}
		return m;
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
	
	public static boolean add(Transaction t){
		if(records == null) initialize();
		if(records.containsKey(t.getId())) return false; 
		records.put(t.getId(), t);
		save();
		return true;
	}
	
	public static boolean save(Transaction t) {
		// TODO Auto-generated method stub
		if(records == null) initialize();
		records.put(t.getId(), t);
		return save();
	}

	public static boolean save() {
		// TODO Auto-generated method stub
		if(records == null) initialize();
		return Database.save(DATABASE_NAME, Transaction.toJSONObjects(records));
	}

	public static void initialize() {
		// TODO Auto-generated method stub
		JSONObject t = Database.getObject(DATABASE_NAME);
		records = Transaction.fromJSONObjects(t);
	}
}
