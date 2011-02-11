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
package com.sirika.hchelpers.scala;
import org.apache.http.HttpResponse
import org.apache.http.StatusLine
import org.apache.http.client.HttpResponseException

object HttpErrorHandler {
  def apply[E](matches: (StatusLine) => Boolean = {s => false}, doOnError: (HttpResponse) => E = {r: HttpResponse => ()}): HttpErrorHandler[E] = new HttpErrorHandler[E] {
    def handle(response: HttpResponse): E = doOnError(response)
    def appliesTo(statusLine: StatusLine): Boolean = matches(statusLine)
  }

  def non2xxErrorHandler: HttpErrorHandler[Exception] =
    HttpErrorHandler(
      matches = {s => s.getStatusCode < 200 || s.getStatusCode >= 300},
      doOnError = {r => new HttpResponseException(r.getStatusLine.getStatusCode, r.getStatusLine.getReasonPhrase)})
}

/**
 * Reacts when there is a specific (set of) Http Errors
 *
 *  @author Sami Dalouche (sami.dalouche@gmail.com)
 */
trait HttpErrorHandler[+E] extends PartialFunction[HttpResponse, E]{
  /**
   *
   * @param statusLine
   * @return whether this matcher applis to the current Http error code
   */
  def appliesTo(statusLine: StatusLine): Boolean

  /**
   * perform the action of the handler
   *
   * @param response
   * @throws Exception
   */
  def handle(response: HttpResponse): E

  def isDefinedAt(response: HttpResponse): Boolean = appliesTo(response.getStatusLine)
  def apply(response: HttpResponse): E = handle(response)
}


