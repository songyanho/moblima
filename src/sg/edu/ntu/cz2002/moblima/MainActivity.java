package sg.edu.ntu.cz2002.moblima;

import java.util.Scanner;

public class MainActivity {

	protected static Scanner sc;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		sc = new Scanner(System.in);
		for(int i=0; i<68; i++)
			System.out.print("*");
		System.out.println("\n*\t Movie Booking and Listing Management Application\t   *");
		for(int i=0; i<68; i++)
			System.out.print("*");
		do{
			System.out.print("\nWelcome\n For movie-goer, please press enter\nFor admin, please enter \"admin\": ");
			String action = sc.nextLine();
			if(action.equalsIgnoreCase("admin")){
				
			}else{

			}
		}while(true);
	}

}
