package team.stephen.sunshine.util.common;


import team.stephen.sunshine.util.element.StringUtils;

/**
 * Created by stephen on 2017/10/29.
 */
public class CodeUtils {
    public static String unicode2String(String unicode) {
        if (StringUtils.isBlank(unicode)) {
            return null;
        }
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
