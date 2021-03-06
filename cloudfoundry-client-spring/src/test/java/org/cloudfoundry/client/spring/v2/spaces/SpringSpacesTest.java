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

package org.cloudfoundry.client.spring.v2.spaces;

import org.cloudfoundry.client.RequestValidationException;
import org.cloudfoundry.client.spring.AbstractRestTest;
import org.cloudfoundry.client.v2.CloudFoundryException;
import org.cloudfoundry.client.v2.applications.ApplicationEntity;
import org.cloudfoundry.client.v2.applications.ApplicationResource;
import org.cloudfoundry.client.v2.spaces.AssociateSpaceAuditorRequest;
import org.cloudfoundry.client.v2.spaces.AssociateSpaceAuditorResponse;
import org.cloudfoundry.client.v2.spaces.AssociateSpaceDeveloperRequest;
import org.cloudfoundry.client.v2.spaces.AssociateSpaceDeveloperResponse;
import org.cloudfoundry.client.v2.spaces.AssociateSpaceManagerRequest;
import org.cloudfoundry.client.v2.spaces.AssociateSpaceManagerResponse;
import org.cloudfoundry.client.v2.spaces.AssociateSpaceSecurityGroupRequest;
import org.cloudfoundry.client.v2.spaces.AssociateSpaceSecurityGroupResponse;
import org.cloudfoundry.client.v2.spaces.CreateSpaceRequest;
import org.cloudfoundry.client.v2.spaces.CreateSpaceResponse;
import org.cloudfoundry.client.v2.spaces.DeleteSpaceRequest;
import org.cloudfoundry.client.v2.spaces.GetSpaceRequest;
import org.cloudfoundry.client.v2.spaces.GetSpaceResponse;
import org.cloudfoundry.client.v2.spaces.GetSpaceSummaryRequest;
import org.cloudfoundry.client.v2.spaces.GetSpaceSummaryResponse;
import org.cloudfoundry.client.v2.spaces.ListSpaceApplicationsRequest;
import org.cloudfoundry.client.v2.spaces.ListSpaceApplicationsResponse;
import org.cloudfoundry.client.v2.spaces.ListSpacesRequest;
import org.cloudfoundry.client.v2.spaces.ListSpacesResponse;
import org.cloudfoundry.client.v2.spaces.SpaceApplicationSummary;
import org.cloudfoundry.client.v2.spaces.SpaceResource;
import org.cloudfoundry.client.v2.spaces.SpaceServiceSummary;
import org.junit.Test;
import reactor.rx.Streams;

import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

public final class SpringSpacesTest extends AbstractRestTest {

    private final SpringSpaces spaces = new SpringSpaces(this.restTemplate, this.root);

    @Test
    public void associateAuditor() {
        mockRequest(new RequestContext()
                .method(PUT).path("/v2/spaces/test-id/auditors/test-auditor-id")
                .status(OK)
                .responsePayload("v2/spaces/PUT_{id}_auditors_{id}_response.json"));

        AssociateSpaceAuditorRequest request = new AssociateSpaceAuditorRequest()
                .withId("test-id")
                .withAuditorId("test-auditor-id");

        AssociateSpaceAuditorResponse response = Streams.wrap(this.spaces.associateAuditor(request)).next().get();

        SpaceResource.Metadata metadata = response.getMetadata();
        assertEquals("2015-07-27T22:43:07Z", metadata.getCreatedAt());
        assertEquals("9639c996-9005-4b70-b852-d40f346d58dc", metadata.getId());
        assertNull(metadata.getUpdatedAt());
        assertEquals("/v2/spaces/9639c996-9005-4b70-b852-d40f346d58dc", metadata.getUrl());

        SpaceResource.SpaceEntity entity = response.getEntity();

        assertTrue(entity.getAllowSsh());
        assertEquals("/v2/spaces/9639c996-9005-4b70-b852-d40f346d58dc/app_events", entity.getApplicationEventsUrl());
        assertEquals("/v2/spaces/9639c996-9005-4b70-b852-d40f346d58dc/apps", entity.getApplicationsUrl());
        assertEquals("/v2/spaces/9639c996-9005-4b70-b852-d40f346d58dc/auditors", entity.getAuditorsUrl());
        assertEquals("/v2/spaces/9639c996-9005-4b70-b852-d40f346d58dc/developers", entity.getDevelopersUrl());
        assertEquals("/v2/spaces/9639c996-9005-4b70-b852-d40f346d58dc/domains", entity.getDomainsUrl());
        assertEquals("/v2/spaces/9639c996-9005-4b70-b852-d40f346d58dc/events", entity.getEventsUrl());
        assertEquals("/v2/spaces/9639c996-9005-4b70-b852-d40f346d58dc/managers", entity.getManagersUrl());
        assertEquals("name-59", entity.getName());
        assertEquals("bc168e1d-b399-4624-b7f6-fbe64eeb870f", entity.getOrganizationId());
        assertEquals("/v2/organizations/bc168e1d-b399-4624-b7f6-fbe64eeb870f", entity.getOrganizationUrl());
        assertEquals("/v2/spaces/9639c996-9005-4b70-b852-d40f346d58dc/routes", entity.getRoutesUrl());
        assertEquals("/v2/spaces/9639c996-9005-4b70-b852-d40f346d58dc/security_groups", entity.getSecurityGroupsUrl());
        assertEquals("/v2/spaces/9639c996-9005-4b70-b852-d40f346d58dc/service_instances",
                entity.getServiceInstancesUrl());
        assertNull(entity.getSpaceQuotaDefinitionId());
        verify();
    }

