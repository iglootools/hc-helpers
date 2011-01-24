/**
 * Copyright 2009 Sami Dalouche
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/**
 * 
 */
package com.sirika.hchelpers.client.internal;

import org.apache.http.StatusLine;

import com.sirika.hchelpers.client.HttpErrorHandler;

/**
 * Abstract implementation for {@link HttpErrorHandler} helper class, that
 * provides a facility to match errors.
 * 
 * TODO: At some point, the {@link ErrorMatcher} abstraction should probably be
 * reworked into a Specification system, allowing to combine conditions, etc.
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
                return statusLine.getStatusCode() >= httpErrorCode;
            }
        };
    }

}