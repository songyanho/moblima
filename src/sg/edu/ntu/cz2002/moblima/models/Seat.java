package sg.edu.ntu.cz2002.moblima.models;

public class Seat {
	protected int id;
	protected int row;
	protected int column;
	protected boolean occupied;
	protected Ticket ticket;
	
	public Seat(int seat_Id) {
		this.id = seat_Id;
		this.occupied = false;
		ticket = new Ticket();
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public boolean isOccupied() {
		return occupied;
	}
	public void setOccupied(boolean occupied) {
		this.occupied = occupied;
	}
	//public void assign(int ticket_Id) {
	//	this.ticket.setTicketId(ticket_Id);
	//	occupied = true;
	//}
	
	//row and column code
	
}
