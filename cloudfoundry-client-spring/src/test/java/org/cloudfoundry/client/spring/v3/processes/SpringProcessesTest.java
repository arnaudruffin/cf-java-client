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

package org.cloudfoundry.client.spring.v3.processes;

import org.cloudfoundry.client.RequestValidationException;
import org.cloudfoundry.client.spring.AbstractRestTest;
import org.cloudfoundry.client.v2.CloudFoundryException;
import org.cloudfoundry.client.v3.processes.DeleteProcessInstanceRequest;
import org.cloudfoundry.client.v3.processes.GetProcessRequest;
import org.cloudfoundry.client.v3.processes.GetProcessResponse;
import org.cloudfoundry.client.v3.processes.ListProcessesRequest;
import org.cloudfoundry.client.v3.processes.ListProcessesResponse;
import org.cloudfoundry.client.v3.processes.ScaleProcessRequest;
import org.cloudfoundry.client.v3.processes.ScaleProcessResponse;
import org.cloudfoundry.client.v3.processes.UpdateProcessRequest;
import org.cloudfoundry.client.v3.processes.UpdateProcessResponse;
import org.junit.Test;
import reactor.rx.Streams;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.PATCH;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

public final class SpringProcessesTest extends AbstractRestTest {

    private final SpringProcesses processes = new SpringProcesses(this.restTemplate, this.root);

    @Test
    public void deleteInstance() {
        mockRequest(new RequestContext()
                .method(DELETE).path("/v3/processes/test-id/instances/test-index")
                .status(NO_CONTENT));

        DeleteProcessInstanceRequest request = DeleteProcessInstanceRequest.builder()
                .id("test-id")
                .index("test-index")
                .build();

        Streams.wrap(this.processes.deleteInstance(request)).next().get();

        verify();
    }

    @Test(expected = CloudFoundryException.class)
    public void deleteInstanceError() {
        mockRequest(new RequestContext()
                .method(DELETE).path("/v3/processes/test-id/instances/test-index")
                .errorResponse());

        DeleteProcessInstanceRequest request = DeleteProcessInstanceRequest.builder()
                .id("test-id")
                .index("test-index")
                .build();

        Streams.wrap(this.processes.deleteInstance(request)).next().get();
    }

    @Test(expected = RequestValidationException.class)
    public void deleteInstanceInvalidRequest() {
        Streams.wrap(this.processes.deleteInstance(DeleteProcessInstanceRequest.builder().build())).next().get();
    }

    @Test
    public void get() {
        mockRequest(new RequestContext()
                .method(GET).path("/v3/processes/test-id")
                .status(OK)
                .responsePayload("v3/processes/GET_{id}_response.json"));

        GetProcessRequest request = GetProcessRequest.builder()
                .id("test-id")
                .build();

        GetProcessResponse response = Streams.wrap(this.processes.get(request))
                .next().get();

        assertEquals("2015-07-27T22:43:31Z", response.getCreatedAt());
        assertNull(response.getCommand());
        assertEquals(Integer.valueOf(1024), response.getDiskInMb());
        assertEquals("07063514-b0ca-4e58-adbb-8e8bd7eebd64", response.getId());
        assertEquals(Integer.valueOf(1), response.getInstances());
        assertEquals(Integer.valueOf(1024), response.getMemoryInMb());
        assertEquals("web", response.getType());
        assertEquals("2015-07-27T22:43:31Z", response.getUpdatedAt());
        validateLinks(response, "self", "scale", "app", "space");
        verify();
    }

    @Test(expected = CloudFoundryException.class)
    public void getError() {
        mockRequest(new RequestContext()
                .method(GET).path("/v3/processes/test-id")
                .errorResponse());

        GetProcessRequest request = GetProcessRequest.builder()
                .id("test-id")
                .build();

        Streams.wrap(this.processes.get(request)).next().get();
    }

    @Test(expected = RequestValidationException.class)
    public void getInvalidRequest() {
        Streams.wrap(this.processes.get(GetProcessRequest.builder().build())).next().get();
    }

    @Test
    public void list() {
        mockRequest(new RequestContext()
                .method(GET).path("/v3/processes?page=1&per_page=2")
                .status(OK)
                .responsePayload("v3/processes/GET_response.json"));

        ListProcessesRequest request = ListProcessesRequest.builder()
                .page(1)
                .perPage(2)
                .build();

        ListProcessesResponse response = Streams.wrap(this.processes.list(request)).next().get();

        ListProcessesResponse.Resource resource = response.getResources().get(0);

        assertEquals("2015-07-27T22:43:31Z", resource.getCreatedAt());
        assertNull(resource.getCommand());
        assertEquals(Integer.valueOf(1024), resource.getDiskInMb());
        assertEquals("fdfa71c4-5e0e-4f68-adb6-82fc250cd233", resource.getId());
        assertEquals(Integer.valueOf(1), resource.getInstances());
        assertEquals(Integer.valueOf(1024), resource.getMemoryInMb());
        assertEquals("web", resource.getType());
        assertEquals("2015-07-27T22:43:31Z", resource.getUpdatedAt());
        validateLinks(resource, "self", "scale", "app", "space");
        verify();
    }

