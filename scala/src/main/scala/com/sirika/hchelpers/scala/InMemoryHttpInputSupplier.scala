package com.sirika.hchelpers.scala

import org.apache.http.client.methods.HttpUriRequest
import java.io.{ByteArrayInputStream, InputStream}
import com.google.common.io.{ByteStreams, InputSupplier}
;

/**
 * An {@link InputSupplier} that fetches its data from an HTTP GET request and stores the intermediate result in memory
 *
 *
 * @author Sami Dalouche (sami.dalouche@gmail.com)
 *
 */
final class InMemoryHttpInputSupplier[E](httpClientTemplate: HttpClientTemplate[E], httpUriRequest: HttpUriRequest, onError: HttpErrorHandler[E]) extends InputSupplier[Either[E, InputStream]] {
  def getInput: Either[E, InputStream] = {
    httpClientTemplate.doWithResponse(
      httpUriRequest=httpUriRequest,
      onSuccess={response =>
        val entity = response.getEntity
        assume(entity != null, "entity is not supposed to be null")
        new ByteArrayInputStream(ByteStreams.toByteArray(entity.getContent))
      },
      onError=onError)
  }

  /**
   * Returns an exception-enabled version of HttpInputSupplier
   *
   *  <p> If any of the error handlers return a failure, this failure is going to be thrown as an exception
   * </p>
   */
  def toInputSupplierThrowingExceptionOnError: InputSupplier[InputStream] = {
    new InputSupplier[InputStream] {
      def getInput: InputStream = {
        val result = InMemoryHttpInputSupplier.this.getInput()
        result match {
          case Right(is) => is
          case Left(e) => e match {
            case ex: Exception => throw ex
            case _ => throw new RuntimeException("" + e)
          }
        }
      }
    }
  }

}