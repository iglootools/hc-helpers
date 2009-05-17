package com.sirika.httpclienthelpers.template;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
/**
 * Spring-like Template for {@link HttpClient}. It makes sure that all resources are properly 
 * closed in case of exceptions
 * 
 * @author Sami Dalouche (sami.dalouche@gmail.com)
 *
 */
public class HttpClientTemplate {
    private final static Logger logger = LoggerFactory.getLogger(HttpClientTemplate.class);
    private HttpClient httpClient;
    private List<HttpErrorHandler> defaultErrorHandlers = Lists.newArrayList();

    public HttpClientTemplate(HttpClient httpClient) {
	super();
	this.httpClient = httpClient;
    }

    /**
     * Add a default error handler to the chain. The handlers are executed in the order they are added
     * @param httpErrorHandler
     */
    public void addDefaultErrorHandler(HttpErrorHandler httpErrorHandler) {
	defaultErrorHandlers.add(httpErrorHandler);
    }

    /**
     * Execute a {@link HttpUriRequest} without any specific {@link HttpErrorHandler}.
     * The resulting {@link HttpResponse} will be consumed automatically
     * @param httpUriRequest
     */
    public void executeWithoutResult(HttpUriRequest httpUriRequest) {
	this.executeWithoutResult(httpUriRequest, noErrorHandler());
    }
    
    /**
     * Executes a {@link HttpUriRequest} with the given list of {@link HttpErrorHandler}'s.
     * @param httpUriRequest
     * @param httpErrorHandlers
     */
    public void executeWithoutResult(HttpUriRequest httpUriRequest, Iterable<HttpErrorHandler> httpErrorHandlers) {
	this.execute(httpUriRequest, new HttpResponseCallback() {
	    public Object doWithHttpResponse(HttpResponse httpResponse) throws Exception {
		return null;
	    }
	    
	}, httpErrorHandlers, true);
    }
    
    
    /**
     * Same as #execute(HttpUriRequest, HttpResponseCallback, Iterable, boolean)) with 
     * no error handlers and no automatic consumption of the {@link HttpResponse}
     * 
     * @param httpUriRequest
     * @param httpResponseCallback
     * @return
     */
    public Object execute(HttpUriRequest httpUriRequest, HttpResponseCallback httpResponseCallback) {
	return this.execute(httpUriRequest, httpResponseCallback, noErrorHandler(), false);
    }
    
    /**
     * Executes the given {@link HttpUriRequest}, considers the given list of {@link HttpErrorHandler} and returns
     * the result of {@link HttpResponseCallback#doWithHttpResponse(HttpResponse)}
     * @param httpUriRequest
     * @param httpResponseCallback
     * @param httpErrorHandlers
     * @return
     */
    public Object execute(HttpUriRequest httpUriRequest, HttpResponseCallback httpResponseCallback, Iterable<HttpErrorHandler> httpErrorHandlers){
	return this.execute(httpUriRequest, httpResponseCallback, httpErrorHandlers, false);
    }
    public Object execute(HttpUriRequest httpUriRequest, HttpResponseCallback httpResponseCallback, Iterable<HttpErrorHandler> httpErrorHandlers, boolean consumeContent) {
	try {
	    final HttpResponse httpResponse = this.httpClient.execute(httpUriRequest);
	    logger.debug("Received Status: {}", httpResponse.getStatusLine());
	    HttpErrorHandler httpErrorHandler = findHttpErrorHandlerApplyingToResponse(httpErrorHandlers, httpResponse);
	    if(httpErrorHandler == null) {
		Object o =  httpResponseCallback.doWithHttpResponse(httpResponse);
		
		if(consumeContent)
		    consumeContent(httpResponse);
		
		return o;
	    } else {
		httpErrorHandler.handle(httpResponse);
	    }
	} catch(ClientProtocolException e) {
	    httpUriRequest.abort();
	    throw new RuntimeException(e);
	} catch(IOException e) {
	    httpUriRequest.abort();
	    throw new RuntimeException(e);
	} catch(RuntimeException e) {
	    httpUriRequest.abort();
	    throw e;
	} catch(Exception e) {
	    httpUriRequest.abort();
	    throw new RuntimeException(e);
	} finally {
	}
	throw new RuntimeException("Should never happen : programming error");
    }

    private void consumeContent(final HttpResponse httpResponse) throws IOException {
	HttpEntity entity = httpResponse.getEntity();
	if(entity != null) {
	    entity.consumeContent();
	}
    }

    private HttpErrorHandler findHttpErrorHandlerApplyingToResponse(Iterable<HttpErrorHandler> httpErrorHandlers, final HttpResponse httpResponse) {
	try {
	    return Iterables.find(Iterables.concat(httpErrorHandlers, this.defaultErrorHandlers), new Predicate<HttpErrorHandler>(){
		public boolean apply(HttpErrorHandler input) {
		    return input.apppliesTo(httpResponse.getStatusLine());
		}
	    });    
	} catch(NoSuchElementException e) {
	    return null;
	}
    }

    private Iterable<HttpErrorHandler> noErrorHandler() {
	return Iterables.emptyIterable();
    }
}
