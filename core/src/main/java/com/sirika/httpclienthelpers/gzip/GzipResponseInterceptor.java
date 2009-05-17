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

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.protocol.HttpContext;

/**
 * {@link HttpResponseInterceptor} that adds GZIP Decompression support
 * 
 * @see GzipDecompressingEntity
 * @see GzipRequestInterceptor
 * @author Sami Dalouche (sami.dalouche@gmail.com)
 *
 */
public final class GzipResponseInterceptor implements HttpResponseInterceptor {
    public void process(final HttpResponse response, final HttpContext context) throws HttpException, IOException {
	HttpEntity entity = response.getEntity();
	if(entity == null) {
	    return;
	}

	Header ceheader = entity.getContentEncoding();
	if (ceheader != null) {
	    HeaderElement[] codecs = ceheader.getElements();
	    for (int i = 0; i < codecs.length; i++) {
		if (codecs[i].getName().equalsIgnoreCase("gzip")) {
		    response.setEntity(new GzipDecompressingEntity(response.getEntity())); 
		    return;
		}
	    }
	}
    }
}