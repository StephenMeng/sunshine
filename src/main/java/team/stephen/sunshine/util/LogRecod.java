package team.stephen.sunshine.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import team.stephen.sunshine.Application;

/**
 * Created by stephen on 2017/7/15.
 */
public class LogRecod {
    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    public static void info(Object object) {
        logger.info(object.toString());
    }

    public static void error(Object e) {
        logger.error(e.toString());
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
