package sg.edu.ntu.cz2002.moblima;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Scanner;

import sg.edu.ntu.cz2002.moblima.dao.AdminDao;
import sg.edu.ntu.cz2002.moblima.dao.CineplexDao;
import sg.edu.ntu.cz2002.moblima.dao.AdminDao;
import sg.edu.ntu.cz2002.moblima.dao.MovieDao;
import sg.edu.ntu.cz2002.moblima.dao.MovieDao;
import sg.edu.ntu.cz2002.moblima.dao.SettingsDao;
import sg.edu.ntu.cz2002.moblima.dao.SettingsDao;
import sg.edu.ntu.cz2002.moblima.models.Admin;
import sg.edu.ntu.cz2002.moblima.models.Cineplex;
import sg.edu.ntu.cz2002.moblima.models.Admin;
import sg.edu.ntu.cz2002.moblima.models.Movie;
import sg.edu.ntu.cz2002.moblima.models.Movie;

public class MainActivity {
	protected static Scanner sc;
	protected static Data data;

	public static void main(String[] args) {
		sc = new Scanner(System.in);
		for(int i=0; i<68; i++)
			System.out.print("*");
		System.out.println("\n*\t Movie Booking and Listing Management Application\t   *");
		for(int i=0; i<68; i++)
			System.out.print("*");
		data = new Data();

		if (CineplexDao.getLastId() == 0) {
			System.out.print("\nNo cineplex has been setup.\n1. Auto Setup\n2. Manual Setup\n");
			int it = sc.nextInt();
			boolean auto = it == 1? true: false;
			setupCineplex(auto);
		}
		
		do{
			System.out.print("\nWelcome\n For movie-goer, please press enter\nFor admin, please enter \"admin\": ");
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

	private static void movieGoerViewController(){
		int choice, it, cineplex;
		String st;
		boolean exit = false;
		
		//CODE for selecting cineplex and cinema
		System.out.println("\nPlease select a cineplex: ");
		cineplex = selectCineplexAndReturnChoice();
		String[] menus = {"Search movie",
				"List movies and details", 
				"Check seat availability", 
				"Book and purchase ticket", 
				"View booking history", 
				"List the Top 5 ranking",
				"Enter review for movie",
		"Quit"};
		do {
			HashMap<Integer, Movie> movies = MovieDao.getAllInHashMap();
			exit = false;
			choice = printMenuAndReturnChoice("Movie-goer", menus);
			switch (choice) {
			case 1:
				searchMovieViewController(false);
				break;
			case 2:
				if(movies.size() <= 0)
					System.out.println("No movies available");
				else
					listMoviesView(movies, false);
				break;
			case 3:
				System.out.println("Which movie you want to check?");
				it = selectMovie(movies);
				//CODE show empty seat
				System.out.print("Total of empty seats is ");
				break;
			case 4:
				//which movie and seat
				System.out.println("Which movie ticket you want to buy?");
				it = selectMovie(movies);
				System.out.print("Number of tickets: ");
				it = sc.nextInt();
				System.out.println("Choose your seat: ");
				//CODE show seat arrangement and confirmation
				
				break;
			case 5:
				//CODE history
				break;
			case 6:
				//store rating of each movie in an array, sort and print the top 5
				break;
			case 7:
				//validate customer with transaction id to enter his/her review
				break;
			case 8:
				exit = true;
				break;
			default:
				System.out.print("Invalid option! Please try again.");
			}
		} while(!exit);
	}

	private static void adminViewController(){
		boolean loggedIn = loginAdmin();
		boolean logout = false;
		String st;
		int it;
		int choice;
		if(!loggedIn){
			System.out.println("Invalid login. Please try again.");
			return;
		}
		System.out.println("Welcome, "+data.getAdmin().getUsername());
		do{
			String[] menus = {"Movie Listing Management", "Showtime Management", "System configuration", "Logout"};
			choice = printMenuAndReturnChoice("Admin Panel", menus);
			switch(choice){
			case 1:
				movieManagementViewController();
				break;
			case 2:
				break;
			case 3:
				adminSystemConfigurationViewController();
				break;
			default: 
				data.setAdminId(0); 
				logout = true; 
				break;
			}
		}while(!logout);
	}

	private static void movieManagementViewController(){
		String st;
		int it, choice;
		boolean exit = false;
		String[] menu = {"List all movies",
				"Search movies",
				"New movie", 
				"Update movie", 
				"Remove movie",
		"Back to previous menu"};
		do{
			choice = printMenuAndReturnChoice("Admin Panel > Movie Listing Management", menu);
			switch (choice) {
			case 1:
				HashMap<Integer, Movie> movies = MovieDao.findActiveMovie();
				if(movies.size() <= 0)
					System.out.println("No movies available");
				else
					listMoviesView(movies, false);
				break;
			case 2:
				searchMovieViewController(false);
				break;
			case 3:
				addMovieViewController();
				break;
			case 4:
				editMovieViewController();
				break;
			case 5:
				removeMovieViewController();
				break;
			default: exit = true; break;
			}
		}while(!exit);
	}

	private static void listCineplexesView(HashMap<Integer, Cineplex> cineplexes, boolean showId){
		for (Cineplex c: cineplexes.values()) {
			listCineplexView(c, showId);
		}
	}
	
	private static void listCineplexView(Cineplex c, boolean showId){
		if (showId)
			System.out.println("Cineplex ID: " + c.getCineplexId());
		System.out.println("Cineplex name: " + c.getCineplexName());
		System.out.println("Cinema number: " + c.getCinemaNum());
		System.out.print("\n");
	}
	
	private static void listMoviesView(HashMap<Integer, Movie> movies, boolean showId){
		for(Movie m: movies.values()){
			listMovieView(m, showId);
		}
	}

	private static void listMovieView(Movie m, boolean showId){
		if(showId)
			System.out.println("Movie ID: "+m.getId());
		System.out.println("Movie << "+m.getTitle()+" >>");
		System.out.println("\tfrom "+m.getDirector());
		System.out.print("\tstarring ");
		for(String c: m.getCasts())
			System.out.print(c+"  ");
		System.out.print("\n\tDuration in minutes: " + m.getDuration());
		System.out.println("\n\tis "+ m.getStatusString());
		System.out.println("\tSynopsis: "+m.getSynopsis());
		System.out.println("\n");
	}

	private static void editMovieViewController(){
		boolean exit = false;
		int choice, it;
		String st;
		String menu[] = {"Edit movie by ID", "Search movie", "Back to previous menu"};
		do{
			choice = printMenuAndReturnChoice("Admin Panel > Movie Listing Management > Update Movie", menu);
			switch(choice){
			case 1:
				System.out.print("Enter ID of movie that you want to modify: ");
				it = sc.nextInt();
				sc.nextLine();
				Movie m = MovieDao.findById(it);
				if(m == null){
					System.out.println("Invalid Movie ID");
					continue;
				}
				System.out.println("Movie selected to be modified: ");
				listMovieView(m, true);
				System.out.println("Please confirm the record:\nTo edit, type Y\nTo redo, type N\nTo exit, type E");
				System.out.print("Your choice: ");
				st = sc.nextLine();
				if(st.equalsIgnoreCase("Y")){
					updateMovieViewController(m);
				}else if(st.equalsIgnoreCase("N")){
					continue;
				}else{
					exit = true;
				}
				break;
			case 2:
				searchMovieViewController(true);
				break;
			default: exit = true; break;
			}
		}while(!exit);
	}

	private static void updateMovieViewController(Movie m){
		int choice, it;
		String st, st2;
		boolean exit = false, exit2 = false;
		String menu[] = {
				"Edit movie title",
				"Edit movie director",
				"Edit movie casts",
				"Edit movie status",
				"Edit movie synopsis",
				"Edit movie duration",
		"Back to previous menu"};
		do{
			exit = false;
			printMenuAndReturnChoice("Movie", null);
			listMovieView(m, true);
			choice = printMenuAndReturnChoice("Admin Panel > Movie Listing Management > Update Movie: "+m.getTitle(), menu);
			System.out.println(menu[choice-1]);
			switch(choice){
			case 1: 
				exit2 = false;
				do{
					System.out.println("Current movie title: "+m.getTitle());
					System.out.print("Update movie title to: ");
					st = sc.nextLine();
					System.out.println("Movie title: "+m.getTitle()+" -> "+st);
					System.out.println("Please confirm the record:\nTo edit, type Y\nTo redo, type N\nTo exit, type E");
					System.out.print("Your choice: ");
					st2 = sc.nextLine();
					if(st2.equalsIgnoreCase("Y")){
						m.setTitle(st);
						MovieDao.save();
						exit2 = true;
					}else if(st2.equalsIgnoreCase("N")){
						continue;
					}else{
						exit2 = true;
					}
				}while(!exit2);
				break;
			case 2:
				exit2 = false;
				do{
					System.out.println("Current movie director: "+m.getDirector());
					System.out.print("Update movie director to: ");
					st = sc.nextLine();
					System.out.println("Movie director: "+m.getDirector()+" -> "+st);
					System.out.println("Please confirm the record:\nTo edit, type Y\nTo redo, type N\nTo exit, type E");
					System.out.print("Your choice: ");
					st2 = sc.nextLine();
					if(st2.equalsIgnoreCase("Y")){
						m.setDirector(st);
						MovieDao.save();
						exit2 = true;
					}else if(st2.equalsIgnoreCase("N")){
						continue;
					}else{
						exit2 = true;
					}
				}while(!exit2);
				break;
			case 3:
				exit2 = false;
				do{
					exit2 = false;
					boolean exit3 = false;
					System.out.println("Current movie casts: ");
					int i=0;
					for(String c: m.getCasts())
						System.out.println("\t"+(++i)+". "+c);
					String[] menuCast = {"Add cast", "Remove cast", "Back to previous menu"};
					i = printMenuAndReturnChoice("Admin Panel > Movie Listing Management > Update casts of Movie: "+m.getTitle(), menuCast);
					switch(i){
					case 1:
						exit3 = false;
						do{
							System.out.print("New cast name: ");
							String st3 = sc.nextLine();
							System.out.println("Confirm adding new cast with name "+st3);
							System.out.println("Please confirm the record:\nTo add, type Y\nTo redo, type N\nTo exit, type E");
							System.out.print("Your choice: ");
							String st4 = sc.nextLine();
							if(st4.equalsIgnoreCase("Y")){
								m.addCast(st3);
								MovieDao.save();
								System.out.println("New cast "+st3+" is added");
								exit3 = true;
							}else if(st4.equalsIgnoreCase("N")){
								continue;
							}else{
								System.out.println("No changes made");
								exit3 = true;
							}
						}while(!exit3);
						break;
					case 2:
						exit3 = false;
						do{
							System.out.println("Casts with index:");
							i = 0;
							for(String c: m.getCasts())
								System.out.println("\t"+(++i)+". "+c);
							do{
								System.out.print("Remove cast with index: ");
								it = sc.nextInt();
								sc.nextLine();
							}while(it<1 || it > i);
							System.out.println("Confirm removing cast "+m.getCasts().get(i-1));
							System.out.println("Please confirm the record:\nTo add, type Y\nTo redo, type N\nTo exit, type E");
							System.out.print("Your choice: ");
							String st4 = sc.nextLine();
							if(st4.equalsIgnoreCase("Y")){
								m.removeCast(i-1);
								MovieDao.save();
								System.out.println("Cast is removed");
								exit3 = true;
							}else if(st4.equalsIgnoreCase("N")){
								continue;
							}else{
								System.out.println("No changes made");
								exit3 = true;
							}
						}while(!exit3);
						break;
					default: exit2 = true; break;
					}
				}while(!exit2);
				break;
			case 4:
				exit2 = false;
				do{
					System.out.println("Current movie status: "+m.getStatusString());
					do{
						System.out.println("Movie status: ");
						Movie.printMovieStatusChoice();
						System.out.print("Update movie status to: ");
						it = sc.nextInt();
						sc.nextLine();
					}while(it<1 || it>4);
					System.out.println("Movie status: "+m.getStatusString()+" -> "+Movie.getStatusStringFromChoice(it));
					System.out.println("Please confirm the record:\nTo edit, type Y\nTo redo, type N\nTo exit, type E");
					System.out.print("Your choice: ");
					st2 = sc.nextLine();
					if(st2.equalsIgnoreCase("Y")){
						m.setStatusFromChoice(it);
						MovieDao.save();
						exit2 = true;
					}else if(st2.equalsIgnoreCase("N")){
						continue;
					}else{
						exit2 = true;
					}
				}while(!exit2);
				break;
			case 5:
				exit2 = false;
				do{
					System.out.println("Current movie synopsis: \n"+m.getSynopsis());
					System.out.println("Update movie synopsis to: ");
					st = sc.nextLine();
					System.out.println("Movie synopsis: \n"+m.getSynopsis()+"\n->\n"+st);
					System.out.println("Please confirm the record:\nTo edit, type Y\nTo redo, type N\nTo exit, type E");
					System.out.print("Your choice: ");
					st2 = sc.nextLine();
					if(st2.equalsIgnoreCase("Y")){
						m.setSynopsis(st);
						MovieDao.save();
						exit2 = true;
					}else if(st2.equalsIgnoreCase("N")){
						continue;
					}else{
						exit2 = true;
					}
				}while(!exit2);
				break;
			case 6:
				exit2 = false;
				do{
					System.out.println("Current movie duration: " + m.getDuration());
					System.out.println("Update movie duration(in minutes) to: ");
					it = sc.nextInt();
					System.out.println("Movie duration: " + m.getDuration() + " -> " + it);
					System.out.println("Please confirm the record:\nTo edit, type Y\nTo redo, type N\nTo exit, type E");
					System.out.print("Your choice: ");
					st2 = sc.nextLine();
					if(st2.equalsIgnoreCase("Y")){
						m.setDuration(it);
						MovieDao.save();
						exit2 = true;
					}else if(st2.equalsIgnoreCase("N")){
						continue;
					}else
						exit2 = true;
				}while(!exit2);
				break;
			default: exit = true; break;
			}
		}while(!exit);
	}

	private static void removeMovieViewController(){
		int choice, it;
		String st;
		boolean exit = false;
		String menu[] = {"Delete movie by ID", "Search movie", "Back to previous menu"};
		do{
			choice = printMenuAndReturnChoice("Admin Panel > Movie Listing Management > Remove Movie", menu);
			switch(choice){
			case 1: 
				System.out.print("Enter ID of movie that you want to delete: ");
				it = sc.nextInt();
				sc.nextLine();
				Movie m = MovieDao.findById(it);
				if(m == null){
					System.out.println("Invalid Movie ID");
					continue;
				}
				System.out.println("Movie selected to be removed: ");
				listMovieView(m, true);
				System.out.println("Please confirm the record:\nTo remove, type Y\nTo redo, type N\nTo exit, type E");
				System.out.print("Your choice: ");
				st = sc.nextLine();
				if(st.equalsIgnoreCase("Y")){
					m.setStatusFromChoice(4);
					MovieDao.save();
					System.out.println("This movie is set to 'End of Showing'");
					exit = true;
				}else if(st.equalsIgnoreCase("N")){
					continue;
				}else{
					System.out.println("Zero record was removed");
					exit = true;
				}
				break;
			case 2:
				searchMovieViewController(true);
				break;
			default: exit = true; break;
			}
		}while(!exit);
	}

	private static void searchMovieViewController(boolean showId){
		int choice, it;
		String st;
		HashMap<Integer, Movie> results;
		String[] menu = {
				"Movie title", 
				"Director", 
				"Status",
		"Back to previous menu"};
		boolean exit = false;
		do{
			exit = false;
			choice = printMenuAndReturnChoice("Admin Panel > Movie Listing Management > Search movie\nSearch by", menu);
			switch(choice){
			case 1: case 2:
				System.out.print("Enter the keyword here: ");
				st = sc.nextLine();
				if(choice == 1)
					results = MovieDao.findByTitle(st);
				else
					results = MovieDao.findByDirector(st);
				if(results.size() <= 0)
					System.out.println("No matched movies");
				else
					listMoviesView(results, showId);
				break;
			case 3:
				do{
					System.out.println("Search movies with status: ");
					Movie.printMovieStatusChoice();
					it = sc.nextInt();
					sc.nextLine();
				}while(it<1 || it>4);
				results = MovieDao.findByStatus(it);
				if(results.size() <= 0)
					System.out.println("No matched movies");
				else
					listMoviesView(results, showId);
				break;
			default: exit = true; break;
			}
		}while(!exit);
	}

	private static void addMovieViewController(){
		int it;
		String st;
		boolean exitNewMovie;
		do{
			exitNewMovie = false;
			System.out.println("Adding new movie: ");
			Movie movie = new Movie();
			System.out.print("Movie title: ");
			st = sc.nextLine();
			movie.setTitle(st);
			System.out.print("Sypnosis: ");
			st = sc.nextLine();
			movie.setSynopsis(st);
			System.out.print("Director: ");
			st = sc.nextLine();
			movie.setDirector(st);
			ArrayList<String> sat = new ArrayList<String>();
			System.out.println("Casts (Type end to stop): ");
			System.out.print("\t- ");
			String name = sc.nextLine();
			do{
				System.out.print("\t- ");
				name = sc.nextLine();
				if(name.length()>0 && !name.equalsIgnoreCase("end"))
					sat.add(name);
			}while (!name.equalsIgnoreCase("end"));
			movie.setCasts(sat);
			do{
				System.out.println("Movie status: ");
				Movie.printMovieStatusChoice();
				System.out.print("Movie status: ");
				it = sc.nextInt();
				sc.nextLine();
			}while(it<1 || it>4);
			movie.setStatusFromChoice(it);
			System.out.print("Duration in minutes: ");
			it = sc.nextInt();
			sc.nextLine();
			movie.setDuration(it);
			listMovieView(movie, false);
			System.out.println("Please confirm the record:\nTo insert, type Y\nTo redo, type N\nTo exit, type E");
			System.out.print("Your choice: ");
			st = sc.nextLine();
			if(st.equalsIgnoreCase("Y")){
				MovieDao.save(movie);
				System.out.println("One record was added");
				exitNewMovie = true;
			}else if(st.equalsIgnoreCase("N")){
				continue;
			}else{
				System.out.println("Zero record was added");
				exitNewMovie = true;
			}
		}while(!exitNewMovie);
	}

	private static boolean loginAdmin(){
		System.out.println("Please enter your username and password for authorisation");
		System.out.print("Username: ");
		String username = sc.nextLine();
		System.out.print("Password: ");
		String password = sc.nextLine();
		HashMap<Integer, Admin> admins = AdminDao.getAllInHashMap();
		for(Entry<Integer, Admin> e: admins.entrySet()){
			Admin a = e.getValue();
			if(a.isUser(username, password)){
				data.setAdminId(a.getId());
				return true;
			}
		}
		return false;
	}

	private static void adminSystemConfigurationViewController(){
		int choice;
		String[] menus = {"Public Holiday Management", "Back to previous menu"};
		do{
			choice = printMenuAndReturnChoice("Admin Panel > System Configuration", menus);
			switch(choice){
			case 1:
				runAdminHolidayManagement();
				break;
			default: return;
			}
		}while(true);
	}

	private static void runAdminHolidayManagement(){
		String[] menus = {"Show current year calendar", 
				"Show specific calendar of year", 
				"Show list of holidays in current year", 
				"Show list of holidays in specific year",
				"Add new holiday record",
				"Remove existing holiday record",
		"Back to previous menu"};
		int choice, year, month, day, t;
		String st, date;
		boolean exit = false, jump = false;
		Calendar c;
		do{
			jump = false;
			choice = printMenuAndReturnChoice("Admin Panel > System Configuration > Public Holiday Management", menus);
			switch(choice){
			case 1:
				CalendarView.printCalendar(Calendar.getInstance().get(Calendar.YEAR), SettingsDao.getHolidays());
				break;
			case 2:
				System.out.print("Enter the year(eg. "+Calendar.getInstance().get(Calendar.YEAR)+"): ");
				year = sc.nextInt();
				sc.nextLine();
				CalendarView.printCalendar(year, SettingsDao.getHolidays());
				break;
			case 3: 
				t = 0;
				year = Calendar.getInstance().get(Calendar.YEAR);
				System.out.println("List of Holidays in Current Year("+year+")");
				for(String h: SettingsDao.getHolidays()){
					String[] holidayParts = h.split("\\/");
					if(Integer.parseInt(holidayParts[2]) == year)
						System.out.println((++t)+". "+holidayParts[0]+" "+new DateFormatSymbols().getMonths()[Integer.parseInt(holidayParts[1])-1]+" "+year);
				}
				if(t==0)
					System.out.println("No record found");
				break;
			case 4:
				t = 0;
				System.out.print("Enter the year(eg. "+Calendar.getInstance().get(Calendar.YEAR)+"): ");
				year = sc.nextInt();
				sc.nextLine();
				System.out.println("List of Holidays in Year "+year);
				for(String h: SettingsDao.getHolidays()){
					String[] holidayParts = h.split("\\/");
					if(Integer.parseInt(holidayParts[2]) == year)
						System.out.println((++t)+". "+holidayParts[0]+" "+new DateFormatSymbols().getMonths()[Integer.parseInt(holidayParts[1])-1]+" "+year);
				}
				if(t==0)
					System.out.println("No record found");
				break;
			case 5:
				do{
					System.out.println("New Holiday Record");
					do{
						System.out.print("Enter year: ");
						year = sc.nextInt();
						sc.nextLine();
					}while(year<Calendar.getInstance().get(Calendar.YEAR));
					do{
						System.out.print("Enter month: ");
						month = sc.nextInt();
						sc.nextLine();
					}while(month<1 || month>12);
					c = new GregorianCalendar(year, month-1, 1);
					do{
						System.out.print("Enter day: ");
						day = sc.nextInt();
						sc.nextLine();
					}while(day<1 || day>c.getActualMaximum(Calendar.DAY_OF_MONTH));
					date = day+"/"+month+"/"+year;
					System.out.println("Please confirm the record:\n"+date+"\nTo insert, type Y\nTo redo, type N\nTo exit, type E");
					System.out.print("Your choice: ");
					st = sc.nextLine();
					if(st.equalsIgnoreCase("Y")){
						ArrayList<String> th = SettingsDao.getHolidays();
						th.add(date);
						SettingsDao.save();
						System.out.println("One record was added");
						jump = true;
					}else if(st.equalsIgnoreCase("N")){
						continue;
					}else{
						System.out.println("Zero record was added");
						jump = true;
					}
				}while(!jump);
				break;
			case 6:
				t = 0;
				System.out.print("Enter the year(eg. "+Calendar.getInstance().get(Calendar.YEAR)+"): ");
				year = sc.nextInt();
				sc.nextLine();
				do{
					t = 0;
					System.out.println("List of Holidays in Year "+year);
					ArrayList<String> holidays = new ArrayList<String>();
					for(String h: SettingsDao.getHolidays()){
						String[] holidayParts = h.split("\\/");
						if(Integer.parseInt(holidayParts[2]) == year){
							holidays.add(h);
							System.out.println((++t)+". "+holidayParts[0]+" "+new DateFormatSymbols().getMonths()[Integer.parseInt(holidayParts[1])-1]+" "+year);
						}
					}
					if(t==0){
						System.out.println("No record found");
						jump = true;
					}
					System.out.println("\nTo remove record, please enter the row number of record.");
					System.out.println("To exit, please enter 0");
					System.out.print("Delete record at row: ");
					t = sc.nextInt();
					sc.nextLine();
					if(t == 0)
						jump = true;
					if(t > holidays.size()){
						System.out.println("Please enter valid row number (1 to "+holidays.size()+")");
						continue;
					}
					date = holidays.get(t-1);
					System.out.println("Please confirm the record:\n"+date+"\nTo remove, type Y\nTo reselect, type N\nTo exit, type E");
					System.out.print("Your choice: ");
					st = sc.nextLine();
					if(st.equalsIgnoreCase("Y")){
						ArrayList<String>th = SettingsDao.getHolidays();
						boolean success = th.remove(date);
						SettingsDao.save();
						if(success)
							System.out.println("One record was removed");
						else
							System.out.println("Zero record was removed. Record is not found");
						jump = true;
					}else if(st.equalsIgnoreCase("N")){
						continue;
					}else{
						System.out.println("Zero record was removed");
						jump = true;
					}
				}while(!jump);
				break;
			default: exit = true; break;
			}
		}while(!exit);
	}

	private static int printMenuAndReturnChoice(String title, String[] menus){
		System.out.println("\n\n");
		for(int i=0; i<68; i++)
			System.out.print("*");
		System.out.println("\n"+title);
		for(int i=0; i<68; i++)
			System.out.print("-");
		System.out.println("");
		if(menus != null && menus.length > 0){
			for(int i=0; i<menus.length; i++)
				System.out.println((i+1)+". "+menus[i]);
			for(int i=0; i<68; i++)
				System.out.print("*");
			System.out.print("\nChoice: ");
			int choice = sc.nextInt();
			sc.nextLine();
			return choice;
		}
		return 0;
	}
	
	private static int selectMovie(HashMap<Integer, Movie> movies) {
		int i = 0;
		for (Movie m : movies.values()) {
			i++;
			System.out.println(i + ". " + m.getTitle());
		}
		int choice = sc.nextInt();
		sc.nextLine();
		return choice;
	}
	
	private static void setupCineplex(boolean auto) {
		int it = 0;
		String st;
		boolean exit = false;

		if (auto) {
			for (int i = 1; i <= 3; i++) {
				Cineplex c = new Cineplex();
				String name = new StringBuilder().append("Cineplex ").append(Integer.toString(i)).toString();
				c.setCineplexId(i);
				c.setCineplexName(name);
				c.setCinemaNum(3);
				CineplexDao.save(c);
			}
		}
		else {
			System.out.println("Enter number of cineplex: ");
			it = sc.nextInt();
			sc.nextLine();
			int index = 1;
			while(index <= it) {
				do {
					Cineplex c = new Cineplex();
					System.out.print("\n");
					System.out.print("Name for cineplex " + index + ": ");
					st = sc.nextLine();
					System.out.print("Number of cinema: ");
					it = sc.nextInt();
					sc.nextLine();
					c.setCineplexName(st);
					c.setCineplexId(index);
					c.setCinemaNum(it);
					for(int j=0; j<68; j++)
						System.out.print("*");
					System.out.print("\nCineplex " + index + "\nName: " + st);
					System.out.println("\nNumber of cinema: " + it);
					for(int j=0; j<68; j++)
						System.out.print("*");
					System.out.println("\nPlease confirm the record:\nTo insert, type Y\nTo redo, type N\nTo exit, type E");
					System.out.print("Your choice: ");
					st = sc.nextLine();
					if(st.equalsIgnoreCase("Y")){
						CineplexDao.save(c);
						System.out.println("One record was added");
						exit = true;
						index++;
					}else if(st.equalsIgnoreCase("N")){
						continue;
					}else{
						System.out.println("Zero record was added");
						exit = true;
						index++;
					}
				} while (!exit);
			}
		}
	}
	
	private static int selectCineplexAndReturnChoice() {
		int choice;
		HashMap<Integer, Cineplex> c = CineplexDao.getAllInHashMap();
		listCineplexesView(c, true);
		System.out.print("Select which cineplex to enter: ");
		choice = sc.nextInt();
		return choice;
	}

}
