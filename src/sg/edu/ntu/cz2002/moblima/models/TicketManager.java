package sg.edu.ntu.cz2002.moblima.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import sg.edu.ntu.cz2002.moblima.dao.SeatDao;
import sg.edu.ntu.cz2002.moblima.dao.TicketDao;
import sg.edu.ntu.cz2002.moblima.dao.TransactionDao;
import sg.edu.ntu.cz2002.moblima.view.SeatPlaneView;

public class TicketManager {
	static Scanner sc = new Scanner(System.in);
	private Transaction t = new Transaction();
	private double total;
	public ArrayList<Integer> showtimeList = new ArrayList<Integer>();
	public ArrayList<Ticket> ticketList = new ArrayList<Ticket>();
	public ArrayList<String> seatIds = new ArrayList<String>();
	

	public TicketManager() {}
	
	public void selectSeat(Showtime showtime) {
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
				tick.setPrice(Math.round(tick.calculatePrice()));
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
			ticketList.get(i).printTicket();
			total += ticketList.get(i).getPrice();
		}
	}
	
	private boolean seatInputChecking(String seatId) {
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
	
	public void checkout(Showtime showtime) {
		String st;
		boolean exit = false;
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
				t.setTID(showtime);
				TransactionDao.add(t);
				for (int i = 0; i < seatIds.size(); i++) {
					Ticket tt = ticketList.get(i);
					tt.setId(TicketDao.getLastId() + 1);
					TicketDao.add(tt);
					System.out.print("\nTransaction " + (i+1));
					tt.printTicket();
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
}
