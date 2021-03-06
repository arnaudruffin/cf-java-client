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

import org.cloudfoundry.client.ValidationResult;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public final class PaginatedRequestTest {

    @Test
    public void test() {
        StubPaginatedRequest request = new StubPaginatedRequest()
                .withPage(-1)
                .withPerPage(-2);

        assertEquals(Integer.valueOf(-1), request.getPage());
        assertEquals(Integer.valueOf(-2), request.getPerPage());
    }

    @Test
    public void isPaginationRequestValid() {
        ValidationResult result = new StubPaginatedRequest()
                .withPage(10)
                .withPerPage(10)
                .isPaginatedRequestValid();

        assertEquals(ValidationResult.Status.VALID, result.getStatus());
    }

    @Test
    public void isPaginationRequestValidNull() {
        ValidationResult result = new StubPaginatedRequest()
                .isPaginatedRequestValid();

        assertEquals(ValidationResult.Status.VALID, result.getStatus());
    }

    @Test
    public void isPaginationRequestValidZeroPage() {
        ValidationResult result = new StubPaginatedRequest()
                .withPage(0)
                .isPaginatedRequestValid();

        assertEquals(ValidationResult.Status.INVALID, result.getStatus());
        assertEquals("page must be greater than or equal to 1", result.getMessages().get(0));
    }

    @Test
    public void isPaginationRequestValidZeroPerPage() {
        ValidationResult result = new StubPaginatedRequest()
                .withPerPage(0)
                .isPaginatedRequestValid();

        assertEquals(ValidationResult.Status.INVALID, result.getStatus());
        assertEquals("perPage must be between 1 and 5000 inclusive", result.getMessages().get(0));
    }

    @Test
    public void isPaginationRequestValidExcessivePerPage() {
        ValidationResult result = new StubPaginatedRequest()
                .withPerPage(10_000)
                .isPaginatedRequestValid();

        assertEquals(ValidationResult.Status.INVALID, result.getStatus());
        assertEquals("perPage must be between 1 and 5000 inclusive", result.getMessages().get(0));
    }

    private static final class StubPaginatedRequest extends PaginatedRequest<StubPaginatedRequest> {
    }

}
