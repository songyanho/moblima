package sg.edu.ntu.cz2002.moblima.dao;

import java.util.HashMap;

import org.json.simple.JSONObject;

import sg.edu.ntu.cz2002.moblima.database.Database;
import sg.edu.ntu.cz2002.moblima.models.Admin;

public class AdminDao {
	
	protected static final String DATABASE_NAME = "admin";
	
	protected static HashMap<Integer, Admin> records;

	public static HashMap<Integer, Admin> getAllInHashMap() {
		// TODO Auto-generated method stub
		if(records == null) initialize();
		return records;
	}

	public static Admin getById(int id) {
		// TODO Auto-generated method stub
		if(records == null) initialize();
		return records.get(id);
	}

	public static boolean save(Admin t) {
		// TODO Auto-generated method stub
		if(records == null) initialize();
		records.put(t.getId(), t);
		return Database.save(DATABASE_NAME, Admin.toJSONObjects(records));
	}

	public static void initialize() {
		// TODO Auto-generated method stub
		JSONObject t = Database.getObject(DATABASE_NAME);
		records = Admin.fromJSONObjects(t);
	}
	
}
