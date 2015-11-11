package sg.edu.ntu.cz2002.moblima.models;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;

import org.json.simple.JSONObject;

import sg.edu.ntu.cz2002.moblima.dao.*;

public class Transaction {
	protected int id;
	protected String TID;
	protected String name;
	protected String email;
	protected String mobileNumber;
	protected Double total;
//	protected Ticket ticket;
//	protected int ticketId;
	
	public Transaction() {
		this.id = TransactionDao.getLastId() + 1;
		//this.ticketId = TicketDao.getLastId() + 1;
	}
	
	public Transaction(int id, String TID, String name, String email, String mobileNumber, Double total) {
		this.id = id;
		this.TID = TID;
		this.name = name;
		this.email = email;
		this.mobileNumber = mobileNumber;
		this.total = total;
//		this.ticketId = ticketId;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTID() {
		return TID;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getMobileNumber() {
		return mobileNumber;
	}
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
	
	public HashMap<Integer, Ticket> getTickets() {
		return TicketDao.findTicketsByTransactionId(this.id);
	}
	
	public void setTID(Showtime showtime) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmm");
		Date date = new Date();
		int code = showtime.getCinemaId();
		this.TID = String.format("%03d", code) + formatter.format(date);
	}

	public void printTransaction() {
		TicketManager ticketMgr = new TicketManager();
		System.out.println("\nTransaction ID: " + this.TID);
		System.out.println("Customer name: " + this.name);
		System.out.println("Customer email: " + this.email);
		System.out.println("Customer mobile number: " + this.mobileNumber);
		for(Ticket t: this.getTickets().values()){
			ticketMgr.printTicket(t);
		}
		System.out.print("\n");
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject toJSONObject() {
		JSONObject o = new JSONObject();
		o.put("id", this.id);
		o.put("TID", this.TID);
		o.put("name", this.name);
		o.put("email", this.email);
		o.put("mobileNumber", this.mobileNumber);
		o.put("total", this.total);
		return o;
	}
	
	public static Transaction fromJSONObject(JSONObject o) {
		return new Transaction(
				Integer.parseInt(o.get("id").toString()),
				o.get("TID").toString(),
				o.get("name").toString(),
				o.get("email").toString(),
				o.get("mobileNumber").toString(),
				Double.parseDouble(o.get("total").toString()));
	}
	
	public static HashMap<String, JSONObject> toJSONObjects(HashMap<Integer, Transaction> o){
		HashMap<String, JSONObject> a = new HashMap<String, JSONObject>();
		Set<Integer> s = o.keySet();
		for(Integer i: s){
			a.put(""+i, o.get(i).toJSONObject());
		}
		return a;
	}
	
	@SuppressWarnings("unchecked")
	public static HashMap<Integer, Transaction> fromJSONObjects(JSONObject o){
		HashMap<Integer, Transaction> a = new HashMap<Integer, Transaction>();
		Set<String> s = o.keySet();
		for(String i: s){
			JSONObject n = (JSONObject) o.get(i);
			Transaction t =  Transaction.fromJSONObject(n);
			a.put(t.getId(), t);
		}
		return a;
	}
}
