package sg.edu.ntu.cz2002.moblima.models;

public class Cinema {
	protected String name;
	protected String cinemaClass;
	protected int id;
	protected Seat[] seat;
	private int seatNum = 300;
	private int numEmptySeat;
	
	public Cinema(int index) {
		this.id = index;
		seat = new Seat[seatNum];
		for (int i = 0; i < seatNum; i++)
			seat[i] = new Seat(i);
	}
	
	public String getCinemaName() {
		return name;
	}
	
	public int getCinemaId() {
		return id;
	}
	
	public String getcinemaClass() {
		return cinemaClass;
	}
	
	public void setCinemaName(String cinema_Name) {
		this.name = cinema_Name;
	}
	
	public void setCinemaId(int cinema_Id) {
		this.id = cinema_Id;
	}
	
	public void setClass(String cinema_Class) {
		this.cinemaClass = cinema_Class;
	}
	
	public void assign(int seatId, int ticketId) {
		if (seat[seatId].ticket.getTicketId() == -1) {
			seat[seatId].assign(ticketId);
			this.setNumEmptySeat(this.getNumEmptySeat() - 1);
		}
		else
			System.out.println("Seat already assigned to a customer.");
	}
	
	public int getNumEmptySeat() {
		return numEmptySeat;
	}

	public void setNumEmptySeat(int numEmptySeat) {
		this.numEmptySeat = numEmptySeat;
	}
}
