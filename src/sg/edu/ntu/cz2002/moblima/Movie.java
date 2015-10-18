package sg.edu.ntu.cz2002.moblima;
import java.util.Scanner;

public class Movie {
	private String title;
	private int status;
	private String synopsis;
	private String director;
	private String[] casts;
	private double rating;
	private int movieId;
	private Review[] review;
	private final Max = 99;
	
	public Movie() {
		Scanner sc = new Scanner(System.in);
		int i = 0; int j = 0;
		String[] Casts = new String[Max];
		
		System.out.print("Movie title:");
		this.title = sc.nextLine();
		
		System.out.print("Movie status:");
		this.status = sc.nextInt();
		
		System.out.print("Sypnosis:");
		this.synopsis = nextLine();
		
		System.out.print("Director:");
		this.director = sc.nextLine();
		
		System.out.print("Casts:");
		String name = sc.nextLine();
		while (name != "end") {
			Casts[i] = name;
			name = sc.nextLine();
			i++;
		}
		
		System.out.print("Movie Id:");
		this.moveId = sc.nextInt();
		
		this.rating = 0; //no rating entered before
		
		this.review = new Review[Max];
	}
	
	public String getTitle() {
		return title;
	}
	
	public int getStatus() {
		return status;
	}
	
	public String getSypnosis() {
		return sypnosis;
	}
	
	public String getDirector() {
		return director;
	}
	
	public String[] getCast() {
		return casts;
	}
	
	public double getRating() {
		int num = 0;
		if (Movie.review.total < 2)
			System.out.println("NA");
		else {
			while (num < review.total) {
				this.rating += this.review[num].rating;
				num++;
			}
			return rating/review.total;
		}
	}
	
	public int getMovieId() {
		return movieId;
	}
	
	public Review[] getReview() {
		return review;
	}
	
	public void setTitle(String movie_Title) {
		this.title = movie_Title;
	}
	
	public void setStatus(String movie_Status) {
		this.status = movie_Status;
	}
	
	public void setSypnosis(String movie_Sypnosis) {
		this.synopsis = movie_Sypnosis;
	}
	
	public void setDirector(String movie_Director) {
		this.director = movie_Director;
	}
	
	public void setCast() {
		
	}
}
