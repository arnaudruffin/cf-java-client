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

import org.cloudfoundry.client.ValidationResult;
import org.junit.Test;

import java.util.Collections;

import static org.cloudfoundry.client.ValidationResult.Status.INVALID;
import static org.cloudfoundry.client.ValidationResult.Status.VALID;
import static org.junit.Assert.assertEquals;

public final class CreateSpaceRequestTest {

    @Test
    public void test() {
        CreateSpaceRequest request = new CreateSpaceRequest()
                .withAllowSsh(true)
                .withAuditorId("test-auditor-id")
                .withDeveloperId("test-developer-id")
                .withDomainId("test-domain-id")
                .withManagerId("test-manager-id")
                .withName("test-name")
                .withOrganizationId("test-organization-id")
                .withSecurityGroupId("test-security-group-id")
                .withSpaceQuotaDefinitionId("test-space-quote-definition-id");

        assertEquals(true, request.getAllowSsh());
        assertEquals(Collections.singletonList("test-auditor-id"), request.getAuditorIds());
        assertEquals(Collections.singletonList("test-developer-id"), request.getDeveloperIds());
        assertEquals(Collections.singletonList("test-domain-id"), request.getDomainIds());
        assertEquals(Collections.singletonList("test-manager-id"), request.getManagerIds());
        assertEquals("test-name", request.getName());
        assertEquals("test-organization-id", request.getOrganizationId());
        assertEquals(Collections.singletonList("test-security-group-id"), request.getSecurityGroups());
        assertEquals("test-space-quote-definition-id", request.getSpaceQuotaDefinitionId());
    }

    @Test
    public void isValid() {
        ValidationResult result = new CreateSpaceRequest()
                .withName("test-name")
                .withOrganizationId("test-organization-id")
                .isValid();

        assertEquals(VALID, result.getStatus());
    }

    @Test
    public void isValidNoName() {
        ValidationResult result = new CreateSpaceRequest()
                .withOrganizationId("test-organization-id")
                .isValid();

        assertEquals(INVALID, result.getStatus());
        assertEquals("name must be specified", result.getMessages().get(0));
    }

    @Test
    public void isValidNoOrganizationId() {
        ValidationResult result = new CreateSpaceRequest()
                .withName("test-name")
                .isValid();

        assertEquals(INVALID, result.getStatus());
        assertEquals("organization id must be specified", result.getMessages().get(0));
    }

}
