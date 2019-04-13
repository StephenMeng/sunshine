package team.stephen.sunshine.util.common;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import team.stephen.sunshine.Application;

/**
 * @author stephen
 * @date 2017/7/15
 */
public class LogRecord {
    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    public static void info(Object object) {
        logger.info(object.toString());
    }

    public static void error(Object e) {
        logger.error(e.toString());
    }

    public static void warn(String s, Object e) {
        logger.warn(String.format(s, e));
    }

    public static void print(Object object) {
        logger.info("************************************");
        if (object == null) {
            logger.info("**** null object ! ****");
        } else {
            logger.info(object.toString());
        }
        logger.info("************************************");

    }
}
