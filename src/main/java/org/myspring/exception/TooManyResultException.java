package org.myspring.exception;

/**
 * @author yibozhang
 * @date 2020/3/30 11:27
 */
public class TooManyResultException extends RuntimeException {

    public TooManyResultException() {
        super();
    }

    public TooManyResultException(String message) {
        super(message);
    }

    public TooManyResultException(String message, Throwable cause) {
        super(message, cause);
    }

    public TooManyResultException(Throwable cause) {
        super(cause);
    }

    protected TooManyResultException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
