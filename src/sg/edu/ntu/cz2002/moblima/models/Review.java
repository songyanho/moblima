package sg.edu.ntu.cz2002.moblima.models;
import java.util.Scanner;

public class Review {
	protected String name;
	protected double rating;
	protected String comment;
	protected int entryId;
	
	public Review(int index) {
		/* Scanner sc = new Scanner(System.in);
		System.out.print("Your name:");
		this.name = sc.nextLine();
		System.out.print("Rating: ");
		this.rating = sc.nextInt();
		System.out.print("Review: ");
		this.comment = sc.nextLine();
		*/
		this.rating = 0;
		this.entryId = index;
	}
	
	public void setName(String entry_name) {
		this.name = entry_name;
	}
	
	public void setRating (int rate) {
		if (rate >= 1.0 && rate <= 5.0)
			this.rating = rate;
		else
			System.out.println("Range: 1-5.");
	}
	
	public void setComment (String review) {
		this.comment = review;
	}
	
	public void setEntryId(int entry_Id) {
		this.entryId = entry_Id;
	}
	
	public String getName() {
		return name;
	}
	
	public double getRating() {
		return rating;
	}
	
	public String getComment() {
		return comment;
	}
	
	public int getEntryId() {
		return entryId;
	}
}