    @Test(expected = CloudFoundryException.class)
    public void associateAuditorError() {
        mockRequest(new RequestContext()
                .method(PUT).path("/v2/spaces/test-id/auditors/test-auditor-id")
                .errorResponse());

        AssociateSpaceAuditorRequest request = new AssociateSpaceAuditorRequest()
                .withId("test-id")
                .withAuditorId("test-auditor-id");

        Streams.wrap(this.spaces.associateAuditor(request)).next().get();
    }

    @Test(expected = RequestValidationException.class)
    public void associateAuditorInvalidRequest() {
        Streams.wrap(this.spaces.associateAuditor(new AssociateSpaceAuditorRequest())).next().get();
    }

    @Test
    public void associateDeveloper() {
        mockRequest(new RequestContext()
                .method(PUT).path("/v2/spaces/test-id/developers/test-developer-id")
                .status(OK)
                .responsePayload("v2/spaces/PUT_{id}_developers_{id}_response.json"));

        AssociateSpaceDeveloperRequest request = new AssociateSpaceDeveloperRequest()
                .withId("test-id")
                .withDeveloperId("test-developer-id");

        AssociateSpaceDeveloperResponse response = Streams.wrap(this.spaces.associateDeveloper(request)).next().get();

        SpaceResource.Metadata metadata = response.getMetadata();
        assertEquals("2015-07-27T22:43:07Z", metadata.getCreatedAt());
        assertEquals("6f8f8e0d-54f2-4736-a08e-1044fcf061d3", metadata.getId());
        assertNull(metadata.getUpdatedAt());
        assertEquals("/v2/spaces/6f8f8e0d-54f2-4736-a08e-1044fcf061d3", metadata.getUrl());

        SpaceResource.SpaceEntity entity = response.getEntity();

        assertTrue(entity.getAllowSsh());
        assertEquals("/v2/spaces/6f8f8e0d-54f2-4736-a08e-1044fcf061d3/app_events", entity.getApplicationEventsUrl());
        assertEquals("/v2/spaces/6f8f8e0d-54f2-4736-a08e-1044fcf061d3/apps", entity.getApplicationsUrl());
        assertEquals("/v2/spaces/6f8f8e0d-54f2-4736-a08e-1044fcf061d3/auditors", entity.getAuditorsUrl());
        assertEquals("/v2/spaces/6f8f8e0d-54f2-4736-a08e-1044fcf061d3/developers", entity.getDevelopersUrl());
        assertEquals("/v2/spaces/6f8f8e0d-54f2-4736-a08e-1044fcf061d3/domains", entity.getDomainsUrl());
        assertEquals("/v2/spaces/6f8f8e0d-54f2-4736-a08e-1044fcf061d3/events", entity.getEventsUrl());
        assertEquals("/v2/spaces/6f8f8e0d-54f2-4736-a08e-1044fcf061d3/managers", entity.getManagersUrl());
        assertEquals("name-68", entity.getName());
        assertEquals("5b556f7c-63f5-43e5-9522-c4fec533b09d", entity.getOrganizationId());
        assertEquals("/v2/organizations/5b556f7c-63f5-43e5-9522-c4fec533b09d", entity.getOrganizationUrl());
        assertEquals("/v2/spaces/6f8f8e0d-54f2-4736-a08e-1044fcf061d3/routes", entity.getRoutesUrl());
        assertEquals("/v2/spaces/6f8f8e0d-54f2-4736-a08e-1044fcf061d3/security_groups", entity.getSecurityGroupsUrl());
        assertEquals("/v2/spaces/6f8f8e0d-54f2-4736-a08e-1044fcf061d3/service_instances",
                entity.getServiceInstancesUrl());
        assertNull(entity.getSpaceQuotaDefinitionId());
        verify();
    }

    @Test(expected = CloudFoundryException.class)
    public void associateDeveloperError() {
        mockRequest(new RequestContext()
                .method(PUT).path("/v2/spaces/test-id/developers/test-developer-id")
                .errorResponse());

        AssociateSpaceDeveloperRequest request = new AssociateSpaceDeveloperRequest()
                .withId("test-id")
                .withDeveloperId("test-developer-id");

        Streams.wrap(this.spaces.associateDeveloper(request)).next().get();
    }

