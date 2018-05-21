package team.stephen.sunshine.util.element;

public class StringUtils {
    public static boolean isNull(String str) {
        return str == null || str.equals("") || str.equals("null");
    }

    public static boolean isNotNull(String str) {
        return !isNull(str);
    }

    public static boolean isBlank(String str) {
        return str == null || str.equals("");
    }

    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }

}
