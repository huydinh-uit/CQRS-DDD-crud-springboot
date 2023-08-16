package vn.com.vng.mcrusprofile.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class RegexUtils {
    private RegexUtils() {

    }

    public static final boolean matches(String regex, String str) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str.trim());
        return matcher.matches();
    }

    public static final boolean found(String regex, String str) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str.trim());
        return matcher.find();
    }

    public static final boolean notMatches(String regex, String str) {
        return !matches(regex, str);
    }

    public static final boolean notFound(String regex, String str) {
        return !found(regex, str);
    }


}
