package sg.edu.ntu.cz2002.moblima.models;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import sg.edu.ntu.cz2002.moblima.dao.*;
import sg.edu.ntu.cz2002.moblima.models.Movie.MovieType;
import sg.edu.ntu.cz2002.moblima.view.GeneralView;

public class MovieManager {
	CineplexManager cineplexMgr = new CineplexManager();
	static Scanner sc = new Scanner(System.in);

	/**
	 * List all the movie details in database
	 * @param movies
	 * @param showId
	 */
	public void listMoviesView(HashMap<Integer, Movie> movies, boolean showId){
		for(Movie m: movies.values()){
			listMovieView(m, showId);
		}
	}

	/**
	 * List specific movie detail
	 * @param m
	 * @param showId
	 */
	protected void listMovieView(Movie m, boolean showId){
		if(showId)
			System.out.println("Movie ID: "+m.getId());
		System.out.println("\nMovie << "+m.getTitle()+" >> ("+m.getRatingString()+")");
		System.out.println("\tfrom "+m.getDirector());
		System.out.print("\tstarring: ");
		for(String c: m.getCasts())
			System.out.print(c+"  ");
		System.out.print("\n\tDuration in minutes: " + m.getDuration());
		System.out.println("\n\tis "+ m.getStatusString());
		System.out.println("\tType: "+Movie.getTypeStringFromMovieType(m.getType()));
		System.out.println("\tSynopsis: "+m.getSynopsis().replaceAll("(.{1,57})\\s+", "$1\n\t\t  "));
		double rating = 0;
		DecimalFormat df = new DecimalFormat("#.#");
		rating = cineplexMgr.calculateOverallRating(m.getId());
		System.out.print("\tReviewers' overall rating: ");
		if (rating >= 1.0 && rating <= 5.0)
			System.out.print(df.format(rating));
		else
			System.out.print("NA");
		System.out.print("\n");
	}

	/**
	 * Interface for user to select which movie to be edited
	 */
	protected void editMovieViewController(){
		boolean exit = false;
		int choice, it;
		String st;
		String menu[] = {"Edit movie by ID", "Search movie", "Back to previous menu"};
		do{
			choice = GeneralView.printMenuAndReturnChoice("Admin Panel > Movie Listing Management > Update Movie", menu);
			switch(choice){
			case 1:
				System.out.print("\nEnter ID of movie that you want to modify: ");
				it = sc.nextInt();
				sc.nextLine();
				Movie m = MovieDao.findById(it);
				if(m == null){
					System.out.println("Invalid Movie ID");
					continue;
				}
				System.out.println("Movie selected to be modified: ");
				listMovieView(m, true);
				System.out.println("\nPlease confirm the record:\nTo edit, type Y\nTo redo, type N\nTo exit, type E");
				System.out.print("\nYour choice: ");
				st = sc.nextLine();
				if(st.equalsIgnoreCase("Y")){
					updateMovieViewController(m);
				}else if(st.equalsIgnoreCase("N")){
					continue;
				}else{
					exit = true;
				}
				break;
			case 2:
				searchMovieViewController(true, "admin");
				break;
			default: exit = true; break;
			}
		}while(!exit);
	}

