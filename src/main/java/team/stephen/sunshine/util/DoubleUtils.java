package team.stephen.sunshine.util;

import java.text.DecimalFormat;

public class DoubleUtils {
    public static String twoBit(Double item) {
        return format(item, "#.00");
    }

    public static String threeBit(Double item) {
        return format(item, "#.000");
    }

    private static String format(Double item, String pattern) {
        if (item == null) {
            return "";
        }
        DecimalFormat df = new java.text.DecimalFormat(pattern);
        return df.format(item);
    }
}
