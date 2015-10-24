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

package org.cloudfoundry.client.spring.v3.applications;

import org.cloudfoundry.client.RequestValidationException;
import org.cloudfoundry.client.spring.AbstractRestTest;
import org.cloudfoundry.client.v2.CloudFoundryException;
import org.cloudfoundry.client.v3.applications.AssignApplicationDropletRequest;
import org.cloudfoundry.client.v3.applications.AssignApplicationDropletResponse;
import org.cloudfoundry.client.v3.applications.CreateApplicationRequest;
import org.cloudfoundry.client.v3.applications.CreateApplicationResponse;
import org.cloudfoundry.client.v3.applications.DeleteApplicationInstanceRequest;
import org.cloudfoundry.client.v3.applications.DeleteApplicationRequest;
import org.cloudfoundry.client.v3.applications.GetApplicationEnvironmentRequest;
import org.cloudfoundry.client.v3.applications.GetApplicationEnvironmentResponse;
import org.cloudfoundry.client.v3.applications.GetApplicationProcessRequest;
import org.cloudfoundry.client.v3.applications.GetApplicationProcessResponse;
import org.cloudfoundry.client.v3.applications.GetApplicationRequest;
import org.cloudfoundry.client.v3.applications.GetApplicationResponse;
import org.cloudfoundry.client.v3.applications.ListApplicationDropletsRequest;
import org.cloudfoundry.client.v3.applications.ListApplicationDropletsResponse;
import org.cloudfoundry.client.v3.applications.ListApplicationPackagesRequest;
import org.cloudfoundry.client.v3.applications.ListApplicationPackagesResponse;
import org.cloudfoundry.client.v3.applications.ListApplicationProcessesRequest;
import org.cloudfoundry.client.v3.applications.ListApplicationProcessesResponse;
import org.cloudfoundry.client.v3.applications.ListApplicationRoutesRequest;
import org.cloudfoundry.client.v3.applications.ListApplicationRoutesResponse;
import org.cloudfoundry.client.v3.applications.ListApplicationsRequest;
import org.cloudfoundry.client.v3.applications.ListApplicationsResponse;
import org.cloudfoundry.client.v3.applications.MapApplicationRouteRequest;
import org.cloudfoundry.client.v3.applications.ScaleApplicationRequest;
import org.cloudfoundry.client.v3.applications.ScaleApplicationResponse;
import org.cloudfoundry.client.v3.applications.StartApplicationRequest;
import org.cloudfoundry.client.v3.applications.StartApplicationResponse;
import org.cloudfoundry.client.v3.applications.StopApplicationRequest;
import org.cloudfoundry.client.v3.applications.StopApplicationResponse;
import org.cloudfoundry.client.v3.applications.UnmapApplicationRouteRequest;
import org.cloudfoundry.client.v3.applications.UpdateApplicationRequest;
import org.cloudfoundry.client.v3.applications.UpdateApplicationResponse;
import org.junit.Test;
import reactor.rx.Streams;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.cloudfoundry.client.v3.PaginatedAndSortedRequest.OrderBy.CREATED_AT;
import static org.cloudfoundry.client.v3.PaginatedAndSortedRequest.OrderDirection.ASC;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.PATCH;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

public final class SpringApplicationsTest extends AbstractRestTest {

    private final SpringApplications applications = new SpringApplications(this.restTemplate, this.root);

    @Test
    public void assignDroplet() {
        mockRequest(new RequestContext()
                .method(PUT).path("v3/apps/test-id/current_droplet")
                .requestPayload("v3/apps/PUT_{id}_current_droplet_request.json")
                .status(OK)
                .responsePayload("v3/apps/PUT_{id}_current_droplet_response.json"));

        AssignApplicationDropletRequest request = AssignApplicationDropletRequest.builder()
                .dropletId("guid-3b5793e7-f6c8-40cb-a8d8-07080280da83")
                .id("test-id")
                .build();

        AssignApplicationDropletResponse response = Streams.wrap(this.applications.assignDroplet(request)).next().get();

        assertNull(response.getBuildpack());
        assertEquals("2015-07-27T22:43:15Z", response.getCreatedAt());
        assertEquals("STOPPED", response.getDesiredState());
        assertEquals(Collections.emptyMap(), response.getEnvironmentVariables());
        assertEquals("guid-9f33c9e4-4b31-4dda-b188-adf197dbea0a", response.getId());
        assertEquals("name1", response.getName());
        assertEquals(Integer.valueOf(1), response.getTotalDesiredInstances());
        assertEquals("2015-07-27T22:43:15Z", response.getUpdatedAt());
        validateLinks(response, "self", "processes", "packages", "droplet", "space", "start", "stop",
                "assign_current_droplet");
        verify();
    }

