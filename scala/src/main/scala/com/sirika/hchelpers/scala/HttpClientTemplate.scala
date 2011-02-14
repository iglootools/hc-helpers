package com.sirika.hchelpers.scala

import org.apache.http.client.methods.HttpUriRequest
import org.apache.http.HttpResponse
import org.apache.http.client.HttpClient
import org.apache.http.util.EntityUtils
import com.google.common.io.InputSupplier
import java.io.InputStream

/**
 * Scala-ish Spring-inspired Template for {@link HttpClient}. It makes sure that all resources
 * are properly closed in case of exceptions
 *
 * @author Sami Dalouche (sami.dalouche@gmail.com)
 *
 */
final class HttpClientTemplate[E](private[this] val httpClient: HttpClient,
                                  private[this] val defaultErrorHandler: HttpErrorHandler[E] = HttpErrorHandler.non2xxErrorHandler) {
  require(httpClient != null, "httpClient is required")
  require(defaultErrorHandler != null, "defaultErrorHandler is required")

  /**
   * @throws Exception for system and IO failures, but reports results of result handlers as a Left(e)
   */
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



