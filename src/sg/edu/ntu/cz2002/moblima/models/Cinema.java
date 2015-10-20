package sg.edu.ntu.cz2002.moblima.models;
import java.util.Scanner;

public class Cinema {
	protected String name;
	protected String cinemaClass;
	protected int hall;
	protected int id;
	// protected Ticket[] ticket;
	
	/*
	public Cinema() {
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter cinema name:");
		String cinema_Name = sc.nextLine();
		System.out.println("Enter cinema Id:");
		int cinema_Id = sc.nextInt();
		System.out.println("Enter cinema class:");
		String cinema_Class = sc.nextLine();
		System.out.println("Enter cinema number of halls:");
		int hallNum = sc.nextInt();
		this.name = cinema_Name;
		this.cinemaId = cinema_Id;
		this.cinemaClass = cinema_Class;
		this.hall = hallNum;
	}
	*/
	
	public Cinema(int index) {
		this.id = index;
		this.hall = -1;
	}
	
	public String getName() {
		return name;
	}
	
	public int getId() {
		return id;
	}
	
	public String getcinemaClass() {
		return cinemaClass;
	}
	
	public int getHall() {
		return hall;
	}
	
	public void setName(String cinema_Name) {
		this.name = cinema_Name;
	}
	
	public void setId(int cinema_Id) {
		this.id = cinema_Id;
	}
	
	public void setClass(String cinema_Class) {
		this.cinemaClass = cinema_Class;
	}
	
	public void setHall(int number) {
		this.hall = number;
	}
}
