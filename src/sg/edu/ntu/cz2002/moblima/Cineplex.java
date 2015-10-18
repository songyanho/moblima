package sg.edu.ntu.cz2002.moblima;

public class Cineplex {
	public String name;
	private Cinema[] cinema;
	private int cineplexId;
	private int cinemaNum;
	
	public Cineplex(int cineplex_Id, String cineplex_Name, int cinemaNum) {
		this.cineplexId = cineplex_Id;
		this.name = cineplex_Name;
		this.cinema = new Cinema[cinemaNum];
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
	
	public int getCinemaNum() {
		return cinemaNum;
	}
	
	public void setName(String cineplex_Name) {
		this.name = cineplex_Name;
	}
	
	public void setId(int cineplex_Id) {
		this.cineplexId = cineplex_Id;
	}

	private void setCinemaNum(int num) {
		this.cinemaNum = num;
	}
}
