package sg.edu.ntu.cz2002.moblima.models;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.Map.Entry;
import java.util.Scanner;

import sg.edu.ntu.cz2002.moblima.CalendarView;
import sg.edu.ntu.cz2002.moblima.Data;
import sg.edu.ntu.cz2002.moblima.dao.*;
import sg.edu.ntu.cz2002.moblima.models.Cinema.CinemaClass;
import sg.edu.ntu.cz2002.moblima.models.Movie.MovieType;
import sg.edu.ntu.cz2002.moblima.models.Seat.SeatType;
import sg.edu.ntu.cz2002.moblima.models.Showtime.Day;
import sg.edu.ntu.cz2002.moblima.models.Ticket.AgeGroup;
import sg.edu.ntu.cz2002.moblima.view.GeneralView;

public class CineplexManager {
	static Scanner sc = new Scanner(System.in);

	/**
	 * Admin User Interface to CRUD the adiitional charges/discount for
	 * ticket base price, extra charges for different cinema class, movie types,
	 * age group, day and seat type
	 */
	public void runAdminChargesManagement() {
		int choice;
		String st;
		boolean exit;
		String[] menus = {"List all current charges",
				"Set base price", 
				"Set cinema class charge", 
				"Set movie type charge", 
				"Set age group charge", 
				"Set day charge",
				"Set seat charge",
		"Back to main menu"};
		do {
			Settings settings = SettingsDao.getSettings();
			choice = GeneralView.printMenuAndReturnChoice("Admin Panel > System Configuration > Ticket Charges Management", menus);
			int select;
			boolean exit2;
			switch (choice) {
			case 1:
				printCharge();
				exit = false;
				break;
			case 2:
				Double base = settings.getBasePrice();
				System.out.println("\nOriginal base price: " + base);
				System.out.print("Enter new base price: ");
				base = sc.nextDouble();
				sc.nextLine();
				settings.setBasePrice(base);
				System.out.println("\nNew base price: " + base);
				System.out.print("Confirm (Y|N): ");
				st = sc.nextLine();
				if (st.equalsIgnoreCase("Y")) {
					SettingsDao.save();
				}
				exit = false;
				break;
			case 3:
				do {
					String[] classMenus = {"Edit for class PREMIUM", "Edit for class PLATINUM", "Edit for class GOLD",
							"Edit for class NORMAL", "Back to previous menu"};
					select = GeneralView.printMenuAndReturnChoice("Admin Panel > System Configuration > Ticket Charges Management > Cinema Class", classMenus);
					if (select == classMenus.length) {
						exit2 = true;
					}
					else {
						setCharge(CinemaClass.class, select);
						exit2 = false;
					}
				} while (!exit2);
				exit = false;
				break;
			case 4:
				do {
					String[] movieTypeMenus = {"Edit for BLOCKBUSTER", "Edit for THREED", "Edit for NORMAL", "Back to previous menu"};
					select = GeneralView.printMenuAndReturnChoice("Admin Panel > System Configuration > Ticket Charges Management > Movie Type", movieTypeMenus);
					if (select == movieTypeMenus.length)
						exit2 = true;
					else {
						setCharge(MovieType.class, select);
						exit2 = false;
					}
				} while (!exit2);
				exit = false;
				break;
			case 5:
				do {
					String[] ageGroupMenus = {"Edit for CHILD", "Edit for ADULT", "Edit for SENIOR", "Back to previous menu"};
					select = GeneralView.printMenuAndReturnChoice("Admin Panel > System Configuration > Ticket Charges Management > Age Group", ageGroupMenus);
					if (select == ageGroupMenus.length)
						exit2 = true;
					else {
						setCharge(AgeGroup.class, select);
						exit2 = false;
					}
				} while (!exit2);
				exit = false;
				break;
			case 6:
				do {
					String[] dayMenus = {"Edit for WEEKDAY", "Edit for WEEKEND", "Edit for PUBLICHOLIDAY", "Back to previous menu"};
					select = GeneralView.printMenuAndReturnChoice("Admin Panel > System Configuration > Ticket Charges Management > Day Type", dayMenus);
					if (select == dayMenus.length)
						exit2 = true;
					else {
						setCharge(Day.class, select);
						exit2 = false;
					}
				} while (!exit2);
				exit = false;
				break;
			case 7:
				do {
					String[] seatMenus = {"Edit for NORMAL", "Edit for COUPLE", "Edit for ULTIMA", "Edit for RESERVED", "Back to previous menu"};
					select = GeneralView.printMenuAndReturnChoice("Admin Panel > System Configuration > Ticket Charges Management > Seat Type", seatMenus);
					if (select == seatMenus.length)
						exit2 = true;
					else {
						setCharge(SeatType.class, select);
						exit2 = false;
					}
				} while (!exit2);
				exit = false;
				break;
			default:
				exit = true;
			}
		} while (!exit);
	}

