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
	private final int Max = 99;

	public Movie() {
		this.casts = new ArrayList<String>();
		this.review = new Review[Max];
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
		if (this.review.length < 2) {
			return 0;
		}
		else {
			while (num < this.review.length) {
				this.rating += this.review[num].rating;
				num++;
			}
			return rating*1.0f/this.review.length;
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

}
