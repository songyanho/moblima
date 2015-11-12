package sg.edu.ntu.cz2002.moblima.models;
import java.util.HashMap;
import java.util.Set;

import org.json.simple.JSONObject;

import sg.edu.ntu.cz2002.moblima.dao.CinemaDao;
import sg.edu.ntu.cz2002.moblima.dao.SeatPlaneDao;

public class Cinema {
	protected int id;
	protected String name;
	protected CinemaClass cinemaClass;
	protected int cineplexId;
	protected int seatPlaneId;

	public enum CinemaClass {
		PREMIUM, PLATINUM, GOLD, NORMAL;
	}

	public Cinema() {
		this.id = CinemaDao.getLastId() + 1;
	}

	/**
	 * Constructor strictly for Database Initialization
	 * @param id Unique ID of Cinema
	 * @param name Name of Cinema
	 * @param cinemaClass Cinema Class in ordinal
	 * @param cineplexId Unique ID points to parent Cineplex
	 * @param seatPlaneId Unique ID of the seat plane
	 */
	public Cinema(int id, String name, int cinemaClass, int cineplexId, int seatPlaneId) {
		this.id = id;
		this.name = name;
		this.cinemaClass = Cinema.getCinemaClassEnumFromOrdinal(cinemaClass);
		this.cineplexId = cineplexId;
		this.seatPlaneId = seatPlaneId;
	}

	/**
	 * Set the respective CinameClass based on user input
	 * @param choice Integer from 1 to 4
	 */
	public void setCinemaClassFromChoice(int choice) {
		switch (choice) {
		case 1: 
			this.cinemaClass = CinemaClass.PREMIUM;
			break;
		case 2:
			this.cinemaClass = CinemaClass.PLATINUM;
			break;
		case 3:
			this.cinemaClass = CinemaClass.GOLD;
			break;
		default:
			this.cinemaClass = CinemaClass.NORMAL;
		}
	}

	/**
	 * Get the CinemaClass based on user input
	 * @param choice Integer from 1 to 4
	 * @return Respective CinemaClass based on user input
	 */
	public static CinemaClass getCinemaClassEnumFromChoice(int choice) {
		return choice == 1? CinemaClass.PREMIUM:
			   choice == 2? CinemaClass.PLATINUM:
			   choice == 3? CinemaClass.GOLD:
				   			CinemaClass.NORMAL;
	}

	/**
	 * Assign enum CinemaClass from its ordinal value 
	 * @param ordinal
	 * @return 
	 */
	public static CinemaClass getCinemaClassEnumFromOrdinal(int ordinal) {
		return ordinal == CinemaClass.PREMIUM.ordinal() ? CinemaClass.PREMIUM:
			   ordinal == CinemaClass.PLATINUM.ordinal() ? CinemaClass.PLATINUM:
			   ordinal == CinemaClass.GOLD.ordinal() ? CinemaClass.GOLD:
				   CinemaClass.NORMAL;
	}

	/**
	 * Return the name of enum CinemaClass from user choice
	 * @param choice
	 * @return "Premium", "Platinum", "Gold", "Normal"
	 */
	public static String getCinemaClassStringFromChoice(int choice) {
		return choice == 1? "Premium":
			   choice == 2? "Platinum":
			   choice == 3? "Gold":
				   			"Normal";
	}

	/**
	 * Return the name of enum CinemaClass from CinemaClass itself
	 * @param choice
	 * @return "Premium", "Platinum", "Gold", "Normal"
	 */
	public static String getCinemaClassStringFromCinemaClass(CinemaClass choice) {
		return choice == CinemaClass.PREMIUM? "Premium":
			   choice == CinemaClass.PLATINUM? "Platinum":
			   choice == CinemaClass.GOLD? "Gold":
				   			"Normal";
	}

	/**
	 * Print all the available choices in CinemaClass
	 */
	public static void printCinemaClassChoice() {
		for (CinemaClass cc: CinemaClass.values())
			System.out.print("\t" + (cc.ordinal()+1) + ". " + cc.name());
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCineplexId() {
		return cineplexId;
	}

	public void setCineplexId(int cineplexId) {
		this.cineplexId = cineplexId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}


	/**
	 * Only reset CinemaClass during initialization
	 * @param cinemaClass CinemaClass enumeration
	 */
	@Deprecated
	public void setCinemaClass(CinemaClass cinemaClass) {
		this.cinemaClass = cinemaClass;
	}

	public CinemaClass getCinemaClass() {
		return cinemaClass;
	}

	/**
	 * Get Presentable String of respective CinemaClass
	 * @return Presentable String of CinemaClass
	 */
	public String getCinemaClassString() {
		return this.cinemaClass == CinemaClass.PREMIUM? "Premium":
			   this.cinemaClass == CinemaClass.PLATINUM? "Platinum":
			   this.cinemaClass == CinemaClass.GOLD? "Gold":
				   	"Normal";
	}
	
	public int getSeatPlaneId() {
		return seatPlaneId;
	}

	/**
	 * Retrieve respective SeatPlane based on the seatPlaneId
	 * @return SeatPlane of this cinema
	 */
	public SeatPlane getSeatPlane(){
		return SeatPlaneDao.findById(this.seatPlaneId);
	}

	public void setSeatPlaneId(int seatPlaneId) {
		this.seatPlaneId = seatPlaneId;
	}

	@SuppressWarnings("unchecked")
	public JSONObject toJSONObject() {
		JSONObject o = new JSONObject();
		o.put("id", this.id);
		o.put("name", this.name);
		o.put("cinemaClass", this.cinemaClass.ordinal());
		o.put("cineplexid", this.cineplexId);
		o.put("seatplaneid", this.seatPlaneId);
		return o;
	}

	public static Cinema fromJSONObject(JSONObject o){
		return new Cinema(
				Integer.parseInt(o.get("id").toString()), 
				o.get("name").toString(), 
				Integer.parseInt(o.get("cinemaClass").toString()), 
				Integer.parseInt(o.get("cineplexid").toString()),
				Integer.parseInt(o.get("seatplaneid").toString())
				);
	}

	public static HashMap<String, JSONObject> toJSONObjects(HashMap<Integer, Cinema> o){
		HashMap<String, JSONObject> a = new HashMap<String, JSONObject>();
		Set<Integer> s = o.keySet();
		for(Integer i: s){
			a.put(""+i, o.get(i).toJSONObject());
		}
		return a;
	}

	@SuppressWarnings("unchecked")
	public static HashMap<Integer, Cinema> fromJSONObjects(JSONObject o){
		HashMap<Integer, Cinema> a = new HashMap<Integer, Cinema>();
		Set<String> s = o.keySet();
		for(String i: s){
			JSONObject n = (JSONObject) o.get(i);
			Cinema t =  Cinema.fromJSONObject(n);
			a.put(t.getId(), t);
		}
		return a;
	}
}
