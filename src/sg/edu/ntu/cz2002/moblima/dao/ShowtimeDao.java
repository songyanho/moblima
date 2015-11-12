package sg.edu.ntu.cz2002.moblima.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.json.simple.JSONObject;

import sg.edu.ntu.cz2002.moblima.CalendarView;
import sg.edu.ntu.cz2002.moblima.database.Database;
import sg.edu.ntu.cz2002.moblima.models.*;
import sg.edu.ntu.cz2002.moblima.models.Movie.MovieStatus;

public class ShowtimeDao {
	protected static final String DATABASE_NAME = "showtime";
	
	protected static HashMap<Integer, Showtime> records;
	
	public static HashMap<Integer, Showtime> getAllInHashMap() {
		if(records == null) initialize();
		return records;
	}
	
	public static int getLastId(){
		if(records == null) initialize();
		Set<Integer> ids = records.keySet();
		if(ids.size() == 0) return 0;
		List<Integer> a = new ArrayList<Integer>();
		for(Integer i: ids)
			a.add(i);
		Collections.sort(a);
		return a.get(a.size()-1);
	}
	
	public static<T extends Comparable<? super T>> List<T> asSortedList(Collection<T> c) {
	  List<T> list = new ArrayList<T>(c);
	  java.util.Collections.sort(list);
	  return list;
	}
	
	public static Showtime findById(int id) {
		if(records == null) initialize();
		if(records.containsKey(id))
			return records.get(id);
		return null;
	}
	
	public static HashMap<Movie, ArrayList<Showtime>> getAllOnDate(Calendar c, Cineplex cineplex){
		if(records == null) initialize();
		HashMap<Movie, ArrayList<Showtime>> movieWithShowtime = new HashMap<Movie, ArrayList<Showtime>>();
		HashMap<Movie, HashMap<Long, Showtime>> m = new HashMap<Movie, HashMap<Long, Showtime>>();
		for(Showtime s: records.values()){
			if(CalendarView.sameDay(c, s.getDate()) && cineplex.getId() == s.getCineplexId() && s.getMovie().getStatus()!=MovieStatus.ENDOFSHOWING){
				if(!m.containsKey(s.getMovie())){
					movieWithShowtime.put(s.getMovie(), new ArrayList<Showtime>());
					m.put(s.getMovie(), new HashMap<Long, Showtime>());
				}
				m.get(s.getMovie()).put(s.getDate().getTimeInMillis(), s);
			}
		}
		for(Movie mm: m.keySet()){
			Collection<Long> unsorted = m.get(mm).keySet();
			List<Long> sorted = asSortedList(unsorted);
			for(Long ll: sorted)
				movieWithShowtime.get(mm).add(m.get(mm).get(ll));
		}
		return movieWithShowtime;
	}
	
	public static HashMap<Cineplex, ArrayList<Showtime>> getAllOnDate(Calendar c, Movie movie){
		if(records == null) initialize();
		HashMap<Cineplex, ArrayList<Showtime>> movieWithShowtime = new HashMap<Cineplex, ArrayList<Showtime>>();
		HashMap<Cineplex, HashMap<Long, Showtime>> m = new HashMap<Cineplex, HashMap<Long, Showtime>>();
		for(Showtime s: records.values()){
			if(CalendarView.sameDay(c, s.getDate()) && movie.getId() == s.getMovieId() && s.getMovie().getStatus()!=MovieStatus.ENDOFSHOWING){
				if(!m.containsKey(s.getCineplex())){
					movieWithShowtime.put(s.getCineplex(), new ArrayList<Showtime>());
					m.put(s.getCineplex(), new HashMap<Long, Showtime>());
				}
				m.get(s.getCineplex()).put(s.getDate().getTimeInMillis(), s);
			}
		}
		for(Cineplex mm: m.keySet()){
			Collection<Long> unsorted = m.get(mm).keySet();
			List<Long> sorted = asSortedList(unsorted);
			for(Long ll: sorted)
				movieWithShowtime.get(mm).add(m.get(mm).get(ll));
		}
		return movieWithShowtime;
	}
	
	public static ArrayList<Showtime> getAllOnDate(Calendar c, Cinema cnm){
		if(records == null) initialize();
		ArrayList<Showtime> t = new ArrayList<Showtime>();
		for(Showtime s: records.values()){
			if(s.getCinemaId() == cnm.getId() && CalendarView.sameDay(c, s.getDate()) && s.getMovie().getStatus()!=MovieStatus.ENDOFSHOWING){
				t.add(s);
			}
		}
		return t;
	}
	
	public static boolean deleteShowtimeWithId(int id){
		if(records == null) initialize();
		if(!records.containsKey(id)) return false;
		records.remove(id);
		return save();
	}
	
	public static boolean deleteAllWithMovieId(int movieId){
		if(records == null) initialize();
		for(Showtime s: records.values()){
			if(s.getMovieId() == movieId)
				records.remove(s.getId());
		}
		return save();
	}
	
	public static boolean hasShowtime(int showtimeId) {
		if(records == null) initialize();
		for (Showtime s: records.values()) {
			if (s.getId() == showtimeId)
				return true;
		}
		return false;
	}
	
	public static boolean save(Showtime t) {
		if(records == null) initialize();
		records.put(t.getId(), t);
		return save();
	}
	
	public static boolean save(){
		if(records == null) initialize();
		return Database.save(DATABASE_NAME, Showtime.toJSONObjects(records));
	}
	
	public static boolean add(Showtime t){
		if(records == null) initialize();
		if(records.containsKey(t.getId())) return false; // Records exists
		records.put(t.getId(), t);
		save();
		return true;
	}

	public static ArrayList<Integer> getOccupiedSeats(int showtimeId) {
		ArrayList<Integer> occupiedSeats = new ArrayList<Integer>();
		HashMap<Integer, Ticket> tickets = TicketDao.getAllInHashMap();
		for (Ticket t: tickets.values()) {
			if (t.getShowtime() == showtimeId) {
				occupiedSeats.add(t.getSeatId());
			}
		}
		return occupiedSeats;
	}
	
	public static void initialize() {
		JSONObject t = Database.getObject(DATABASE_NAME);
		records = Showtime.fromJSONObjects(t);
	}

}