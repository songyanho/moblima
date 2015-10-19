package sg.edu.ntu.cz2002.moblima;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class CalendarView {
	public static void printCalendar(int year, List<String> holidays){
		for (int m=0; m<12; m++){
			String monthName = new DateFormatSymbols().getMonths()[m];
			Calendar c = new GregorianCalendar(year, m, 1);
			int numD = c.getActualMaximum(Calendar.DAY_OF_MONTH);
			int weekDay = c.get(Calendar.DAY_OF_WEEK)-1;
			System.out.println("\n                    " + monthName + " " + year);
			System.out.println("___________________________________________________");
			System.out.println("Sun\tMon\tTue\tWed\tThu\tFri\tSat");
			for (int sp=0; sp<weekDay; sp++)
					System.out.print("\t");
			for (int p=1; p<=numD; p++)
			{	
				if (weekDay%7==0 && weekDay!=0)
					System.out.println();
				if(holidays.contains(p+"/"+(m+1)+"/"+year)){
					System.out.flush();
					System.err.print(p+"\t");
					try {
						Thread.sleep(1);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.err.flush();
				}else{
					System.out.print(p+"\t");
				}
				weekDay+=1;
			}
			weekDay%=7;
			
			System.out.print("\n\n");
		}
	}
}
