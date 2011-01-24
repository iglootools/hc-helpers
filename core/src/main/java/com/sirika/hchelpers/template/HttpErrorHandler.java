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
package com.sirika.hchelpers.template;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;

/**
 * Reacts when there is a specific (set of) Http Errors
 * 
 * @author Sami Dalouche (sami.dalouche@gmail.com)
 * 
 */
public interface HttpErrorHandler {
    /**
     * 
     * @param statusLine
     * @return whether this matcher applis to the current Http error code
     */
    boolean apppliesTo(StatusLine statusLine);

    /**
     * perform the action of the handler
     * 
     * @param response
     * @throws Exception
     */
    void handle(HttpResponse response) throws Exception;
}