    @Test(expected = CloudFoundryException.class)
    public void assignDropletError() {
        mockRequest(new RequestContext()
                        .method(PUT).path("v3/apps/test-id/current_droplet")
                        .requestPayload("v3/apps/PUT_{id}_current_droplet_request.json")
                        .errorResponse()
        );

        AssignApplicationDropletRequest request = AssignApplicationDropletRequest.builder()
                .dropletId("guid-3b5793e7-f6c8-40cb-a8d8-07080280da83")
                .id("test-id")
                .build();

        Streams.wrap(this.applications.assignDroplet(request)).next().get();
    }

    @Test(expected = RequestValidationException.class)
    public void assignDropletInvalidRequest() {
        Streams.wrap(this.applications.assignDroplet(AssignApplicationDropletRequest.builder().build())).next().get();
    }

    @Test
    public void create() throws IOException {
        mockRequest(new RequestContext()
                .method(POST).path("/v3/apps")
                .requestPayload("v3/apps/POST_request.json")
                .status(CREATED)
                .responsePayload("v3/apps/POST_response.json"));

        CreateApplicationRequest request = CreateApplicationRequest.builder()
                .name("my_app")
                .spaceId("31627bdc-5bc4-4c4d-a883-c7b2f53db249")
                .environmentVariable("open", "source")
                .buildpack("name-410")
                .build();

        CreateApplicationResponse response = Streams.wrap(this.applications.create(request)).next().get();

        assertEquals("name-410", response.getBuildpack());
        assertEquals("2015-07-27T22:43:15Z", response.getCreatedAt());
        assertEquals("STOPPED", response.getDesiredState());
        assertEquals(Collections.singletonMap("open", "source"), response.getEnvironmentVariables());
        assertEquals("8b51db6f-7bae-47ca-bc75-74bc957ed460", response.getId());
        assertEquals("my_app", response.getName());
        assertEquals(Integer.valueOf(0), response.getTotalDesiredInstances());
        assertNull(response.getUpdatedAt());
        validateLinks(response, "self", "processes", "packages", "space", "start", "stop", "assign_current_droplet");
        verify();
    }

    @Test(expected = CloudFoundryException.class)
    public void createError() throws IOException {
        mockRequest(new RequestContext()
                .method(POST).path("/v3/apps")
                .requestPayload("v3/apps/POST_request.json")
                .errorResponse());

        CreateApplicationRequest request = CreateApplicationRequest.builder()
                .name("my_app")
                .spaceId("31627bdc-5bc4-4c4d-a883-c7b2f53db249")
                .environmentVariable("open", "source")
                .buildpack("name-410")
                .build();

        Streams.wrap(this.applications.create(request)).next().get();
    }

    @Test(expected = RequestValidationException.class)
    public void createInvalidRequest() throws Throwable {
        Streams.wrap(this.applications.create(CreateApplicationRequest.builder().build())).next().get();
    }

    @Test
    public void delete() {
        mockRequest(new RequestContext()
                .method(DELETE).path("/v3/apps/test-id")
                .status(NO_CONTENT));

        DeleteApplicationRequest request = DeleteApplicationRequest.builder()
                .id("test-id")
                .build();

        Streams.wrap(this.applications.delete(request)).next().get();

        verify();
    }

    @Test(expected = CloudFoundryException.class)
    public void deleteError() {
        mockRequest(new RequestContext()
                .method(DELETE).path("/v3/apps/test-id")
                .errorResponse());

        DeleteApplicationRequest request = DeleteApplicationRequest.builder()
                .id("test-id")
                .build();

        Streams.wrap(this.applications.delete(request)).next().get();
    }

    @Test(expected = RequestValidationException.class)
    public void deleteInvalidRequest() {
        Streams.wrap(this.applications.delete(DeleteApplicationRequest.builder().build())).next().get();
    }

    @Test
    public void deleteProcess() {
        mockRequest(new RequestContext()
                .method(DELETE).path("/v3/apps/test-id/processes/test-type/instances/test-index")
                .status(NO_CONTENT));

        DeleteApplicationInstanceRequest request = DeleteApplicationInstanceRequest.builder()
                .id("test-id")
                .index("test-index")
                .type("test-type")
                .build();

        Streams.wrap(this.applications.deleteInstance(request)).next().get();

        verify();
    }

