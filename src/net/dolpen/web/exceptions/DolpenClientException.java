package net.dolpen.web.exceptions;

/**
 * Created by yamada on 2014/09/19.
 */
public class DolpenClientException extends DolpenException {

    protected DolpenClientException(String message) {
        super(400, message);
    }
}
