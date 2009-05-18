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
package com.sirika.httpclienthelpers.springframework;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamSource;

import com.google.common.collect.Iterables;
import com.sirika.httpclienthelpers.template.HttpClientTemplate;
import com.sirika.httpclienthelpers.template.HttpErrorHandler;
import com.sirika.httpclienthelpers.template.HttpResponseCallback;

public class HttpDownloadInputStreamSource implements InputStreamSource{
    private final static Logger logger = LoggerFactory.getLogger(HttpDownloadInputStreamSource.class);
    private HttpClientTemplate httpClientTemplate;
    private HttpGet httpGet;
    private Iterable<HttpErrorHandler> httpErrorHandlers;
    
    public HttpDownloadInputStreamSource(HttpClientTemplate httpClientTemplate, HttpGet httpGet) {
	this(httpClientTemplate, httpGet, noErrorHandler());
    }
    
    public HttpDownloadInputStreamSource(HttpClientTemplate httpClientTemplate, HttpGet httpGet, Iterable<HttpErrorHandler> httpErrorHandlers) {
	super();
	this.httpClientTemplate = httpClientTemplate;
	this.httpGet = httpGet;
	this.httpErrorHandlers = httpErrorHandlers;
    }

    private static Iterable<HttpErrorHandler> noErrorHandler() {
	return Iterables.emptyIterable();
    }

    /**
     * @throws IOException if the underlying HTTP connection fails
     * @throws RuntimeException depending on the miscellaneous exception handlers
     */
    public InputStream getInputStream() throws IOException, RuntimeException {
	logger.debug("Generating InputStream");
	
	return (InputStream) this.httpClientTemplate.execute(this.httpGet, new HttpResponseCallback() {
	    public Object doWithHttpResponse(HttpResponse httpResponse) throws Exception {
		return generateInputStream(httpResponse.getEntity());
	    }    
	}, this.httpErrorHandlers);

    }

    private InputStream generateInputStream(HttpEntity entity) throws IOException {
	if(entity != null) {
	    return entity.getContent();
	} else {
	    return null;
	}
    }
    
}