package bit.xchangecrypt.client.exceptions;

/**
 * Created by Peter on 25.04.2018.
 */

public class TradingException extends RuntimeException {
    public TradingException(String message) {
        super(message);
    }

    public TradingException(String message, Exception e) {
        super(message, e);
    }
}