    @Test(expected = RequestValidationException.class)
    public void associateDeveloperInvalidRequest() {
        Streams.wrap(this.spaces.associateDeveloper(new AssociateSpaceDeveloperRequest())).next().get();
    }

    @Test
    public void associateManager() {
        mockRequest(new RequestContext()
                .method(PUT).path("/v2/spaces/test-id/managers/test-manager-id")
                .status(OK)
                .responsePayload("v2/spaces/PUT_{id}_managers_{id}_response.json"));

        AssociateSpaceManagerRequest request = new AssociateSpaceManagerRequest()
                .withId("test-id")
                .withManagerId("test-manager-id");

        AssociateSpaceManagerResponse response = Streams.wrap(this.spaces.associateManager(request)).next().get();

        SpaceResource.Metadata metadata = response.getMetadata();
        assertEquals("2015-07-27T22:43:07Z", metadata.getCreatedAt());
        assertEquals("542943ff-a40b-4004-9559-434b0169508c", metadata.getId());
        assertNull(metadata.getUpdatedAt());
        assertEquals("/v2/spaces/542943ff-a40b-4004-9559-434b0169508c", metadata.getUrl());

        SpaceResource.SpaceEntity entity = response.getEntity();

        assertTrue(entity.getAllowSsh());
        assertEquals("/v2/spaces/542943ff-a40b-4004-9559-434b0169508c/app_events", entity.getApplicationEventsUrl());
        assertEquals("/v2/spaces/542943ff-a40b-4004-9559-434b0169508c/apps", entity.getApplicationsUrl());
        assertEquals("/v2/spaces/542943ff-a40b-4004-9559-434b0169508c/auditors", entity.getAuditorsUrl());
        assertEquals("/v2/spaces/542943ff-a40b-4004-9559-434b0169508c/developers", entity.getDevelopersUrl());
        assertEquals("/v2/spaces/542943ff-a40b-4004-9559-434b0169508c/domains", entity.getDomainsUrl());
        assertEquals("/v2/spaces/542943ff-a40b-4004-9559-434b0169508c/events", entity.getEventsUrl());
        assertEquals("/v2/spaces/542943ff-a40b-4004-9559-434b0169508c/managers", entity.getManagersUrl());
        assertEquals("name-85", entity.getName());
        assertEquals("0a68fcd5-dc1c-48d0-98dc-33008ce0d7ce", entity.getOrganizationId());
        assertEquals("/v2/organizations/0a68fcd5-dc1c-48d0-98dc-33008ce0d7ce", entity.getOrganizationUrl());
        assertEquals("/v2/spaces/542943ff-a40b-4004-9559-434b0169508c/routes", entity.getRoutesUrl());
        assertEquals("/v2/spaces/542943ff-a40b-4004-9559-434b0169508c/security_groups", entity.getSecurityGroupsUrl());
        assertEquals("/v2/spaces/542943ff-a40b-4004-9559-434b0169508c/service_instances",
                entity.getServiceInstancesUrl());
        assertNull(entity.getSpaceQuotaDefinitionId());
        verify();
    }

    @Test(expected = CloudFoundryException.class)
    public void associateManagerError() {
        mockRequest(new RequestContext()
                .method(PUT).path("/v2/spaces/test-id/managers/test-manager-id")
                .errorResponse());

        AssociateSpaceManagerRequest request = new AssociateSpaceManagerRequest()
                .withId("test-id")
                .withManagerId("test-manager-id");

        Streams.wrap(this.spaces.associateManager(request)).next().get();
    }

    @Test(expected = RequestValidationException.class)
    public void associateManagerInvalidRequest() {
        Streams.wrap(this.spaces.associateManager(new AssociateSpaceManagerRequest())).next().get();
    }