	/**
	 * Prompt user to set new charges to respective type
	 * @param classType Class of respective type
	 * @param choice Choice from 1 to n, refer to available choices
	 */
	public void setCharge(Object classType, int choice) {
		String st;
		Settings settings = SettingsDao.getSettings();

		if (classType.getClass().isInstance(CinemaClass.class)) {
			HashMap<CinemaClass, Double> c = settings.getCinemaClassCharges();
			System.out.println("\nOriginal charge for cinema class of " + Cinema.getCinemaClassStringFromChoice(choice) + 
					" is " + c.get(Cinema.getCinemaClassEnumFromChoice(choice)));
		}
		else if (classType.getClass().isInstance(MovieType.class)) {
			HashMap<MovieType, Double> m = settings.getMovieTypeCharges();
			System.out.println("\nOriginal charge for movie type of " + Movie.getTypeStringFromChoice(choice) + 
					" is " + m.get(Movie.getTypeEnumFromChoice(choice)));
		}
		else if (classType.getClass().isInstance(AgeGroup.class)) {
			HashMap<AgeGroup, Double> a = settings.getAgeGroupCharges();
			System.out.println("\nOriginal charge for age group of " + Ticket.getAgeGroupStringFromChoice(choice) + 
					" is " + a.get(Ticket.getAgeGroupEnumFromChoice(choice)));
		}
		else if (classType.getClass().isInstance(Day.class)) {
			HashMap<Day, Double> d = settings.getDayCharges();
			System.out.println("\nOriginal charge for day type " + Showtime.getDayStringFromChoice(choice) + 
					" is " + d.get(Showtime.getDayEnumFromChoice(choice)));
		}
		else if (classType.getClass().isInstance(SeatType.class)) {
			HashMap<SeatType, Double> d = settings.getSeatTypeCharges();
			System.out.println("\nOriginal multiplier for seat type " + Seat.getSeatTypeStringFromChoice(choice) + 
					" is " + d.get(Seat.getSeatTypeEnumFromChoice(choice)));
		}
		else
			return;
		System.out.print("\nEnter new value: ");
		Double value = sc.nextDouble();
		sc.nextLine();
		System.out.print("Confirm (Y|N): ");
		st = sc.nextLine();
		if (st.equalsIgnoreCase("Y")) {
			if (classType.getClass().isInstance(CinemaClass.class)) {
				System.out.println("\nNew cinema class ["+Cinema.getCinemaClassStringFromChoice(choice)+"] charge: " + value);
				settings.setCinemaClassCharges(choice, value);
			}
			else if (classType.getClass().isInstance(MovieType.class)) {
				System.out.println("\nNew movie type ["+Movie.getTypeStringFromChoice(choice)+"] charge: " + value);
				settings.setMovieTypeCharges(choice, value);
			}
			else if (classType.getClass().isInstance(AgeGroup.class)) {
				System.out.println("\nNew age group ["+Ticket.getAgeGroupStringFromChoice(choice)+"] charge: " + value);
				settings.setAgeGroupCharges(choice, value);
			}
			else if (classType.getClass().isInstance(Day.class)) {
				System.out.println("\nNew day type ["+Showtime.getDayStringFromChoice(choice)+"] charge: " + value);
				settings.setDayCharges(choice, value);
			}
			else {
				System.out.println("\nNew seat type ["+Seat.getSeatTypeStringFromChoice(choice)+"] multiplier: " + value);
				settings.setSeatTypeCharges(choice, value);
			}
			SettingsDao.save();
			System.out.println("Change updated.");
		}
		else 
			return;
	}


