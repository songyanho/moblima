package sg.edu.ntu.cz2002.moblima.view;

import java.util.HashMap;

import sg.edu.ntu.cz2002.moblima.models.SeatPlane;
import sg.edu.ntu.cz2002.moblima.models.Showtime;

public class SeatPlaneView {
	
	/**
	 * Print Seat Plane of the selected showtime with occupied seats
	 * @param showtime Showtime of the selected movie at selected cinema
	 * @return HashMap of SeatAID (A1-I16) to SeatId
	 */
	public static HashMap<String, Integer> printSeatPlane(Showtime showtime){

		SeatPlane seatPlane= showtime.getCinema().getSeatPlane();
		int[][] seatArray = showtime.getSeatPlaneViewArray();
		printScreen(seatPlane.getColumn());
		return printSeatArrangement(seatPlane.getRow(), seatPlane.getColumn(), seatArray);
	}

	/**
	 * Print seat arrangement in console
	 * @param row number of available rows
	 * @param column number of available columns
	 * @param seatArray 2D array with seat and non-seat
	 * @return
	 */
	private static HashMap<String, Integer> printSeatArrangement(int row, int column, int[][] seatArray){
		HashMap<String, Integer> seatToId = new HashMap<String, Integer>();
		String character = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		int cc=1, rr=0;
		int[] rowToId = new int[column];
		System.out.print("   ");
		for(int i=0; i<column; i++){
			boolean path = true;
			for(int j=0; j<row; j++){
				if(seatArray[j][i]!=0){
					path = false;
					break;
				}
			}
			if(path){
				rowToId[i] = 0;
				System.out.print("   ");
			}else{
				rowToId[i] = cc;
				if(cc<10)
					System.out.print(" "+(cc++)+" ");
				else
					System.out.print(" "+(cc++));
			}
		}
		System.out.println("");
		for(int j=0; j<row; j++){
			boolean path = true;
			for(int i=0; i<column; i++){
				if(seatArray[j][i]!=0){
					path = false;
					break;
				}
			}
			if(path) break;
			System.out.print(character.charAt(rr)+"  ");
			for(int i=0; i<column; i++){
				if(seatArray[j][i] < 0){
					if(i==0){
						if(seatArray[j][i] == seatArray[j][i+1]){
							System.out.print("[X ");
						}else{
							System.out.print("[X]");
						}
					}else if(i==column-1){
						if(seatArray[j][i] == seatArray[j][i-1]){
							System.out.print(" X]");
						}else{
							System.out.print("[X]");
						}
					}else{
						System.out.print((seatArray[j][i] == seatArray[j][i-1])?" ":"[");
						System.out.print("X");
						System.out.print((seatArray[j][i] == seatArray[j][i+1])?" ":"]");
					}
				}else if(seatArray[j][i] > 0){
					if(i==0){
						if(seatArray[j][i] == seatArray[j][i+1]){
							System.out.print("[  ");
						}else{
							System.out.print("[ ]");
						}
					}else if(i==column-1){
						if(seatArray[j][i] == seatArray[j][i-1]){
							System.out.print("  ]");
						}else{
							System.out.print("[ ]");
						}
					}else{
						System.out.print((seatArray[j][i] == seatArray[j][i-1])?" ":"[");
						System.out.print(" ");
						System.out.print((seatArray[j][i] == seatArray[j][i+1])?" ":"]");
					}
				}else{
					System.out.print("   ");
				}
				seatToId.put(character.charAt(rr)+""+rowToId[i]+"", seatArray[j][i]);
			}
			System.out.println("");
			rr++;
		}
		return seatToId;
	}

	/**
	 * Prints screen with width provided
	 * @param width Width/Total number of columns in cinema
	 */
	private static void printScreen(int width){
		System.out.print("\n");	
		System.out.print("   ");
		for(int i=0; i<width; i++)
			System.out.print("***");
		System.out.println("");
		System.out.print("   ");
		for(int i=0; i<(width-6)/2; i++)
			System.out.print("    ");
		System.out.print("S C R E E N");
		for(int i=0; i<(width-6)/2; i++)
			System.out.print("    ");
		System.out.println("");
		System.out.print("   ");
		for(int i=0; i<width; i++)
			System.out.print("***");
		System.out.println("\n");
	}
}
