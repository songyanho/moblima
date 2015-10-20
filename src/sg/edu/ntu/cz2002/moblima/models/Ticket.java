package sg.edu.ntu.cz2002.moblima.models;

public class Ticket {
	protected int id;
	protected String type;
	protected int dayOfWeek;
	protected int age;
	protected double price;
	//protected PurchasedTickets[] purchasedTickets;
	
	public Ticket() {
		this.id = 0;
		this.price = 0;
		//this.purchasedTickets = new PurchasedTickets[300];
	}
	
	public int getId() {
		return id;
	}
	
	public String getType() {
		return type;
	}
	
	public int getDayOfWeek() {
		return dayOfWeek;
	}

	public double getPrice() {
		return price;
	}
	
	/*
	 public PurchasedTickets[] getPurchasedTickets() {
		return purchasedTickets;
	}
	*/
	
	public void setId(int ticket_Id) {
		this.id = ticket_Id;
	}
	
	public void setType (String movie_type) {
		this.type = movie_type;
	}
	
	public void setDayOfWeek (int day) {
		this.dayOfWeek = day;
	}
	
	
	public void setPrice(double ticket_price) {
		this.price = ticket_price;
	}
	
	public void setPurchasedTickets() {
	}
}
