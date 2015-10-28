package sg.edu.ntu.cz2002.moblima;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

public class CalendarView {
	
	public static final String[] dayOfWeek(boolean back){ 
		String[] a;
		if(back)
			a =  new String[]{"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Back to previous menu"};
		else
			a =  new String[]{"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
		Calendar c = new GregorianCalendar();
//		int b = c.get(Calendar.DAY_OF_WEEK);
//		int d = a.length-c.get(Calendar.DAY_OF_WEEK);
		a = Arrays.copyOfRange(a, c.get(Calendar.DAY_OF_WEEK)-1, 8);
		return a;
	}
	
	public static String[] timeslotInString(ArrayList<Calendar> c, boolean back){
		String[] menu = new String[c.size()+(back?1:0)];
		int i = 0;
		for(Calendar t: c){
			menu[i++] = t.get(Calendar.HOUR_OF_DAY)+":"+(t.get(Calendar.MINUTE)==0?"00":t.get(Calendar.MINUTE));
		}
		if(back)
			menu[c.size()] = "Back to previous menu";
		return menu;
	}
	
	public static ArrayList<Calendar> timeslot(boolean back, int[][] timeslot, int duration, int dayOfWeek, int weekOffset){
		Calendar current = (Calendar) getWeekCalendars(weekOffset).get(dayOfWeek).clone();
		ArrayList<Calendar> a = new ArrayList<Calendar>();
		for(int h=10; h<=24; h++){
			for(int m=0; m<=30; m+=30){
				current.set(Calendar.HOUR_OF_DAY, h);
				current.set(Calendar.MINUTE, m);
				current.set(Calendar.SECOND, 0);
				current.add(Calendar.MINUTE, duration);
				int endHour = current.get(Calendar.HOUR_OF_DAY);
				int endMinute = current.get(Calendar.MINUTE);
				if(!sameDay(current, getWeekCalendars(weekOffset).get(dayOfWeek)))
					continue;
				boolean fit = true;
				for(int hh=h; hh<=endHour; hh++){
					if(hh==h){
						if(m == 0){
							if(timeslot[hh-10][0] == 1){
								fit = false;
								break;
							}
						}
						if(timeslot[hh-10][1] == 1){
							fit = false;
							break;
						}
					}else if(hh==endHour){
						if(endMinute == 30){
							if(timeslot[hh-10][1] == 1){
								fit = false;
								break;
							}
						}
						if(timeslot[hh-10][0] == 1){
							fit = false;
							break;
						}
					}else{
						if(timeslot[hh-10][0] == 1 || timeslot[hh-10][1] == 1){
							fit = false;
							break;
						}
					}
				}
				if(fit){
					current.add(Calendar.MINUTE, -duration);
					a.add((Calendar) current.clone());
				}
			}
		}
		return a;
	}
	
	public static ArrayList<Calendar> getWeekCalendars(int weekOffset){
		Calendar calendar = Calendar.getInstance();
		if(weekOffset > 0){
			calendar.add(Calendar.DATE, weekOffset*7);
		}
		ArrayList<Calendar> calendars = new ArrayList<Calendar>();
		calendar.setTimeZone(TimeZone.getTimeZone("Asia/Singapore"));
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.MINUTE, 0);
		for(int i=0; i<7; i++){
			calendars.add((Calendar)calendar.clone());
			calendar.add(Calendar.DATE, 1);
		}
		calendar = Calendar.getInstance();
		calendar.setTimeZone(TimeZone.getTimeZone("Asia/Singapore"));
		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.MINUTE, 1);
		return calendars;
	}
	
	public static boolean sameDay(Calendar cal1, Calendar cal2){
		if (cal1 == null || cal2 == null)
	        return false;
	    return (cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA)
	            && cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) 
	            && cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR));
	}
	
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
