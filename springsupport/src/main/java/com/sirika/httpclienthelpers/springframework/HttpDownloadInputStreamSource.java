/**
 * PyMager Java REST Client
 * Copyright (C) 2008 Sami Dalouche
 *
 * This file is part of PyMager Java REST Client.
 *
 * PyMager Java REST Client is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * PyMager Java REST Client is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with PyMager Java REST Client.  If not, see <http://www.gnu.org/licenses/>.
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