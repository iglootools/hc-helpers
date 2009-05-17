/**
 * 
 */
package com.sirika.httpclienthelpers.gzip;

import java.io.IOException;

import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.protocol.HttpContext;

/**
 * {@link HttpRequestInterceptor} that advertises GZIP compression support to the server.
 * 
 * @see GzipDecompressingEntity
 * @see GzipResponseInterceptor
 * 
 * @author Sami Dalouche (sami.dalouche@gmail.com)
 *
 */
public final class GzipRequestInterceptor implements HttpRequestInterceptor {
    public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
	if (!request.containsHeader("Accept-Encoding")) {
	    request.addHeader("Accept-Encoding", "gzip");
	}
    }
}