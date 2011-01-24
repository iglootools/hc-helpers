package com.sirika.hchelpers.spring;

import java.io.InputStream;

import org.springframework.core.io.InputStreamSource;

import com.google.common.io.InputSupplier;
import com.sirika.hchelpers.spring.internal.InputStreamSourceToInputSupplierAdapter;

public class InputStreamSources {
    /**
     * Transforms a Spring {@link InputStreamSource} to a Google-Guava {@link InputSupplier}
     * 
     * @param inputSupplier
     * @return
     */
    public static InputSupplier<InputStream> toInputSupplier(InputStreamSource inputStreamSource) {
        return new InputStreamSourceToInputSupplierAdapter(inputStreamSource);
    }
    
}
