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

package org.cloudfoundry.client.v3.applications;

import org.cloudfoundry.client.v3.Hash;
import org.cloudfoundry.client.v3.Link;
import org.cloudfoundry.client.v3.applications.ListApplicationDropletsResponse.Resource;
import org.junit.Test;

import java.util.Collections;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public final class ListApplicationDropletsResponseTest {

    @Test
    public void test() {
        Hash hash = ApplicationsTestUtil.getHash();
        Map<String, String> environmentVariables = ApplicationsTestUtil.getEnvironment();
        Map<String, Link> links = ApplicationsTestUtil.getLinks();

        Resource resource = new Resource()
                .withBuildpack("test-buildpack")
                .withCreatedAt("test-created-at")
                .withEnvironmentVariable("test-key-1", environmentVariables.get("test-key-1"))
                .withEnvironmentVariables(Collections.singletonMap("test-key-2",
                        environmentVariables.get("test-key-2")))
                .withError("test-error")
                .withHash(hash)
                .withId("test-id")
                .withLink("test-link-1", links.get("test-link-1"))
                .withLinks(Collections.singletonMap("test-link-2", links.get("test-link-2")))
                .withProcfile("test-procfile")
                .withState("test-state")
                .withUpdatedAt("test-updated-at");

        assertEquals("test-buildpack", resource.getBuildpack());
        assertEquals("test-created-at", resource.getCreatedAt());
        assertEquals(environmentVariables, resource.getEnvironmentVariables());
        assertEquals("test-error", resource.getError());
        assertEquals(hash, resource.getHash());
        assertEquals("test-id", resource.getId());
        assertEquals(links, resource.getLinks());
        assertEquals("test-procfile", resource.getProcfile());
        assertEquals("test-state", resource.getState());
        assertEquals("test-updated-at", resource.getUpdatedAt());

    }

}
