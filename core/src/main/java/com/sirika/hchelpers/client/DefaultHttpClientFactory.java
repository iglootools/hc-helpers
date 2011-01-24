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
package com.sirika.hchelpers.client;

import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;

import com.sirika.hchelpers.gzip.GzipRequestInterceptor;
import com.sirika.hchelpers.gzip.GzipResponseInterceptor;

/**
 * Helper class to ease the creation of {@link HttpClient}
 * 
 * @author Sami Dalouche (sami.dalouche@gmail.com)
 * 
 */
public class DefaultHttpClientFactory {
    public static DefaultHttpClient defaultHttpClient() {
        return new DefaultHttpClient(threadSafeClientConnManager(defaultHttpParams()), defaultHttpParams());
    }

    public static DefaultHttpClient httpClient(
            Map<AuthScope, Credentials> credentials,
            Map<String, Object> params, CookieStore cookieStore,
            boolean shouldUseGzipCompression) {
        DefaultHttpClient httpClient = new DefaultHttpClient(
                threadSafeClientConnManager(httpParams(params)),
                httpParams(params));
        for (Entry<AuthScope, Credentials> e : credentials.entrySet()) {
            httpClient.getCredentialsProvider().setCredentials(e.getKey(),
                    e.getValue());
        }

        if (cookieStore != null) {
            httpClient.setCookieStore(cookieStore);
        }

        if (shouldUseGzipCompression) {
            handleGzipContentCompression(httpClient);

        }
        return httpClient;
    }

    private static void handleGzipContentCompression(DefaultHttpClient httpClient) {
        httpClient.addRequestInterceptor(new GzipRequestInterceptor());
        httpClient.addResponseInterceptor(new GzipResponseInterceptor());
    }

    public static ThreadSafeClientConnManager threadSafeClientConnManager(HttpParams httpParams) {
        return new ThreadSafeClientConnManager(httpParams, defaultSchemeRegistry());
    }

    public static SchemeRegistry defaultSchemeRegistry() {
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("http", 80, PlainSocketFactory.getSocketFactory()));
        schemeRegistry.register(new Scheme("https", 443, SSLSocketFactory.getSocketFactory()));
        return schemeRegistry;
    }

    public static BasicHttpParams httpParams(Map<String, Object> params) {
        BasicHttpParams httpParams = new BasicHttpParams();
        for (Entry<String, Object> e : params.entrySet()) {
            httpParams.setParameter(e.getKey(), e.getValue());
        }
        return httpParams;
    }

    public static BasicHttpParams defaultHttpParams() {
        return new BasicHttpParams();
    }
}
