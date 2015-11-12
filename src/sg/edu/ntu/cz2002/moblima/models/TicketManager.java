package sg.edu.ntu.cz2002.moblima.models;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
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

	/**
	 * Select seat for a specific showtime and check for correct style of seat input format
	 * @param showtime
	 */
	public void selectSeat(Showtime showtime) {
		seatIds = new ArrayList<String>();
		t = new Transaction();
		ticketList = new ArrayList<Ticket>();
		int choice;
		String seatId;
		HashMap<String, Integer> seatToSeatIdMap = SeatPlaneView.printSeatPlane(showtime);
		HashMap<String, ArrayList<Integer>> selectedSeats = new HashMap<String, ArrayList<Integer>>();
		ArrayList<String> seatIdAlpha = new ArrayList<String>();
		seatIdAlpha.addAll(seatToSeatIdMap.keySet());
		Collections.sort(seatIdAlpha);

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
				if(!alternateSeatBooking(seatToSeatIdMap, selectedSeats, seatId)){
					System.out.println("Please do not leave a single unoccupied seat between selected seat.");
					continue;
				}
				seatIds.add(seatId);
				int seatIdd = Math.abs(seatToSeatIdMap.get(seatId));
				Seat thisSeat = SeatDao.findById(seatIdd);
				if(thisSeat.getSeatType()==SeatType.COUPLE){
					String row = seatId.charAt(0)+"";
					Integer column = Integer.parseInt(seatId.replaceAll("[^0-9]", ""));
					if(seatToSeatIdMap.containsKey(row+(column-1)) && Math.abs(seatToSeatIdMap.get(row+(column-1)))==seatIdd){
						seatIds.add(row+(column-1));
					}
					if(seatToSeatIdMap.containsKey(row+(column+1)) && Math.abs(seatToSeatIdMap.get(row+(column+1)))==seatIdd){
						seatIds.add(row+(column+1));
					}
				}
				Ticket tick = new Ticket(t.getId());
				Ticket.printAgeGroupChoice();
				System.out.print("\nAge group for ticket " + (i+1) +": ");
				choice = sc.nextInt();
				sc.nextLine();
				tick.setAgeGroupFromChoice(choice);
				tick.setSeatId(seatToSeatIdMap.get(seatId));
				tick.setShowtime(showtime.getId());
				tick.setPrice(Math.round(calculatePrice(tick)));
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

	/**
	 * Method to validate whether a seat ID inputed is valid or not
	 * @param seatId
	 * @return
	 */
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

	/**
	 * Method to check and prevent the user from leaving a single unoccupied seat between selected seat
	 * @param seatToSeatId
	 * @param selectedSeats
	 * @param nextSeat
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private boolean alternateSeatBooking(HashMap<String, Integer> seatToSeatId, HashMap<String, ArrayList<Integer>> selectedSeats, String nextSeat){
		String row = nextSeat.charAt(0)+"";
		Integer column = Integer.parseInt(nextSeat.replaceAll("[^0-9]", ""));
		if(!selectedSeats.containsKey(row)){
			ArrayList<Integer> columns = new ArrayList<Integer>();
			columns.add(column);
			selectedSeats.put(row, columns);
			return true;
		}
		Seat seat = SeatDao.findById(Math.abs(seatToSeatId.get(nextSeat)));
		if(seat.getSeatType() == SeatType.COUPLE){
			ArrayList<Integer> columns = selectedSeats.get(row);
			columns.add(column);
			if(seatToSeatId.containsKey(row+(column-1)) && seatToSeatId.get(row+(column-1)) == seatToSeatId.get(row+column))
				columns.add(column-1);
			else
				columns.add(column+1);
			return true;
		}
		ArrayList<Integer> thisRow = (ArrayList<Integer>) selectedSeats.get(row).clone();
		thisRow.add(column);
		Collections.sort(thisRow);
		int i=0;
		for(Integer sc: thisRow){
			if(i==0){
				i=sc;
				continue;
			}
			if(sc-i!=2){
				i=sc;
				continue;
			}
			if(seatToSeatId.get(row+(sc-1)) > 0)
				return false;
		}
		selectedSeats.replace(row, thisRow);
		return true;
	}

	/**
	 * Interface to get confirmation of booking of tickets from user. After capturing user's name, email and mobile number
	 * A transaction and ticket info will be printed
	 * @param showtime
	 */
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

	/**
	 * Interface to allow the user to check his booking history
	 */
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

	/**
	 * Method to check for the date of showtime belongs to which category of Enum Day, such as public holiday, weekday and weekend
	 * @param s
	 * @return
	 */
	public static Day checkDayType(Showtime s) {
		int check;
		SimpleDateFormat formatter = new SimpleDateFormat("d/M/yyyy");
		Calendar cal = s.getDate();
		cal.setTimeZone(TimeZone.getTimeZone("Asia/Singapore"));
		String showtimeDateString = formatter.format(cal.getTime());
		if(SettingsDao.getHolidays().contains(showtimeDateString))
			return Day.PUBLICHOLIDAY;
		check = cal.get(Calendar.DAY_OF_WEEK);
		if (check == 7 || check == 1)
			return Day.WEEKEND;
		else
			return Day.WEEKDAY;
	}

	/**
	 * Method to calculate the price of a ticket based on cinema class, day type, movie type, age group, seat type and base price
	 * @param t
	 * @return
	 */
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

	/**
	 * Method to print the ticket information such as cineplex, cinema, movie, age group, day type, seat Id and its price
	 * @param t
	 */
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