    @Test
    public void associateSecurityGroup() {
        mockRequest(new RequestContext()
                .method(PUT).path("/v2/spaces/test-id/security_groups/test-security-group-id")
                .status(OK)
                .responsePayload("v2/spaces/PUT_{id}_security_group_{id}_response.json"));

        AssociateSpaceSecurityGroupRequest request = new AssociateSpaceSecurityGroupRequest()
                .withId("test-id")
                .withSecurityGroupId("test-security-group-id");

        AssociateSpaceSecurityGroupResponse response = Streams.wrap(this.spaces.associateSecurityGroup(request)).next
                ().get();

        SpaceResource.Metadata metadata = response.getMetadata();
        assertEquals("2015-07-27T22:43:06Z", metadata.getCreatedAt());
        assertEquals("c9424692-395b-403b-90e6-10049bbd9e23", metadata.getId());
        assertNull(metadata.getUpdatedAt());
        assertEquals("/v2/spaces/c9424692-395b-403b-90e6-10049bbd9e23", metadata.getUrl());

        SpaceResource.SpaceEntity entity = response.getEntity();

        assertTrue(entity.getAllowSsh());
        assertEquals("/v2/spaces/c9424692-395b-403b-90e6-10049bbd9e23/app_events", entity.getApplicationEventsUrl());
        assertEquals("/v2/spaces/c9424692-395b-403b-90e6-10049bbd9e23/apps", entity.getApplicationsUrl());
        assertEquals("/v2/spaces/c9424692-395b-403b-90e6-10049bbd9e23/auditors", entity.getAuditorsUrl());
        assertEquals("/v2/spaces/c9424692-395b-403b-90e6-10049bbd9e23/developers", entity.getDevelopersUrl());
        assertEquals("/v2/spaces/c9424692-395b-403b-90e6-10049bbd9e23/domains", entity.getDomainsUrl());
        assertEquals("/v2/spaces/c9424692-395b-403b-90e6-10049bbd9e23/events", entity.getEventsUrl());
        assertEquals("/v2/spaces/c9424692-395b-403b-90e6-10049bbd9e23/managers", entity.getManagersUrl());
        assertEquals("name-39", entity.getName());
        assertEquals("67096164-bdcf-4b53-92e1-a2991882a066", entity.getOrganizationId());
        assertEquals("/v2/organizations/67096164-bdcf-4b53-92e1-a2991882a066", entity.getOrganizationUrl());
        assertEquals("/v2/spaces/c9424692-395b-403b-90e6-10049bbd9e23/routes", entity.getRoutesUrl());
        assertEquals("/v2/spaces/c9424692-395b-403b-90e6-10049bbd9e23/security_groups", entity.getSecurityGroupsUrl());
        assertEquals("/v2/spaces/c9424692-395b-403b-90e6-10049bbd9e23/service_instances",
                entity.getServiceInstancesUrl());
        assertNull(entity.getSpaceQuotaDefinitionId());
        verify();
    }

    @Test(expected = CloudFoundryException.class)
    public void associateSecurityGroupError() {
        mockRequest(new RequestContext()
                .method(PUT).path("/v2/spaces/test-id/security_groups/test-security-group-id")
                .errorResponse());

        AssociateSpaceSecurityGroupRequest request = new AssociateSpaceSecurityGroupRequest()
                .withId("test-id")
                .withSecurityGroupId("test-security-group-id");

        Streams.wrap(this.spaces.associateSecurityGroup(request)).next().get();
    }

    @Test(expected = RequestValidationException.class)
    public void associateSecurityGroupInvalidRequest() {
        Streams.wrap(this.spaces.associateSecurityGroup(new AssociateSpaceSecurityGroupRequest())).next().get();
    }

    @Test
    public void create() {
        mockRequest(new RequestContext()
                .method(POST).path("/v2/spaces")
                .requestPayload("v2/spaces/POST_request.json")
                .status(OK)
                .responsePayload("v2/spaces/POST_response.json"));

        CreateSpaceRequest request = new CreateSpaceRequest()
                .withName("development")
                .withOrganizationId("c523070c-3006-4715-86dd-414afaecd949");

        CreateSpaceResponse response = Streams.wrap(this.spaces.create(request)).next().get();

        SpaceResource.Metadata metadata = response.getMetadata();
        assertEquals("2015-07-27T22:43:08Z", metadata.getCreatedAt());
        assertEquals("d29dc30c-793c-49a6-97fe-9aff75dcbd12", metadata.getId());
        assertNull(metadata.getUpdatedAt());
        assertEquals("/v2/spaces/d29dc30c-793c-49a6-97fe-9aff75dcbd12", metadata.getUrl());

        SpaceResource.SpaceEntity entity = response.getEntity();

        assertTrue(entity.getAllowSsh());
        assertEquals("/v2/spaces/d29dc30c-793c-49a6-97fe-9aff75dcbd12/app_events", entity.getApplicationEventsUrl());
        assertEquals("/v2/spaces/d29dc30c-793c-49a6-97fe-9aff75dcbd12/apps", entity.getApplicationsUrl());
        assertEquals("/v2/spaces/d29dc30c-793c-49a6-97fe-9aff75dcbd12/auditors", entity.getAuditorsUrl());
        assertEquals("/v2/spaces/d29dc30c-793c-49a6-97fe-9aff75dcbd12/developers", entity.getDevelopersUrl());
        assertEquals("/v2/spaces/d29dc30c-793c-49a6-97fe-9aff75dcbd12/domains", entity.getDomainsUrl());
        assertEquals("/v2/spaces/d29dc30c-793c-49a6-97fe-9aff75dcbd12/events", entity.getEventsUrl());
        assertEquals("/v2/spaces/d29dc30c-793c-49a6-97fe-9aff75dcbd12/managers", entity.getManagersUrl());
        assertEquals("development", entity.getName());
        assertEquals("c523070c-3006-4715-86dd-414afaecd949", entity.getOrganizationId());
        assertEquals("/v2/organizations/c523070c-3006-4715-86dd-414afaecd949", entity.getOrganizationUrl());
        assertEquals("/v2/spaces/d29dc30c-793c-49a6-97fe-9aff75dcbd12/routes", entity.getRoutesUrl());
        assertEquals("/v2/spaces/d29dc30c-793c-49a6-97fe-9aff75dcbd12/security_groups", entity.getSecurityGroupsUrl());
        assertEquals("/v2/spaces/d29dc30c-793c-49a6-97fe-9aff75dcbd12/service_instances",
                entity.getServiceInstancesUrl());
        assertNull(entity.getSpaceQuotaDefinitionId());
        verify();
    }

