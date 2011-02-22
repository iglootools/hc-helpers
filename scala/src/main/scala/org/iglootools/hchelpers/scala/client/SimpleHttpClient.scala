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
package org.iglootools.hchelpers.scala.client

import org.apache.http.auth.{AuthScope, Credentials}
import org.apache.http.client.{CookieStore, HttpClient}
import org.iglootools.hchelpers.core.DefaultHttpClientFactory
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