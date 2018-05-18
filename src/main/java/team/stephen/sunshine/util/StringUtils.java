package team.stephen.sunshine.util;

public class StringUtils {
    public static boolean isNull(String str) {
        return str == null || str.equals("") || str.equals("null");
    }

    public static boolean isBlank(String str) {
        return str == null || str.equals("");
    }
}