    @Test(expected = CloudFoundryException.class)
    public void createError() {
        mockRequest(new RequestContext()
                .method(POST).path("/v2/spaces")
                .requestPayload("v2/spaces/POST_request.json")
                .errorResponse());

        CreateSpaceRequest request = new CreateSpaceRequest()
                .withName("development")
                .withOrganizationId("c523070c-3006-4715-86dd-414afaecd949");

        Streams.wrap(this.spaces.create(request)).next().get();
    }

    @Test(expected = RequestValidationException.class)
    public void createInvalidRequest() {
        Streams.wrap(this.spaces.create(new CreateSpaceRequest())).next().get();
    }

    @Test
    public void delete() {
        mockRequest(new RequestContext()
                .method(DELETE).path("v2/spaces/test-id?async=true")
                .status(NO_CONTENT));

        DeleteSpaceRequest request = new DeleteSpaceRequest()
                .withAsync(true)
                .withId("test-id");

        Streams.wrap(this.spaces.delete(request)).next().get();

        verify();
    }

    @Test(expected = CloudFoundryException.class)
    public void deleteError() {
        mockRequest(new RequestContext()
                .method(DELETE).path("v2/spaces/test-id?async=true")
                .errorResponse());

        DeleteSpaceRequest request = new DeleteSpaceRequest()
                .withAsync(true)
                .withId("test-id");

        Streams.wrap(this.spaces.delete(request)).next().get();
    }

    @Test(expected = RequestValidationException.class)
    public void deleteInvalidRequest() {
        Streams.wrap(this.spaces.delete(new DeleteSpaceRequest())).next().get();
    }

    public void get() {
        mockRequest(new RequestContext()
                .method(GET).path("/v2/spaces/test-id")
                .status(OK)
                .responsePayload("v2/spaces/GET_{id}_response.json"));

        GetSpaceRequest request = new GetSpaceRequest()
                .withId("test-id");

        GetSpaceResponse response = Streams.wrap(this.spaces.get(request)).next().get();

        SpaceResource.Metadata metadata = response.getMetadata();
        assertEquals("2015-07-27T22:43:08Z", metadata.getCreatedAt());
        assertEquals("0f102457-c1fc-42e5-9c81-c7be2bc65dcd", metadata.getId());
        assertNull(metadata.getUpdatedAt());
        assertEquals("/v2/spaces/0f102457-c1fc-42e5-9c81-c7be2bc65dcd", metadata.getUrl());

        SpaceResource.SpaceEntity entity = response.getEntity();

        assertTrue(entity.getAllowSsh());
        assertEquals("/v2/spaces/0f102457-c1fc-42e5-9c81-c7be2bc65dcd/app_events", entity.getApplicationEventsUrl());
        assertEquals("/v2/spaces/0f102457-c1fc-42e5-9c81-c7be2bc65dcd/apps", entity.getApplicationsUrl());
        assertEquals("/v2/spaces/0f102457-c1fc-42e5-9c81-c7be2bc65dcd/auditors", entity.getAuditorsUrl());
        assertEquals("/v2/spaces/0f102457-c1fc-42e5-9c81-c7be2bc65dcd/developers", entity.getDevelopersUrl());
        assertEquals("/v2/spaces/0f102457-c1fc-42e5-9c81-c7be2bc65dcd/domains", entity.getDomainsUrl());
        assertEquals("/v2/spaces/0f102457-c1fc-42e5-9c81-c7be2bc65dcd/events", entity.getEventsUrl());
        assertEquals("/v2/spaces/0f102457-c1fc-42e5-9c81-c7be2bc65dcd/managers", entity.getManagersUrl());
        assertEquals("name-108", entity.getName());
        assertEquals("525a31fb-bc2b-4f7f-865e-1c93b42a6762", entity.getOrganizationId());
        assertEquals("/v2/organizations/525a31fb-bc2b-4f7f-865e-1c93b42a6762", entity.getOrganizationUrl());
        assertEquals("/v2/spaces/0f102457-c1fc-42e5-9c81-c7be2bc65dcd/routes", entity.getRoutesUrl());
        assertEquals("/v2/spaces/0f102457-c1fc-42e5-9c81-c7be2bc65dcd/security_groups", entity.getSecurityGroupsUrl());
        assertEquals("/v2/spaces/0f102457-c1fc-42e5-9c81-c7be2bc65dcd/service_instances",
                entity.getServiceInstancesUrl());
        assertNull(entity.getSpaceQuotaDefinitionId());
        verify();
    }