	public boolean loginAdmin(Data data){
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

	/**
	 * Print every charges for every type of Cinema Class,
	 * Movie Type, Age Group, Day Type and Seat Type
	 */
	public void printCharge() {
		Settings s = SettingsDao.getSettings();
		Double basePrice = s.getBasePrice();
		System.out.println("\nBase price for a ticket: " + basePrice);

		HashMap<CinemaClass, Double> cinemaClass = s.getCinemaClassCharges();
		System.out.println("\n<< CINEMA CLASS >>");
		for (CinemaClass v: cinemaClass.keySet())
			System.out.println(Cinema.getCinemaClassStringFromCinemaClass(v) + ", Charge: " + cinemaClass.get(v));

		HashMap<MovieType, Double> movieType = s.getMovieTypeCharges();
		System.out.println("\n<< MOVIE TYPE >>");
		for (MovieType v: movieType.keySet()) 
			System.out.println(Movie.getTypeStringFromMovieType(v) + ", Charge: " + movieType.get(v));

		HashMap<AgeGroup, Double> ageGroup = s.getAgeGroupCharges();
		System.out.println("\n<< AGE GROUP >>");
		for (AgeGroup v: ageGroup.keySet())
			System.out.println(Ticket.getAgeGroupStringFromAgeGroup(v) + ", Charge: " + ageGroup.get(v));

		HashMap<Day, Double> day = s.getDayCharges();
		System.out.println("\n<< DAY TYPE >>");
		for (Day v: day.keySet())
			System.out.println(Showtime.getDayStringFromDay(v) + ", Charge: " + day.get(v));

		HashMap<SeatType, Double> seat = s.getSeatTypeCharges();
		System.out.println("\n<< SEAT TYPE >>");
		for (SeatType v: seat.keySet())
			System.out.println(Seat.getSeatTypeStringFromSeatType(v) + ", Multiplier: " + seat.get(v));
	}

	/**
	 * Admin User Interface for CRUD of public holidays
	 */
	public void runAdminHolidayManagement(){
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
		SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy");
		do{
			jump = false;
			choice = GeneralView.printMenuAndReturnChoice("Admin Panel > System Configuration > Public Holiday Management", menus);
			switch(choice){
			case 1:
				CalendarView.printCalendar(Calendar.getInstance().get(Calendar.YEAR), SettingsDao.getHolidays());
				break;
			case 2:
				System.out.print("\nEnter the year(eg. "+Calendar.getInstance().get(Calendar.YEAR)+"): ");
				year = sc.nextInt();
				sc.nextLine();
				CalendarView.printCalendar(year, SettingsDao.getHolidays());
				break;
			case 3: 
				t = 0;
				year = Calendar.getInstance().get(Calendar.YEAR);
				System.out.println("\nList of Holidays in Current Year("+year+")");
				for(Calendar holiday: sortedDates()){
					if(holiday.get(Calendar.YEAR) == year)
						System.out.println((++t)+". "+df.format(holiday.getTime()));
				}
				if(t==0)
					System.out.println("No record found");
				break;
			case 4:
				t = 0;
				System.out.print("\nEnter the year(eg. "+Calendar.getInstance().get(Calendar.YEAR)+"): ");
				year = sc.nextInt();
				sc.nextLine();
				System.out.println("\nList of Holidays in Year "+year);
				for(Calendar holiday: sortedDates()){
					if(holiday.get(Calendar.YEAR) == year)
						System.out.println((++t)+". "+df.format(holiday.getTime()));
				}
				if(t==0)
					System.out.println("No record found");
				break;
			case 5:
				do{
					System.out.println("\nNew Holiday Record");
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
					System.out.println("\nPlease confirm the record:\n"+date+"\nTo insert, type Y\nTo redo, type N\nTo exit, type E");
					System.out.print("Your choice: ");
					st = sc.nextLine();
					if(st.equalsIgnoreCase("Y")){
						Calendar newHoliday = new GregorianCalendar(year, month-1, day);
						newHoliday.setTimeZone(TimeZone.getTimeZone("Asia/Singapore"));
						ArrayList<Calendar> th = SettingsDao.getHolidays();
						if (th.contains(newHoliday)) {
							System.out.println("This record is already saved.");
							continue;
						}
						th.add(newHoliday);
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
				System.out.print("\nEnter the year(eg. "+Calendar.getInstance().get(Calendar.YEAR)+"): ");
				year = sc.nextInt();
				sc.nextLine();
				do{
					t = 0;
					System.out.println("\nList of Holidays in Year "+year);
					for(Calendar holiday: SettingsDao.getHolidays()){
						if(holiday.get(Calendar.YEAR) == year)
							System.out.println((++t)+". "+df.format(holiday.getTime()));
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
					if(t > SettingsDao.getHolidays().size()){
						System.out.println("Please enter valid row number (1 to "+SettingsDao.getHolidays().size()+")");
						continue;
					}
					date = df.format(SettingsDao.getHolidays().get(t-1).getTime());
					System.out.println("\nPlease confirm the record:\n"+date+"\nTo remove, type Y\nTo reselect, type N\nTo exit, type E");
					System.out.print("Your choice: ");
					st = sc.nextLine();
					if(st.equalsIgnoreCase("Y")){
						if(t<=0 || t>SettingsDao.getHolidays().size())
							System.out.println("Zero record was removed. Record is not found");
						else{
							SettingsDao.getHolidays().remove(t-1);
							SettingsDao.save();
							System.out.println("One record was removed");
						}
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

	/**
	 * Calculate overall rating from reviews provided by movie goers
	 * @param movieId Unique ID of user
	 * @return Average rating of the movie
	 */
	public double calculateOverallRating(int movieId) {
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

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public HashMap sortByValues(HashMap map) { 
		List l = new LinkedList(map.entrySet());
		Collections.sort(l, new Comparator() {
			public int compare(Object o1, Object o2) {
				return ((Comparable) ((Map.Entry) (o2)).getValue())
						.compareTo(((Map.Entry) (o1)).getValue());
			}
		});

		HashMap sortedHashMap = new LinkedHashMap();
		for (Iterator it = l.iterator(); it.hasNext();) {
			Map.Entry entry = (Map.Entry) it.next();
			sortedHashMap.put(entry.getKey(), entry.getValue());
		} 
		return sortedHashMap;
	}
	
	public static class MyObject implements Comparable<MyObject> {
		private Date dateTime;

		public Date getDateTime() {
			return dateTime;
		}

		public void setDateTime(Date datetime) {
			this.dateTime = datetime;
		}

		@Override
		public int compareTo(MyObject o) {
			if (getDateTime() == null || o.getDateTime() == null)
				return 0;
			return getDateTime().compareTo(o.getDateTime());
		}
	}
	
	public ArrayList<Calendar> sortedDates() {
		Collections.sort(SettingsDao.getHolidays());
		if (SettingsDao.save())
			return SettingsDao.getHolidays();
		else
			return null;
	}
	
	/**
	 * List all the cineplexes details in database
	 * @param cineplexes
	 * @param showId
	 */
	public void listCineplexesView(HashMap<Integer, Cineplex> cineplexes, boolean showId){
		for (Cineplex c: cineplexes.values()) {
			listCineplexView(c, showId);
		}
	}

	/**
	 * List the specific cineplex detail
	 * @param c
	 * @param showId
	 */
	public void listCineplexView(Cineplex c, boolean showId){
		if (showId)
			System.out.println("Cineplex ID: " + c.getId());
		System.out.println("Cineplex name: " + c.getCineplexName());
		System.out.println("Cinema number: " + c.getCinemaNum());
		System.out.print("\n");
	}
	

	
}
