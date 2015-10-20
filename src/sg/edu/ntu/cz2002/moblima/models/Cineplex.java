package sg.edu.ntu.cz2002.moblima.models;
import java.util.Scanner;

public class Cineplex {
	public String name;
	private Cinema[] cinema;
	private int id;
	private int cinemaNum;
	
	public Cineplex(int cineplex_Id, String cineplex_Name, int cinemaNum) {
		this.id = cineplex_Id;
		this.name = cineplex_Name;
		this.cinema = new Cinema[cinemaNum];
		int i;
		for (i = 0; i < cinemaNum; i++) {
			cinema[i] = new Cinema(i);
		}
	}
	
	public String getName() {
		return name;
	}
	
	public int getId() {
		return id;
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
		this.id = cineplex_Id;
	}

	public void setCinemaNum(int num) {
		this.cinemaNum = num;
	}
	
	/*
	public void setCinema() {
		Scanner sc = new Scanner(System.in);
		int i;
		for (i = 0; i < this.cinemaNum; i++) {
			System.out.print("Cinema name: ");
			this.cinema[i].setName(sc.nextLine());
			System.out.print("Cinema Id: ");
			this.cinema[i].setId(sc.nextInt());
			System.out.print("Cinema class: ");
			this.cinema[i].setClass(sc.nextLine());
			System.out.print("Cinema number of halls: ");
			this.cinema[i].setHall(sc.nextInt());
		}
	}
	*/
}
