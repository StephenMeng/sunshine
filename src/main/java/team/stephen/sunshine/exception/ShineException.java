package team.stephen.sunshine.exception;

/**
 * @author Stephen
 * @date 2019/03/18 23:51
 */
public class ShineException extends Exception {
    public ShineException(String message) {
        super(message);
    }

    public ShineException(String message, Throwable cause) {
        super(message, cause);
    }

    public ShineException(Throwable cause) {
        super(cause);
    }

}
