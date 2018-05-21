package team.stephen.sunshine.util.element;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeFormateUtil {

    public static Date parseStringToDate(String dateStr, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        Date date = null;
        try {
            date = dateFormat.parse(dateStr);
        } catch (Exception e) {
            return null;
        }
        return date;
    }

    public static Date parseStringToDate(String dateStr) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = dateFormat.parse(dateStr);
        } catch (Exception e) {
            return null;
        }
        return date;
    }
}
