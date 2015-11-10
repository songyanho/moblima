package sg.edu.ntu.cz2002.moblima.models;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Scanner;
import java.util.TimeZone;

import sg.edu.ntu.cz2002.moblima.CalendarView;
import sg.edu.ntu.cz2002.moblima.dao.*;
import sg.edu.ntu.cz2002.moblima.models.Movie.MovieType;
import sg.edu.ntu.cz2002.moblima.models.Showtime.Day;
import sg.edu.ntu.cz2002.moblima.view.GeneralView;

public class ShowtimeManager {
	public Showtime showtime;
	CineplexManager cineplexMgr = new CineplexManager();
	protected ArrayList<Integer> showtimeList = new ArrayList<Integer>();
	static Scanner sc = new Scanner(System.in);
	
	public ArrayList<Integer> listShowtimeViewController(String panel) {
		int it = 0, choice;
		int weekOffset = 0;
		boolean repeat;
		ArrayList<Cineplex> cineplexes = new ArrayList<Cineplex>();
		ArrayList<Movie> movies = new ArrayList<Movie>();
		ArrayList<Integer> showtimeList = new ArrayList<Integer>();
		String[] menu =new String[CineplexDao.getAllInHashMap().size()+1];
		
		for(Cineplex c: CineplexDao.getAllInHashMap().values()){
			menu[it++] = c.getCineplexName();
			cineplexes.add(c);
		}
		menu[it] = "Back to previous menu";
		do {
			if (panel.equals("booking"))
					choice = GeneralView.printMenuAndReturnChoice("Movie-goer Panel > Book ticket > Select cineplex", menu);
			else
				choice = GeneralView.printMenuAndReturnChoice("Movie-goer Panel > Check seat availability > Select cineplex", menu);
			if(choice <= 0 || choice >= menu.length) { 
				if (choice == menu.length)
					return showtimeList;
				else
					repeat = true;
			}
			else
				repeat = false;
		} while (repeat);
		
		Cineplex selectedCineplex = cineplexes.get(choice-1);
		System.out.println("\nYou have selected <<" + selectedCineplex.getCineplexName()+">>");
		
		it = 0;
		menu = new String[MovieDao.findActiveMovie().size()+1];
		for(Movie m: MovieDao.findActiveMovie().values()) {
			menu[it++] = m.getTitle();
			movies.add(m);
		}
		menu[it] = "Back to previous menu";
		do {
			if (panel.equals("booking"))
				choice = GeneralView.printMenuAndReturnChoice("Movie-goer Panel > Book ticket > Select movie", menu);
			else
				choice = GeneralView.printMenuAndReturnChoice("Movie-goer Panel > Check seat availability > Select movie", menu);
			if(choice <= 0 || choice >= menu.length) { 
				if (choice == menu.length)
					return showtimeList;
				else
					repeat = true;
			}
			else
				repeat = false;
		} while (repeat);
		Movie selectedMovie = movies.get(choice-1);
		System.out.println("\nYou have selected <<" + selectedMovie.getTitle() + ">>");
		
		String[] weekMenu = {"Current week", "Next week", "Next 2 week", "Back to previous menu"};
		do {
			if (panel.equals("booking"))
				choice = GeneralView.printMenuAndReturnChoice("Movie-goer Panel > Book ticket > Select week", weekMenu);
			else
				choice = GeneralView.printMenuAndReturnChoice("Movie-goer Panel > Check seat availability > Select week", weekMenu);
			if(choice <= 0 || choice >= weekMenu.length) { 
				if (choice == weekMenu.length)
					return showtimeList;
				else
					repeat = true;
			}
			else
				repeat = false;
		} while (repeat);
		weekOffset = choice-1;
		
		Calendar cal = new GregorianCalendar();
		cal.setTimeZone(TimeZone.getTimeZone("Asia/Singapore"));
		cal.add(Calendar.WEEK_OF_YEAR, weekOffset);
		SimpleDateFormat df = new SimpleDateFormat("EEE, MMM dd, YYYY");
		for(int i=1; i<=7; i++){
			cal.set(Calendar.DAY_OF_WEEK, i);
			HashMap<Cineplex, ArrayList<Showtime>> sortedShowtimes = ShowtimeDao.getAllOnDate(cal, selectedMovie);
			if(sortedShowtimes.size() <= 0){
				System.out.print("\n");
				for (int j = 0; j < 68; j++)
					System.out.print("-");
				System.out.println("\nDate: "+df.format(cal.getTime()));
				for (int j = 0; j < 68; j++)
					System.out.print("-");
				System.out.println("\nNo showtimes.\n");
				continue;
			}
			for(Cineplex cine: sortedShowtimes.keySet()){
				if (cine.getId() == selectedCineplex.getId()) {
					if (!sortedShowtimes.get(cine).isEmpty()) {
						showtimeOfTheDayView(cine, sortedShowtimes.get(cine), selectedMovie, df.format(cal.getTime()), true);
						ArrayList<Showtime> a = sortedShowtimes.get(cine);
						for (Showtime s: a)
							showtimeList.add(s.getId());
						break;
					}
				}
			}
		}
		return showtimeList;
	}
	
