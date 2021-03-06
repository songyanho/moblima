package sg.edu.ntu.cz2002.moblima.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.json.simple.JSONObject;

import sg.edu.ntu.cz2002.moblima.database.Database;
import sg.edu.ntu.cz2002.moblima.models.Ticket;

public class TicketDao {
	protected static final String DATABASE_NAME = "ticket";
	
	protected static HashMap<Integer, Ticket> records;
	
	public static HashMap<Integer, Ticket> getAllInHashMap() {
		if(records == null) initialize();
		return records;
	}
	
	public static Ticket findById(int id) {
		if(records == null) initialize();
		if(records.containsKey(id))
			return records.get(id);
		return null;
	}
	
	public static HashMap<Integer, Ticket> findTicketsByTransactionId(int txid){
		if(records == null) initialize();
		HashMap<Integer, Ticket> ts = new HashMap<Integer, Ticket>();
		for(Ticket t: records.values()){
			if(t.getTransactionId() == txid)
				ts.put(t.getId(), t);
		}
		return ts;
	}
	
	public static HashMap<Integer, Ticket> findByShowtimeId(int showtimeId) {
		if(records == null) initialize();
		HashMap<Integer, Ticket> bookedTickets = new HashMap<Integer, Ticket>();
		for (Ticket t: records.values()) {
			if (t.getShowtime() == showtimeId)
				bookedTickets.put(t.getId(), t);
		}
		return bookedTickets;
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
	
	public static HashMap<Integer, Ticket> getTicketWithShowtime(int showtimeId){
		if(records == null) initialize();
		HashMap<Integer, Ticket> tickets = new HashMap<Integer, Ticket>();
		for(Ticket t: records.values()){
			if(t.getShowtime() == showtimeId)
				tickets.put(t.getId(), t);
		}
		return tickets;
	}
	
	public static boolean add(Ticket t){
		if(records == null) initialize();
		if(records.containsKey(t.getId())) return false; 
		records.put(t.getId(), t);
		save();
		return true;
	}
	
	public static boolean save(Ticket t) {
		if(records == null) initialize();
		records.put(t.getId(), t);
		return save();
	}

	public static boolean save() {
		if(records == null) initialize();
		return Database.save(DATABASE_NAME, Ticket.toJSONObjects(records));
	}

	public static void initialize() {
		JSONObject t = Database.getObject(DATABASE_NAME);
		records = Ticket.fromJSONObjects(t);
	}
}