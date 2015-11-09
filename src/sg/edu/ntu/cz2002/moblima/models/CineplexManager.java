package sg.edu.ntu.cz2002.moblima.models;

import java.text.DateFormatSymbols;
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
import java.util.TimeZone;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;

import sg.edu.ntu.cz2002.moblima.CalendarView;
import sg.edu.ntu.cz2002.moblima.Data;
import sg.edu.ntu.cz2002.moblima.dao.AdminDao;
import sg.edu.ntu.cz2002.moblima.dao.MovieDao;
import sg.edu.ntu.cz2002.moblima.dao.ReviewDao;
import sg.edu.ntu.cz2002.moblima.dao.SettingsDao;
import sg.edu.ntu.cz2002.moblima.dao.ShowtimeDao;
import sg.edu.ntu.cz2002.moblima.dao.TicketDao;
import sg.edu.ntu.cz2002.moblima.models.Showtime.Day;
import sg.edu.ntu.cz2002.moblima.view.GeneralView;

public class CineplexManager {
	static Scanner sc = new Scanner(System.in);
	
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
				"Back to previous menu"};
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
							setCharge("CinemaClass", select);
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
							setCharge("MovieType", select);
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
							setCharge("AgeGroup", select);
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
							setCharge("Day", select);
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
	
	public void setCharge(String className, int choice) {
		String st;
		Settings settings = SettingsDao.getSettings();
		
		if (className == "CinemaClass") {
			HashMap<Integer, Double> c = settings.getCinemaClassCharges();
			System.out.println("\nOriginal charge for cinema class of " + Cinema.getCinemaClassStringFromChoice(choice) + 
				" is " + c.get(choice-1));
		}
		else if (className == "MovieType") {
			HashMap<Integer, Double> m = settings.getMovieTypeCharges();
			System.out.println("\nOriginal charge for movie type of " + Movie.getTypeStringFromChoice(choice) + 
					" is " + m.get(choice-1));
		}
		else if (className == "AgeGroup") {
			HashMap<Integer, Double> a = settings.getAgeGroupCharges();
			System.out.println("\nOriginal charge for age group of " + Ticket.getAgeGroupStringFromChoice(choice) + 
					" is " + a.get(choice-1));
		}
		else if (className == "Day") {
			HashMap<Integer, Double> d = settings.getDayCharges();
			System.out.println("\nOriginal charge for day type " + Showtime.getDayStringFromChoice(choice) + 
					" is " + d.get(choice-1));
		}
		else
			return;
		System.out.print("\nEnter new charge: ");
		Double multiplier = sc.nextDouble();
		sc.nextLine();
		System.out.print("Confirm (Y|N): ");
		st = sc.nextLine();
		if (st.equalsIgnoreCase("Y")) {
			SettingsDao.save();
		}
		else
			return;
		if (className == "CinemaClass") {
			System.out.println("New cinema class charge: " + multiplier);
			settings.setCinemaClassCharges(choice, multiplier);
		}
		else if (className == "MovieType") {
			System.out.println("New movie type charge: " + multiplier);
			settings.setMovieTypeCharges(choice, multiplier);
		}
		else if (className == "AgeGroup") {
			System.out.println("New age group charge: " + multiplier);
			settings.setAgeGroupCharges(choice, multiplier);
		}
		else if (className == "Day") {
			System.out.println("New day type charge: " + multiplier);
			settings.setDayCharges(choice, multiplier);
		}
		System.out.println("Change updated.");
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
	
	public void printCharge() {
		int i;
		
		Settings s = SettingsDao.getSettings();
		Double basePrice = s.getBasePrice();
		System.out.println("\nBase price for a ticket: " + basePrice);
		
		HashMap<Integer, Double> cinemaClass = s.getCinemaClassCharges();
		System.out.println("\n<< CINEMA CLASS >>");
		i = 0;
		for (Double v: cinemaClass.values()) {
			System.out.println(Cinema.getCinemaClassStringFromChoice(i+1) +
					", Charge: " + v);
			i++;
		}
		
		HashMap<Integer, Double> movieType = s.getMovieTypeCharges();
		System.out.println("\n<< MOVIE TYPE >>");
		i = 0;
		for (Double v: movieType.values()) {
			System.out.println(Movie.getTypeStringFromChoice(i+1) +
					", Charge: " + v);
			i++;
		}

		HashMap<Integer, Double> ageGroup = s.getAgeGroupCharges();
		System.out.println("\n<< AGE GROUP >>");
		i = 0;
		for (Double v: ageGroup.values()) {
			System.out.println(Ticket.getAgeGroupStringFromChoice(i+1) +
					", Charge: " + v);
			i++;
		}

		HashMap<Integer, Double> day = s.getDayCharges();
		System.out.println("\n<< DAY TYPE >>");
		i = 0;
		for (Double v: day.values()) {
			System.out.println(Showtime.getDayStringFromChoice(i+1) +
					", Charge: " + v);
			i++;
		}
	}
	
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
		do{
			jump = false;
			choice = GeneralView.printMenuAndReturnChoice("Admin Panel > System Configuration > Public Holiday Management", menus);
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
	
	  public Day checkDayType(Showtime s) {
		  int check;
		  SimpleDateFormat formatter = new SimpleDateFormat("dd/M/yyyy");
		  Calendar cal = s.getDate();
		  int year = Calendar.getInstance().get(Calendar.YEAR);
		  for(String h: SettingsDao.getHolidays()){
			  String[] holidayParts = h.split("\\/");
			  if(Integer.parseInt(holidayParts[2]) == year) {
				  int month = Integer.parseInt(holidayParts[1]) - 1;
				  int date = Integer.parseInt(holidayParts[0]);
				  Calendar other = new GregorianCalendar(year, month, date);
				  String s1 = formatter.format(cal.getTime());
				  String s2 = formatter.format(other.getTime());
				  if (s1.compareTo(s2) == 0)
					  return Day.PUBLICHOLIDAY;
			  }
		  }
		  check = cal.get(Calendar.DAY_OF_WEEK);
		  if (check == 7 || check == 1)
			  return Day.WEEKEND;
		  else
			  return Day.WEEKDAY;
	  }

}
