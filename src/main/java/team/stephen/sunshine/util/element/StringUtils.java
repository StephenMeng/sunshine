package team.stephen.sunshine.util.element;

public class StringUtils {
    public static final String EMPTY = "";

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
    public static String filterEmoji(String source) {
        if (source != null && source.length() > 0) {
            return source.replaceAll("[\ud800\udc00-\udbff\udfff\ud800-\udfff]", "");
        } else {
            return source;
        }
    }
    public static String decodeUnicode(final String unicode) {
        if (StringUtils.isBlank(unicode)) return null;
        StringBuilder sb = new StringBuilder();
        int i = -1;
        int pos = 0;

        while ((i = unicode.indexOf("\\u", pos)) != -1) {
            sb.append(unicode.substring(pos, i));
            if (i + 5 < unicode.length()) {
                pos = i + 6;
                sb.append((char) Integer.parseInt(unicode.substring(i + 2, i + 6), 16));
            }
        }

        return sb.toString();
    }

}
