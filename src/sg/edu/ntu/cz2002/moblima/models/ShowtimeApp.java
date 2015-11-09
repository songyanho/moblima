package sg.edu.ntu.cz2002.moblima.models;

import sg.edu.ntu.cz2002.moblima.view.GeneralView;

public class ShowtimeApp {
	ShowtimeManager showtimeMgr;
	
	public ShowtimeApp(ShowtimeManager showtimeMgr) {
		this.showtimeMgr = showtimeMgr;
	}
	
	public void printView(){
		int choice;
		boolean exit = false;
		String[] menu = {"List showtimes",
				"Search showtimes",
				"Add new showtime",
				"Update showtime",
				"Remove showtime",
		"Back to previous menu"};
		do{
			choice = GeneralView.printMenuAndReturnChoice("Admin Panel > Movie Showtime Management", menu);
			switch(choice){
			case 1:
				showtimeMgr.listShowtimeViewController(false);
				break;
			case 2:
				showtimeMgr.searchShowtimeViewController();
				break;
			case 3:
				showtimeMgr.addNewShowtimeViewController();
				break;
			case 4:
				showtimeMgr.updateShowtimeViewController();
				break;
			case 5:
				showtimeMgr.removeShowtimeViewController();
				break;
			default:
				exit = true;
				break;
			}
		}while(!exit);
	}
	
}
