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

import org.junit.Test;

import static org.cloudfoundry.client.v2.PaginatedRequest.OrderDirection.ASC;
import static org.cloudfoundry.client.v2.PaginatedRequest.OrderDirection.DESC;
import static org.junit.Assert.assertEquals;

public final class PaginatedRequestTest {

    @Test
    public void test() {
        StubPaginatedRequest request = new StubPaginatedRequest()
                .withOrderDirection(ASC)
                .withPage(-1)
                .withResultsPerPage(-2);

        assertEquals(ASC, request.getOrderDirection());
        assertEquals(Integer.valueOf(-1), request.getPage());
        assertEquals(Integer.valueOf(-2), request.getResultsPerPage());
    }

    @Test
    public void orderedBy() {
        assertEquals("asc", ASC.toString());
        assertEquals("desc", DESC.toString());
    }

    private static final class StubPaginatedRequest extends PaginatedRequest<StubPaginatedRequest> {
    }

}
