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
        Preconditions.checkState(inputStreamSource != null, "inputStreamSource is mandatory");
        this.inputStreamSource = inputStreamSource;
    }

    public InputStream getInput() throws IOException {
        return inputStreamSource.getInputStream();
    }


}
