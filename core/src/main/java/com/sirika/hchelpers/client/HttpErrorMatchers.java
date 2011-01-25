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
