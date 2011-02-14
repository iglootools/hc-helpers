package com.sirika.hchelpers.scala

import org.apache.http.auth.{AuthScope, Credentials}
import org.apache.http.client.{CookieStore, HttpClient}
import com.sirika.hchelpers.core.DefaultHttpClientFactory
import scala.collection.JavaConversions._

final object SimpleHttpClient {
  def apply(credentials: Map[AuthScope, Credentials]=Map(), params: Map[String, AnyRef]=Map(), cookieStore: Option[CookieStore]=None, shouldUseGzipCompression: Boolean=true): HttpClient = {
    DefaultHttpClientFactory.httpClient(credentials, params, cookieStore.getOrElse(null), shouldUseGzipCompression)
  }
}