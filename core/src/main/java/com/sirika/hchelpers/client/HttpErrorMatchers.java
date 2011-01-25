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

import org.apache.http.StatusLine;

public class HttpErrorMatchers {
    public static HttpErrorMatcher statusCodeEquals(final int httpErrorCode) {
        return new HttpErrorMatcher() {
            public boolean matches(StatusLine statusLine) {
                return httpErrorCode == statusLine.getStatusCode();
            }
        };
    }

    public static HttpErrorMatcher statusCodeGreaterOrEquals(final int httpErrorCode) {
        return new HttpErrorMatcher() {
            public boolean matches(StatusLine statusLine) {
                return statusLine.getStatusCode() >= httpErrorCode;
            }
        };
    }
}