    @Test(expected = CloudFoundryException.class)
    public void deleteProcessError() {
        mockRequest(new RequestContext()
                .method(DELETE).path("/v3/apps/test-id/processes/test-type/instances/test-index")
                .errorResponse());

        DeleteApplicationInstanceRequest request = DeleteApplicationInstanceRequest.builder()
                .id("test-id")
                .index("test-index")
                .type("test-type")
                .build();

        Streams.wrap(this.applications.deleteInstance(request)).next().get();
    }

    @Test(expected = RequestValidationException.class)
    public void deleteProcessInvalidRequest() {
        Streams.wrap(this.applications.deleteInstance(DeleteApplicationInstanceRequest.builder().build())).next().get();
    }

    @Test
    public void get() {
        mockRequest(new RequestContext()
                .method(GET).path("/v3/apps/test-id")
                .status(OK)
                .responsePayload("v3/apps/GET_{id}_response.json"));

        GetApplicationRequest request = GetApplicationRequest.builder()
                .id("test-id")
                .build();

        GetApplicationResponse response = Streams.wrap(this.applications.get(request)).next().get();

        assertEquals("name-371", response.getBuildpack());
        assertEquals("2015-07-27T22:43:15Z", response.getCreatedAt());
        assertEquals("STOPPED", response.getDesiredState());
        assertEquals(Collections.singletonMap("unicorn", "horn"), response.getEnvironmentVariables());
        assertEquals("guid-e23c9834-9c4a-4397-be7d-e0fb686cb646", response.getId());
        assertEquals("my_app", response.getName());
        assertEquals(Integer.valueOf(3), response.getTotalDesiredInstances());
        assertNull(response.getUpdatedAt());
        validateLinks(response, "self", "processes", "packages", "droplet", "space", "start", "stop",
                "assign_current_droplet");
        verify();
    }

    @Test(expected = CloudFoundryException.class)
    public void getError() {
        mockRequest(new RequestContext()
                .method(GET).path("/v3/apps/test-id")
                .errorResponse());

        GetApplicationRequest request = GetApplicationRequest.builder()
                .id("test-id")
                .build();

        Streams.wrap(this.applications.get(request)).next().get();
    }

    @Test(expected = RequestValidationException.class)
    public void getInvalidRequest() {
        Streams.wrap(this.applications.get(GetApplicationRequest.builder().build())).next().get();
    }

    @Test
    public void getEnvironment() {
        mockRequest(new RequestContext()
                .method(GET).path("/v3/apps/test-id/env")
                .status(OK)
                .responsePayload("v3/apps/GET_{id}_env_response.json"));

        GetApplicationEnvironmentRequest request = GetApplicationEnvironmentRequest.builder()
                .id("test-id")
                .build();

        GetApplicationEnvironmentResponse response = Streams.wrap(this.applications.getEnvironment(request))
                .next().get();

        Map<String, Object> vcapApplication = new HashMap<>();
        vcapApplication.put("limits", Collections.singletonMap("fds", 16384));
        vcapApplication.put("application_name", "app_name");
        vcapApplication.put("application_uris", Collections.emptyList());
        vcapApplication.put("name", "app_name");
        vcapApplication.put("space_name", "some_space");
        vcapApplication.put("space_id", "c595c2ee-df01-4769-a61f-df5bd5e4cbc1");
        vcapApplication.put("uris", Collections.emptyList());
        vcapApplication.put("users", null);

        Map<String, Object> applicationEnvironmentVariables = Collections.singletonMap("VCAP_APPLICATION",
                vcapApplication);
        assertEquals(applicationEnvironmentVariables, response.getApplicationEnvironmentVariables());

        Map<String, Object> environmentVariables = Collections.singletonMap("SOME_KEY", "some_val");
        assertEquals(environmentVariables, response.getEnvironmentVariables());

        Map<String, Object> runningEnvironmentVariables = Collections.singletonMap("RUNNING_ENV", "running_value");
        assertEquals(runningEnvironmentVariables, response.getRunningEnvironmentVariables());

        Map<String, Object> stagingEnvironmentVariables = Collections.singletonMap("STAGING_ENV", "staging_value");
        assertEquals(stagingEnvironmentVariables, response.getStagingEnvironmentVariables());

        verify();
    }

