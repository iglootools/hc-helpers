package com.sirika.hchelpers.scala.internal

import org.apache.http.{HttpResponse, StatusLine}
import com.sirika.hchelpers.scala.HttpErrorHandler

;

/**
 * {@link HttpErrorHandler} that delegates to {@link HttpErrorMatcher} and {@link HttpResponseCallback}
 *
 * @author Sami Dalouche (sami.dalouche@gmail.com)
 *
 */
class FunctionalHttpErrorHandler[T](val matches: (StatusLine) => Boolean, doOnError: (HttpResponse)=> T) extends HttpErrorHandler[T] {
  require(matches != null, "matches is required")
  require(doOnError != null, "callback is required")

  def appliesTo(statusLine: StatusLine): Boolean = {
    matches(statusLine)
  }

  def handle(response: HttpResponse): T = {
    doOnError(response)
  }
}