    @Test(expected = CloudFoundryException.class)
    public void getError() {
        mockRequest(new RequestContext()
                .method(GET).path("/v2/spaces/test-id")
                .errorResponse());

        GetSpaceRequest request = new GetSpaceRequest()
                .withId("test-id");

        Streams.wrap(this.spaces.get(request)).next().get();
    }

    @Test(expected = RequestValidationException.class)
    public void getInvalidRequest() {
        Streams.wrap(this.spaces.get(new GetSpaceRequest())).next().get();
    }

    @Test
    public void getSummary() {
        mockRequest(new RequestContext()
                .method(GET).path("/v2/spaces/test-id/summary")
                .status(OK)
                .responsePayload("v2/spaces/GET_{id}_summary_response.json"));

        GetSpaceSummaryRequest request = new GetSpaceSummaryRequest()
                .withId("test-id");

        GetSpaceSummaryResponse response = Streams.wrap(this.spaces.getSummary(request)).next().get();

        assertTrue(null != response.getApplications() && response.getApplications().size() == 1);
        SpaceApplicationSummary app = response.getApplications().get(0);

        assertEquals("e1efe0a2-a931-4604-a419-f76dbe23ad76", app.getId());
        assertEquals(Integer.valueOf(0), app.getRunningInstances());

        assertTrue(null != app.getRoutes() && app.getRoutes().size() == 1);
        SpaceApplicationSummary.Route route = app.getRoutes().get(0);

        assertEquals("af154090-baca-4805-a8a2-9db93a16a84b", route.getDomain().getId());
        assertEquals("domain-48.example.com", route.getDomain().getName());
        assertEquals("host-11", route.getHost());
        assertEquals("3445e88d-adda-4255-9b9d-6f701fb0de17", route.getId());

        assertEquals(Integer.valueOf(1), app.getServiceCount());
        assertEquals(Collections.singletonList("name-654"), app.getServiceNames());
        assertEquals(Collections.singletonList("host-11.domain-48.example.com"), app.getUrls());

        assertNull(app.getBuildpack());
        assertNull(app.getCommand());
        assertFalse(app.getConsole());
        assertNull(app.getDebug());
        assertNull(app.getDetectedBuildpack());
        assertEquals("", app.getDetectedStartCommand());
        assertFalse(app.getDiego());
        assertEquals(Integer.valueOf(1024), app.getDiskQuota());
        assertEquals(Collections.singletonMap("redacted_message", "[PRIVATE DATA HIDDEN]"),
                app.getDockerCredentialsJson());
        assertNull(app.getDockerImage());
        assertTrue(app.getEnableSsh());
        assertNull(app.getEnvironmentJson());
        assertNull(app.getHealthCheckTimeout());
        assertEquals("port", app.getHealthCheckType());
        assertEquals(Integer.valueOf(1), app.getInstances());
        assertEquals(Integer.valueOf(1024), app.getMemory());
        assertEquals("name-652", app.getName());
        assertEquals("PENDING", app.getPackageState());
        assertEquals("2015-07-27T22:43:19Z", app.getPackageUpdatedAt());
        assertFalse(app.getProduction());
        assertEquals("f9c44c5c-9613-40b2-9296-e156c661a0ba", app.getSpaceId());
        assertEquals("01a9ea88-1028-4d1a-a8ee-d1acc686815c", app.getStackId());
        assertNull(app.getStagingFailedDescription());
        assertNull(app.getStagingFailedReason());
        assertNull(app.getStagingTaskId());
        assertEquals("STOPPED", app.getState());
        assertEquals("6505d60e-2a6f-475c-8c1d-85c66139447e", app.getVersion());

        assertEquals("f9c44c5c-9613-40b2-9296-e156c661a0ba", response.getId());
        assertEquals("name-649", response.getName());

        assertTrue(null != response.getServices() && response.getServices().size() == 1);
        SpaceServiceSummary serviceSummary = response.getServices().get(0);

        assertEquals(Integer.valueOf(1), serviceSummary.getBoundAppCount());
        assertNull(serviceSummary.getDashboardUrl());
        assertEquals("83e3713f-5f9b-4168-a43c-02cc66493cc0", serviceSummary.getId());
        assertNull(serviceSummary.getLastOperation());
        assertEquals("name-654", serviceSummary.getName());

        assertEquals("67bd9226-6d63-48ac-9114-a756a01bff7c", serviceSummary.getServicePlan().getId());
        assertEquals("name-655", serviceSummary.getServicePlan().getName());
        assertEquals("64ce598e-0c24-4dba-bfa1-594187db7404", serviceSummary.getServicePlan().getService().getId());
        assertEquals("label-23", serviceSummary.getServicePlan().getService().getLabel());
        assertNull(serviceSummary.getServicePlan().getService().getProvider());
        assertNull(serviceSummary.getServicePlan().getService().getVersion());

        verify();
    }

