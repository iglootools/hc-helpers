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
/**
 * 
 */
package com.sirika.hchelpers.mime;

import org.apache.http.entity.mime.MultipartEntity;


/**
 * A {@link MultipartEntity} that is repeatable. It is meant to be used with
 * SourceBody's that are repeatable, such as InputSupplierSourceBody
 * 
 * @author Sami Dalouche (sami.dalouche@gmail.com)
 * 
 */
public final class RepeatableMultipartEntity extends MultipartEntity {
    @Override
    public boolean isRepeatable() {
        return true;
    }
}