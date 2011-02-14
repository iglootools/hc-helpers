package com.sirika.hchelpers.scala

import org.apache.http.client.HttpClient
;
object Implicits {
  implicit def asHttpClientTemplate(httpClient: HttpClient): HttpClientTemplate[Exception] = new HttpClientTemplate(httpClient=httpClient)
}