	/**
	 * Interface to edit the movie detail
	 * @param m
	 */
	protected void updateMovieViewController(Movie m){
		int choice, it;
		String st, st2;
		boolean exit = false, exit2 = false;
		String menu[] = {
				"Edit movie title",
				"Edit movie director",
				"Edit movie casts",
				"Edit movie status",
				"Edit movie synopsis",
				"Edit movie duration",
				"Edit movie type",
		"Back to previous menu"};
		do{
			exit = false;
			GeneralView.printMenuAndReturnChoice("Movie", null);
			listMovieView(m, true);
			choice = GeneralView.printMenuAndReturnChoice("Admin Panel > Movie Listing Management > Update Movie: "+m.getTitle(), menu);
			System.out.println(menu[choice-1]);
			switch(choice){
			case 1: 
				exit2 = false;
				do{
					System.out.println("\nCurrent movie title: "+m.getTitle());
					System.out.print("Update movie title to: ");
					st = sc.nextLine();
					System.out.println("\nMovie title: "+m.getTitle()+" -> "+st);
					System.out.println("Please confirm the record:\nTo edit, type Y\nTo redo, type N\nTo exit, type E");
					System.out.print("Your choice: ");
					st2 = sc.nextLine();
					if(st2.equalsIgnoreCase("Y")){
						m.setTitle(st);
						MovieDao.save();
						exit2 = true;
					}else if(st2.equalsIgnoreCase("N")){
						continue;
					}else{
						exit2 = true;
					}
				}while(!exit2);
				break;
			case 2:
				exit2 = false;
				do{
					System.out.println("\nCurrent movie director: "+m.getDirector());
					System.out.print("Update movie director to: ");
					st = sc.nextLine();
					System.out.println("\nMovie director: "+m.getDirector()+" -> "+st);
					System.out.println("Please confirm the record:\nTo edit, type Y\nTo redo, type N\nTo exit, type E");
					System.out.print("Your choice: ");
					st2 = sc.nextLine();
					if(st2.equalsIgnoreCase("Y")){
						m.setDirector(st);
						MovieDao.save();
						exit2 = true;
					}else if(st2.equalsIgnoreCase("N")){
						continue;
					}else{
						exit2 = true;
					}
				}while(!exit2);
				break;
			case 3:
				exit2 = false;
				do{
					exit2 = false;
					boolean exit3 = false;
					System.out.println("\nCurrent movie casts: ");
					int i=0;
					for(String c: m.getCasts())
						System.out.println("\t"+(++i)+". "+c);
					String[] menuCast = {"Add cast", "Remove cast", "Back to previous menu"};
					i = GeneralView.printMenuAndReturnChoice("Admin Panel > Movie Listing Management > Update casts of Movie: "+m.getTitle(), menuCast);
					switch(i){
					case 1:
						exit3 = false;
						do{
							System.out.print("\nNew cast name: ");
							String st3 = sc.nextLine();
							System.out.println("Confirm adding new cast with name "+st3);
							System.out.println("Please confirm the record:\nTo add, type Y\nTo redo, type N\nTo exit, type E");
							System.out.print("Your choice: ");
							String st4 = sc.nextLine();
							if(st4.equalsIgnoreCase("Y")){
								m.addCast(st3);
								MovieDao.save();
								System.out.println("New cast "+st3+" is added");
								exit3 = true;
							}else if(st4.equalsIgnoreCase("N")){
								continue;
							}else{
								System.out.println("No changes made");
								exit3 = true;
							}
						}while(!exit3);
						break;
					case 2:
						exit3 = false;
						do{
							System.out.println("Casts with index:");
							i = 0;
							for(String c: m.getCasts())
								System.out.println("\t"+(++i)+". "+c);
							do{
								System.out.print("Remove cast with index: ");
								it = sc.nextInt();
								sc.nextLine();
							}while(it<1 || it > i);
							System.out.println("Confirm removing cast "+m.getCasts().get(i-1));
							System.out.println("Please confirm the record:\nTo add, type Y\nTo redo, type N\nTo exit, type E");
							System.out.print("Your choice: ");
							String st4 = sc.nextLine();
							if(st4.equalsIgnoreCase("Y")){
								m.removeCast(i-1);
								MovieDao.save();
								System.out.println("Cast is removed");
								exit3 = true;
							}else if(st4.equalsIgnoreCase("N")){
								continue;
							}else{
								System.out.println("No changes made");
								exit3 = true;
							}
						}while(!exit3);
						break;
					default: exit2 = true; break;
					}
				}while(!exit2);
				break;
			case 4:
				exit2 = false;
				do{
					System.out.println("\nCurrent movie status: "+m.getStatusString());
					do{
						System.out.println("Movie status: ");
						Movie.printMovieStatusChoice();
						System.out.print("Update movie status to: ");
						it = sc.nextInt();
						sc.nextLine();
					}while(it<1 || it>4);
					System.out.println("\nMovie status: "+m.getStatusString()+" -> "+Movie.getStatusStringFromChoice(it));
					System.out.println("Please confirm the record:\nTo edit, type Y\nTo redo, type N\nTo exit, type E");
					System.out.print("Your choice: ");
					st2 = sc.nextLine();
					if(st2.equalsIgnoreCase("Y")){
						m.setStatusFromChoice(it);
						MovieDao.save();
						exit2 = true;
					}else if(st2.equalsIgnoreCase("N")){
						continue;
					}else{
						exit2 = true;
					}
				}while(!exit2);
				break;
			case 5:
				exit2 = false;
				do{
					System.out.println("\nCurrent movie synopsis: \n"+m.getSynopsis());
					System.out.println("Update movie synopsis to: ");
					st = sc.nextLine();
					System.out.println("\nMovie synopsis: \n"+m.getSynopsis()+"\n->\n"+st);
					System.out.println("Please confirm the record:\nTo edit, type Y\nTo redo, type N\nTo exit, type E");
					System.out.print("Your choice: ");
					st2 = sc.nextLine();
					if(st2.equalsIgnoreCase("Y")){
						m.setSynopsis(st);
						MovieDao.save();
						exit2 = true;
					}else if(st2.equalsIgnoreCase("N")){
						continue;
					}else{
						exit2 = true;
					}
				}while(!exit2);
				break;
			case 6:
				exit2 = false;
				do{
					System.out.println("\nCurrent movie duration: " + m.getDuration());
					System.out.println("Update movie duration(in minutes) to: ");
					it = sc.nextInt();
					sc.nextLine();
					System.out.println("\nMovie duration: " + m.getDuration() + " -> " + it);
					System.out.println("Please confirm the record:\nTo edit, type Y\nTo redo, type N\nTo exit, type E");
					System.out.print("Your choice: ");
					st2 = sc.nextLine();
					if(st2.equalsIgnoreCase("Y")){
						m.setDuration(it);
						MovieDao.save();
						exit2 = true;
					}else if(st2.equalsIgnoreCase("N")){
						continue;
					}else
						exit2 = true;
				}while(!exit2);
				break;
			case 7:
				exit2 = false;
				do{
					System.out.println("\nCurrent movie type: "+Movie.getTypeStringFromMovieType(m.getType()));
					do{
						System.out.println("Movie type: ");
						Movie.printMovieTypeChoice();;
						System.out.print("Update movie type to: ");
						it = sc.nextInt();
						sc.nextLine();
					}while(it<1 || it>MovieType.values().length);
					System.out.println("Movie type: "+Movie.getTypeStringFromMovieType(m.getType())+" -> "+Movie.getTypeStringFromChoice(it));
					System.out.println("Please confirm the record:\nTo edit, type Y\nTo redo, type N\nTo exit, type E");
					System.out.print("Your choice: ");
					st2 = sc.nextLine();
					if(st2.equalsIgnoreCase("Y")){
						m.setTypeFromChoice(it);;
						MovieDao.save();
						exit2 = true;
					}else if(st2.equalsIgnoreCase("N")){
						continue;
					}else{
						exit2 = true;
					}
				}while(!exit2);
				break;
			default: exit = true; break;
			}
		}while(!exit);
	}

