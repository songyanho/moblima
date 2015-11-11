package sg.edu.ntu.cz2002.moblima.models;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Scanner;
import java.util.TimeZone;

import sg.edu.ntu.cz2002.moblima.dao.MovieDao;
import sg.edu.ntu.cz2002.moblima.dao.SeatDao;
import sg.edu.ntu.cz2002.moblima.dao.SettingsDao;
import sg.edu.ntu.cz2002.moblima.dao.ShowtimeDao;
import sg.edu.ntu.cz2002.moblima.dao.TicketDao;
import sg.edu.ntu.cz2002.moblima.dao.TransactionDao;
import sg.edu.ntu.cz2002.moblima.models.Cinema.CinemaClass;
import sg.edu.ntu.cz2002.moblima.models.Movie.MovieType;
import sg.edu.ntu.cz2002.moblima.models.Seat.SeatType;
import sg.edu.ntu.cz2002.moblima.models.Showtime.Day;
import sg.edu.ntu.cz2002.moblima.models.Ticket.AgeGroup;
import sg.edu.ntu.cz2002.moblima.view.SeatPlaneView;

public class TicketManager {
	static Scanner sc = new Scanner(System.in);
	private Transaction t = new Transaction();
	private double total;
	protected ArrayList<Ticket> ticketList = new ArrayList<Ticket>();
	protected ArrayList<String> seatIds = new ArrayList<String>();
	
	public TicketManager() {}
	
	public void selectSeat(Showtime showtime) {
		seatIds = new ArrayList<String>();
		t = new Transaction();
		ticketList = new ArrayList<Ticket>();
		int choice;
		String seatId;
		HashMap<String, Integer> seatToSeatIdMap = SeatPlaneView.printSeatPlane(showtime);
		ArrayList<String> seatIdAlpha = new ArrayList<String>();
		seatIdAlpha.addAll(seatToSeatIdMap.keySet());

		do {
			int i = 0;
			System.out.println("\nTo book seat, please enter any seat ID from A1 to "+seatIdAlpha.get(seatIdAlpha.size()-1));
			System.out.println("To end seat selection, please enter \"END\"");
			System.out.print("\nEnter seat ID: ");
			seatId = sc.nextLine();
			seatId = seatId.toUpperCase();
			if (seatInputChecking(seatId)) {
				if(seatToSeatIdMap.get(seatId) < 0){
					System.out.println("Seat "+seatId+" is occupied.");
					continue;
				}
				else if(seatIds.contains(seatId)){
					System.out.println("You have already selected seat "+seatId);
					continue;
				}
				if(!seatToSeatIdMap.containsKey(seatId) || seatToSeatIdMap.get(seatId) == 0){
					System.out.println("Please select valid seat");
					continue;
				}

				seatIds.add(seatId);
				Ticket tick = new Ticket(t.getId());
				Ticket.printAgeGroupChoice();
				System.out.print("\nAge group for ticket " + (i+1) +": ");
				choice = sc.nextInt();
				sc.nextLine();
				tick.setAgeGroupFromChoice(choice);
				tick.setSeatId(seatToSeatIdMap.get(seatId));
				tick.setShowtime(showtime.getId());
				tick.setPrice(Math.round(calculatePrice(tick)));
				Seat thisSeat = SeatDao.findById(tick.getSeatId());
				thisSeat.setSeatName(seatId);
				SeatDao.save();
				ticketList.add(tick);
				i++;
			}
			else {
				if(seatId.equalsIgnoreCase("END"))
					break;
				System.out.println("Please enter seat in the format of (alphabet + digit)");
				continue;
			}
		} while (!seatId.equalsIgnoreCase("end"));
		total = 0;
		for (int i = 0; i < seatIds.size(); i++) {
			System.out.println("\n<< Ticket " + (i+1) + " >>");
			printTicket(ticketList.get(i));
			total += ticketList.get(i).getPrice();
		}
	}
	
