package sg.edu.ntu.cz2002.moblima.models;

public class Ticket {
	protected int id;
	protected String type;
	protected int dayOfWeek;
	protected int age;
	
	public Ticket() {
		this.id = -1;
	}
	
	public int getTicketId() {
		return id;
	}
	
	public String getType() {
		return type;
	}
	
	public int getDayOfWeek() {
		return dayOfWeek;
	}
	
	public void setTicketId(int ticket_Id) {
		this.id = ticket_Id;
	}
	
	public void setType (String movie_type) {
		this.type = movie_type;
	}
	
	public void setDayOfWeek (int day) {
		this.dayOfWeek = day;
	}
	
	public void setPurchasedTickets() {
	}
}
