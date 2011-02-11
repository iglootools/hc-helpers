package com.sirika.hchelpers.scala

import org.apache.http.client.methods.HttpUriRequest
import com.google.common.io.NullOutputStream

import com.sirika.hchelpers.scala.Implicits._
import org.apache.http.{HttpEntity, HttpResponse}
import org.apache.http.client.{HttpResponseException, HttpClient}
import org.apache.http.util.EntityUtils

/**
 * Scala-ish Spring-inspired Template for {@link HttpClient}. It makes sure that all resources
 * are properly closed in case of exceptions
 *
 * @author Sami Dalouche (sami.dalouche@gmail.com)
 *
 */
class HttpClientTemplate[E <: Exception](private[this] val httpClient: HttpClient,
                                         private[this] val defaultErrorHandler: HttpErrorHandler[E] = HttpErrorHandler.non2xxErrorHandler) {
  require(httpClient != null, "httpClient is required")
  require(defaultErrorHandler != null, "defaultErrorHandler is required")

  def doWithResponse[R](httpUriRequest: HttpUriRequest,
                        doOnSuccess: (HttpResponse)=> R = {r: HttpResponse => ()},
                        doOnError:HttpErrorHandler[E] = defaultErrorHandler):Either[E,R] = {
    try {
      val httpResponse = this.httpClient.execute(httpUriRequest)
      val errorHandler = doOnError.orElse(defaultErrorHandler)
      val result =  if(errorHandler.appliesTo(httpResponse)) {
        Left(errorHandler.handle(httpResponse))
      } else {
        Right(doOnSuccess(httpResponse))
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

  private def releaseResources(httpResponse: HttpResponse): Unit = {
    val entity = Option(httpResponse.getEntity)
    entity.foreach { e =>
      e.writeTo(new NullOutputStream)
      EntityUtils.consume(e)
    }
  }
}



