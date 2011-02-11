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
final class InputSupplierWrapperThrowingExceptionOnError[E <: Throwable](inputSupplier: InputSupplier[Either[E, InputStream]]) extends InputSupplier[InputStream] {
  def getInput: InputStream = {
    val result = inputSupplier.getInput()
    result match {
      case Right(is) => is
      case Left(e) => throw e
    }
  }
}