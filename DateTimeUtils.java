package vn.com.vng.mcrusprofile.util;

import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;

/**
 * @author sonnd
 *
 */
public class DateTimeUtils {
    
    public static boolean isRepeatable(String dateTimeStr) {
        if (dateTimeStr != null && dateTimeStr.startsWith("R")) {
            return true;
        }
        
        return false;
    }
    
    public static boolean isPeriod(String dateTimeStr) {
        if (dateTimeStr != null && dateTimeStr.startsWith("P")) {
            return true;
        }
        
        return false;
    }
    
    public static boolean isBusinessPeriod(String dateTimeStr) {
        if (dateTimeStr != null && dateTimeStr.startsWith("B")) {
            return true;
        }
        
        return false;
    }

    public static long parseDateTime(String dateTimeStr) {
            DateTime dt = ISODateTimeFormat.dateTimeNoMillis().parseDateTime(dateTimeStr);
            return dt.getMillis();
    }
    
    public static String[] parseISORepeatable(String isoString) {
    	String[] result = new String[3];
    	String[] elements = isoString.split("/");
        if (elements.length==3) {
        	result[0] = elements[0].substring(1);
        	result[1] = elements[1];
        	result[2] = elements[2];
        } else {
        	result[0] = elements[0].substring(1);
        	result[1] = new DateTime().toString();
        	result[2] = elements[1];
        }
        
        return result;
    }
    
    public static Date toStartOfDay(Date date) {
		DateTime d = new DateTime(date);
		d = d.withTime(0, 0, 0, 0);
		return d.toDate();
	}
	
	public static Date toEndOfDay(Date date) {
		DateTime d = new DateTime(date);
		d = d.plusDays(1);
		d = d.withTime(0, 0, 0, 0);
		d = d.minusMillis(1);
		return d.toDate();
	}
	
	public static DateTime toStartOfDay(DateTime d) {
		d = d.withTimeAtStartOfDay();
		return d;
	}
	
	public static DateTime toEndOfDay(DateTime d) {
		d = d.plusDays(1);
		d = d.withTime(0, 0, 0, 0);
		d = d.minusMillis(1);
		return d;
	}
	
	public static boolean checkDuplicateDate(List<Date> existedDates, Date dt) {
		if(existedDates == null || existedDates.isEmpty()) {
			return false;
		}
		Date dateToCompare = DateTimeUtils.toStartOfDay(dt);
		for(Date d : existedDates) {
			if(DateTimeUtils.toStartOfDay(d).compareTo(dateToCompare) == 0) {
				return true;
			}
		}
		
		return false;
	}
}
