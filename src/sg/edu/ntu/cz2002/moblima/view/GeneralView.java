package sg.edu.ntu.cz2002.moblima.view;

import java.util.Scanner;

public class GeneralView {
	static Scanner sc = new Scanner(System.in); 
	public static int printMenuAndReturnChoice(String title, String[] menus){
		int choice;
		boolean error = true;
		System.out.println("\n\n");
		for(int i=0; i<78; i++)
			System.out.print("*");
		System.out.println("\n"+title);
		for(int i=0; i<78; i++)
			System.out.print("-");
		System.out.println("");
		if(menus != null && menus.length > 0){
			for(int i=0; i<menus.length; i++)
				System.out.println((i+1)+". "+menus[i]);
			for(int i=0; i<78; i++)
				System.out.print("*");
			while (error) {
				System.out.print("\nChoice: ");
				if (sc.hasNextInt()) {
					choice = sc.nextInt();
					sc.nextLine();
					if (choice < 1 || choice > menus.length)
						continue;
					else
						return choice;
				}
				else {
					sc.next();
					continue;
				}
			}
		}
		return 0;
	}
}
