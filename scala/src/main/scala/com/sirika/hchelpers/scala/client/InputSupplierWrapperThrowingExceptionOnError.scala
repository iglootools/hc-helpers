package com.sirika.hchelpers.scala.client

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
import java.io.InputStream
import com.google.common.io.InputSupplier
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
protected[hchelpers] final class InputSupplierWrapperThrowingExceptionOnError[E <: Throwable](inputSupplier: InputSupplier[Either[E, InputStream]]) extends InputSupplier[InputStream] {
  def getInput: InputStream = {
    val result = inputSupplier.getInput()
    result match {
      case Right(is) => is
      case Left(e) => throw e
    }
  }
}