	public void updateShowtimeViewController(){
		String st;
		int it,choice;
		boolean exit = false;
		String menu[] = {"Update showtime with ID", "Search showtime ID" ,"Back to previous menu"};
		do{
			exit = false;
			choice = GeneralView.printMenuAndReturnChoice("Admin Panel > Movie Showtime Management > Update showtimes", menu);
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
					choice = GeneralView.printMenuAndReturnChoice("Admin Panel > Movie Showtime Management > Update showtimes ID: "+s.getId(), menu2);
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
	
	public void removeShowtimeViewController(){
		String st;
		int it,choice;
		boolean exit = false;
		String menu[] = {"Remove showtime with ID", "Search showtime ID" ,"Back to previous menu"};
		do{
			choice = GeneralView.printMenuAndReturnChoice("Admin Panel > Movie Showtime Management > Remove showtimes", menu);
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
	
	public void printShowtimeInformationView(Showtime s){
		SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm aaa");
		System.out.println("\n\nShowtime ID: "+s.getId());
		System.out.println("Movie: (ID "+s.getMovieId()+") "+s.getMovie().getTitle());
		System.out.println("Cineplex: "+s.getCineplex().getCineplexName()+" <"+s.getCinema().getName()+">");
		System.out.println("Showtime: "+df.format(s.getDate().getTime()));
	}

	public void searchShowtimeViewController(){
		int choice;
		boolean exit = false;
		String menu[] = {"Search by cineplex", "Search by movie" ,"Back to previous menu"};
		do{
			choice = GeneralView.printMenuAndReturnChoice("Admin Panel > Movie Showtime Management > Search showtimes", menu);
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
	
	public void listShowtimeByMovieViewController(boolean showId){
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
		choice = GeneralView.printMenuAndReturnChoice("Admin Panel > Movie Showtime Management > Search Showtimes \n> Select Movie", movieMenu);
		if(choice <= 0 || choice >= movieMenu.length)
			return;
		Movie selectedMovie = movies.get(choice-1);
		System.out.println("You have selected <<" + selectedMovie.getTitle()+">>");

		String[] weekMenu = {"Last 2 week", "Last week", "Current week", "Next week", "Next 2 week", "Back to previous menu"};
		choice = GeneralView.printMenuAndReturnChoice("Admin Panel > Movie Showtime Management > Select week", weekMenu);
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

	public void listShowtimeViewController(boolean showId){
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
		choice = GeneralView.printMenuAndReturnChoice("Admin Panel > Movie Showtime Management > "+(showId?"Search showtimes \n":"List ")+"> Select Cineplex", cineplexMenu);
		if(choice <= 0 || choice >= cineplexes.size()+1)
			return;
		Cineplex selectedCineplex = cineplexes.get(choice-1);
		System.out.println("You have selected <<" + selectedCineplex.getCineplexName()+">>");

		String[] weekMenu = {"Last 2 week", "Last week", "Current week", "Next week", "Next 2 week", "Back to previous menu"};
		choice = GeneralView.printMenuAndReturnChoice("Admin Panel > Movie Showtime Management > Select week", weekMenu);
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
	
	public void listShowtimeViewController(boolean cineplex, boolean showId){
		int it = 0, choice;
		int weekOffset = 0;
		ArrayList<Cineplex> cineplexes = new ArrayList<Cineplex>();
		ArrayList<Movie> movies = new ArrayList<Movie>();
		String[] menu = cineplex == true? new String[CineplexDao.getAllInHashMap().size()+1]:  new String[MovieDao.getAllInHashMap().size()+1];
		
		if (cineplex) {
			for(Cineplex c: CineplexDao.getAllInHashMap().values()){
				menu[it++] = c.getCineplexName();
				cineplexes.add(c);
			}
		}
		else {
			for(Movie c: MovieDao.getAllInHashMap().values()){
				menu[it++] = c.getTitle();
				movies.add(c);
			}
		}
		menu[it] = "Back to previous menu";
		choice = GeneralView.printMenuAndReturnChoice("Admin Panel > Movie Showtime Management > "+(showId?"Search showtimes \n":"List ")+"> Select Cineplex", menu);
		if(choice <= 0 || choice >= menu.length)
			return;
		System.out.print("You have selected << ");
		if (cineplex) {
			Cineplex selectedCineplex = cineplexes.get(choice-1);
			System.out.print(selectedCineplex.getCineplexName() + " >>\n");
		}
		else {
			Movie selectedMovie = movies.get(choice-1);
			System.out.print(selectedMovie.getTitle() + " >>\n");
		}
		
		String[] weekMenu = {"Last 2 week", "Last week", "Current week", "Next week", "Next 2 week", "Back to previous menu"};
		int choice2 = GeneralView.printMenuAndReturnChoice("Admin Panel > Movie Showtime Management > Select week", weekMenu);
		if(choice2<=0 || choice2 >= weekMenu.length)
			return;
		weekOffset = choice2-3;
		
		Calendar c = new GregorianCalendar();
		c.setTimeZone(TimeZone.getTimeZone("Asia/Singapore"));
		c.add(Calendar.WEEK_OF_YEAR, weekOffset);
		SimpleDateFormat df = new SimpleDateFormat("EEE, MMM dd, YYYY");
		for(int i=1; i<=7; i++){
			c.set(Calendar.DAY_OF_WEEK, i);
			if (cineplex) {
				Cineplex selectedCineplex = cineplexes.get(choice-1);
				HashMap<Movie, ArrayList<Showtime>> sortedShowtimes = ShowtimeDao.getAllOnDate(c, selectedCineplex);
				showtimeOfTheDayView(selectedCineplex, sortedShowtimes, df.format(c.getTime()), showId);
			}
			else {
				Movie selectedMovie = movies.get(choice-1);
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
				for(Cineplex cine: sortedShowtimes.keySet()){
					showtimeOfTheDayView(cine, sortedShowtimes.get(cine), selectedMovie, df.format(c.getTime()), showId);
				}
			}
		}
	}
	
	public void showtimeOfTheDayView(Cineplex cineplex, ArrayList<Showtime> st, Movie movie, String date, boolean showId){
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

	public void showtimeOfTheDayView(Cineplex cineplex, HashMap<Movie, ArrayList<Showtime>> showtimes, String date, boolean showId){
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

	public void addNewShowtimeViewController(){
		String st;
		int it, choice;
		boolean exit = false;
		int weekOffset = 0;
		GeneralView.printMenuAndReturnChoice("Admin Panel > Movie Showtime Management > Select Movie", null);
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
			choice = GeneralView.printMenuAndReturnChoice("Admin Panel > Movie Showtime Management > New > Select Cineplex", cineplexMenu);
			if(choice <= 0 || choice >= cineplexes.size()+1)
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
				choice = GeneralView.printMenuAndReturnChoice("Admin Panel > Movie Showtime Management > New > Select Cinema", cinemaMenu);
				if(choice<=0 || choice >= cinemas.size()+1)
					break;
				Cinema selectedCinema = cinemas.get(choice-1);
				System.out.println("You have selected <<" + selectedCinema.getName()+">>");

				String[] weekMenu = {"Current week", "Next week", "Next 2 week", "Back to previous menu"};
				choice = GeneralView.printMenuAndReturnChoice("Admin Panel > Movie Showtime Management > New > Select week", weekMenu);
				if(choice<=0 || choice >= weekMenu.length)
					break;
				weekOffset = choice-1;
				int timeslot[][][] = cinemaTimetableView(selectedCinema, weekOffset, null, true);

				String[] dayMenu = CalendarView.dayOfWeek(true, weekOffset, false);
				choice = GeneralView.printMenuAndReturnChoice("Admin Panel > Movie Showtime Management > New > Select Showtime Day", dayMenu);
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
				choice = GeneralView.printMenuAndReturnChoice("Admin Panel > Movie Showtime Management > New > Select Showtime Time", timeMenu);
				if(choice<=0 || choice >= timeMenu.length)
					break;
				Showtime ns = new Showtime();
				ns.setCinemaId(selectedCinema.getId());
				ns.setType(MovieType.BLOCKBUSTER);
				ns.setMovieId(selectedMovie.getId());
				ns.setCineplexId(selectedCineplex.getId());
				ns.setDate((Calendar) availableTimeSlot.get(choice-1).clone());
				Day dayType = cineplexMgr.checkDayType(ns);
				int numEmptySeat = CinemaDao.findById(ns.getCinemaId()).getSeatNum();
				ns.setDayType(dayType);
				ns.setNumEmptySeat(numEmptySeat);
				ShowtimeDao.add(ns);
				System.out.println("Showtime saved");
			}while(true);
		}while(true);
	}

	public int[][][] cinemaTimetableView(Cinema c, int weekOffset, Showtime excludeShowtime, boolean show){
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

	public Showtime selectShowtime() {
		SimpleDateFormat formatter = new SimpleDateFormat("h:mm a, EEE, MMM d, yyyy");
		DecimalFormat deciformat = new DecimalFormat("#0.00");
		deciformat.setRoundingMode(RoundingMode.HALF_UP);
		int i = 0, choice;
		boolean exit = false;

		do {
			System.out.print("\n");
			showtimeList = listShowtimeViewController("seat");		
			String[] menu = {"Enter showtime ID", "Back to cineplex and movie selection", "Back to main menu"};
			choice = GeneralView.printMenuAndReturnChoice("Movie-goer Panel > Check seat availability", menu);
			switch (choice) {
			case 1:
				if (showtimeList.isEmpty()) {
					System.out.println("\nSorry, no showtime is available for this movie in this period of time");
					continue;
				}
				exit = true;
				break;
			case 2:
				continue;
			default:
				return null;
			}
		} while (!exit);

		String[] idList = new String[showtimeList.size()+1];
		for (i = 0; i < showtimeList.size(); i++) {
			Calendar calendar = ShowtimeDao.findById(showtimeList.get(i)).getDate();
			idList[i] = "Showtime: " + formatter.format(calendar.getTime());
		}
		idList[i] = "Back to main menu";
		choice = GeneralView.printMenuAndReturnChoice("Movie-goer Panel > Check seat availability > Enter Showtime ID", idList);
		if (choice == idList.length)
			return null;
		return showtime = ShowtimeDao.findById(showtimeList.get(choice-1));
	}
	
	public int getNumEmptySeat(Showtime showtime) {
		return showtime.getNumEmptySeat();
	}
}