	/**
	 * Remove a movie by changing its status to "End of Showing"
	 */
	protected void removeMovieViewController(){
		int choice, it;
		String st;
		boolean exit = false;
		String menu[] = {"Delete movie by ID", "Search movie", "Back to previous menu"};
		do{
			choice = GeneralView.printMenuAndReturnChoice("Admin Panel > Movie Listing Management > Remove Movie", menu);
			switch(choice){
			case 1: 
				System.out.print("Enter ID of movie that you want to delete: ");
				it = sc.nextInt();
				sc.nextLine();
				Movie m = MovieDao.findById(it);
				if(m == null){
					System.out.println("Invalid Movie ID");
					continue;
				}
				System.out.println("Movie selected to be removed: ");
				listMovieView(m, true);
				System.out.println("Please confirm the record:\nTo remove, type Y\nTo redo, type N\nTo exit, type E");
				System.out.print("Your choice: ");
				st = sc.nextLine();
				if(st.equalsIgnoreCase("Y")){
					m.setStatusFromChoice(4);
					MovieDao.save();
					System.out.println("This movie is set to 'End of Showing'");
					exit = true;
				}else if(st.equalsIgnoreCase("N")){
					continue;
				}else{
					System.out.println("Zero record was removed");
					exit = true;
				}
				break;
			case 2:
				searchMovieViewController(true, "admin");
				break;
			default: exit = true; break;
			}
		}while(!exit);
	}

