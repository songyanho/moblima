package sg.edu.ntu.cz2002.moblima.models;

import java.math.RoundingMode;
import java.text.*;
import java.util.*;
import sg.edu.ntu.cz2002.moblima.dao.*;
import sg.edu.ntu.cz2002.moblima.view.GeneralView;

public class TicketApp {
	static TicketManager ticketMgr = null;
	static ShowtimeManager showtimeMgr = null;
	static Scanner sc = new Scanner(System.in);
	
	public TicketApp(TicketManager ticketMgr, ShowtimeManager showtimeMgr) {
		this.ticketMgr = ticketMgr;
		this.showtimeMgr = showtimeMgr;
	}
	
	public void printView() {
		SimpleDateFormat formatter = new SimpleDateFormat("h:mm a, EEE, MMM d, yyyy");
		DecimalFormat deciformat = new DecimalFormat("#0.00");
		deciformat.setRoundingMode(RoundingMode.HALF_UP);
		int i = 0, choice;
		boolean exit = false;

		do {
			System.out.print("\n");
			ticketMgr.showtimeList = showtimeMgr.listShowtimeForBookingViewController();		
			String[] menu = {"Enter showtime ID", "Back to cineplex and movie selection", "Back to main menu"};
			choice = GeneralView.printMenuAndReturnChoice("Movie-goer Panel > Book ticket", menu);
			switch (choice) {
			case 1:
				if (ticketMgr.showtimeList.isEmpty()) {
					System.out.println("\nSorry, no showtime is available for this movie in this period of time");
					continue;
				}
				exit = true;
				break;
			case 2:
				continue;
			default:
				return;
			}
		} while (!exit);

		String[] idList = new String[ticketMgr.showtimeList.size()+1];
		for (i = 0; i < ticketMgr.showtimeList.size(); i++) {
			Calendar calendar = ShowtimeDao.findById(ticketMgr.showtimeList.get(i)).getDate();
			idList[i] = "Showtime: " + formatter.format(calendar.getTime());
		}
		idList[i] = "Back to main menu";
		choice = GeneralView.printMenuAndReturnChoice("Movie-goer Panel > Book Ticket > Enter Showtime ID", idList);
		if (choice == idList.length)
			return;
		showtimeMgr.showtime = ShowtimeDao.findById(ticketMgr.showtimeList.get(choice-1));
		ticketMgr.selectSeat(showtimeMgr.showtime);
		ticketMgr.checkout(showtimeMgr.showtime);
	}
}
