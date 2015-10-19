package sg.edu.ntu.cz2002.moblima;

public class Ticket {
	protected int id;
	protected String type;
	protected int dayOfWeek;
	protected int age;
	protected String cinemaClass;
	protected double price;
	//protected PurchasedTickets[] purchasedTickets;
	
	public Ticket() {
		this.id = -1;
		this.type = "";
		this.dayOfWeek = -1;
		this.age = 0;
		this.cinemaClass = "";
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
	
	public String getCinemaClass() {
		return cinemaClass;
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
	
	public void setCinemaClass(String cinema_Class) {
		this.cinemaClass = cinema_Class;
	}
	
	public void setPrice(double ticket_price) {
		this.price = ticket_price;
	}
	
	public void setPurchasedTickets() {
	}
}
