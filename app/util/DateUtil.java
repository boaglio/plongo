package util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

public class DateUtil {

	public static String getNowString() {
		Calendar cal = new GregorianCalendar();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH_mm_ss",Locale.US);
		return sdf.format(cal.getTime());
	}

	public static String getTimestampStr() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy_HH_mm_ss",Locale.US);
		return sdf.format(new GregorianCalendar().getTime());
	}
}
