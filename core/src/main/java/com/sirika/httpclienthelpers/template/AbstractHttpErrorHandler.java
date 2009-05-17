/**
 * 
 */
package com.sirika.httpclienthelpers.template;

import org.apache.http.StatusLine;

/**
 * Abstract implementation for {@link HttpErrorHandler} helper class, that provides a 
 * facility to match errors.
 * 
 * TODO: At some point, the {@link ErrorMatcher} abstraction should probably be reworked into a 
 * Specification system, allowing to combine conditions, etc.
 * 
 * @author Sami Dalouche (sami.dalouche@gmail.com)
 *
 */
public abstract class AbstractHttpErrorHandler implements HttpErrorHandler {
    public interface ErrorMatcher {
	boolean matches(StatusLine statusLine);
    }
    
    private ErrorMatcher errorMatcher;
    public AbstractHttpErrorHandler(ErrorMatcher errorMatcher) {
        super();
        this.errorMatcher = errorMatcher;
    }

    public boolean apppliesTo(StatusLine statusLine) {
        return errorMatcher.matches(statusLine);
    }
    
    public static ErrorMatcher statusCodeEquals(final int httpErrorCode) {
	return new ErrorMatcher() {
	    public boolean matches(StatusLine statusLine) {
		return httpErrorCode == statusLine.getStatusCode();
	    }
	};
    }
    
    public static ErrorMatcher statusCodeGreaterOrEquals(final int httpErrorCode) {
	return new ErrorMatcher() {
	    public boolean matches(StatusLine statusLine) {
		return statusLine.getStatusCode() >= httpErrorCode ;
	    }
	};
    }

}