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
package org.iglootools.hchelpers.spring;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.params.AllClientPNames;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.impl.client.BasicCookieStore;
import org.springframework.beans.factory.FactoryBean;

import org.iglootools.hchelpers.core.DefaultHttpClientFactory;

/**
 * Spring {@link FactoryBean} helper that eases the creation and configuration
 * of HttpComponents' {@link HttpClient}. Configuring HttpClient authentication,
 * number of connections, ... cannot be done programmatically using HttpClient's
 * native mechanisms. {@link HttpClientFactoryBean}'s goal is to expose these
 * settings so they can be changed declaratively using Spring.
 *
 *
 * @author Sami Dalouche (sami.dalouche@gmail.com)
 *
 */
public final class HttpClientFactoryBean implements FactoryBean<HttpClient> {
    private Map<AuthScope, Credentials> credentials = new HashMap<AuthScope, Credentials>();
    private Map<String, Object> params = new HashMap<String, Object>();
    private CookieStore cookieStore = null;
    private boolean shouldUseCookieStore = false;
    private boolean shouldUseGzipContentcompression = true;
    private int maxTotalConnections = DefaultHttpClientFactory.DEFAULT_MAX_NUMBER_OF_CONNECTIONS;
    private int defaultMaxConnectionsPerRoute = DefaultHttpClientFactory.DEFAULT_MAX_NUMBER_OF_CONNECTIONS_PER_ROUTE;

    public HttpClient getObject() throws Exception {
        return DefaultHttpClientFactory.httpClient(
                credentials,
                cookieStore(),
                shouldUseGzipContentcompression,
                new HashMap<HttpRoute, Integer>(),
                maxTotalConnections,
                defaultMaxConnectionsPerRoute,
                params);
    }

    private CookieStore cookieStore() {
        CookieStore cookieStoreToUse = null;
        if (shouldUseCookieStore == true) {
            cookieStoreToUse = cookieStore == null ? new BasicCookieStore()
                    : cookieStore;
        }
        return cookieStoreToUse;
    }

    public Class<HttpClient> getObjectType() {
        return HttpClient.class;
    }

    public boolean isSingleton() {
        return true;
    }

    public void setCredentials(Map<AuthScope, Credentials> credentials) {
        this.credentials = credentials;
    }

    public void setCookieStore(CookieStore cookieStore) {
        this.shouldUseCookieStore = true;
        this.cookieStore = cookieStore;
    }

    public void setShouldUseCookieStore(boolean shouldUseCookieStore) {
        this.shouldUseCookieStore = shouldUseCookieStore;
    }

    public void setShouldUseGzipContentcompression(
            boolean shouldUseGzipContentcompression) {
        this.shouldUseGzipContentcompression = shouldUseGzipContentcompression;
    }

    /**
     * See http://hc.apache.org/httpclient-3.x/preference-api.html
     *
     * @param params
     */
    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    public void setMaxTotalConnections(int maxTotalConnections) {
        this.maxTotalConnections = maxTotalConnections;
    }

    public void setDefaultMaxConnectionsPerRoute(int defaultMaxConnectionsPerRoute) {
        this.defaultMaxConnectionsPerRoute = defaultMaxConnectionsPerRoute;
    }

}
