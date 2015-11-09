package sg.edu.ntu.cz2002.moblima.dao;

import java.util.HashMap;

import org.json.simple.JSONObject;

import sg.edu.ntu.cz2002.moblima.database.Database;
import sg.edu.ntu.cz2002.moblima.models.Movie;
import sg.edu.ntu.cz2002.moblima.models.Seat;
import sg.edu.ntu.cz2002.moblima.models.Seat.SeatType;
import sg.edu.ntu.cz2002.moblima.models.SeatPlane;

public class SeatPlaneDao {

	protected static final String DATABASE_NAME = "seatplane";
	
	protected static HashMap<Integer, SeatPlane> records;
	
	public static HashMap<Integer, SeatPlane> getAllInHashMap() {
		if(records == null) initialize();
		return records;
	}

	public static SeatPlane findById(int id) {
		if(records == null) initialize();
		if(records.containsKey(id))
			return records.get(id);
		return null;
	}
	
	public static boolean save(SeatPlane t) {
		if(records == null) initialize();
		records.put(t.getId(), t);
		return save();
	}
	
	public static boolean save(){
		if(records == null) initialize();
		return Database.save(DATABASE_NAME, SeatPlane.toJSONObjects(records));
	}
	
	public static boolean add(SeatPlane t){
		if(records == null) initialize();
		if(records.containsKey(t.getId())) return false; // Records exists
		records.put(t.getId(), t);
		save();
		return true;
	}
	
	public static void initialize() {
		JSONObject t = Database.getObject(DATABASE_NAME);
		records = SeatPlane.fromJSONObjects(t);
	}
	
	@Deprecated
	public static void reset(){
		records = new HashMap<Integer, SeatPlane>();
		SeatDao.reset();
		save(new SeatPlane(1, "Seat Plane 1", 0, 9, 17));
		for(int j=0; j<9; j++){
			for(int i=0; i<17; i++){
				if(i==8) continue;
				if(j<6){
					if(i<2) continue;
					SeatDao.save(new Seat(SeatDao.getLastId()+1, j, i, 1, 0));
				}else{
					if((i<8 && i%2==1) || (i>8 && i%2==0)) continue;
					SeatDao.save(new Seat(SeatDao.getLastId()+1, j, i, 1, 1));
				}
			}
		}
	}

}
