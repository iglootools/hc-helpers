package org.iglootools.hchelpers.scala.client

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
import org.apache.http.client.methods.HttpUriRequest
import java.io.{ByteArrayInputStream, InputStream}
import com.google.common.io.{ByteStreams, InputSupplier}

/**
 * An {@link InputSupplier} that fetches its data from an HTTP GET request and stores the intermediate result in memory
 *
 *
 * @author Sami Dalouche (sami.dalouche@gmail.com)
 *
 */
protected[hchelpers] final class InMemoryHttpInputSupplier[E <: Throwable](httpClientTemplate: HttpClientTemplate[E], httpUriRequest: HttpUriRequest, onError: HttpErrorHandler[E]) extends InputSupplier[Either[E, InputStream]] {
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
          case Left(e) => throw e
        }
      }
    }
  }

}