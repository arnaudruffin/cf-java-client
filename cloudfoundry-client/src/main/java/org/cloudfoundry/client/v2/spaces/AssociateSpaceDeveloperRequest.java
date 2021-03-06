/*
 * Copyright 2013-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.cloudfoundry.client.v2.spaces;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.cloudfoundry.client.Validatable;
import org.cloudfoundry.client.ValidationResult;

/**
 * The request payload for the Associate Developer with the Space operation
 *
 * <p><b>This class is NOT threadsafe.</b>
 */
public final class AssociateSpaceDeveloperRequest implements Validatable {

    private volatile String developerId;

    private volatile String id;

    /**
     * Returns the developer id
     *
     * @return the developer id
     */
    @JsonIgnore
    public String getDeveloperId() {
        return developerId;
    }

    /**
     * Configure the developer id
     *
     * @param developerId the developer id
     * @return {@code this}
     */
    public AssociateSpaceDeveloperRequest withDeveloperId(String developerId) {
        this.developerId = developerId;
        return this;
    }

    /**
     * Returns the id
     *
     * @return the id
     */
    @JsonIgnore
    public String getId() {
        return id;
    }

    /**
     * Configure the id
     *
     * @param id the id
     * @return {@code this}
     */
    public AssociateSpaceDeveloperRequest withId(String id) {
        this.id = id;
        return this;
    }

    @Override
    public ValidationResult isValid() {
        ValidationResult result = new ValidationResult();

        if (this.id == null) {
            result.invalid("id must be specified");
        }

        if (this.developerId == null) {
            result.invalid("developerId must be specified");
        }

        return result;
    }
}
