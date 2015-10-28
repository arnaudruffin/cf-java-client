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
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Singular;
import lombok.ToString;
import org.cloudfoundry.client.Validatable;
import org.cloudfoundry.client.ValidationResult;
import org.cloudfoundry.client.v2.FilterParameter;
import org.cloudfoundry.client.v2.PaginatedRequest;

import java.util.List;

/**
 * The request payload for the List all Apps for the Space operation
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public final class ListSpaceApplicationsRequest extends PaginatedRequest implements Validatable {

    /**
     * The diego flags
     *
     * @param diegos the diego flags
     * @return the diego flags
     */
    @Getter(onMethod = @__(@FilterParameter("diego")))
    private final List<Boolean> diegos;

    /**
     * The id
     *
     * @param id the id
     * @return the id
     */
    @Getter(onMethod = @__(@JsonIgnore))
    private final String id;

    /**
     * The names
     *
     * @param names the names
     * @return the names
     */
    @Getter(onMethod = @__(@FilterParameter("name")))
    private final List<String> names;

    /**
     * The organization ids
     *
     * @param organizationIds the organization ids
     * @return the organization ids
     */
    @Getter(onMethod = @__(@FilterParameter("organization_guid")))
    private final List<String> organizationIds;

    /**
     * The space ids
     *
     * @param spaceIds the space ids
     * @return the space ids
     */
    @Getter(onMethod = @__(@FilterParameter("space_guid")))
    private final List<String> spaceIds;

    /**
     * The stack ids
     *
     * @param stackIds the stack ids
     * @return the stack ids
     */
    @Getter(onMethod = @__(@FilterParameter("stack_guid")))
    private final List<String> stackIds;

    @Builder
    ListSpaceApplicationsRequest(OrderDirection orderDirection, Integer page, Integer resultsPerPage,
                                 @Singular List<Boolean> diegos, String id, @Singular List<String> names,
                                 @Singular List<String> organizationIds, @Singular List<String> spaceIds,
                                 @Singular List<String> stackIds) {
        super(orderDirection, page, resultsPerPage);
        this.diegos = diegos;
        this.id = id;
        this.names = names;
        this.organizationIds = organizationIds;
        this.spaceIds = spaceIds;
        this.stackIds = stackIds;
    }

    @Override
    public ValidationResult isValid() {
        ValidationResult result = new ValidationResult();

        if (this.id == null) {
            result.invalid("id must be specified");
        }

        return result;
    }

}