    @Test(expected = CloudFoundryException.class)
    public void getSummaryError() {
        mockRequest(new RequestContext()
                .method(GET).path("/v2/spaces/test-id/summary")
                .errorResponse());

        GetSpaceSummaryRequest request = new GetSpaceSummaryRequest()
                .withId("test-id");

        Streams.wrap(this.spaces.getSummary(request)).next().get();
    }

    @Test(expected = RequestValidationException.class)
    public void getSummaryInvalidRequest() {
        Streams.wrap(this.spaces.getSummary(new GetSpaceSummaryRequest())).next().get();
    }

    @Test
    public void list() {
        mockRequest(new RequestContext()
                        .method(GET).path("/v2/spaces?q=name%20IN%20test-name&page=-1")
                        .status(OK)
                        .responsePayload("v2/spaces/GET_response.json")
        );

        ListSpacesRequest request = new ListSpacesRequest()
                .withName("test-name")
                .withPage(-1);

        ListSpacesResponse response = Streams.wrap(this.spaces.list(request)).next().get();

        assertNull(response.getNextUrl());
        assertNull(response.getPreviousUrl());
        assertEquals(Integer.valueOf(1), response.getTotalPages());
        assertEquals(Integer.valueOf(1), response.getTotalResults());

        assertEquals(1, response.getResources().size());
        ListSpacesResponse.ListSpacesResponseResource resource = response.getResources().get(0);

        SpaceResource.Metadata metadata = resource.getMetadata();
        assertEquals("2015-07-27T22:43:08Z", metadata.getCreatedAt());
        assertEquals("b4293b09-8316-472c-a29a-6468a3adff59", metadata.getId());
        assertNull(metadata.getUpdatedAt());
        assertEquals("/v2/spaces/b4293b09-8316-472c-a29a-6468a3adff59", metadata.getUrl());

        SpaceResource.SpaceEntity entity = resource.getEntity();
        assertTrue(entity.getAllowSsh());
        assertEquals("/v2/spaces/b4293b09-8316-472c-a29a-6468a3adff59/app_events", entity.getApplicationEventsUrl());
        assertEquals("/v2/spaces/b4293b09-8316-472c-a29a-6468a3adff59/apps", entity.getApplicationsUrl());
        assertEquals("/v2/spaces/b4293b09-8316-472c-a29a-6468a3adff59/auditors", entity.getAuditorsUrl());
        assertEquals("/v2/spaces/b4293b09-8316-472c-a29a-6468a3adff59/developers", entity.getDevelopersUrl());
        assertEquals("/v2/spaces/b4293b09-8316-472c-a29a-6468a3adff59/domains", entity.getDomainsUrl());
        assertEquals("/v2/spaces/b4293b09-8316-472c-a29a-6468a3adff59/events", entity.getEventsUrl());
        assertEquals("/v2/spaces/b4293b09-8316-472c-a29a-6468a3adff59/managers", entity.getManagersUrl());
        assertEquals("name-111", entity.getName());
        assertEquals("3ce736dd-3b8c-4f64-acab-ed76488b79a3", entity.getOrganizationId());
        assertEquals("/v2/organizations/3ce736dd-3b8c-4f64-acab-ed76488b79a3", entity.getOrganizationUrl());
        assertEquals("/v2/spaces/b4293b09-8316-472c-a29a-6468a3adff59/routes", entity.getRoutesUrl());
        assertEquals("/v2/spaces/b4293b09-8316-472c-a29a-6468a3adff59/security_groups", entity.getSecurityGroupsUrl());
        assertEquals("/v2/spaces/b4293b09-8316-472c-a29a-6468a3adff59/service_instances",
                entity.getServiceInstancesUrl());
        assertNull(entity.getSpaceQuotaDefinitionId());

        verify();
    }

    @Test(expected = CloudFoundryException.class)
    public void listError() {
        mockRequest(new RequestContext()
                .method(GET).path("/v2/spaces?q=name%20IN%20test-name&page=-1")
                .errorResponse());

        ListSpacesRequest request = new ListSpacesRequest()
                .withName("test-name")
                .withPage(-1);

        Streams.wrap(this.spaces.list(request)).next().get();
    }