	/**
	 * Interface to search a movie by movie title, director or status
	 * @param showId
	 * @param panel
	 */
	public void searchMovieViewController(boolean showId, String panel){
		int choice, it;
		String st;
		HashMap<Integer, Movie> results;
		String[] menu = {
				"Movie title", 
				"Director", 
				"Status",
		"Back to previous menu"};
		boolean exit = false;
		do{
			exit = false;
			if (panel == "admin")
				choice = GeneralView.printMenuAndReturnChoice("Admin Panel > Movie Listing Management > Search movie by", menu);
			else
				choice = GeneralView.printMenuAndReturnChoice("Movie-goer Panel > List movies and details > Search movie by", menu);
			switch(choice){
			case 1: case 2:
				System.out.print("Enter the keyword here: ");
				st = sc.nextLine();
				if(choice == 1)
					results = MovieDao.findByTitle(st);
				else
					results = MovieDao.findByDirector(st);
				if(results.size() <= 0)
					System.out.println("No matched movies");
				else
					listMoviesView(results, showId);
				break;
			case 3:
				do{
					System.out.println("Search movies with status: ");
					Movie.printMovieStatusChoice();
					it = sc.nextInt();
					sc.nextLine();
				}while(it<1 || it>4);
				results = MovieDao.findByStatus(it);
				if(results.size() <= 0)
					System.out.println("No matched movies");
				else
					listMoviesView(results, showId);
				break;
			default: exit = true; break;
			}
		}while(!exit);
	}

	/**
	 * Interface to add a new movie
	 */
	protected void addMovieViewController(){
		int it, check = 0;
		String st, name;
		boolean exitNewMovie;
		do{
			exitNewMovie = false;
			System.out.println("\nAdding new movie: ");
			Movie movie = new Movie();
			System.out.print("Movie title: ");
			st = sc.nextLine();
			movie.setTitle(st);
			System.out.print("Sypnosis: ");
			st = sc.nextLine();
			movie.setSynopsis(st);
			System.out.print("Director: ");
			st = sc.nextLine();
			movie.setDirector(st);
			ArrayList<String> sat = new ArrayList<String>();
			System.out.println("Casts (At least 2, type end to stop): ");
			do{
				System.out.print("\t- ");
				name = sc.nextLine();
				if(name.length()>0 && !name.equalsIgnoreCase("end")) {
					sat.add(name);
					check++;
				}
			}while (!name.equalsIgnoreCase("end") || check < 2);
			movie.setCasts(sat);
			do{
				System.out.println("Movie status: ");
				Movie.printMovieStatusChoice();
				System.out.print("Movie status: ");
				it = sc.nextInt();
				sc.nextLine();
			}while(it<1 || it>4);
			movie.setStatusFromChoice(it);
			do{
				System.out.println("Movie rating: ");
				Movie.printMovieRatingChoice();
				System.out.print("Movie rating: ");
				it = sc.nextInt();
				sc.nextLine();
			}while(it<1 || it>6);
			movie.setRatingFromChoice(it);
			System.out.print("Duration in minutes: ");
			it = sc.nextInt();
			sc.nextLine();
			movie.setDuration(it);
			System.out.println("Movie type: ");
			Movie.printMovieTypeChoice();
			it = sc.nextInt();
			sc.nextLine();
			movie.setTypeFromChoice(it);
			listMovieView(movie, false);
			System.out.println("\nPlease confirm the record:\nTo insert, type Y\nTo redo, type N\nTo exit, type E");
			System.out.print("Your choice: ");
			st = sc.nextLine();
			if(st.equalsIgnoreCase("Y")){
				MovieDao.save(movie);
				System.out.println("One record was added");
				exitNewMovie = true;
			}else if(st.equalsIgnoreCase("N")){
				continue;
			}else{
				System.out.println("Zero record was added");
				exitNewMovie = true;
			}
		}while(!exitNewMovie);
	}

