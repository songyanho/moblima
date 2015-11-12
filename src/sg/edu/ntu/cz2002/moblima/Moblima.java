package sg.edu.ntu.cz2002.moblima;

import java.util.HashMap;
import java.util.Scanner;

import sg.edu.ntu.cz2002.moblima.dao.*;
import sg.edu.ntu.cz2002.moblima.models.*;
import sg.edu.ntu.cz2002.moblima.view.GeneralView;

public class Moblima {
	protected static Scanner sc;
	protected static Data data;

	/**
	 * Executable main function for Movie Booking and Listing Management Application
	 * @param args
	 */
	public static void main(String[] args) {
		sc = new Scanner(System.in);
		for(int i=0; i<68; i++)
			System.out.print("*");
		System.out.println("\n*\t Movie Booking and Listing Management Application\t   *");
		for(int i=0; i<68; i++)
			System.out.print("*");
		data = new Data();

		do{
			System.out.print("\nWelcome\nFor movie-goer, please press enter\nFor admin, please enter \"admin\": ");
			String action = sc.nextLine();
			if(action.equalsIgnoreCase("exit")){
				break;
			}else if(action.equalsIgnoreCase("admin")){
				adminViewController();
			}else{
				movieGoerViewController();
			}

		}while(true);
	}

	/**
	 * Movie Goer User Interface to perform following actions:
	 * Movie Searching and Listing,
	 * Ticket Booking and Query,
	 * Reviews Overview and Submission
	 */
	private static void movieGoerViewController(){
		int choice, it;
		boolean exit = false;
		MovieManager movieMgr = new MovieManager();
		String[] menus = {"Search movie",
				"List movies and details", 
				"Check seat availability", 
				"Book and purchase ticket", 
				"View booking history", 
				"Enter review for movie",
				"Show past reviews",
				"Quit"};
		do {
			HashMap<Integer, Movie> movies = MovieDao.findActiveMovie();
			TicketManager ticketMgr = new TicketManager();
			ShowtimeManager showtimeMgr = new ShowtimeManager();
			exit = false;
			choice = GeneralView.printMenuAndReturnChoice("Movie-goer Panel", menus);
			switch (choice) {
			case 1:
				movieMgr.searchMovieViewController(false, "visitor");
				break;
			case 2:
				if(movies.size() <= 0)
					System.out.println("No movies available");
				else
					movieMgr.listMoviesView(movies, false);
				break;
			case 3:
				showtimeMgr.selectShowtime("seat");
				showtimeMgr.showSeatAvailability(showtimeMgr.getShowtime());
				break;
			case 4:
				showtimeMgr.selectShowtime("booking");
				if (showtimeMgr.getShowtime() != null) {
					ticketMgr.selectSeat(showtimeMgr.getShowtime());
					ticketMgr.checkout(showtimeMgr.getShowtime());
				}
				break;
			case 5:
				ticketMgr.viewBookingHistory();
				break;
			case 6:
				movieMgr.addReviewViewController(movies);
				break;
			case 7:
				it = movieMgr.selectMovie(movies);
				movieMgr.showReviewsByMovie(it);
				break;
			default:
				exit = true;
			}
		} while(!exit);
	}

	/**
	 * Admin User Interface to perform following actions:
	 * Movie Management,
	 * Showtime Management,
	 * System Configuration
	 */
	private static void adminViewController(){
		CineplexManager cineplexMgr = new CineplexManager();
		CineplexApp cineplexApp = new CineplexApp(cineplexMgr);
		MovieManager movieMgr = new MovieManager();
		MovieApp movieApp = new MovieApp(movieMgr);
		ShowtimeManager showtimeMgr = new ShowtimeManager();
		ShowtimeApp showtimeApp = new ShowtimeApp(showtimeMgr);
		boolean loggedIn = cineplexMgr.loginAdmin(data);
		boolean logout = false;
		int choice;
		if(!loggedIn){
			System.out.println("Invalid login. Please try again.");
			return;
		}
		System.out.println("Welcome, "+data.getAdmin().getUsername());
		do{
			String[] menus = {"Movie Listing Management", "Showtime Management", "Cineplex Management", "System configuration", "List top 5 ranking", "Logout"};
			choice = GeneralView.printMenuAndReturnChoice("Admin Panel", menus);
			switch(choice){
			case 1:
				movieApp.printView();
				break;
			case 2:
				showtimeApp.printView();
				break;
			case 3:
				cineplexMgr.runAdminCineplexManagement();
				break;
			case 4:
				cineplexApp.printView();
				break;
			case 5:
				cineplexApp.listRanking();
				break;
			default: 
				data.setAdminId(0); 
				logout = true; 
				break;
			}
		}while(!logout);
	}
}