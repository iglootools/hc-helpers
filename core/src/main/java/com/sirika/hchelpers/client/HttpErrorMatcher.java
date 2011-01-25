package com.sirika.hchelpers.client;

import org.apache.http.StatusLine;

/**
 * Determines whether a given error happened by looking at the {@link StatusLine}
 * 
 * @author Sami Dalouche (sami.dalouche@gmail.com)
 *
 */
public interface HttpErrorMatcher {
    boolean matches(StatusLine statusLine);
}