    @Test(expected = CloudFoundryException.class)
    public void getEnvironmentError() {
        mockRequest(new RequestContext()
                .method(GET).path("/v3/apps/test-id/env")
                .errorResponse());

        GetApplicationEnvironmentRequest request = GetApplicationEnvironmentRequest.builder()
                .id("test-id")
                .build();

        Streams.wrap(this.applications.getEnvironment(request)).next().get();
    }

    @Test(expected = RequestValidationException.class)
    public void getEnvironmentInvalidRequest() {
        Streams.wrap(this.applications.getEnvironment(GetApplicationEnvironmentRequest.builder().build())).next().get();
    }

    @Test
    public void getProcess() {
        mockRequest(new RequestContext()
                .method(GET).path("/v3/apps/test-id/processes/web")
                .status(OK)
                .responsePayload("v3/apps/GET_{id}_processes_{type}_response.json"));

        GetApplicationProcessRequest request = GetApplicationProcessRequest.builder()
                .id("test-id")
                .type("web")
                .build();

        GetApplicationProcessResponse response = Streams.wrap(this.applications.getProcess(request))
                .next().get();

        assertEquals("2015-07-27T22:43:29Z", response.getCreatedAt());
        assertNull(response.getCommand());
        assertEquals(Integer.valueOf(1024), response.getDiskInMb());
        assertEquals("32f64d22-ab45-4a9b-ba93-2b3b160f3750", response.getId());
        assertEquals(Integer.valueOf(1), response.getInstances());
        assertEquals(Integer.valueOf(1024), response.getMemoryInMb());
        assertEquals("web", response.getType());
        assertEquals("2015-07-27T22:43:29Z", response.getUpdatedAt());
        validateLinks(response, "self", "scale", "app", "space");
        verify();
    }

    @Test(expected = CloudFoundryException.class)
    public void getProcessError() {
        mockRequest(new RequestContext()
                .method(GET).path("/v3/apps/test-id/processes/web")
                .errorResponse());

        GetApplicationProcessRequest request = GetApplicationProcessRequest.builder()
                .id("test-id")
                .type("web")
                .build();

        Streams.wrap(this.applications.getProcess(request)).next().get();
    }

    @Test(expected = RequestValidationException.class)
    public void getProcessInvalidRequest() {
        Streams.wrap(this.applications.getProcess(GetApplicationProcessRequest.builder().build())).next().get();
    }

    @Test
    public void list() {
        mockRequest(new RequestContext()
                .method(GET).path("/v3/apps?names[]=test-name&order_by=created_at&page=1")
                .status(OK)
                .responsePayload("v3/apps/GET_response.json"));

        ListApplicationsRequest request = ListApplicationsRequest.builder()
                .page(1)
                .orderBy(CREATED_AT)
                .name("test-name")
                .build();

        ListApplicationsResponse response = Streams.wrap(this.applications.list(request)).next().get();
        ListApplicationsResponse.Resource resource = response.getResources().get(0);

        assertEquals("name-383", resource.getBuildpack());
        assertEquals("1970-01-01T00:00:03Z", resource.getCreatedAt());
        assertEquals("STOPPED", resource.getDesiredState());
        assertEquals(Collections.singletonMap("magic", "beautiful"), resource.getEnvironmentVariables());
        assertEquals("guid-acfbae75-7d3a-45b1-b730-ca3cc4263045", resource.getId());
        assertEquals("my_app3", resource.getName());
        assertEquals(Integer.valueOf(0), resource.getTotalDesiredInstances());
        assertNull(resource.getUpdatedAt());
        validateLinks(resource, "self", "processes", "packages", "space", "start", "stop", "assign_current_droplet");
        verify();
    }

    @Test(expected = CloudFoundryException.class)
    public void listError() {
        mockRequest(new RequestContext()
                .method(GET).path("/v3/apps?names[]=test-name&order_by=created_at&page=1")
                .errorResponse());

        ListApplicationsRequest request = ListApplicationsRequest.builder()
                .page(1)
                .orderBy(CREATED_AT)
                .name("test-name")
                .build();

        Streams.wrap(this.applications.list(request)).next().get();
    }

    @Test(expected = RequestValidationException.class)
    public void listInvalidRequest() {
        Streams.wrap(this.applications.list(ListApplicationsRequest.builder().page(-1).build())).next().get();
    }

