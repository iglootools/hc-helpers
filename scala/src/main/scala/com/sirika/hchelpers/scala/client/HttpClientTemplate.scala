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
package com.sirika.hchelpers.scala.client

import org.apache.http.client.methods.HttpUriRequest
import org.apache.http.HttpResponse
import org.apache.http.client.HttpClient
import org.apache.http.util.EntityUtils
import com.google.common.io.{CharStreams, InputSupplier}
import java.io.{InputStreamReader, InputStream}
import com.google.common.base.Charsets

/**
 * Scala-ish Spring-inspired Template for {@link HttpClient}. It makes sure that all resources
 * are properly closed in case of exceptions
 *
 * @author Sami Dalouche (sami.dalouche@gmail.com)
 *
 */
final class HttpClientTemplate[E <: Throwable](private[this] val httpClient: HttpClient,
                                  private[this] val defaultErrorHandler: HttpErrorHandler[E] = HttpErrorHandler.non2xxErrorHandler) {
  require(httpClient != null, "httpClient is required")
  require(defaultErrorHandler != null, "defaultErrorHandler is required")

  /**
   * @throws Exception for system and IO failures, but reports results of result handlers as a Left(e)
   */
  def doWithResponse[R](httpUriRequest: HttpUriRequest,
                        onSuccess: (HttpResponse)=> R,
                        onError:HttpErrorHandler[E] = defaultErrorHandler):Either[E,R] = {
    try {
      val httpResponse = this.httpClient.execute(httpUriRequest)
      val errorHandler = onError.orElse(defaultErrorHandler)
      val result =  if(errorHandler.appliesTo(httpResponse)) {
        Left(errorHandler.handle(httpResponse))
      } else {
        Right(onSuccess(httpResponse))
      }
      releaseResources(httpResponse)
      result
    }
    catch {
      case e: Exception => {
        httpUriRequest.abort
        throw e
      }
    }
  }

  /**
   * Simple shortcut for doWithResponse that considers the result is a well-formed 2xx result that can be converted to a String.
   * @throws Exception if anything wrong happens
   */
  def fetchAsString(httpUriRequest: HttpUriRequest): String = {
    val result = doWithResponse(httpUriRequest = httpUriRequest,onSuccess =  {r: HttpResponse =>
      assume(r.getEntity != null, "entity cannot be null")
      CharStreams.toString(new InputStreamReader(r.getEntity.getContent, Charsets.UTF_8))})

    result match {
      case Left(e) => throw e
      case Right(s) => s
    }
  }

  def inMemoryHttpInputSupplierFor(httpUriRequest: HttpUriRequest,
                                   doOnError:HttpErrorHandler[E] = defaultErrorHandler): InputSupplier[Either[E, InputStream]] = {
    new InMemoryHttpInputSupplier(this, httpUriRequest, doOnError)
  }


  private def releaseResources(httpResponse: HttpResponse): Unit = {
    val entity = Option(httpResponse.getEntity)
    entity.foreach { e =>
    //e.writeTo(new NullOutputStream)
      EntityUtils.consume(e)
    }
  }
}



