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

package org.cloudfoundry.client.v2;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Singular;

import java.util.List;

/**
 * Base class for requests that are paginated
 *
 * @param <T> the resource type
 */
@Data
public abstract class PaginatedResponse<T extends Resource<?>> {

    private final String nextUrl;

    private final String previousUrl;

    private final List<T> resources;

    private final Integer totalPages;

    private final Integer totalResults;

    protected PaginatedResponse(@JsonProperty("next_url") String nextUrl,
                                @JsonProperty("prev_url") String previousUrl,
                                @JsonProperty("resources") @Singular List<T> resources,
                                @JsonProperty("total_pages") Integer totalPages,
                                @JsonProperty("total_results") Integer totalResults) {
        this.nextUrl = nextUrl;
        this.previousUrl = previousUrl;
        this.resources = resources;
        this.totalPages = totalPages;
        this.totalResults = totalResults;
    }

}
