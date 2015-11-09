package sg.edu.ntu.cz2002.moblima.models;

import java.util.HashMap;
import java.util.Set;

import org.json.simple.JSONObject;

public class Seat implements StandardData {
	protected int id;
	protected int row;
	protected int column;
	protected int seatPlaneId;
	protected SeatType seatType;
	protected SeatStatus seatStatus;
	
	public enum SeatType{
		NORMAL, COUPLE, ULTIMA, RESERVED
	}
	
	public enum SeatStatus{
		AVAILABLE, BOOKED
	}
	
	public static SeatType getSeatTypeEnumFromChoice(int choice) {
		return choice == 1? SeatType.NORMAL:
			   choice == 2? SeatType.COUPLE:
			   choice == 3? SeatType.ULTIMA:
				   			SeatType.RESERVED;
	}
	
	public Seat(int id, int row, int column, int seatPlaneId, int seatType) {
		this.id = id;
		this.row = row;
		this.column = column;
		this.seatPlaneId = seatPlaneId;
		this.seatType = SeatType.values()[seatType];
		this.seatStatus = SeatStatus.AVAILABLE;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getColumn() {
		return column;
	}

	public void setColumn(int column) {
		this.column = column;
	}

	public int getSeatPlaneId() {
		return seatPlaneId;
	}

	public void setSeatPlaneId(int seatPlaneId) {
		this.seatPlaneId = seatPlaneId;
	}

	public SeatType getSeatType() {
		return seatType;
	}

	public void setSeatType(SeatType seatType) {
		this.seatType = seatType;
	}

	public SeatStatus getSeatStatus() {
		return seatStatus;
	}

	public void setSeatStatus(SeatStatus seatStatus) {
		this.seatStatus = seatStatus;
	}

	public boolean isOccupied(int showtimeId){
		return true;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public JSONObject toJSONObject() {
		JSONObject o = new JSONObject();
		o.put("id", this.id);
		o.put("row", this.row);
		o.put("column", this.column);
		o.put("seatplaneid", this.seatPlaneId);
		o.put("seattype", this.seatType.ordinal());
		return o;
	}
	
	public static Seat fromJSONObject(JSONObject o){
		return new Seat(
				Integer.parseInt(o.get("id").toString()), 
				Integer.parseInt(o.get("row").toString()), 
				Integer.parseInt(o.get("column").toString()), 
				Integer.parseInt(o.get("seatplaneid").toString()), 
				Integer.parseInt(o.get("seattype").toString()));
	}
	
	public static HashMap<String, JSONObject> toJSONObjects(HashMap<Integer, Seat> o){
		HashMap<String, JSONObject> a = new HashMap<String, JSONObject>();
		Set<Integer> s = o.keySet();
		for(Integer i: s){
			a.put(""+i, o.get(i).toJSONObject());
		}
		return a;
	}
	
	@SuppressWarnings("unchecked")
	public static HashMap<Integer, Seat> fromJSONObjects(JSONObject o){
		HashMap<Integer, Seat> a = new HashMap<Integer, Seat>();
		Set<String> s = o.keySet();
		for(String i: s){
			JSONObject n = (JSONObject) o.get(i);
			Seat t =  Seat.fromJSONObject(n);
			a.put(t.getId(), t);
		}
		return a;
	}
	
}
