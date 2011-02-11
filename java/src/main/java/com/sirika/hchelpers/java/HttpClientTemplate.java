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
package com.sirika.hchelpers.java;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.io.NullOutputStream;
import com.sirika.hchelpers.java.internal.DoNothingHttpResponseCallback;

/**
 * Spring-like Template for {@link HttpClient}. It makes sure that all resources
 * are properly closed in case of exceptions
 *
 * @author Sami Dalouche (sami.dalouche@gmail.com)
 *
 */
public final class HttpClientTemplate {
    private final static Logger logger = LoggerFactory.getLogger(HttpClientTemplate.class);
    private HttpClient httpClient;
    private List<HttpErrorHandler> defaultErrorHandlers = Lists.newArrayList(defaultHandler());

    public HttpClientTemplate(HttpClient httpClient) {
        super();
        Preconditions.checkArgument(httpClient != null, "httpClient cannot be null");
        this.httpClient = httpClient;
    }

    /**
     * Execute a {@link HttpUriRequest} without any specific
     * {@link HttpErrorHandler}. The resulting {@link HttpResponse} will be
     * consumed automatically
     *
     * @param httpUriRequest
     */
    public void executeWithoutResult(HttpUriRequest httpUriRequest) {
        this.executeWithoutResult(httpUriRequest, noErrorHandler());
    }

    /**
     * Executes a {@link HttpUriRequest} with the given list of
     * {@link HttpErrorHandler}'s.
     *
     * @param httpUriRequest
     * @param httpErrorHandlers
     */
    public void executeWithoutResult(HttpUriRequest httpUriRequest, Iterable<HttpErrorHandler> httpErrorHandlers) {
        this.execute(httpUriRequest, new DoNothingHttpResponseCallback(), httpErrorHandlers);
    }

    /**
     * Same as #execute(HttpUriRequest, HttpResponseCallback, Iterable,
     * boolean)) with no error handlers and no automatic consumption of the
     * {@link HttpResponse}
     *
     * @param httpUriRequest
     * @param httpResponseCallback
     * @return
     */
    public Object execute(HttpUriRequest httpUriRequest, HttpResponseCallback httpResponseCallback) {
        return this.execute(httpUriRequest, httpResponseCallback, noErrorHandler());
    }

    /**
     * Executes the given {@link HttpUriRequest}, considers the given list of
     * {@link HttpErrorHandler} and returns the result of
     * {@link HttpResponseCallback#doWithHttpResponse(HttpResponse)}
     *
     * @param httpUriRequest
     * @param httpResponseCallback
     * @param httpErrorHandlers
     * @return
     */
    public <T> T execute(HttpUriRequest httpUriRequest, HttpResponseCallback<T> httpResponseCallback,
                          Iterable<HttpErrorHandler> httpErrorHandlers) {
        try {
            final HttpResponse httpResponse = this.httpClient.execute(httpUriRequest);
            logger.debug("Received Status: {}", httpResponse.getStatusLine());
            HttpErrorHandler httpErrorHandler = findHttpErrorHandlerApplyingToResponse(httpErrorHandlers, httpResponse);
            if (httpErrorHandler == null) {
                T o = httpResponseCallback.doWithHttpResponse(httpResponse);
                consumeContent(httpResponse);
                return o;
            } else {
                httpErrorHandler.handle(httpResponse);
            }
        } catch (ClientProtocolException e) {
            httpUriRequest.abort();
            throw new RuntimeException(e);
        } catch (IOException e) {
            httpUriRequest.abort();
            throw new RuntimeException(e);
        } catch (RuntimeException e) {
            httpUriRequest.abort();
            throw e;
        } catch (Exception e) {
            httpUriRequest.abort();
            throw new RuntimeException(e);
        } finally {
        }
        throw new RuntimeException("Should never happen : programming error");
    }

    private void consumeContent(final HttpResponse httpResponse) throws IOException {
        HttpEntity entity = httpResponse.getEntity();
        if (entity != null) {
            //entity.writeTo(new NullOutputStream());
            EntityUtils.consume(entity);
        }
    }

    private HttpErrorHandler findHttpErrorHandlerApplyingToResponse(
            Iterable<HttpErrorHandler> httpErrorHandlers,
            final HttpResponse httpResponse) {
        try {
            return Iterables.find(Iterables.concat(httpErrorHandlers,
                    this.defaultErrorHandlers),
                    new Predicate<HttpErrorHandler>() {
                        public boolean apply(HttpErrorHandler input) {
                            return input.apppliesTo(httpResponse.getStatusLine());
                        }
                    });
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    private Iterable<HttpErrorHandler> noErrorHandler() {
        return Lists.newArrayList();
    }

    private HttpErrorHandler defaultHandler() {
        return new DelegatingHttpErrorHandler(HttpErrorMatchers.statusCodeGreaterOrEquals(300), new HttpResponseCallback() {
            public Object doWithHttpResponse(HttpResponse httpResponse) throws Exception {
                throw new HttpResponseException(
                        httpResponse.getStatusLine().getStatusCode(),
                        httpResponse.getStatusLine().getReasonPhrase());
            }
        });
    }

}
