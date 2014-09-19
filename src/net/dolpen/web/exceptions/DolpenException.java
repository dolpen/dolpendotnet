package net.dolpen.web.exceptions;

import java.util.List;

/**
 * Created by yamada on 2014/09/19.
 */
public abstract class DolpenException extends RuntimeException {

    public int httpStatusCode;

    public String errorMessage;

    public List<String> information;

    protected DolpenException(int httpStatusCode, String errorMessage, List<String> information) {
        this.httpStatusCode = httpStatusCode;
        this.errorMessage = errorMessage;
        this.information = information;
    }

    protected DolpenException(int httpStatusCode, String errorMessage) {
        this(httpStatusCode, errorMessage, null);
    }

}
