/**
 * 
 */
package com.sirika.httpclienthelpers.template;

import org.apache.http.HttpResponse;

public final class DoNothingHttpResponseCallback implements HttpResponseCallback {
    public Object doWithHttpResponse(HttpResponse httpResponse) throws Exception {
        return null;
    }
}