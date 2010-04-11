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
package com.sirika.httpclienthelpers.gzip;

import java.io.IOException;

import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.protocol.HttpContext;

/**
 * {@link HttpRequestInterceptor} that advertises GZIP compression support to
 * the server.
 * 
 * @see GzipDecompressingEntity
 * @see GzipResponseInterceptor
 * 
 * @author Sami Dalouche (sami.dalouche@gmail.com)
 * 
 */
public final class GzipRequestInterceptor implements HttpRequestInterceptor {
    public void process(HttpRequest request, HttpContext context)
            throws HttpException, IOException {
        if (!request.containsHeader("Accept-Encoding")) {
            request.addHeader("Accept-Encoding", "gzip");
        }
    }
}