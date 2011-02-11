package com.sirika.hchelpers.scala

import org.apache.http.client.methods.HttpUriRequest
import com.google.common.io.NullOutputStream

import com.sirika.hchelpers.scala.Implicits._
import org.apache.http.{HttpEntity, HttpResponse}
import org.apache.http.client.{HttpResponseException, HttpClient}

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
    var httpResponse: Option[HttpResponse] = None
    try {
      httpResponse = Some(this.httpClient.execute(httpUriRequest))
      if(doOnError.orElse(defaultErrorHandler).appliesTo(httpResponse.get)) {
        Left(doOnError.handle(httpResponse.get))
      } else {
        Right(doOnSuccess(httpResponse.get))
      }
    }
    catch {
      case e: Exception => {
        httpUriRequest.abort
        throw e
      }
    }
    finally {
      httpResponse.foreach(releaseResources(_))
    }
  }

  private def releaseResources(httpResponse: HttpResponse): Unit = {
    if(httpResponse != null) {
      var entity: HttpEntity = httpResponse.getEntity
      if (entity != null)
        entity.writeTo(new NullOutputStream)
    }
  }
}