	/**
	 * Interface to add a review for a movie
	 * @param movies
	 */
	public void addReviewViewController(HashMap<Integer, Movie> movies) {
		int it, edit, movie, index;
		boolean movieExit, showExit;
		double dt;
		String st;
		DecimalFormat df = new DecimalFormat("#.##");
		df.setRoundingMode(RoundingMode.DOWN);
		Review r = new Review();
		movieExit = false;
		while (!movieExit) {
			System.out.println("\nWhich movie you want to review for: ");
			index = 1;
			for (Movie m: movies.values()) {
				System.out.println(index + ". " + m.getTitle());
				index++;
			}
			System.out.print("\n");
			movie = sc.nextInt();
			sc.nextLine();
			r.setMovieId(movie);
			System.out.print("Enter name: ");
			st = sc.nextLine();
			r.setName(st.toUpperCase());
			do {
				System.out.print("Enter rating: ");
				dt = sc.nextDouble();
				dt = Double.parseDouble(df.format(dt));
				if (dt < 1.0 || dt > 5.0)
					System.out.println("Enter only 1 - 5 [best]");
				sc.nextLine();
			} while (dt < 1.0 || dt > 5.0);
			r.setRating(dt);
			System.out.print("Enter comment: ");
			st = sc.nextLine();
			r.setComment(st);
			showExit = false;
			while (!showExit) {
				System.out.println("\nReviewing for movie <<" + movies.get(movie).getTitle() + ">>");
				System.out.println("Name: " + r.getName());
				System.out.println("Rating: " + r.getRating());
				System.out.println("Comment: " + r.getComment());
				String[] menus = {"Confirm to add review", "Edit entry", "Back to movie selection", "Back to main menu"};
				it = GeneralView.printMenuAndReturnChoice("Movie-goer Panel > Add Review", menus);
				switch (it) {
				case 1: 
					ReviewDao.save(r);
					HashMap<Integer, Review> reviews = new HashMap<Integer, Review>();
					reviews.put(r.getId(), r);
					movies.get(movie).setReviews(reviews);
					System.out.print("Review successfully added.");
					movieExit = true;
					showExit = true;
					break;
				case 2: 
					String[] editMenus = {"Edit name", "Edit rating", "Edit comment"};
					edit = GeneralView.printMenuAndReturnChoice("Add Review > Edit", editMenus);
					if (edit == 1) {
						System.out.print("Enter name: ");
						st = sc.nextLine();
						r.setName(st.toUpperCase());
					}
					else if (edit == 2) {
						do {
							System.out.print("Enter rating: ");
							dt = sc.nextDouble();
							if (dt < 1.0 || dt > 5.0)
								System.out.println("Enter only 1 - 5 [best]");
						} while (dt < 1.0 || dt > 5.0);
						r.setRating(dt);
					}
					else {
						System.out.print("Enter comment: ");
						st = sc.nextLine();
						r.setComment(st);
					}
					movieExit = false;
					showExit = false;
					break;
				case 3:
					showExit = true;
					movieExit = false;
					break;
				case 4:
					showExit = true;
					movieExit = true;
					break;
				default:
					System.out.println("Invalid option.");
					break;
				}
			}
		}
	}

	/**
	 * List all the previous reviews made by others for a particular movie
	 * @param movieId
	 */
	public void showReviewsByMovie(int movieId) {
		Movie m = MovieDao.findById(movieId);
		HashMap<Integer, Review> reviews = m.getReviews();
		int index = 1;
		if (!reviews.isEmpty()) {
			for (Review r: reviews.values()) {
				System.out.print("\n");
				System.out.println("[" + m.getTitle() + "] " + "<< Review " + index + " >>");
				System.out.println("Name: " + r.getName());
				System.out.println("Rating: " + r.getRating());
				System.out.println("Comment: " + r.getComment());
				index++;
			}
		}
		else
			System.out.print("No past reviews.");
	}

	/**
	 * Select a movie from a list of movies from database
	 * @param movies
	 * @return
	 */
	public int selectMovie(HashMap<Integer, Movie> movies) {
		int i = 0;
		int choice;
		System.out.println("\nWhich movie you are interested in?");
		for (Movie m : movies.values()) {
			i++;
			System.out.println(i + ". " + m.getTitle());
		}
		do {
			choice = sc.nextInt();
		} while (choice < 0 || choice > i);
		sc.nextLine();
		return choice;
	}
}
