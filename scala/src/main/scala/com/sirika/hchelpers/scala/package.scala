package com.sirika.hchelpers

import org.apache.http.client.HttpClient

package object scala {
  type HttpClientTemplate[E <: Throwable] = scala.client.HttpClientTemplate[E]
  val SimpleHttpClient = scala.client.SimpleHttpClient

  type HttpErrorHandler[+E] = scala.client.HttpErrorHandler[E]
  val HttpErrorHandler = scala.client.HttpErrorHandler


  implicit def asHttpClientTemplate(httpClient: HttpClient): HttpClientTemplate[Exception] = new HttpClientTemplate(httpClient=httpClient)
}