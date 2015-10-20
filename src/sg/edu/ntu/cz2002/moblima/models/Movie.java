package sg.edu.ntu.cz2002.moblima.models;
import java.util.ArrayList;
import java.util.Scanner;

public class Movie {
	protected String title;
	protected int status;
	protected String synopsis;
	protected String director;
	protected ArrayList<String> casts;
	protected double rating;
	protected int id;
	protected Review[] review;
	private int numReview;
	private final int Max = 99;
	private Seat[] seat;
	private int seatNum;
	private int numEmptySeat;

	public Movie() {
		/*
		Scanner sc = new Scanner(System.in);
		int i = 0; int j = 0;
		
		System.out.print("Movie title:");
		this.title = sc.nextLine();
		
		System.out.print("Movie status:");
		this.status = sc.nextInt();
		
		System.out.print("Sypnosis:");
		this.synopsis = sc.nextLine();
		
		System.out.print("Director:");
		this.director = sc.nextLine();
		
		this.casts = new String[Max];
		System.out.print("Casts:");
		String name = sc.nextLine();
		while (name != "end") {
			this.casts[i] = name;
			name = sc.nextLine();
		}
		
		System.out.print("Movie Id:");
		this.moveId = sc.nextInt();
		
		this.rating = 0; //no rating entered before
		this.review = new Review[Max];
		this.numReview = 0;
		*/
		this.casts = new ArrayList<String>();
		this.review = new Review[Max];
		this.numReview = 0;
		this.seatNum = 300;
		this.seat = new Seat[this.seatNum];
		for (int i = 0; i < this.seatNum; i++)
			this.seat[i] = new Seat(i);
	}
	
	public String getTitle() {
		return title;
	}
	
	public int getStatus() {
		return status;
	}
	
	public String getSynopsis() {
		return synopsis;
	}
	
	public String getDirector() {
		return director;
	}
	
	public ArrayList<String> getCast() {
		return casts;
	}
	
	public double getRating() {
		int num = 0;
		if (this.numReview < 2) {
			System.out.println("NA");
			return 0;
		}
		else {
			while (num < this.numReview) {
				this.rating += this.review[num].rating;
				num++;
			}
			return rating*1.0f/this.numReview;
		}
	}
	
	public int getMovieId() {
		return id;
	}
	
	public Review[] getReview() {
		return review;
	}
	
	public void setTitle(String movie_Title) {
		this.title = movie_Title;
	}
	
	public void setStatus(int movie_Status) {
		this.status = movie_Status;
	}
	
	public void setSynopsis(String movie_Sypnosis) {
		this.synopsis = movie_Sypnosis;
	}
	
	public void setDirector(String movie_Director) {
		this.director = movie_Director;
	}
	
	public void addCast(String name) {
		this.casts.add(name);
	}
	
	public void setCasts(ArrayList<String> casts){
		this.casts = (ArrayList<String>) casts.clone();
	}
		
	public void setRating(double rate) {
		this.rating = rate;
	}
	
	public void setMovieId(int movie_Id) {
		this.id = movie_Id;
	}
	
	
	public int getSeatNum() {
		return seatNum;
	}

	public void setSeatNum(int seatNum) {
		this.seatNum = seatNum;
	}

	public void assign(int seatId, int ticketId) {
		if (seat[seatId].getId() == 0) {
			seat[seatId].assign(ticketId);
			this.setNumEmptySeat(this.getNumEmptySeat() - 1);
		}
		else
			System.out.println("Seat already assigned to a customer.");
	}
	/*
	public void setReview() {
		int index = this.numReview;
		this.review[index] = new Review(index);
		Scanner sc = new Scanner(System.in);
		System.out.print("Your name:");
		this.review[index].setName(sc.nextLine());
		System.out.print("Rating: ");
		this.review[index].setRating(sc.nextInt());
		System.out.print("Review: ");
		this.review[index].setComment(sc.nextLine());
		this.numReview++;
	}
	*/

	public int getNumEmptySeat() {
		return numEmptySeat;
	}

	public void setNumEmptySeat(int numEmptySeat) {
		this.numEmptySeat = numEmptySeat;
	}
}
