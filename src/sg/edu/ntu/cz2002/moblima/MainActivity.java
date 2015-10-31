package sg.edu.ntu.cz2002.moblima;

import java.math.RoundingMode;
import java.text.DateFormatSymbols;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.TimeZone;

import sg.edu.ntu.cz2002.moblima.dao.*;
import sg.edu.ntu.cz2002.moblima.models.*;
import sg.edu.ntu.cz2002.moblima.models.Showtime.MovieType;

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
		
		if (CinemaDao.getLastId() == 0) {
			int loop = CineplexDao.getLastId();		
			Random rand = new Random();
			for (int i = 0; i < loop; i++) {
				for (int j = 0; j < 3; j++) {
					Cinema c = new Cinema();
					String name = "Cinema " + (j+1);
					c.setName(name);
					ArrayList<String> seat = new ArrayList<String>();
					c.setSeat(seat);
					int r = rand.nextInt(4);
					c.setCinemaClassFromChoice(r);
					c.setCineplexId(i);
					c.setNumEmptySeat(300);
					CinemaDao.save(c);
				}
			}
		}
		
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

	private static void movieGoerViewController(){
		int choice, it;
		boolean exit = false;
		String[] menus = {"Search movie",
				"List movies and details", 
				"Check seat availability", 
				"Book and purchase ticket", 
				"View booking history", 
				"Enter review for movie",
				"Show past reviews",
				"Quit"};
		do {
			HashMap<Integer, Movie> movies = MovieDao.getAllInHashMap();
			exit = false;
			choice = printMenuAndReturnChoice("Movie-goer Panel", menus);
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
				it = selectMovie(movies);
				//CODE show empty seat
				System.out.print("Total of empty seats is ");
				break;
			case 4:
				//which movie and seat
				bookTicketViewController();
				break;
			case 5:
				//CODE history
				break;
			case 6:
				addReviewViewController(movies);
				break;
			case 7:
				//validate customer with transaction id to enter his/her review
				it = selectMovie(movies);
				showReviewsByMovie(it);
				break;
			default:
				exit = true;
			}
		} while(!exit);
	}

	private static void adminViewController(){
		boolean loggedIn = loginAdmin();
		boolean logout = false;
		int choice;
		if(!loggedIn){
			System.out.println("Invalid login. Please try again.");
			return;
		}
		System.out.println("Welcome, "+data.getAdmin().getUsername());
		do{
			String[] menus = {"Movie Listing Management", "Showtime Management", "System configuration", "List top 5 ranking", "Logout"};
			choice = printMenuAndReturnChoice("Admin Panel", menus);
			switch(choice){
			case 1:
				movieManagementViewController();
				break;
			case 2:
				movieShowtimeViewController();
				break;
			case 3:
				adminSystemConfigurationViewController();
				break;
			case 4:
				listRanking();
				break;
			default: 
				data.setAdminId(0); 
				logout = true; 
				break;
			}
		}while(!logout);
	}

	private static void movieShowtimeViewController(){
		int choice;
		boolean exit = false;
		String[] menu = {"List showtimes",
				"Search showtimes",
				"Add new showtime",
				"Update showtime",
				"Remove showtime",
		"Back to previous menu"};
		do{
			choice = printMenuAndReturnChoice("Admin Panel > Movie Showtime Management", menu);
			switch(choice){
			case 1:
				listShowtimeViewController(false);
				break;
			case 2:
				searchShowtimeViewController();
				break;
			case 3:
				addNewShowtimeViewController();
				break;
			case 4:
				updateShowtimeViewController();
				break;
			case 5:
				removeShowtimeViewController();
				break;
			default:
				exit = true;
				break;
			}
		}while(!exit);
	}
	
	private static void updateShowtimeViewController(){
		String st;
		int it,choice;
		boolean exit = false;
		String menu[] = {"Update showtime with ID", "Search showtime ID" ,"Back to previous menu"};
		do{
			exit = false;
			choice = printMenuAndReturnChoice("Admin Panel > Movie Showtime Management > Update showtimes", menu);
			switch(choice){
			case 1:
				System.out.print("Showtime ID: ");
				it = sc.nextInt();
				sc.nextLine();
				Showtime s = ShowtimeDao.findById(it);
				if(s == null){
					System.out.println("Invalid Showtime ID");
					continue;
				}
				printShowtimeInformationView(s);
				boolean exit2 = false;
				String menu2[] = {"Change movie", "Change cinema" ,"Back to previous menu"};
				do{
					exit2 = false;
					choice = printMenuAndReturnChoice("Admin Panel > Movie Showtime Management > Update showtimes ID: "+s.getId(), menu2);
					switch(choice){
					case 1:
						boolean exit3 = false;
						Movie selectedMovie = new Movie();
						HashMap<Integer, Movie> movies = MovieDao.findActiveMovie();
						for(Movie movie: movies.values()){
							System.out.println("ID: "+movie.getId()+" - "+movie.getTitle()+" <<"+movie.getRatingString()+">>");
						}
						do{
							exit3 = false;
							System.out.println("To exit, enter 0");
							System.out.print("Your choice: Movie ID -> ");
							it = sc.nextInt();
							sc.nextLine();
							if(it <= 0){
								exit2 = true;
								break;
							}
							if(movies.keySet().contains(it)){
								selectedMovie = MovieDao.findById(it);
								exit3 = true;
							}else{
								System.out.println("Invalid movie ID "+it);
								System.out.println("To enter movie ID, press enter.");
								System.out.println("To exit, enter X");
								System.out.print("Your choice: ");
								st = sc.nextLine();
								if(st.equalsIgnoreCase("x"))
									return;
							}
						}while(!exit3);
						System.out.println("Please confirm the record:\nTo change movie to "+selectedMovie.getTitle()+", type Y\nTo exit, type E");
						System.out.print("Your choice: ");
						st = sc.nextLine();
						if(st.equalsIgnoreCase("Y")){
							Calendar tt = s.getDate();
							Calendar today = Calendar.getInstance();
							today.setTimeZone(TimeZone.getTimeZone("Asia/Singapore"));
							int weekOffset = tt.get(Calendar.WEEK_OF_YEAR)-today.get(Calendar.WEEK_OF_YEAR);
							int timeslot[][][] = cinemaTimetableView(s.getCinema(), weekOffset, s, false);
							ArrayList<Calendar> availableTimeSlot = CalendarView.timeslot(true, timeslot[tt.get(Calendar.DAY_OF_WEEK)-1], selectedMovie.getDuration(), tt.get(Calendar.DAY_OF_WEEK)-1, weekOffset);
							String[] availableShowtimes = CalendarView.timeslotInString(availableTimeSlot, true);
							boolean pass = false;
							for(String sss: availableShowtimes){
								if(sss.equals(tt.get(Calendar.HOUR_OF_DAY)+":"+(tt.get(Calendar.MINUTE)==0?"00":tt.get(Calendar.MINUTE)))){
									pass = true;
									break;
								}
							}
							if(pass){
								s.setMovieId(selectedMovie.getId());
								ShowtimeDao.save();
								System.out.println("Showtime with ID: "+s.getId()+" was updated.");
							}else{
								System.out.println("The duration of selected movie will overlap with the next showing movie.");
								System.out.println("Showtime with ID: "+s.getId()+" was not updated.");
							}
						}
						break;
					case 2:
						
						break;
					default:
						exit2 = true;
						break;
					}
				}while(!exit2);
				break;
			case 2:
				searchShowtimeViewController();
				break;
			default:
				exit = true;
				break;
			}
		}while(!exit);
	}
	
	private static void removeShowtimeViewController(){
		String st;
		int it,choice;
		boolean exit = false;
		String menu[] = {"Remove showtime with ID", "Search showtime ID" ,"Back to previous menu"};
		do{
			choice = printMenuAndReturnChoice("Admin Panel > Movie Showtime Management > Remove showtimes", menu);
			switch(choice){
			case 1:
				System.out.print("Showtime ID: ");
				it = sc.nextInt();
				sc.nextLine();
				Showtime s = ShowtimeDao.findById(it);
				if(s == null){
					System.out.println("Invalid Showtime ID");
					continue;
				}
				printShowtimeInformationView(s);
				System.out.println("Removing showtime will cause all the ticket sold be void.\nPlease confirm the record:\nTo delete, type Y\nTo exit, type E");
				System.out.print("Your choice: ");
				st = sc.nextLine();
				if(st.equalsIgnoreCase("Y")){
					ShowtimeDao.deleteShowtimeWithId(s.getId());
					System.out.println("Showtime with ID: "+it+" was removed.");
				}
				break;
			case 2:
				searchShowtimeViewController();
				break;
			default:
				exit = true;
				break;
			}
		}while(!exit);
	}
	
	private static void printShowtimeInformationView(Showtime s){
		SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm aaa");
		System.out.println("\n\nShowtime ID: "+s.getId());
		System.out.println("Movie: (ID "+s.getMovieId()+") "+s.getMovie().getTitle());
		System.out.println("Cineplex: "+s.getCineplex().getCineplexName()+" <"+s.getCinema().getName()+">");
		System.out.println("Showtime: "+df.format(s.getDate().getTime()));
	}

	private static void searchShowtimeViewController(){
		int choice;
		boolean exit = false;
		String menu[] = {"Search by cineplex", "Search by movie" ,"Back to previous menu"};
		do{
			choice = printMenuAndReturnChoice("Admin Panel > Movie Showtime Management > Search showtimes", menu);
			switch(choice){
			case 1:
				listShowtimeViewController(true);
				break;
			case 2:
				listShowtimeByMovieViewController(true);
				break;
			default:
				exit = true;
				break;
			}
		}while(!exit);
	}
	
	private static void listShowtimeByMovieViewController(boolean showId){
		int it, choice;
		int weekOffset = 0;
		ArrayList<Movie> movies = new ArrayList<Movie>();
		String[] movieMenu = new String[MovieDao.getAllInHashMap().size()+1];
		it = 0;
		for(Movie c: MovieDao.getAllInHashMap().values()){
			movieMenu[it++] = c.getTitle();
			movies.add(c);
		}
		movieMenu[it] = "Back to previous menu";
		choice = printMenuAndReturnChoice("Admin Panel > Movie Showtime Management > Search Showtimes \n> Select Movie", movieMenu);
		if(choice <= 0 || choice >= movieMenu.length)
			return;
		Movie selectedMovie = movies.get(choice-1);
		System.out.println("You have selected <<" + selectedMovie.getTitle()+">>");

		String[] weekMenu = {"Last 2 week", "Last week", "Current week", "Next week", "Next 2 week", "Back to previous menu"};
		choice = printMenuAndReturnChoice("Admin Panel > Movie Showtime Management > Select week", weekMenu);
		if(choice<=0 || choice >= weekMenu.length)
			return;
		weekOffset = choice-3;

		Calendar c = new GregorianCalendar();
		c.setTimeZone(TimeZone.getTimeZone("Asia/Singapore"));
		c.add(Calendar.WEEK_OF_YEAR, weekOffset);
		SimpleDateFormat df = new SimpleDateFormat("EEE, MMM dd, YYYY");
		for(int i=1; i<=7; i++){
			c.set(Calendar.DAY_OF_WEEK, i);
			HashMap<Cineplex, ArrayList<Showtime>> sortedShowtimes = ShowtimeDao.getAllOnDate(c, selectedMovie);
			if(sortedShowtimes.size() <= 0){
				for (int j = 0; j < 68; j++)
					System.out.print("-");
				System.out.println("\nDate: "+df.format(c.getTime()));
				for (int j = 0; j < 68; j++)
					System.out.print("-");
				System.out.println("\nNo showtimes.\n\n");
				continue;
			}
			for(Cineplex selectedCineplex: sortedShowtimes.keySet()){
				showtimeOfTheDayView(selectedCineplex, sortedShowtimes.get(selectedCineplex), selectedMovie, df.format(c.getTime()), showId);
			}
		}
	}

	private static void listShowtimeViewController(boolean showId){
		int it, choice;
		int weekOffset = 0;
		ArrayList<Cineplex> cineplexes = new ArrayList<Cineplex>();
		String[] cineplexMenu = new String[CineplexDao.getAllInHashMap().size()+1];
		it = 0;
		for(Cineplex c: CineplexDao.getAllInHashMap().values()){
			cineplexMenu[it++] = c.getCineplexName();
			cineplexes.add(c);
		}
		cineplexMenu[it] = "Back to previous menu";
		choice = printMenuAndReturnChoice("Admin Panel > Movie Showtime Management > "+(showId?"Search showtimes \n":"List ")+"> Select Cineplex", cineplexMenu);
		if(choice <= 0 || choice >= cineplexes.size())
			return;
		Cineplex selectedCineplex = cineplexes.get(choice-1);
		System.out.println("You have selected <<" + selectedCineplex.getCineplexName()+">>");

		String[] weekMenu = {"Last 2 week", "Last week", "Current week", "Next week", "Next 2 week", "Back to previous menu"};
		choice = printMenuAndReturnChoice("Admin Panel > Movie Showtime Management > Select week", weekMenu);
		if(choice<=0 || choice >= weekMenu.length)
			return;
		weekOffset = choice-3;
		
		Calendar c = new GregorianCalendar();
		c.setTimeZone(TimeZone.getTimeZone("Asia/Singapore"));
		c.add(Calendar.WEEK_OF_YEAR, weekOffset);
		SimpleDateFormat df = new SimpleDateFormat("EEE, MMM dd, YYYY");
		for(int i=1; i<=7; i++){
			c.set(Calendar.DAY_OF_WEEK, i);
			HashMap<Movie, ArrayList<Showtime>> sortedShowtimes = ShowtimeDao.getAllOnDate(c, selectedCineplex);
			showtimeOfTheDayView(selectedCineplex, sortedShowtimes, df.format(c.getTime()), showId);
		}
	}
	
	private static void showtimeOfTheDayView(Cineplex cineplex, ArrayList<Showtime> st, Movie movie, String date, boolean showId){
		System.out.println("\n\n");
		for(int i=0; i<68; i++)
			System.out.print("-");
		System.out.println("\nCineplex: "+cineplex.getCineplexName());
		System.out.println("Date: "+date);
		for(int i=0; i<68; i++)
			System.out.print("-");
		System.out.println("");
		if(st.size()<=0){
			System.out.println("No movies available in "+cineplex.getCineplexName());
			return;
		}
		SimpleDateFormat df = new SimpleDateFormat("hh:mm aa");
		System.out.println(movie.getTitle());
		for(Showtime s: st){
			if(!showId)
				System.out.print("   "+df.format(s.getDate().getTime()));
			else
				System.out.println("\t(Showtime ID: "+s.getId()+") "+df.format(s.getDate().getTime()));
		}
		System.out.println("\n");
	}

	private static void showtimeOfTheDayView(Cineplex cineplex, HashMap<Movie, ArrayList<Showtime>> showtimes, String date, boolean showId){
		System.out.println("\n\n");
		for(int i=0; i<68; i++)
			System.out.print("-");
		System.out.println("\nCineplex: "+cineplex.getCineplexName());
		System.out.println("Date: "+date);
		for(int i=0; i<68; i++)
			System.out.print("-");
		System.out.println("");
		if(showtimes.keySet().size()<=0){
			System.out.println("No movies available in "+cineplex.getCineplexName());
			return;
		}
		SimpleDateFormat df = new SimpleDateFormat("hh:mm aa");
		for(Movie m: showtimes.keySet()){
			System.out.println(m.getTitle());
			ArrayList<Showtime> st = showtimes.get(m);
			for(Showtime s: st){
				if(!showId)
					System.out.print("   "+df.format(s.getDate().getTime()));
				else
					System.out.println("\t(Showtime ID: "+s.getId()+") "+df.format(s.getDate().getTime()));
			}
			System.out.println("\n");
		}
	}

	private static void addNewShowtimeViewController(){
		String st;
		int it, choice;
		boolean exit = false;
		int weekOffset = 0;
		printMenuAndReturnChoice("Admin Panel > Movie Showtime Management > Select Movie", null);
		System.out.println("List of movies");
		HashMap<Integer, Movie> movies = MovieDao.findActiveMovie();
		for(Movie movie: movies.values()){
			System.out.println("ID: "+movie.getId()+" - "+movie.getTitle()+" <<"+movie.getRatingString()+">>");
		}
		do{
			exit = false;
			System.out.print("Your choice: Movie ID -> ");
			it = sc.nextInt();
			sc.nextLine();
			if(movies.keySet().contains(it)){
				exit = true;
			}else{
				System.out.println("Invalid movie ID "+it);
				System.out.println("To enter movie ID, press enter.");
				System.out.println("To exit, enter X");
				System.out.print("Your choice: ");
				st = sc.nextLine();
				if(st.equalsIgnoreCase("x"))
					return;
			}
		}while(!exit);
		Movie selectedMovie = movies.get(it);
		System.out.println("You have selected <<" + selectedMovie.getTitle()+">>");
		do{
			ArrayList<Cineplex> cineplexes = new ArrayList<Cineplex>();
			String[] cineplexMenu = new String[CineplexDao.getAllInHashMap().size()+1];
			it = 0;
			for(Cineplex c: CineplexDao.getAllInHashMap().values()){
				cineplexMenu[it++] = c.getCineplexName();
				cineplexes.add(c);
			}
			cineplexMenu[it] = "Back to previous menu";
			choice = printMenuAndReturnChoice("Admin Panel > Movie Showtime Management > New > Select Cineplex", cineplexMenu);
			if(choice <= 0 || choice >= cineplexes.size())
				break;
			Cineplex selectedCineplex = cineplexes.get(choice-1);
			System.out.println("You have selected <<" + selectedCineplex.getCineplexName()+">>");
			do{
				HashMap<Integer, Cinema> cinemasHashMap = selectedCineplex.getCinemas();
				String[] cinemaMenu = new String[cinemasHashMap.size()+1];
				ArrayList<Cinema> cinemas = new ArrayList<Cinema>();
				it=0;
				for(Cinema ci: cinemasHashMap.values()){
					cinemaMenu[it++] = ci.getName() + " (" + ci.getCinemaClass() + ")";
					cinemas.add(ci);
				}
				cinemaMenu[it] = "Back to previous menu";
				choice = printMenuAndReturnChoice("Admin Panel > Movie Showtime Management > New > Select Cinema", cinemaMenu);
				if(choice<=0 || choice >= cinemas.size())
					break;
				Cinema selectedCinema = cinemas.get(choice-1);
				System.out.println("You have selected <<" + selectedCinema.getName()+">>");

				String[] weekMenu = {"Current week", "Next week", "Next 2 week", "Back to previous menu"};
				choice = printMenuAndReturnChoice("Admin Panel > Movie Showtime Management > New > Select week", weekMenu);
				if(choice<=0 || choice >= weekMenu.length)
					break;
				weekOffset = choice-1;
				int timeslot[][][] = cinemaTimetableView(selectedCinema, weekOffset, null, true);

				String[] dayMenu = CalendarView.dayOfWeek(true, weekOffset, false);
				choice = printMenuAndReturnChoice("Admin Panel > Movie Showtime Management > New > Select Showtime Day", dayMenu);
				if(choice<=0 || choice >= dayMenu.length)
					break;
				choice-=1;
				int dayOfWeek = dayMenu[choice].equalsIgnoreCase("sunday")?0: 
					dayMenu[choice].equalsIgnoreCase("monday")?1:
						dayMenu[choice].equalsIgnoreCase("tuesday")?2:
							dayMenu[choice].equalsIgnoreCase("wednesday")?3:
								dayMenu[choice].equalsIgnoreCase("thursday")?4:
									dayMenu[choice].equalsIgnoreCase("friday")?5:6;
				ArrayList<Calendar> availableTimeSlot = CalendarView.timeslot(true, timeslot[dayOfWeek], selectedMovie.getDuration(), dayOfWeek, weekOffset);
				String[] timeMenu = CalendarView.timeslotInString(availableTimeSlot, true);
				choice = printMenuAndReturnChoice("Admin Panel > Movie Showtime Management > New > Select Showtime Time", timeMenu);
				if(choice<=0 || choice >= timeMenu.length)
					break;
				Showtime ns = new Showtime();
				ns.setCinemaId(selectedCinema.getId());
				ns.setType(MovieType.BLOCKBUSTER);
				ns.setMovieId(selectedMovie.getId());
				ns.setCineplexId(selectedCineplex.getId());
				ns.setDate((Calendar) availableTimeSlot.get(choice-1).clone());
				ShowtimeDao.add(ns);
				System.out.println("Showtime saved");
			}while(true);
		}while(true);
	}

	private static int[][][] cinemaTimetableView(Cinema c, int weekOffset, Showtime excludeShowtime, boolean show){
		SimpleDateFormat df = new SimpleDateFormat("MMM dd,yyyy");
		ArrayList<Calendar> calendars = CalendarView.getWeekCalendars(weekOffset);
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeZone(TimeZone.getTimeZone("Asia/Singapore"));
		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.MINUTE, 1);
		if(show){
			System.out.print("\n\nTimetable of <"+c.getName()+"> from "+df.format(calendars.get(0).getTime())+" to "+df.format(calendars.get(6).getTime()));
			System.out.println("\n\n|  TIME  | Sun | Mon | Tue | Wed | Thu | Fri | Sat |");
		}
		int timeslot[][][] = new int[7][15][2];
		for(int i=0; i<7; i++){
			for(int j=0; j<15; j++){
				timeslot[i][j][0] = 0;
				timeslot[i][j][1] = 0;
			}
			if(!CalendarView.sameDay(calendar, calendars.get(i)) && calendar.compareTo(calendars.get(i))>0){
				for(int j=0; j<15; j++){
					timeslot[i][j][0] = -1;
					timeslot[i][j][1] = -1;
				}
				continue;
			}
			ArrayList<Showtime> currentShowTimes = ShowtimeDao.getAllOnDate(calendars.get(i), c);
			for(Showtime s: currentShowTimes){
				if(excludeShowtime != null && s.getId() == excludeShowtime.getId())
					continue;
				Calendar temp = (Calendar) s.getDate().clone();
				int startHour = temp.get(Calendar.HOUR_OF_DAY);
				int startMinute = temp.get(Calendar.MINUTE) >= 0 && temp.get(Calendar.MINUTE) < 30 ? 0 : 30;
				temp.add(Calendar.MINUTE, s.getMovie().getDuration());
				int endHour = temp.get(Calendar.HOUR_OF_DAY);
				int endMinute = temp.get(Calendar.MINUTE) >= 0 && s.getDate().get(Calendar.MINUTE) < 30 ? 0 : 30;
				if(endHour < startHour) endHour=24;
				for(int h=startHour; h<=endHour; h++){
					if(h==startHour){
						if(startMinute == 0){
							if(timeslot[i][h-10][0] == 1)
								System.out.println("Overlap liao"+s.getId());
							timeslot[i][h-10][0] = 1;
						}
						if(timeslot[i][h-10][1] == 1)
							System.out.println("Overlap liao"+s.getId());
						timeslot[i][h-10][1] = 1;
					}else if(h==endHour){
						if(timeslot[i][h-10][0] == 1)
							System.out.println("Overlap liao"+s.getId());
						timeslot[i][h-10][0] = 1;
						if(endMinute == 30){
							if(timeslot[i][h-10][1] == 1)
								System.out.println("Overlap liao"+s.getId());
							timeslot[i][h-10][1] = 1;
						}
					}else{
						if(timeslot[i][h-10][0] == 1)
							System.out.println("Overlap liao"+s.getId());
						timeslot[i][h-10][0] = 1;
						if(timeslot[i][h-10][1] == 1)
							System.out.println("Overlap liao"+s.getId());
						timeslot[i][h-10][1] = 1;
					}
				}
			}
		}
		if(show){
			for(int h=10; h<=24; h++){
				for(int m=0; m<=30; m+=30){
					System.out.print("|  "+h+":"+(m==0?"00":m));
					System.out.print(" |");
					for(int i=0; i<7; i++){
						if(timeslot[i][h-10][m/30] == -1)
							System.out.print("  -  |");
						else if(timeslot[i][h-10][m/30] == 0)
							System.out.print("     |");
						else
							System.out.print("  X  |");
					}
					System.out.print("\n");
				}
			}
		}
		return timeslot;
	}

	private static void movieManagementViewController(){
		int choice;
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
			System.out.println("Cineplex ID: " + c.getId());
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
		System.out.println("Movie << "+m.getTitle()+" >> ("+m.getRatingString()+")");
		System.out.println("\tfrom "+m.getDirector());
		System.out.print("\tstarring: ");
		for(String c: m.getCasts())
			System.out.print(c+"  ");
		System.out.print("\n\tDuration in minutes: " + m.getDuration());
		System.out.println("\n\tis "+ m.getStatusString());
		System.out.println("\tSynopsis: "+m.getSynopsis());
		double rating = 0;
		DecimalFormat df = new DecimalFormat("#.#");
		rating = calculateOverallRating(m.getId());
		System.out.print("\tReviewers' overall rating: ");
		if (rating >= 1.0 && rating <= 5.0)
			System.out.print(df.format(rating));
		else
			System.out.print("NA");
		System.out.print("\n\n");
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
		int it, check = 0;
		String st, name;
		boolean exitNewMovie;
		do{
			exitNewMovie = false;
			System.out.println("\nAdding new movie: ");
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
			System.out.println("Casts (At least 2, type end to stop): ");
			do{
				System.out.print("\t- ");
				name = sc.nextLine();
				if(name.length()>0 && !name.equalsIgnoreCase("end")) {
					sat.add(name);
					check++;
				}
			}while (!name.equalsIgnoreCase("end") || check < 2);
			movie.setCasts(sat);
			do{
				System.out.println("Movie status: ");
				Movie.printMovieStatusChoice();
				System.out.print("Movie status: ");
				it = sc.nextInt();
				sc.nextLine();
			}while(it<1 || it>4);
			movie.setStatusFromChoice(it);
			do{
				System.out.println("Movie rating: ");
				Movie.printMovieRatingChoice();
				System.out.print("Movie rating: ");
				it = sc.nextInt();
				sc.nextLine();
			}while(it<1 || it>6);
			movie.setRatingFromChoice(it);
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
					c.setTimeZone(TimeZone.getTimeZone("Asia/Singapore"));
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
		int choice;
		System.out.println("\nWhich movie you are interested in?");
		for (Movie m : movies.values()) {
			i++;
			System.out.println(i + ". " + m.getTitle());
		}
		do {
			choice = sc.nextInt();
		} while (choice < 0 || choice > i);
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
				c.setId(i);
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
					c.setId(index);
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
		System.out.print("Select cineplex: ");
		choice = sc.nextInt();
		return choice;
	}

	//incomplete
	private static void showSeatsArrangement(int cinemaId, int movieId) {
		int row = 15;
		int column = 22;
		int i, j;
		char c = 'A';
		int ascii = (int) c - 2;
		int digit;
		// System.out.println("Showing seats arrangement for cinema " + CinemaDao.findById(cinemaId).getCinemaName() + " in " + CineplexDao.findById(cineplexId).getCineplexName());
		System.out.print("\n");
		for (i = 0; i < row; i++) {
			for (j = 0; j < column; j++) {
				//first row
				if (i == 0) {
					if (j == 0 || j == 1 || j == column - 2 || j == column - 1)
						System.out.print("   ");
					else {
						digit = j-1;
						if (digit > 9)
							System.out.print(" "+digit+"  ");
						else
							System.out.print("  "+digit+"  ");
					}
				}
				//second row as seperated line
				else if (i == 1) {
					for (int k = 0; k < 15; k++)
						System.out.print("------------");
					System.out.print("\n");
					break;
				}
				//other rows
				else {
					//if (j > 10)
					//System.out.print(" ");
					if (j == 0 || j == column - 1) {
						c = (char) ascii;
						System.out.print(" "+c+" ");
					}
					else if (j == 1 || j == column - 2)
						System.out.print(" | ");
					//isle
					else if (j == 4 || j == column - 5)
						System.out.print("     ");
					else {
						//if available
						//Cinema cinema = CinemaDao.findById(cinemaId);
						//ArrayList<String> isOccupied = cinema.getSeat();
						//String seatId = new StringBuilder().append(c).append(j).toString();
						//if (isOccupied.contains(seatId)) // implement seat into cinema.json??
							//System.out.print("  X  ");
						// Movie movie = MovieDao.findById(movieId);
						//else
							System.out.print(" |_| ");
						//if not available
					}
				}
				if (j % (column - 1) == 0 && j != 0)
					System.out.print("\n");
			}
			ascii += 1;
		}
		System.out.print("\n");
	}

	//incomplete
	private static void bookTicketViewController() {
		Calendar c = new GregorianCalendar();
		c.setTimeZone(TimeZone.getTimeZone("Asia/Singapore"));
		c.add(Calendar.WEEK_OF_YEAR, 3);
		SimpleDateFormat df = new SimpleDateFormat("EEE, MMM dd, YYYY");
		
		int ticketNum, showtimeId;
		int i = 0;
		boolean exit = false;
		String seatId;
		String st;
		ArrayList<String> seat = new ArrayList<String>();
		HashMap<Integer, Movie> movies = MovieDao.findActiveMovie();
		
		System.out.print("\n");
		/*
		int movieId = selectMovie(movies);
		int cineplexId = selectCineplexAndReturnChoice();
		Cineplex cineplex = CineplexDao.findById(cineplexId);
		HashMap<Movie, ArrayList<Showtime>> movieShowtime = ShowtimeDao.getAllOnDate(c, cineplex);
		Set<Movie> movieSet = movieShowtime.keySet();

		for (Movie m: movieSet) {
			if (movies.get(movieId).getId() == m.getId()) {
				for(int j=1; i<=7; i++){
					c.set(Calendar.DAY_OF_WEEK, j);
					HashMap<Movie, ArrayList<Showtime>> sortedShowtimes = ShowtimeDao.getAllOnDate(c, cineplex);
					showtimeOfTheDayView(cineplex, sortedShowtimes, df.format(c.getTime()), true);
				}
				showtimeOfTheDayView(cineplex, movieShowtime, df.format(c.getTime()), true);
			}
			else {
				System.out.println("The movie << " + m.getTitle() + " >> is not currently available in " + cineplex.getCineplexName());
				return;
			}
		}
		*/
		listShowtimeByMovieViewController(true);
		System.out.print("Enter showtime ID: ");
		showtimeId = sc.nextInt();
		sc.nextLine();
		Showtime showtime = ShowtimeDao.findById(showtimeId);
		Cinema cinema = CinemaDao.findById(showtime.getCinemaId());
		Cineplex cineplex = CineplexDao.findById(showtime.getCineplexId());
		ArrayList<String> occupiedSeat = cinema.getSeat();
		boolean empty = occupiedSeat.isEmpty();
		ArrayList<Ticket> ticketList = new ArrayList<Ticket>();
		double total = 0;
		DecimalFormat deciformat = new DecimalFormat("#.##");
		deciformat.setRoundingMode(RoundingMode.HALF_UP);
		
		System.out.print("How many ticket: ");
		ticketNum = sc.nextInt();
		sc.nextLine();
		do {
			System.out.print("\nEnter seat " + (i+1) + ": ");
			seatId = sc.nextLine();
			if (seatInputChecking(seatId)) {
				if (!empty) {
					if (occupiedSeat.contains(seatId)) {
						System.out.println("This seat has been assigned to another person.");
						continue;
					}
					if (seat.contains(seatId)) {
						System.out.println("You have just entered this seat.");
						continue;
					}
				}
				seatId = seatId.toUpperCase();
				seat.add(seatId);
				Ticket tick = new Ticket();
				Ticket.printAgeGroupChoice();
				System.out.print("\nAge group for ticket " + (i+1) +": ");
				int choice = sc.nextInt();
				sc.nextLine();
				tick.setAgeGroupFromChoice(choice);
				tick.setSeatId(seatId);
				tick.setShowtime(showtimeId);
				tick.setPrice(tick.calculatePrice());
				ticketList.add(tick);
				i++;
			}
			else {
				System.out.println("Please enter seat in the format of (alphabet + digit)");
				continue;
			}
		} while (i < ticketNum);
		
		System.out.println("\nBooking for seats in " + cineplex.getCineplexName());
		System.out.println("Cinema class: " + cinema.getCinemaClassString());
		for (i = 0; i < ticketNum; i++) {
			double price = ticketList.get(i).getPrice();
			System.out.println("<< " + seat.get(i) + " >>");
			System.out.println(ticketList.get(i).getAgeGroupString() + " price: " + deciformat.format(price));
			System.out.print("\n");
			total += price;
		}
		System.out.println("Total price is " + deciformat.format(total));
		System.out.println("\nConfirm booking (Y|N): ");
		do {
			st = sc.nextLine();
			if (st.equalsIgnoreCase("Y")) {				
				for (i = 0; i < ticketNum; i++) {
					cinema.addSeat(seat.get(i));
					CinemaDao.save(cinema);
					TicketDao.save(ticketList.get(i));
				}
				//System.out.print(ticketNum + " ticket(s) successfully booked for << " + movies.get(movieId).getTitle() + " >>.");
				exit = true;
			}
			else if (st.equalsIgnoreCase("N"))
				exit = true;
			else
				exit = false;
		} while (!exit);
	}
	
	private static boolean seatInputChecking(String seatId) {
		char c1, c2, c3;
		int length = seatId.length();
		if (length < 2 && length > 3)
			return false;
		c1 = seatId.charAt(0);
		c2 = seatId.charAt(1);
		if (length == 2) {
			if (Character.isAlphabetic(c1) && Character.isDigit(c2)) 
				return true;
		}
		if (length == 3) {
			c3 = seatId.charAt(2);
			if (Character.isAlphabetic(c1) && Character.isDigit(c2) && Character.isDigit(c3))
				return true;
		}
		return false;
	}
	
	private static double calculateOverallRating(int movieId) {
		HashMap<Integer, Review> reviews = ReviewDao.findByMovieId(movieId);
		double rating = 0;
		int length = 0;
		if (!reviews.isEmpty()) {
			for (Review r: reviews.values()) {
				rating += r.getRating();
				length++;
			}
			if (length <= 1) {
				return 0;
			}
			else {
				return rating/length;
			}
		}
		else
			return 0;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static void listRanking() {
	    String[] menus = {"By ticket sales", "By overall reviews' rating"};
	    int choice = printMenuAndReturnChoice("Admin Panel > List top 5 ranking", menus);
	    if (choice == 1)
	    	//do something
	    	System.out.println("\nWaiting to be implemented... You want to try? XD");
	    else {
			double rating;
			HashMap<Integer, Movie> movies = MovieDao.getAllInHashMap();
			HashMap<String, Double> ratingList = new HashMap<String, Double>();
			for (Movie m: movies.values()) {
				rating = calculateOverallRating(m.getId());
				if (rating != 0)
					ratingList.put(m.getTitle(), rating);
			}
		    Map<String, Double> map = sortByValues(ratingList);
		    
		    System.out.println("\nThe top 5 ranking");
	    	for (int i = 0; i < 68; i++)
	    		System.out.print("-");
	    	Set set = map.entrySet();
	    	Iterator it = set.iterator();
	    	int index = 1;
	    	if (set.isEmpty()) {
	    		System.out.println("\nNo rating is available in all movies.");
	    	}
	    	while(it.hasNext()) {
	    		Map.Entry entry = (Map.Entry)it.next();
	    		if (!Double.isNaN(Double.parseDouble(entry.getValue().toString()))) {
	    			System.out.print("\nRank " + index + ". " + entry.getKey() + ", rating = ");
	    			System.out.format("%.1f", entry.getValue());
	    			index++;
	    		}
	    	}
	    }
	}
	
	  @SuppressWarnings({ "rawtypes", "unchecked" })
	private static HashMap sortByValues(HashMap map) { 
	       List l = new LinkedList(map.entrySet());
	       // Defined Custom Comparator here
	       Collections.sort(l, new Comparator() {
	            public int compare(Object o1, Object o2) {
	               return ((Comparable) ((Map.Entry) (o2)).getValue())
	                  .compareTo(((Map.Entry) (o1)).getValue());
	            }
	       });

	       // Here I am copying the sorted list in HashMap
	       // using LinkedHashMap to preserve the insertion order
	       HashMap sortedHashMap = new LinkedHashMap();
	       for (Iterator it = l.iterator(); it.hasNext();) {
	              Map.Entry entry = (Map.Entry) it.next();
	              sortedHashMap.put(entry.getKey(), entry.getValue());
	       } 
	       return sortedHashMap;
	  }
	  
	  private static void addReviewViewController(HashMap<Integer, Movie> movies) {
		  int it, edit, movie, index = 1;
		  boolean movieExit, showExit;
		  double dt;
		  String st;
		  DecimalFormat df = new DecimalFormat("#.##");
		  df.setRoundingMode(RoundingMode.DOWN);
		  Review r = new Review();
		  movieExit = false;
		  while (!movieExit) {
			  System.out.println("Which movie you want to review for: ");
			  for (Movie m: movies.values()) {
				  System.out.println(index + ". " + m.getTitle());
				  index++;
			  }
			  System.out.print("\n");
			  movie = sc.nextInt();
			  sc.nextLine();
			  r.setMovieId(movie);
			  System.out.print("Enter name: ");
			  st = sc.nextLine();
			  r.setName(st);
			  System.out.print("Enter rating: ");
			  dt = sc.nextDouble();
			  dt = Double.parseDouble(df.format(dt));
			  sc.nextLine();
			  r.setRating(dt);
			  System.out.print("Enter comment: ");
			  st = sc.nextLine();
			  r.setComment(st);
			  showExit = false;
			  while (!showExit) {
				  System.out.println("\nReviewing for movie <<" + movies.get(movie).getTitle() + ">>");
				  System.out.println("Name: " + r.getName());
				  System.out.println("Rating: " + r.getRating());
				  System.out.println("Comment: " + r.getComment());
				  String[] menus = {"Confirm to add review", "Edit entry", "Back to movie selection", "Back to main menu"};
				  it = printMenuAndReturnChoice("Movie-goer Panel > Add Review", menus);
				  switch (it) {
				  case 1: 
					  ReviewDao.save(r);
					  HashMap<Integer, Review> reviews = new HashMap<Integer, Review>();
					  reviews.put(r.getId(), r);
					  movies.get(movie).setReviews(reviews);
					  System.out.print("Review successfully added.");
					  movieExit = true;
					  showExit = true;
					  break;
				  case 2: 
					  String[] editMenus = {"Edit name", "Edit rating", "Edit comment"};
					  edit = printMenuAndReturnChoice("Add Review > Edit", editMenus);
					  if (edit == 1) {
						  System.out.print("Enter name: ");
						  st = sc.nextLine();
						  r.setName(st);
					  }
					  else if (edit == 2) {
						  System.out.print("Enter rating: ");
						  dt = sc.nextDouble();
						  r.setRating(dt);
					  }
					  else {
						  System.out.print("Enter comment: ");
						  st = sc.nextLine();
						  r.setComment(st);
					  }
					  movieExit = false;
					  showExit = false;
					  break;
				  case 3:
					  showExit = true;
					  movieExit = false;
					  break;
				  case 4:
					  showExit = true;
					  movieExit = true;
					  break;
				  default:
					  System.out.println("Invalid option.");
					  break;
				  }
			  }
		  }
	  }
	  
	  @SuppressWarnings("unused")
	private static void showReviews(HashMap<Integer, Movie> movies) {
		  int index = 1;
		  int movie;
		  System.out.println("Which movie reviews you want to read?");
		  for (Movie m: movies.values()) {
			  System.out.println(index + ". " + m.getTitle());
			  index++;
		  }
		  System.out.print("\n");
		  movie = sc.nextInt();
		  sc.nextLine();
		  HashMap<Integer, Review> reviews = ReviewDao.findByMovieId(movie);
		  index = 1;
		  if (!reviews.isEmpty()) {
			  for (Review r: reviews.values()) {
				  System.out.print("\n");
				  System.out.println("[" + MovieDao.findById(movie).getTitle() + "] " + "<< Review " + index + " >>");
				  System.out.println("Name: " + r.getName());
				  System.out.println("Rating: " + r.getRating());
				  System.out.println("Comment: " + r.getComment());
				  index++;
			  }
		  }
		  else
			  System.out.print("No past reviews.");
	  }
	  
	  private static void showReviewsByMovie(int movieId) {
		  Movie m = MovieDao.findById(movieId);
		  HashMap<Integer, Review> reviews = m.getReviews();
		  int index = 1;
		  if (!reviews.isEmpty()) {
			  for (Review r: reviews.values()) {
				  System.out.print("\n");
				  System.out.println("[" + m.getTitle() + "] " + "<< Review " + index + " >>");
				  System.out.println("Name: " + r.getName());
				  System.out.println("Rating: " + r.getRating());
				  System.out.println("Comment: " + r.getComment());
				  index++;
			  }
		  }
		  else
			  System.out.print("No past reviews.");
	  }

}