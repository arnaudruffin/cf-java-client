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

package org.cloudfoundry.client.v2.organizations.auditors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import org.cloudfoundry.client.Validatable;
import org.cloudfoundry.client.ValidationResult;

/**
 * The request payload for the Create Auditor operation
 */
@Data
public final class CreateAuditorRequest implements Validatable {

    /**
     * The auditor id
     *
     * @param auditorId the auditor id
     * @return the auditor id
     */
    @Getter(onMethod = @__(@JsonIgnore))
    private final String auditorId;

    /**
     * The organization id
     *
     * @param organizationId the organization id
     * @return the organization id
     */
    @Getter(onMethod = @__(@JsonIgnore))
    private final String organizationId;

    @Builder
    CreateAuditorRequest(String auditorId, String organizationId) {
        this.auditorId = auditorId;
        this.organizationId = organizationId;
    }

    @Override
    public ValidationResult isValid() {
        ValidationResult result = new ValidationResult();

        if (this.auditorId == null) {
            result.invalid("auditor id must be specified");
        }

        if (this.organizationId == null) {
            result.invalid("organization id must be specified");
        }

        return result;
    }
}
