package sg.edu.ntu.cz2002.moblima.models;

import java.util.HashMap;

import sg.edu.ntu.cz2002.moblima.dao.MovieDao;
import sg.edu.ntu.cz2002.moblima.view.GeneralView;

public class MovieApp {
	public static MovieManager movieMgr;
	
	public MovieApp(MovieManager movieMgr) {
		MovieApp.movieMgr = movieMgr;
	}
	public void printView(){
		int choice;
		boolean exit = false;
		String[] menu = {"List all movies",
				"Search movies",
				"New movie", 
				"Update movie", 
				"Remove movie",
		"Back to previous menu"};
		do{
			choice = GeneralView.printMenuAndReturnChoice("Admin Panel > Movie Listing Management", menu);
			switch (choice) {
			case 1:
				HashMap<Integer, Movie> movies = MovieDao.findActiveMovie();
				if(movies.size() <= 0)
					System.out.println("No movies available");
				else
					movieMgr.listMoviesView(movies, false);
				break;
			case 2:
				movieMgr.searchMovieViewController(false);
				break;
			case 3:
				movieMgr.addMovieViewController();
				break;
			case 4:
				movieMgr.editMovieViewController();
				break;
			case 5:
				movieMgr.removeMovieViewController();
				break;
			default: exit = true; break;
			}
		}while(!exit);
	}
}
