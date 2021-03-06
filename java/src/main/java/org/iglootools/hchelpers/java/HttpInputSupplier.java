/**
 * Copyright 2011 Sami Dalouche
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
package org.iglootools.hchelpers.java;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.google.common.base.Preconditions;
import com.google.common.io.ByteStreams;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.io.InputSupplier;

/**
 * An {@link InputSupplier} that fetches its data from an HTTP GET request
 *
 * @author Sami Dalouche (sami.dalouche@gmail.com)
 *
 */
public final class HttpInputSupplier implements InputSupplier<InputStream> {
    private final static Logger logger = LoggerFactory.getLogger(HttpInputSupplier.class);
    private HttpClientTemplate httpClientTemplate;
    private HttpUriRequest httpUriRequest;
    private Iterable<HttpErrorHandler> httpErrorHandlers;

    public HttpInputSupplier(HttpClientTemplate httpClientTemplate, HttpUriRequest httpUriRequest) {
        this(httpClientTemplate, httpUriRequest, noErrorHandler());
    }

    public HttpInputSupplier(
            HttpClientTemplate httpClientTemplate,
            HttpUriRequest httpUriRequest,
            Iterable<HttpErrorHandler> httpErrorHandlers) {
        super();
        this.httpClientTemplate = httpClientTemplate;
        this.httpUriRequest = httpUriRequest;
        this.httpErrorHandlers = httpErrorHandlers;
    }

    private static Iterable<HttpErrorHandler> noErrorHandler() {
        return Lists.newArrayList();
    }

    private InputStream generateInputStream(HttpEntity entity) throws IOException {
        Preconditions.checkArgument(entity != null, "entity is required");
        return new ByteArrayInputStream(ByteStreams.toByteArray(entity.getContent()));
    }

    /**
     * @throws IOException
     *             if the underlying HTTP connection fails
     * @throws RuntimeException
     *             depending on the miscellaneous exception handlers
     */
    public InputStream getInput() throws IOException {
        return this.httpClientTemplate.execute(this.httpUriRequest,
                new HttpResponseCallback<InputStream>() {
                    public InputStream doWithHttpResponse(HttpResponse httpResponse) throws Exception {
                        return generateInputStream(httpResponse.getEntity());
                    }
                }, this.httpErrorHandlers);
    }

}