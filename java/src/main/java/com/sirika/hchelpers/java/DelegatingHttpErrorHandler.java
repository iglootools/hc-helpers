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
package com.sirika.hchelpers.java;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;

import com.google.common.base.Preconditions;


/**
 * {@link HttpErrorHandler} that delegates to {@link HttpErrorMatcher} and {@link HttpResponseCallback}
 * 
 * @author Sami Dalouche (sami.dalouche@gmail.com)
 * 
 */
public final class DelegatingHttpErrorHandler implements HttpErrorHandler {
    private HttpErrorMatcher errorMatcher;
    private HttpResponseCallback httpResponseCallback;

    public DelegatingHttpErrorHandler(HttpErrorMatcher errorMatcher, HttpResponseCallback httpResponseCallback) {
        super();
        Preconditions.checkArgument(errorMatcher != null, "errorMatcher is required");
        Preconditions.checkArgument(httpResponseCallback != null, "doWithHttpResponse is required");
        this.errorMatcher = errorMatcher;
        this.httpResponseCallback = httpResponseCallback;
    }

    public boolean apppliesTo(StatusLine statusLine) {
        return errorMatcher.matches(statusLine);
    }

    public void handle(HttpResponse response) throws Exception {
        httpResponseCallback.doWithHttpResponse(response);
        
    }

}