	private boolean seatInputChecking(String seatId) {
		char c1, c2, c3;
		int length = seatId.length();
		if (length < 2 || length > 3)
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
	
	public void checkout(Showtime showtime) {
		String st;
		boolean exit = false;
		if(ticketList.size()<=0){
			System.out.println("No tickets booked. Transaction is cancelled.");
			return;
		}
		System.out.println("Total price is " + Math.round(total));
		System.out.println("\nConfirm booking (Y|N): ");
		do {
			st = sc.nextLine();
			if (st.equalsIgnoreCase("Y")) {	
				String name, email, mobileNumber, st2;
				boolean infoExit = false;

				do {
					System.out.print("\nYour name: ");
					name = sc.nextLine();
					name = name.toUpperCase();
					System.out.print("Your email: ");
					email = sc.nextLine();
					System.out.print("Your mobile number: ");
					mobileNumber = sc.nextLine();
					System.out.println("\n<< Your info >>");
					System.out.println("\tName: " + name);
					System.out.println("\tEmail: " + email);
					System.out.println("\tMobile number: " + mobileNumber);
					System.out.println("\nConfirm record (Y|N)");
					st2 = sc.nextLine();
					if (st2.equalsIgnoreCase("Y")) {
						infoExit = true;
					}
				} while (!infoExit);
				t.setName(name.toUpperCase()); t.setEmail(email); t.setMobileNumber(mobileNumber);
				t.setTID(showtime); t.setTotal(total);
				TransactionDao.add(t);
				for (int i = 0; i < seatIds.size(); i++) {
					Ticket tt = ticketList.get(i);
					tt.setId(TicketDao.getLastId() + 1);
					TicketDao.add(tt);
					System.out.print("\nTransaction ID " + t.getTID());
					System.out.print("\n");
					printTicket(tt);
				}
				exit = true;
			}
			else if (st.equalsIgnoreCase("N"))
				exit = true;
			else
				exit = false;
		} while (!exit);
	}
	
	public void viewBookingHistory() {
		String name;
		HashMap <Integer, Transaction> trans;
		System.out.print("\nYour name: ");
		name = sc.nextLine();
		trans = TransactionDao.findByName(name.toUpperCase());
		if (trans.isEmpty())
			System.out.println("\nNo record found.");
		else {
			for (Transaction t: trans.values()) {
				t.printTransaction();
			}
		}
	}
	
	  public static Day checkDayType(Showtime s) {
		  int check;
		  SimpleDateFormat formatter = new SimpleDateFormat("d/M/yyyy");
		  Calendar cal = s.getDate();
		  cal.setTimeZone(TimeZone.getTimeZone("Asia/Singapore"));
		  String showtimeDateString = formatter.format(cal.getTime());
		  if(SettingsDao.getHolidays().contains(showtimeDateString))
			  return Day.PUBLICHOLIDAY;
//		  int year = Calendar.getInstance().get(Calendar.YEAR);
//		  for(String h: SettingsDao.getHolidays()){
//			  String[] holidayParts = h.split("\\/");
//			  if(Integer.parseInt(holidayParts[2]) == year) {
//				  int month = Integer.parseInt(holidayParts[1]);
//				  int date = Integer.parseInt(holidayParts[0]);
//				  Calendar other = new GregorianCalendar(year, month, date);
//				  String s1 = formatter.format(cal.getTime());
//				  String s2 = formatter.format(other.getTime());
//				  if (s1.compareTo(s2) == 0)
//					  return Day.PUBLICHOLIDAY;
//			  }
//		  }
		  check = cal.get(Calendar.DAY_OF_WEEK);
		  if (check == 7 || check == 1)
			  return Day.WEEKEND;
		  else
			  return Day.WEEKDAY;
	  }
	  
		public double calculatePrice(Ticket t) {
			double classCharge, dayCharge, typeCharge, ageCharge, basePrice, seatPrice;
			Showtime st = ShowtimeDao.findById(t.getShowtime());
			Settings settings = SettingsDao.getSettings();
			//Ticket t = TicketDao.findByShowtimeId(showtimeId);
			MovieType mt = st.getMovie().getType();
			CinemaClass cc = st.getCinema().getCinemaClass();
			Day day = checkDayType(st);
			if (day != st.getDayType()) {
				st.setDayType(day);
				ShowtimeDao.save();
			}
			AgeGroup ag = t.getAgeGroup();
			SeatType seatType = t.getSeat().getSeatType();
			classCharge = settings.getCinemaClassCharges().get(cc);
			dayCharge = settings.getDayCharges().get(day);
			typeCharge = settings.getMovieTypeCharges().get(mt);
			ageCharge = settings.getAgeGroupCharges().get(ag);
			basePrice = settings.getBasePrice();
			seatPrice = settings.getSeatTypeCharges().get(seatType);
			return (basePrice + classCharge + dayCharge + typeCharge + ageCharge) * seatPrice;
		} 
		
		public void printTicket(Ticket t) {
			Cineplex cineplex = ShowtimeDao.findById(t.getShowtime()).getCineplex();
			Cinema cinema = ShowtimeDao.findById(t.getShowtime()).getCinema();
			Showtime s = ShowtimeDao.findById(t.getShowtime());
			Movie m = MovieDao.findById(s.getMovieId());
			System.out.println("Cineplex: " + cineplex.getCineplexName() + ", " + cinema.getName() + " <" + cinema.getCinemaClassString() + ">");
			System.out.println("Movie: " + m.getTitle() + " <" + Movie.getTypeStringFromMovieType(m.getType()) + ">");
			System.out.println("Age group: " + t.getAgeGroupString());
			System.out.println("Day type: " + Showtime.getDayStringFromDay(s.getDayType()));
			System.out.println("Seat ID: " + t.getSeat().getSeatName() +" <"+Seat.getSeatTypeStringFromSeatType(t.getSeat().getSeatType())+">");
			System.out.println("Ticket price: " + Math.round(t.getPrice()));
			System.out.print("\n");
		}
}
