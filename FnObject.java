package vn.com.vng.mcrusprofile.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public final class FnObject {
	
	public static String getStringValue(Object obj) {
		if(obj == null){
			return null;
		}
		return (String)obj;
	}
	
	public static Date getDateValue(Object obj) {
		if(obj == null){
			return null;
		}
		return (Date)obj;
	}
	
	public static Long getLongValue(Object obj) {
		if(obj == null){
			return null;
		}
		return ((Number)obj).longValue();
	}
	
	public static Double getDoubleValue(Object obj) {
		if(obj == null){
			return null;
		}
		return ((Number)obj).doubleValue();
	}
	
	public static String getStringParam(String value) {
		if(value == null){
			return "";
		}
		return value;
	}
	
	public static Number getNumberParam(Number value) {
		if(value == null){
			return -1;
		}
		return value;
	}
	
	public static String getDateParam(Date value) {
		if(value == null){
			return "";
		}
		SimpleDateFormat dfParam = new SimpleDateFormat("yyyy-MM-dd");
		return dfParam.format(value);
	}
	
	
	public static String listToString(List listUnit,String sepe){
		
//		Collections.sort(listUnit);
		if(listUnit == null){
			return "";
		}
		StringBuilder str = new StringBuilder();
		Boolean isFirst = true;
		for (Object unit : listUnit) {
			if(!isFirst){
				str.append(sepe);
			}
			isFirst = false;
			
			str.append(unit);
		}
		return str.toString();
	}
	
}
