package de.achim.eutravelcenter.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class BahnUtils {
	
	public static String makeBahnDatum(String dateString) throws ParseException {
		//parse the input
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", java.util.Locale.GERMAN);
		Date myDate = sdf.parse(dateString);
		
		//prepare to format output
		sdf.applyPattern("dd.MM.yyyy");
		String resultDate = sdf.format(myDate);
		
		sdf.applyPattern("EE");
		String dayOfWeek = sdf.format(myDate);
		
		return dayOfWeek.substring(0, dayOfWeek.length()-1) + ", " +resultDate;
	}
	
	public static long getUnixTimeStamp() {
		Date today = Calendar.getInstance().getTime();
		return today.getTime();
	}
	
	//43.7764860000	7.5043490000
		//X=13369549@Y=52525589@U=80@L=008011160
		public static String formatXYCoordinates(String coord) {
			String result = "";
			coord = coord.replace(".","");
			result = coord.substring(0, 8);
			return result;
		}
}
