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

package org.cloudfoundry.client.v3;

import lombok.Builder;
import org.cloudfoundry.client.ValidationResult;
import org.junit.Test;

import static org.cloudfoundry.client.ValidationResult.Status.INVALID;
import static org.cloudfoundry.client.ValidationResult.Status.VALID;
import static org.cloudfoundry.client.v3.PaginatedAndSortedRequest.OrderBy.CREATED_AT;
import static org.cloudfoundry.client.v3.PaginatedAndSortedRequest.OrderBy.UPDATED_AT;
import static org.cloudfoundry.client.v3.PaginatedAndSortedRequest.OrderDirection.ASC;
import static org.cloudfoundry.client.v3.PaginatedAndSortedRequest.OrderDirection.DESC;
import static org.junit.Assert.assertEquals;

public final class PaginatedAndSortedRequestTest {

    @Test
    public void orderedBy() {
        assertEquals("created_at", CREATED_AT.toString());
        assertEquals("updated_at", UPDATED_AT.toString());
    }

    @Test
    public void orderDirection() {
        assertEquals("asc", ASC.toString());
        assertEquals("desc", DESC.toString());
    }

    @Test
    public void isPaginatedAndSortedRequestValid() {
        ValidationResult result = StubPaginatedAndSortedRequest.builder()
                .build()
                .isPaginatedAndSortedRequestValid();

        assertEquals(VALID, result.getStatus());
    }

    @Test
    public void isPaginatedAndSortedRequestValidInvalidPaginatedRequest() {
        ValidationResult result = StubPaginatedAndSortedRequest.builder()
                .page(-1)
                .build()
                .isPaginatedAndSortedRequestValid();

        assertEquals(INVALID, result.getStatus());
        assertEquals("page must be greater than or equal to 1", result.getMessages().get(0));
    }

    private static final class StubPaginatedAndSortedRequest extends PaginatedAndSortedRequest {

        @Builder
        private StubPaginatedAndSortedRequest(Integer page, Integer perPage, OrderBy orderBy,
                                              OrderDirection orderDirection) {
            super(page, perPage, orderBy, orderDirection);
        }
    }

}