    @Test
    public void listPackages() {
        mockRequest(new RequestContext()
                        .method(GET).path("/v3/apps/test-id/packages")
                        .status(OK)
                        .responsePayload("v3/apps/GET_{id}_packages_response.json")
        );

        ListApplicationPackagesRequest request = ListApplicationPackagesRequest.builder()
                .page(1)
                .id("test-id")
                .build();

        ListApplicationPackagesResponse response = Streams.wrap(this.applications.listPackages(request)).next().get();
        ListApplicationPackagesResponse.Resource resource = response.getResources().get(0);

        assertEquals("2015-07-27T22:43:34Z", resource.getCreatedAt());
        assertNull(resource.getError());
        assertEquals("sha1", resource.getHash().getType());
        assertNull(resource.getHash().getValue());
        assertEquals("guid-3d792a08-e415-4f9e-912b-2a8485db781a", resource.getId());
        assertEquals("AWAITING_UPLOAD", resource.getState());
        assertEquals("bits", resource.getType());
        assertNull(resource.getUpdatedAt());
        assertNull(resource.getUrl());
        validateLinks(resource, "self", "upload", "download", "stage", "app");
        verify();
    }

    @Test(expected = CloudFoundryException.class)
    public void listPackagesError() {
        mockRequest(new RequestContext()
                .method(GET).path("/v3/apps/test-id/packages")
                .errorResponse());

        ListApplicationPackagesRequest request = ListApplicationPackagesRequest.builder()
                .page(1)
                .id("test-id")
                .build();

        Streams.wrap(this.applications.listPackages(request)).next().get();
    }

    @Test(expected = RequestValidationException.class)
    public void listPackagesInvalidRequest() {
        Streams.wrap(this.applications.listPackages(ListApplicationPackagesRequest.builder().build())).next().get();
    }

    @Test
    public void listDroplets() {
        mockRequest(new RequestContext()
                .method(GET).path("/v3/apps/test-id/droplets?order_by=created_at&order_direction=asc&page=1&per_page=2")
                .status(OK)
                .responsePayload("v3/apps/GET_{id}_droplets_response.json"));

        ListApplicationDropletsRequest request = ListApplicationDropletsRequest.builder()
                .page(1)
                .perPage(2)
                .orderBy(CREATED_AT)
                .orderDirection(ASC)
                .id("test-id")
                .build();

        ListApplicationDropletsResponse response = Streams.wrap(this.applications.listDroplets(request)).next().get();
        ListApplicationDropletsResponse.Resource resource = response.getResources().get(0);

        Map<String, Object> environmentVariables = new HashMap<>();
        environmentVariables.put("yuu", "huuu");

        assertEquals("name-2089", resource.getBuildpack());
        assertEquals("1970-01-01T00:00:01Z", resource.getCreatedAt());
        assertEquals(environmentVariables, resource.getEnvironmentVariables());
        assertNull(resource.getError());
        assertEquals("sha1", resource.getHash().getType());
        assertNull(resource.getHash().getValue());
        assertEquals("guid-5df0a4bb-4fcb-4393-acdd-868524ad761e", resource.getId());
        assertNull(resource.getProcfile());
        assertEquals("STAGING", resource.getState());
        assertNull(resource.getUpdatedAt());
        validateLinks(resource, "self", "package", "app", "assign_current_droplet", "buildpack");
        verify();
    }

    @Test(expected = CloudFoundryException.class)
    public void listDropletsError() {
        mockRequest(new RequestContext()
                .method(GET).path("/v3/apps/test-id/droplets?order_by=created_at&order_direction=asc&page=1&per_page=2")
                .errorResponse());

        ListApplicationDropletsRequest request = ListApplicationDropletsRequest.builder()
                .page(1)
                .perPage(2)
                .orderBy(CREATED_AT)
                .orderDirection(ASC)
                .id("test-id")
                .build();

        Streams.wrap(this.applications.listDroplets(request)).next().get();
    }

    @Test(expected = RequestValidationException.class)
    public void listDropletsInvalidRequest() {
        Streams.wrap(this.applications.listDroplets(ListApplicationDropletsRequest.builder().build())).next().get();
    }

