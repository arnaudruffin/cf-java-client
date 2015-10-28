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
 * The request payload for the List Spaces operation
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public final class ListSpacesRequest extends PaginatedRequest implements Validatable {

    /**
     * The application ids
     *
     * @param applicationIds the application ids
     * @return the application ids
     */
    @Getter(onMethod = @__(@FilterParameter("app_guid")))
    private final List<String> applicationIds;

    /**
     * The developer ids
     *
     * @param developerIds the developer ids
     * @return the developer ids
     */
    @Getter(onMethod = @__(@FilterParameter("developer_guid")))
    private final List<String> developerIds;

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

    @Builder
    ListSpacesRequest(OrderDirection orderDirection, Integer page, Integer resultsPerPage,
                      @Singular List<String> applicationIds, @Singular List<String> developerIds,
                      @Singular List<String> names, @Singular List<String> organizationIds) {
        super(orderDirection, page, resultsPerPage);
        this.applicationIds = applicationIds;
        this.developerIds = developerIds;
        this.names = names;
        this.organizationIds = organizationIds;
    }

    @Override
    public ValidationResult isValid() {
        return new ValidationResult();
    }

}
