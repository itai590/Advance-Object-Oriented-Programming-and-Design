/**
 * @author Itai
 * @HW 3
 */

import java.util.Calendar;
import java.util.GregorianCalendar;

public class CalendarAdapter {
	
	//private Calendar calendar;
	Calendar calendar = new GregorianCalendar();

	public int getHour() {
		return calendar.get(Calendar.HOUR);

	}

	public int getMinute() {
		return calendar.get(Calendar.MINUTE);
	}

	public int getSecond() {
		return calendar.get(Calendar.SECOND);
	}

}