    @Test
    public void listProcesses() {
        mockRequest(new RequestContext()
                .method(GET).path("/v3/apps/test-id/processes")
                .status(OK)
                .responsePayload("v3/apps/GET_{id}_processes_response.json"));

        ListApplicationProcessesRequest request = ListApplicationProcessesRequest.builder()
                .page(1)
                .id("test-id")
                .build();

        ListApplicationProcessesResponse response = Streams.wrap(this.applications.listProcesses(request)).next().get();
        ListApplicationProcessesResponse.Resource resource = response.getResources().get(0);

        assertEquals("2015-07-27T22:43:29Z", resource.getCreatedAt());
        assertNull(resource.getCommand());
        assertEquals(Integer.valueOf(1024), resource.getDiskInMb());
        assertEquals("38fcaafa-5356-4f74-af10-dc70da151993", resource.getId());
        assertEquals(Integer.valueOf(1), resource.getInstances());
        assertEquals(Integer.valueOf(1024), resource.getMemoryInMb());
        assertEquals("web", resource.getType());
        assertEquals("2015-07-27T22:43:29Z", resource.getUpdatedAt());
        validateLinks(resource, "self", "scale", "app", "space");
        verify();
    }

    @Test(expected = CloudFoundryException.class)
    public void listProcessesError() {
        mockRequest(new RequestContext()
                .method(GET).path("/v3/apps/test-id/processes")
                .errorResponse());

        ListApplicationProcessesRequest request = ListApplicationProcessesRequest.builder()
                .page(1)
                .id("test-id")
                .build();

        Streams.wrap(this.applications.listProcesses(request)).next().get();
    }

    @Test(expected = RequestValidationException.class)
    public void listProcessesInvalidRequest() {
        Streams.wrap(this.applications.listProcesses(ListApplicationProcessesRequest.builder().build())).next().get();
    }

    @Test
    public void listRoutes() {
        mockRequest(new RequestContext()
                .method(GET).path("/v3/apps/test-id/routes")
                .status(OK)
                .responsePayload("v3/apps/GET_{id}_routes_response.json"));

        ListApplicationRoutesRequest request = ListApplicationRoutesRequest.builder()
                .id("test-id")
                .build();

        ListApplicationRoutesResponse response = Streams.wrap(this.applications.listRoutes(request)).next().get();
        ListApplicationRoutesResponse.Resource resource = response.getResources().get(0);

        assertEquals("2015-07-27T22:43:32Z", resource.getCreatedAt());
        assertEquals("host-20", resource.getHost());
        assertEquals("cad6fe1d-d6de-4698-9b8e-caf9506ecf8d", resource.getId());
        assertEquals("", resource.getPath());
        assertNull(resource.getUpdatedAt());
        validateLinks(resource, "space", "domain");
        verify();
    }

    @Test(expected = CloudFoundryException.class)
    public void listRoutesError() {
        mockRequest(new RequestContext()
                .method(GET).path("/v3/apps/test-id/routes")
                .errorResponse());

        ListApplicationRoutesRequest request = ListApplicationRoutesRequest.builder()
                .id("test-id")
                .build();
        Streams.wrap(this.applications.listRoutes(request)).next().get();
    }

    @Test(expected = RequestValidationException.class)
    public void listRoutesInvalidRequest() {
        Streams.wrap(this.applications.listRoutes(ListApplicationRoutesRequest.builder().build())).next().get();
    }

    @Test
    public void mapRoute() {
        mockRequest(new RequestContext()
                .method(PUT).path("/v3/apps/test-id/routes")
                .requestPayload("v3/apps/PUT_{id}_routes_request.json")
                .status(NO_CONTENT));

        MapApplicationRouteRequest request = MapApplicationRouteRequest.builder()
                .id("test-id")
                .routeId("9cf0271a-420f-4ae4-b227-16683db93573")
                .build();

        Streams.wrap(this.applications.mapRoute(request)).next().get();

        verify();
    }

    @Test(expected = CloudFoundryException.class)
    public void mapRouteError() {
        mockRequest(new RequestContext()
                .method(PUT).path("/v3/apps/test-id/routes")
                .requestPayload("v3/apps/PUT_{id}_routes_request.json")
                .errorResponse());

        MapApplicationRouteRequest request = MapApplicationRouteRequest.builder()
                .id("test-id")
                .routeId("9cf0271a-420f-4ae4-b227-16683db93573")
                .build();

        Streams.wrap(this.applications.mapRoute(request)).next().get();
    }

    @Test(expected = RequestValidationException.class)
    public void mapRouteInvalidRequest() {
        Streams.wrap(this.applications.mapRoute(MapApplicationRouteRequest.builder().build())).next().get();
    }

