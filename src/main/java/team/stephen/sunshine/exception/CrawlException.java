package team.stephen.sunshine.exception;

import team.stephen.sunshine.util.element.StringUtils;

import java.io.IOException;

/**
 * @author Stephen
 * @date 2019/03/18 23:51
 */
public class CrawlException extends Exception {
    private String message;

    public CrawlException(String message) {
        super(message);
    }

    public CrawlException(String format, String... param) {
        this.message = String.format(format, param);
    }

    public CrawlException(Exception e) {
        super(e);
    }

    @Override
    public String getMessage() {
        if (StringUtils.isNull(message)) {
            return super.getMessage();
        }
        return message;
    }
}
