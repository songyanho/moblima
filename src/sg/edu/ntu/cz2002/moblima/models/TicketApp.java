package sg.edu.ntu.cz2002.moblima.models;

import java.util.*;

public class TicketApp {
	static TicketManager ticketMgr;
	static ShowtimeManager showtimeMgr;
	static Scanner sc = new Scanner(System.in);
	
	public TicketApp(TicketManager ticketMgr, ShowtimeManager showtimeMgr) {
		TicketApp.ticketMgr = ticketMgr;
		TicketApp.showtimeMgr = showtimeMgr;
	}
	
	public void printView() {
		showtimeMgr.selectShowtime("booking");
		ticketMgr.selectSeat(showtimeMgr.getShowtime());
		ticketMgr.checkout(showtimeMgr.getShowtime());
	}
}
