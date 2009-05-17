package com.sirika.httpclienthelpers.template;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;

/**
 * Reacts when there is a specific (set of) Http Errors
 * 
 * @author Sami Dalouche (sami.dalouche@gmail.com)
 *
 */
public interface HttpErrorHandler {
    /**
     * 
     * @param statusLine
     * @return whether this matcher applis to the current Http error code
     */
    boolean apppliesTo(StatusLine statusLine);
    
    /**
     * perform the action of the handler
     * @param response
     * @throws Exception
     */
    void handle(HttpResponse response) throws Exception;
}