    @Test
    public void listApplications() {
        mockRequest(new RequestContext()
                .method(GET).path("/v2/spaces/test-id/apps?q=name%20IN%20test-name&page=-1")
                .status(OK)
                .responsePayload("v2/spaces/GET_{id}_apps_response.json"));

        ListSpaceApplicationsRequest request = new ListSpaceApplicationsRequest()
                .withId("test-id")
                .withNames(Collections.singletonList("test-name"))
                .withPage(-1);

        ListSpaceApplicationsResponse response = Streams.wrap(this.spaces.listApplications(request)).next().get();

        assertNull(response.getNextUrl());
        assertNull(response.getPreviousUrl());
        assertEquals(Integer.valueOf(1), response.getTotalPages());
        assertEquals(Integer.valueOf(1), response.getTotalResults());
        assertEquals(1, response.getResources().size());

        ListSpaceApplicationsResponse.Resource resource = response.getResources().get(0);

        ApplicationResource.Metadata metadata = resource.getMetadata();

        assertEquals("2015-07-27T22:43:08Z", metadata.getCreatedAt());
        assertEquals("4ee31730-3c0e-4ec6-8329-26e727ab8ccd", metadata.getId());
        assertEquals("2015-07-27T22:43:08Z", metadata.getUpdatedAt());
        assertEquals("/v2/apps/4ee31730-3c0e-4ec6-8329-26e727ab8ccd", metadata.getUrl());

        ApplicationEntity entity = resource.getEntity();

        assertNull(entity.getBuildpack());
        assertNull(entity.getCommand());
        assertFalse(entity.getConsole());
        assertNull(entity.getDebug());
        assertNull(entity.getDetectedBuildpack());
        assertEquals("", entity.getDetectedStartCommand());
        assertFalse(entity.getDiego());
        assertEquals(Integer.valueOf(1024), entity.getDiskQuota());
        assertEquals(Collections.singletonMap("redacted_message", "[PRIVATE DATA HIDDEN]"),
                entity.getDockerCredentialsJson());
        assertNull(entity.getDockerImage());
        assertTrue(entity.getEnableSsh());
        assertNull(entity.getEnvironmentJson());
        assertEquals("/v2/apps/4ee31730-3c0e-4ec6-8329-26e727ab8ccd/events", entity.getEventsUrl());
        assertNull(entity.getHealthCheckTimeout());
        assertEquals("port", entity.getHealthCheckType());
        assertEquals(Integer.valueOf(1), entity.getInstances());
        assertEquals(Integer.valueOf(1024), entity.getMemory());
        assertEquals("name-103", entity.getName());
        assertEquals("PENDING", entity.getPackageState());
        assertEquals("2015-07-27T22:43:08Z", entity.getPackageUpdatedAt());
        assertFalse(entity.getProduction());
        assertEquals("/v2/apps/4ee31730-3c0e-4ec6-8329-26e727ab8ccd/routes", entity.getRoutesUrl());
        assertEquals("/v2/apps/4ee31730-3c0e-4ec6-8329-26e727ab8ccd/service_bindings", entity.getServiceBindingsUrl());
        assertEquals("ca816a1b-ed3e-4ea8-bda2-2031d2e5b89f", entity.getSpaceId());
        assertEquals("/v2/spaces/ca816a1b-ed3e-4ea8-bda2-2031d2e5b89f", entity.getSpaceUrl());
        assertEquals("e458a99f-53a4-4da4-b78a-5f2eb212cc47", entity.getStackId());
        assertEquals("/v2/stacks/e458a99f-53a4-4da4-b78a-5f2eb212cc47", entity.getStackUrl());
        assertNull(entity.getStagingFailedDescription());
        assertNull(entity.getStagingFailedReason());
        assertNull(entity.getStagingTaskId());
        assertEquals("STOPPED", entity.getState());
        assertEquals("cc21d137-45d6-4687-ab71-8288ac0e5724", entity.getVersion());

        verify();
    }

    @Test(expected = CloudFoundryException.class)
    public void listApplicationsError() {
        mockRequest(new RequestContext()
                .method(GET).path("/v2/spaces/test-id/apps?q=name%20IN%20test-name&page=-1")
                .errorResponse());

        ListSpaceApplicationsRequest request = new ListSpaceApplicationsRequest()
                .withId("test-id")
                .withNames(Collections.singletonList("test-name"))
                .withPage(-1);

        Streams.wrap(this.spaces.listApplications(request)).next().get();
    }

    @Test(expected = RequestValidationException.class)
    public void listApplicationsInvalidRequest() {
        Streams.wrap(this.spaces.listApplications(new ListSpaceApplicationsRequest())).next().get();
    }

}
