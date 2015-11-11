package sg.edu.ntu.cz2002.moblima.view;

import java.util.Scanner;

public class GeneralView {
	static Scanner sc = new Scanner(System.in); 
	public static int printMenuAndReturnChoice(String title, String[] menus){
		int choice;
		String st;
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
				System.out.println((i==menus.length-1?"Q":i+1)+". "+menus[i]);
			for(int i=0; i<78; i++)
				System.out.print("*");
			while (error) {
				System.out.print("\nChoice: ");
				st = sc.nextLine();
				if(st.length()<=0)
					continue;
				if(st.equalsIgnoreCase("q"))
					return menus.length;
				if(!st.matches(".*\\d.*"))
					continue;
				choice = Integer.parseInt(st);
				if (choice < 1 || choice > menus.length)
					continue;
				else
					return choice;
			}
		}
		return 0;
	}
}