    @Test
    public void scale() {
        mockRequest(new RequestContext()
                .method(PUT).path("/v3/apps/test-id/processes/web/scale")
                .requestPayload("v3/apps/PUT_{id}_processes_{type}_scale_request.json")
                .status(OK)
                .responsePayload("v3/apps/PUT_{id}_processes_{type}_scale_response.json"));

        ScaleApplicationRequest request = ScaleApplicationRequest.builder()
                .diskInMb(100)
                .id("test-id")
                .instances(3)
                .memoryInMb(100)
                .type("web")
                .build();

        ScaleApplicationResponse response = Streams.wrap(this.applications.scale(request)).next().get();

        assertEquals("2015-07-27T22:43:29Z", response.getCreatedAt());
        assertEquals(Integer.valueOf(100), response.getDiskInMb());
        assertEquals("edc2dffe-9f0d-416f-a712-890d56de8bae", response.getId());
        assertEquals(Integer.valueOf(3), response.getInstances());
        assertEquals(Integer.valueOf(100), response.getMemoryInMb());
        assertEquals("web", response.getType());
        assertEquals("2015-07-27T22:43:29Z", response.getUpdatedAt());
        validateLinks(response, "self", "scale", "app", "space");
        verify();
    }

    @Test(expected = CloudFoundryException.class)
    public void scaleError() {
        mockRequest(new RequestContext()
                .method(PUT).path("/v3/apps/test-id/processes/web/scale")
                .requestPayload("v3/apps/PUT_{id}_processes_{type}_scale_request.json")
                .errorResponse());

        ScaleApplicationRequest request = ScaleApplicationRequest.builder()
                .diskInMb(100)
                .id("test-id")
                .instances(3)
                .memoryInMb(100)
                .type("web")
                .build();

        Streams.wrap(this.applications.scale(request)).next().get();
    }

    @Test(expected = RequestValidationException.class)
    public void scaleInvalidRequest() {
        Streams.wrap(this.applications.scale(ScaleApplicationRequest.builder().build())).next().get();
    }

    @Test
    public void start() {
        mockRequest(new RequestContext()
                .method(PUT).path("/v3/apps/test-id/start")
                .status(OK)
                .responsePayload("v3/apps/PUT_{id}_start_response.json"));

        StartApplicationRequest request = StartApplicationRequest.builder()
                .id("test-id")
                .build();

        StartApplicationResponse response = Streams.wrap(this.applications.start(request)).next().get();

        assertNull(response.getBuildpack());
        assertEquals("2015-07-27T22:43:15Z", response.getCreatedAt());
        assertEquals("STARTED", response.getDesiredState());
        assertEquals(Collections.emptyMap(), response.getEnvironmentVariables());
        assertEquals("guid-40460094-d035-4663-b58c-cdf4c802a2c6", response.getId());
        assertEquals("original_name", response.getName());
        assertEquals(Integer.valueOf(0), response.getTotalDesiredInstances());
        assertEquals("2015-07-27T22:43:15Z", response.getUpdatedAt());
        validateLinks(response, "self", "processes", "packages", "space", "droplet", "start", "stop",
                "assign_current_droplet");
        verify();
    }

    @Test(expected = CloudFoundryException.class)
    public void startError() {
        mockRequest(new RequestContext()
                .method(PUT).path("/v3/apps/test-id/start")
                .errorResponse());

        StartApplicationRequest request = StartApplicationRequest.builder()
                .id("test-id")
                .build();

        Streams.wrap(this.applications.start(request)).next().get();
    }

    @Test(expected = RequestValidationException.class)
    public void startInvalidRequest() {
        Streams.wrap(this.applications.start(StartApplicationRequest.builder().build())).next().get();
    }

    @Test
    public void stop() {
        mockRequest(new RequestContext()
                .method(PUT).path("/v3/apps/test-id/stop")
                .status(OK)
                .responsePayload("v3/apps/PUT_{id}_stop_response.json"));

        StopApplicationRequest request = StopApplicationRequest.builder()
                .id("test-id")
                .build();

        StopApplicationResponse response = Streams.wrap(this.applications.stop(request)).next().get();

        assertNull(response.getBuildpack());
        assertEquals("2015-07-27T22:43:15Z", response.getCreatedAt());
        assertEquals("STOPPED", response.getDesiredState());
        assertEquals(Collections.emptyMap(), response.getEnvironmentVariables());
        assertEquals("guid-be4e4357-5a9d-48fc-ae37-821f48c1ace0", response.getId());
        assertEquals("original_name", response.getName());
        assertEquals(Integer.valueOf(0), response.getTotalDesiredInstances());
        assertEquals("2015-07-27T22:43:15Z", response.getUpdatedAt());
        validateLinks(response, "self", "processes", "packages", "space", "droplet", "start", "stop",
                "assign_current_droplet");
        verify();
    }

