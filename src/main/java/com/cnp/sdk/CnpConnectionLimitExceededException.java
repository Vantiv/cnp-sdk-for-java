package com.cnp.sdk;

/**
 * CnpConnectionLimitExceededException is an exception class
 * used to specify the type of error returned by Cnp.
 *
 * Implementers of the Cnp eCommerce SDK can catch a specific
 * exception and take appropriate action.
 *
 * This exception will be thrown when an implementation has created more
 * connections to Cnp then they are allowed.
 *
 * @see <a href="https://developer.vantiv.com/docs/DOC-1190">Open access</a> for more information.
 */
public class CnpConnectionLimitExceededException extends CnpOnlineException {
    private static final long serialVersionUID = 1L;

    public CnpConnectionLimitExceededException(String message, Exception ume) {
        super(message, ume);
    }

    public CnpConnectionLimitExceededException(String message) {
        super(message);
    }
}
