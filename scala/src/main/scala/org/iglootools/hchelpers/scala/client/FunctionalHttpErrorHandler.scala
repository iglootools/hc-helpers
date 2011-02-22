package org.iglootools.hchelpers.scala.client

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
import org.apache.http.{HttpResponse, StatusLine}

/**
 * {@link HttpErrorHandler} that delegates to {@link HttpErrorMatcher} and {@link HttpResponseCallback}
 *
 * @author Sami Dalouche (sami.dalouche@gmail.com)
 *
 */
protected[hchelpers] final class FunctionalHttpErrorHandler[T](val matches: (StatusLine) => Boolean, doOnError: (HttpResponse)=> T) extends HttpErrorHandler[T] {
  require(matches != null, "matches is required")
  require(doOnError != null, "callback is required")

  def appliesTo(response: HttpResponse): Boolean = {
    matches(response.getStatusLine)
  }

  def handle(response: HttpResponse): T = {
    doOnError(response)
  }
}