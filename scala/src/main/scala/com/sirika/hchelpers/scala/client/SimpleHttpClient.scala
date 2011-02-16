package com.sirika.hchelpers.scala.client

import org.apache.http.auth.{AuthScope, Credentials}
import org.apache.http.client.{CookieStore, HttpClient}
import com.sirika.hchelpers.core.DefaultHttpClientFactory
import org.apache.http.conn.routing.HttpRoute
import scala.collection.JavaConversions._
import org.apache.http.impl.client.DefaultHttpClient

final object SimpleHttpClient {
  def apply(credentials: Map[AuthScope,Credentials]=Map(),
            cookieStore: Option[CookieStore]=None,
            shouldUseGzipCompression: Boolean=true,
            maxNumberOfConnectionsPerRoute: Map[HttpRoute, Int] = Map(),
            maxTotalNumberOfConnections: Int = DefaultHttpClientFactory.DEFAULT_MAX_NUMBER_OF_CONNECTIONS,
            defaultMaxNumberOfConnectionsPerRoute: Int = DefaultHttpClientFactory.DEFAULT_MAX_NUMBER_OF_CONNECTIONS_PER_ROUTE,
            params: Map[String, AnyRef]=Map()): HttpClient = {
    DefaultHttpClientFactory.httpClient(
      credentials,
      cookieStore.getOrElse(null),
      shouldUseGzipCompression,
      maxNumberOfConnectionsPerRoute map { case (x,y) => (x,y:java.lang.Integer) },
      maxTotalNumberOfConnections,
      defaultMaxNumberOfConnectionsPerRoute,
      params)
  }
}