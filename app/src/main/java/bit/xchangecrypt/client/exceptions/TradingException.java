package bit.xchangecrypt.client.exceptions;

import io.swagger.client.ApiException;

/**
 * Created by Peter on 25.04.2018.
 */

public class TradingException extends RuntimeException {
    public TradingException(String message) {
        super(message);
    }

    public TradingException(String message, Exception e) {
        super(enrichMessageByCode(message, e), e);
    }

    private static String enrichMessageByCode(String message, Exception e) {
        if (e instanceof ApiException) {
            message += " (" + ((ApiException) e).getCode() + ")";
        }
        return message;
    }
}