    @Test(expected = CloudFoundryException.class)
    public void listError() {
        mockRequest(new RequestContext()
                .method(GET).path("/v3/processes?page=1&per_page=2")
                .errorResponse());

        ListProcessesRequest request = ListProcessesRequest.builder()
                .page(1)
                .perPage(2)
                .build();

        Streams.wrap(this.processes.list(request)).next().get();
    }

    @Test(expected = RequestValidationException.class)
    public void listInvalidRequest() {
        Streams.wrap(this.processes.list(ListProcessesRequest.builder().page(0).build())).next().get();
    }

    @Test
    public void scale() {
        mockRequest(new RequestContext()
                .method(PUT).path("/v3/processes/test-id/scale")
                .requestPayload("v3/processes/PUT_{id}_scale_request.json")
                .status(OK)
                .responsePayload("v3/processes/PUT_{id}_scale_response.json"));

        ScaleProcessRequest request = ScaleProcessRequest.builder()
                .diskInMb(100)
                .id("test-id")
                .instances(3)
                .memoryInMb(100)
                .build();

        ScaleProcessResponse response = Streams.wrap(this.processes.scale(request)).next().get();

        assertEquals("2015-07-27T22:43:31Z", response.getCreatedAt());
        assertEquals(Integer.valueOf(100), response.getDiskInMb());
        assertEquals("1dbdf1dc-ec61-4ade-96bf-4e148092b2e8", response.getId());
        assertEquals(Integer.valueOf(3), response.getInstances());
        assertEquals(Integer.valueOf(100), response.getMemoryInMb());
        assertEquals("web", response.getType());
        assertEquals("2015-07-27T22:43:32Z", response.getUpdatedAt());
        validateLinks(response, "self", "scale", "app", "space");
        verify();
    }

    @Test(expected = CloudFoundryException.class)
    public void scaleError() {
        mockRequest(new RequestContext()
                .method(PUT).path("/v3/processes/test-id/scale")
                .requestPayload("v3/processes/PUT_{id}_scale_request.json")
                .errorResponse());

        ScaleProcessRequest request = ScaleProcessRequest.builder()
                .diskInMb(100)
                .id("test-id")
                .instances(3)
                .memoryInMb(100)
                .build();

        Streams.wrap(this.processes.scale(request)).next().get();
    }

    @Test(expected = RequestValidationException.class)
    public void scaleInvalidRequest() {
        Streams.wrap(this.processes.scale(ScaleProcessRequest.builder().build())).next().get();
    }

    @Test
    public void update() {
        mockRequest(new RequestContext()
                .method(PATCH).path("/v3/processes/test-id")
                .requestPayload("v3/processes/PATCH_{id}_request.json")
                .status(OK)
                .responsePayload("v3/processes/PATCH_{id}_response.json"));

        UpdateProcessRequest request = UpdateProcessRequest.builder()
                .id("test-id")
                .command("test-command")
                .build();

        UpdateProcessResponse response = Streams.wrap(this.processes.update(request))
                .next().get();

        assertEquals("2015-07-27T22:43:32Z", response.getCreatedAt());
        assertEquals("X", response.getCommand());
        assertEquals(Integer.valueOf(1024), response.getDiskInMb());
        assertEquals("92d5b770-32ed-4f5c-8def-b1f2348447fb", response.getId());
        assertEquals(Integer.valueOf(1), response.getInstances());
        assertEquals(Integer.valueOf(1024), response.getMemoryInMb());
        assertEquals("web", response.getType());
        assertEquals("2015-07-27T22:43:32Z", response.getUpdatedAt());
        validateLinks(response, "self", "scale", "app", "space");
        verify();
    }

    @Test(expected = CloudFoundryException.class)
    public void updateError() {
        mockRequest(new RequestContext()
                .method(PATCH).path("/v3/processes/test-id")
                .requestPayload("v3/processes/PATCH_{id}_request.json")
                .errorResponse());

        UpdateProcessRequest request = UpdateProcessRequest.builder()
                .id("test-id")
                .command("test-command")
                .build();

        Streams.wrap(this.processes.update(request)).next().get();
    }

    @Test(expected = RequestValidationException.class)
    public void updateInvalidRequest() {
        Streams.wrap(this.processes.update(UpdateProcessRequest.builder().build())).next().get();
    }

}
