package sg.edu.ntu.cz2002.moblima.models;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;

import org.json.simple.JSONObject;

import sg.edu.ntu.cz2002.moblima.dao.TicketDao;

public class Ticket implements StandardData {
	protected int id; //timeslot
	protected String type;
	protected String dayOfWeek;
	
	public Ticket() {
		this.id = TicketDao.getLastId()+1;
		Date now = new Date();
		SimpleDateFormat day = new SimpleDateFormat("E");
		this.dayOfWeek = day.format(now);
	}
	
	public Ticket(int id, String type, String dayOfWeek) {
		this.id = id;
		this.type = type;
		this.dayOfWeek = dayOfWeek;
	}
	
	public int getTicketId() {
		return id;
	}
	
	public String getType() {
		return type;
	}
	
	public String getDayOfWeek() {
		return dayOfWeek;
	}
	
	public void setTicketId(int ticket_Id) {
		this.id = ticket_Id;
	}
	
	public void setType (String movie_type) {
		this.type = movie_type;
	}
	
	public void setDayOfWeek (String day) {
		this.dayOfWeek = day;
	}
	
	@Override
	public JSONObject toJSONObject() {
		JSONObject o = new JSONObject();
		o.put("id", this.id);
		o.put("type", this.type);
		o.put("dayOfWeek", this.dayOfWeek);
		return o;
	}
	
	public static Ticket fromJSONObject(JSONObject o){
		return new Ticket(Integer.parseInt(o.get("id").toString()), o.get("type").toString(), o.get("dayOfWeek").toString());
	}
	
	public static HashMap<String, JSONObject> toJSONObjects(HashMap<Integer, Ticket> o){
		HashMap<String, JSONObject> a = new HashMap<String, JSONObject>();
		Set<Integer> s = o.keySet();
		for(Integer i: s){
			a.put(""+i, o.get(i).toJSONObject());
		}
		return a;
	}
	
	public static HashMap<Integer, Ticket> fromJSONObjects(JSONObject o){
		HashMap<Integer, Ticket> a = new HashMap<Integer, Ticket>();
		Set<String> s = o.keySet();
		for(String i: s){
			JSONObject n = (JSONObject) o.get(i);
			Ticket t =  Ticket.fromJSONObject(n);
			a.put(t.getTicketId(), t);
		}
		return a;
	}
}
