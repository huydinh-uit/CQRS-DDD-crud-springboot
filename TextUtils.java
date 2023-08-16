package vn.com.vng.mcrusprofile.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

public final class TextUtils {
	private TextUtils() {

	}

	public static final boolean isEmpty(String str) {
		return str == null || str.isEmpty();
	}

	public static final boolean isNotEmpty(String str) {
		return !isEmpty(str);
	}

	public static final boolean theSameIgnoreCase(String str1, String str2) {
		String nmStr1 = StringUtils.normalizeSpace(str1);
		String nmStr2 = StringUtils.normalizeSpace(str2);
		return nmStr1.equalsIgnoreCase(nmStr2);
	}

	public static final String normalizeSpace(String str) {
		if (TextUtils.isEmpty(str)) {
			return str;
		}
		return StringUtils.normalizeSpace(str);
	}

	public static String toLowerCase(String str) {
		return str.toLowerCase();
	}
	
	public static final String normalizePath(String str) {
		if (TextUtils.isEmpty(str)) {
			return str;
		}
		if(str.endsWith(File.separator)){
			return str;
		}
		return str + File.separator;
	}
	
	public boolean checkPatternTax(String taxCode){
		String pattern = "(\\d{10}(-)\\d{3})|\\d{10}";
		return matcher(taxCode, pattern);
	}
	
	private Boolean matcher(String value, String pattern){
    	if(value == null || value.trim().isEmpty()){
    		return false;
    	}
    	try {
    		Matcher m = Pattern.compile(pattern)
    				.matcher(value);
    		return m.matches();
		} catch (Exception e) {
			return false;
		}
    	
    }
	
	public static String buildClientId(String value, String tenantCode) {
		if(value == null || value.contains("#")) {
			return value;
		}
		return tenantCode+"#"+value;
	}
	
	public static  List<String> toList(String values){
		if(values == null || values.trim().isEmpty()) {
			return new ArrayList<>();
		}
		
		List<String> splitStr = Arrays.stream(values.split("[|;,]"))
			    .map(String::trim)
			    .collect(Collectors.toList());
		return splitStr;
	}
}
