package sg.edu.ntu.cz2002.moblima.models;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.json.simple.JSONObject;

import sg.edu.ntu.cz2002.moblima.dao.SeatDao;
import sg.edu.ntu.cz2002.moblima.models.Seat.SeatType;

public class SeatPlane implements StandardData {

	protected int id;
	protected String name;
	protected int totalSeat;
	protected int row;
	protected int column;

	public SeatPlane(int id, String name, int totalSeat, int row, int column) {
		this.id = id;
		this.name = name;
		this.totalSeat = totalSeat;
		this.row = row;
		this.column = column;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getTotalSeat() {
		return totalSeat;
	}

	public void setTotalSeat(int totalSeat) {
		this.totalSeat = totalSeat;
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

	/**
	 * Return a 2D matrix view of seat arrangement
	 * @param t
	 * @return
	 */
	public int[][] getSeatArray(HashMap<Integer, Ticket> t) {
		int[][] seatArray = new int[row][column];
		for(int j=0; j<row; j++){
			for(int i=0; i<column; i++)
				seatArray[j][i] = 0;
		}
		HashSet<Integer> bookSeatIds = new HashSet<Integer>();
		for(Ticket tt: t.values()){
			bookSeatIds.add(tt.getSeatId());
		}
		HashMap<Integer, Seat> seats = SeatDao.getSeatsWithPlane(this.id);
		for(Seat seat: seats.values()){
			int occupied = 1;
			if(seatArray[seat.getRow()][seat.getColumn()] > 0)
				System.out.println("Duplicated seat @ "+seat.getRow()+","+seat.getColumn());
			if(bookSeatIds.contains(seat.getId()))
				occupied *= -1;
			seatArray[seat.getRow()][seat.getColumn()] = occupied*seat.getId();
			if(seat.getSeatType() == SeatType.COUPLE){
				seatArray[seat.getRow()][seat.getColumn()+1] = occupied*seat.getId();
			}
		}
		return seatArray;
	}

	@SuppressWarnings("unchecked")
	@Override
	public JSONObject toJSONObject() {
		JSONObject o = new JSONObject();
		o.put("id", this.id);
		o.put("name", this.name);
		o.put("totalseat", this.totalSeat);
		o.put("row", this.row);
		o.put("column", this.column);
		return o;
	}
	
	public static SeatPlane fromJSONObject(JSONObject o){
		return new SeatPlane(
				Integer.parseInt(o.get("id").toString()), 
				o.get("name").toString(), 
				Integer.parseInt(o.get("totalseat").toString()), 
				Integer.parseInt(o.get("row").toString()), 
						Integer.parseInt(o.get("column").toString()));
	}
	
	public static HashMap<String, JSONObject> toJSONObjects(HashMap<Integer, SeatPlane> o){
		HashMap<String, JSONObject> a = new HashMap<String, JSONObject>();
		Set<Integer> s = o.keySet();
		for(Integer i: s){
			a.put(""+i, o.get(i).toJSONObject());
		}
		return a;
	}
	
	@SuppressWarnings("unchecked")
	public static HashMap<Integer, SeatPlane> fromJSONObjects(JSONObject o){
		HashMap<Integer, SeatPlane> a = new HashMap<Integer, SeatPlane>();
		Set<String> s = o.keySet();
		for(String i: s){
			JSONObject n = (JSONObject) o.get(i);
			SeatPlane t =  SeatPlane.fromJSONObject(n);
			a.put(t.getId(), t);
		}
		return a;
	}

}