    @Test(expected = CloudFoundryException.class)
    public void stopError() {
        mockRequest(new RequestContext()
                .method(PUT).path("/v3/apps/test-id/stop")
                .errorResponse());

        StopApplicationRequest request = StopApplicationRequest.builder()
                .id("test-id")
                .build();

        Streams.wrap(this.applications.stop(request)).next().get();
    }

    @Test(expected = RequestValidationException.class)
    public void stopInvalidRequest() {
        Streams.wrap(this.applications.stop(StopApplicationRequest.builder().build())).next().get();
    }

    @Test
    public void unmapRoute() {
        mockRequest(new RequestContext()
                .method(DELETE).path("/v3/apps/test-id/routes")
                .requestPayload("v3/apps/DELETE_{id}_routes_request.json")
                .status(NO_CONTENT));

        UnmapApplicationRouteRequest request = UnmapApplicationRouteRequest.builder()
                .id("test-id")
                .routeId("3f0121a8-54e1-45c0-8daf-44d0f8ba1091")
                .build();

        Streams.wrap(this.applications.unmapRoute(request)).next().get();

        verify();
    }

    @Test(expected = CloudFoundryException.class)
    public void unmapRouteError() {
        mockRequest(new RequestContext()
                .method(DELETE).path("/v3/apps/test-id/routes")
                .requestPayload("v3/apps/DELETE_{id}_routes_request.json")
                .errorResponse());

        UnmapApplicationRouteRequest request = UnmapApplicationRouteRequest.builder()
                .id("test-id")
                .routeId("3f0121a8-54e1-45c0-8daf-44d0f8ba1091")
                .build();

        Streams.wrap(this.applications.unmapRoute(request)).next().get();
    }

    @Test(expected = RequestValidationException.class)
    public void unmapRouteInvalidRequest() {
        Streams.wrap(this.applications.unmapRoute(UnmapApplicationRouteRequest.builder().build())).next().get();
    }

    @Test
    public void update() throws IOException {
        mockRequest(new RequestContext()
                .method(PATCH).path("/v3/apps/test-id")
                .requestPayload("v3/apps/PATCH_{id}_request.json")
                .status(OK)
                .responsePayload("v3/apps/PATCH_{id}_response.json"));

        Map<String, String> environment_variables = new HashMap<>();
        environment_variables.put("MY_ENV_VAR", "foobar");
        environment_variables.put("FOOBAR", "MY_ENV_VAR");

        UpdateApplicationRequest request = UpdateApplicationRequest.builder()
                .name("new_name")
                .environmentVariables(environment_variables)
                .buildpack("http://gitwheel.org/my-app")
                .id("test-id")
                .build();

        UpdateApplicationResponse response = Streams.wrap(this.applications.update(request)).next().get();

        assertEquals("http://gitwheel.org/my-app", response.getBuildpack());
        assertEquals("2015-07-27T22:43:14Z", response.getCreatedAt());
        assertEquals("STOPPED", response.getDesiredState());
        assertEquals(environment_variables, response.getEnvironmentVariables());
        assertEquals("guid-a7b667e9-2358-4f51-9b1d-92a74beaa30a", response.getId());
        assertEquals("new_name", response.getName());
        assertEquals(Integer.valueOf(0), response.getTotalDesiredInstances());
        assertEquals("2015-07-27T22:43:14Z", response.getUpdatedAt());
        validateLinks(response, "self", "processes", "packages", "space", "start", "stop", "assign_current_droplet");
        verify();
    }

    @Test(expected = CloudFoundryException.class)
    public void updateError() throws IOException {
        mockRequest(new RequestContext()
                .method(PATCH).path("/v3/apps/test-id")
                .requestPayload("v3/apps/PATCH_{id}_request.json")
                .errorResponse());

        Map<String, String> environment_variables = new HashMap<>();
        environment_variables.put("MY_ENV_VAR", "foobar");
        environment_variables.put("FOOBAR", "MY_ENV_VAR");

        UpdateApplicationRequest request = UpdateApplicationRequest.builder()
                .name("new_name")
                .environmentVariables(environment_variables)
                .buildpack("http://gitwheel.org/my-app")
                .id("test-id")
                .build();

        Streams.wrap(this.applications.update(request)).next().get();
    }

    @Test(expected = RequestValidationException.class)
    public void updateInvalidRequest() throws Throwable {
        Streams.wrap(this.applications.update(UpdateApplicationRequest.builder().build())).next().get();
    }

}
