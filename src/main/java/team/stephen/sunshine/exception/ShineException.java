package team.stephen.sunshine.exception;

import team.stephen.sunshine.util.element.StringUtils;

/**
 * @author Stephen
 * @date 2019/03/18 23:51
 */
public class ShineException extends Exception {
    private String message;

    public ShineException(String message) {
        super(message);
    }

    public ShineException(String message, Throwable cause) {
        super(message, cause);
    }

    public ShineException(Throwable cause) {
        super(cause);
    }

    public ShineException(String format, String... param) {
        this.message = String.format(format, param);
    }

    @Override
    public String getMessage() {
        if (StringUtils.isNull(message)) {
            return super.getMessage();
        }
        return message;
    }
}
