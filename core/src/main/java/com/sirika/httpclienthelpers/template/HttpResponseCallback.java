/**
 * 
 */
package com.sirika.httpclienthelpers.template;

import org.apache.http.HttpResponse;

/**
 * @see HttpClientTemplate
 * 
 * @author Sami Dalouche (sami.dalouche@gmail.com)
 *
 */
public interface HttpResponseCallback {
    /**
     * Called automatically by {@link HttpClientTemplate}
     * 
     * @param httpResponse
     * @return
     * @throws Exception
     */
    Object doWithHttpResponse(HttpResponse httpResponse) throws Exception;
}