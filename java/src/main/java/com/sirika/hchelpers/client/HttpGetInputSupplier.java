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
package com.sirika.hchelpers.client;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.io.InputSupplier;

/**
 * An {@link InputSupplier} that fetches its data from an HTTP GET request
 * 
 * <p> {@link #getInput()} can safely be called several time. This will trigger different
 * HTTP requests.
 * </p>
 * 
 * @author Sami Dalouche (sami.dalouche@gmail.com)
 *
 */
public final class HttpGetInputSupplier implements InputSupplier<InputStream> {
    private final static Logger logger = LoggerFactory.getLogger(HttpGetInputSupplier.class);
    private HttpClientTemplate httpClientTemplate;
    private HttpGet httpGet;
    private Iterable<HttpErrorHandler> httpErrorHandlers;

    public HttpGetInputSupplier(HttpClientTemplate httpClientTemplate, HttpGet httpGet) {
        this(httpClientTemplate, httpGet, noErrorHandler());
    }

    public HttpGetInputSupplier(
            HttpClientTemplate httpClientTemplate,
            HttpGet httpGet, 
            Iterable<HttpErrorHandler> httpErrorHandlers) {
        super();
        this.httpClientTemplate = httpClientTemplate;
        this.httpGet = httpGet;
        this.httpErrorHandlers = httpErrorHandlers;
    }

    private static Iterable<HttpErrorHandler> noErrorHandler() {
        return Lists.newArrayList();
    }

    private InputStream generateInputStream(HttpEntity entity)
            throws IOException {
        if (entity != null) {
            return entity.getContent();
        } else {
            return null;
        }
    }

    /**
     * @throws IOException
     *             if the underlying HTTP connection fails
     * @throws RuntimeException
     *             depending on the miscellaneous exception handlers
     */
    public InputStream getInput() throws IOException {
        logger.debug("Generating InputStream");

        return (InputStream) this.httpClientTemplate.execute(this.httpGet,
                new HttpResponseCallback() {
                    public Object doWithHttpResponse(HttpResponse httpResponse)
                            throws Exception {
                        return generateInputStream(httpResponse.getEntity());
                    }
                }, this.httpErrorHandlers);
    }

}