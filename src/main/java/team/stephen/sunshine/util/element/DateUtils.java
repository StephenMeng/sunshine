package team.stephen.sunshine.util.element;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by stephen on 2017/10/22.
 */
public class DateUtils {

    public static Date parseStringToDate(String string, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        Date date = null;
        try {
            date = format.parse(string);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String parseDateToString(Date tmp, String format) {
        return new SimpleDateFormat(format).format(tmp);
    }

    public static Date parseStringToDate(String dateStr) {
        return parseStringToDate(dateStr, "yyyy-MM-dd");
    }

    public static String parseDateToString(Date date) {
        return parseDateToString(date,"yyyy-MM-dd");
    }
}
