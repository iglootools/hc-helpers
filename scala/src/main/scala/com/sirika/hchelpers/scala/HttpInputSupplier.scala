package com.sirika.hchelpers.scala

import org.apache.http.client.methods.{HttpUriRequest}
import java.io.{ByteArrayInputStream, InputStream}
import com.google.common.io.{ByteStreams, InputSupplier}
;

/**
 * An {@link InputSupplier} that fetches its data from an HTTP GET request
 *
 * <p> If any of the error handlers return a failure, this failure is going to be thrown as an exception
 * </p>
 *
 * @author Sami Dalouche (sami.dalouche@gmail.com)
 *
 */
final class HttpInputSupplier[E <: Throwable](httpClientTemplate: HttpClientTemplate[E], httpUriRequest: HttpUriRequest, doOnError: HttpErrorHandler[E]) extends InputSupplier[InputStream] {
  def getInput: InputStream = {
    val result = httpClientTemplate.doWithResponse(
      httpUriRequest=httpUriRequest,
      doOnSuccess={response =>
        val entity = response.getEntity
        assume(entity != null, "entity is not supposed to be null")
        new ByteArrayInputStream(ByteStreams.toByteArray(entity.getContent))
      },
      doOnError=doOnError)
    result match {
      case Right(is) => is
      case Left(e) => throw e
    }
  }
}