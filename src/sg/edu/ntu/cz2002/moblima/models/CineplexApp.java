package sg.edu.ntu.cz2002.moblima.models;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import sg.edu.ntu.cz2002.moblima.dao.MovieDao;
import sg.edu.ntu.cz2002.moblima.dao.ShowtimeDao;
import sg.edu.ntu.cz2002.moblima.dao.TicketDao;
import sg.edu.ntu.cz2002.moblima.view.GeneralView;

public class CineplexApp {
	CineplexManager cineplexMgr;
	
	public CineplexApp(CineplexManager cineplexMgr){
		this.cineplexMgr = cineplexMgr;
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

	/**
	 * Main interface of admin
	 */
	public void printView(){
		int choice;
		String[] menus = {"Public Holiday Management", "Ticket Charges Management","Back to previous menu"};
		do{
			choice = GeneralView.printMenuAndReturnChoice("Admin Panel > System Configuration", menus);
			switch(choice){
			case 1:
				cineplexMgr.runAdminHolidayManagement();
				break;
			case 2:
				cineplexMgr.runAdminChargesManagement();
				break;
			default: return;
			}
		}while(true);
	}
	
	/**
	 * List ranking of the movie by ticket sales or by overall reviews' rating
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void listRanking() {
	    String[] menus = {"By ticket sales", "By overall reviews' rating", "Back to previous menu"};
		HashMap<String, Double> rankList = new HashMap<String, Double>();
	    int choice = GeneralView.printMenuAndReturnChoice("Admin Panel > List top 5 ranking", menus);
	    if (choice == 1) {
	    	Double sales;
	    	String movie;
	    	HashMap<Integer, Ticket> tickets = TicketDao.getAllInHashMap();
	    	for (Ticket t: tickets.values()) {
	    		movie = ShowtimeDao.findById(t.getShowtime()).getMovie().getTitle();
	    		if (rankList.containsKey(movie)) {
	    			sales = rankList.get(movie);
	    			rankList.put(movie, ++sales);
	    		}
	    		else {
	    			rankList.put(movie, 1.0);
	    		}
	    	}
	    }
	    else {
			double rating;
			HashMap<Integer, Movie> movies = MovieDao.getAllInHashMap();
			for (Movie m: movies.values()) {
				rating = cineplexMgr.calculateOverallRating(m.getId());
				if (rating != 0)
					rankList.put(m.getTitle(), rating);
			}
	    }
	    Map<String, Double> map = cineplexMgr.sortByValues(rankList);
	    System.out.println("\nThe top 5 ranking");
	    for (int i = 0; i < 68; i++)
	    	System.out.print("-");
	    Set set = map.entrySet();
	    Iterator it = set.iterator();
	    int index = 1;
	    if (set.isEmpty() && choice == 1) {
	    	System.out.println("\nNo ticket sales in all movies.");
	    }
	    if (set.isEmpty() && choice == 2) {
	    	System.out.println("\nNo rating is available in all movies.");
	    }
	    int limit = 5;
	    while(it.hasNext()) {
	    	Map.Entry entry = (Map.Entry)it.next();
	    	if (!Double.isNaN(Double.parseDouble(entry.getValue().toString())) && limit != 0) {
	    		if (choice == 1) {
	    			System.out.print("\nRank " + index + ". << " + entry.getKey() + " >>, Total sales = ");
	    			System.out.format("%.0f", entry.getValue());
	    		}
	    		if (choice == 2) {
	    			System.out.print("\nRank " + index + ". << " + entry.getKey() + " >>, rating = ");
	    			System.out.format("%.1f", entry.getValue());
	    		}
	    		limit--;
	    		index++;
	    	}
	    }
	}
}
