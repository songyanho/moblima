package sg.edu.ntu.cz2002.moblima;

public class Cineplex {
	public String name;
	private Cinema[] cinema;
	private int cineplexId;
	private int cinemaNum;
	
	public Cineplex(int cineplex_Id, String cineplex_Name, int cinemaNum) {
		cineplexId = cineplex_Id;
		name = cineplex_Name;
		cinema = new Cinema[cinemaNum];
		int i;
		for (i = 0; i < cinemaNum; i++) {
			cinema[i] = new Cinema();
		}
	}
	
	public String getName() {
		return name;
	}
	
	public int getId() {
		return cineplexId;
	}
	
	public void getCinema() {
		int i;
		for (i = 0; i < cinemaNum; i++) {
			System.out.println("Cinema name: " + cinema[i].getName() + ", Class: " + cinema[i].getClass() + ", " + cinema[i].getHall() + " halls.");
		}	
	}
	
	public void setName(String cineplex_Name) {
		name = cineplex_Name;
	}
	
	public void setId(int cineplex_Id) {
		cineplexId = cineplex_Id;
	}

}
