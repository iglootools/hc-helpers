/**
 * Copyright 2009 Sami Dalouche
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
package com.sirika.hchelpers.spring.internal;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.core.io.InputStreamSource;

import com.google.common.base.Preconditions;
import com.google.common.io.InputSupplier;

/**
 * An {@link InputSupplier} that delegates to an  {@link InputStreamSource}.
 * 
 * <p> Spring provides several InputStreamSources that are neat to use. This class bridges these
 * {@link InputStreamSource}'s to google guava's equivalent : {@link InputSupplier}.
 * </p>
 * 
 * @author Sami Dalouche (sami.dalouche@gmail.com)
 *
 */
public class InputStreamSourceToInputSupplierAdapter implements InputSupplier<InputStream> {
    
    private InputStreamSource inputStreamSource;
    
    public InputStreamSourceToInputSupplierAdapter(
            InputStreamSource inputStreamSource) {
        super();
        Preconditions.checkArgument(inputStreamSource != null, "inputStreamSource is mandatory");
        this.inputStreamSource = inputStreamSource;
    }

    public InputStream getInput() throws IOException {
        return inputStreamSource.getInputStream();
    }


}
