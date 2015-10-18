package sg.edu.ntu.cz2002.moblima;
import java.util.Scanner;

public class Cinema {
	public String name;
	public String cinemaClass;
	public int hall;
	private int cinemaId;
	
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
	
	public Cinema(String cinema_Name, int hallNum, String cinema_Class) {
		name = cinema_Name;
		hall = hallNum;
		cinemaClass = cinema_Class;
	}
	
	public String getName() {
		return name;
	}
	
	public int getId() {
		return cinemaId;
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
		this.cinemaId = cinema_Id;
	}
	
	public void setClass(String cinema_Class) {
		this.cinemaClass = cinema_Class;
	}
	
	public void setHall(int number) {
		this.hall = number;
	}
}
