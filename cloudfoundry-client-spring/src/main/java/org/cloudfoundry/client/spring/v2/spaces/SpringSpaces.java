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

import org.cloudfoundry.client.spring.util.AbstractSpringOperations;
import org.cloudfoundry.client.spring.util.QueryBuilder;
import org.cloudfoundry.client.spring.v2.FilterBuilder;
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
import org.cloudfoundry.client.v2.spaces.Spaces;
import org.reactivestreams.Publisher;
import org.springframework.web.client.RestOperations;

import java.net.URI;

/**
 * The Spring-based implementation of {@link Spaces}
 */
public final class SpringSpaces extends AbstractSpringOperations implements Spaces {

    /**
     * Creates an instance
     *
     * @param restOperations the {@link RestOperations} to use to communicate with the server
     * @param root           the root URI of the server.  Typically something like {@code https://api.run.pivotal.io}.
     */
    public SpringSpaces(RestOperations restOperations, URI root) {
        super(restOperations, root);
    }

    @Override
    public Publisher<AssociateSpaceAuditorResponse> associateAuditor(AssociateSpaceAuditorRequest request) {
        return put(request, AssociateSpaceAuditorResponse.class,
                builder -> builder.pathSegment("v2",
                        "spaces", request.getId(),
                        "auditors", request.getAuditorId()));
    }

    @Override
    public Publisher<AssociateSpaceDeveloperResponse> associateDeveloper(AssociateSpaceDeveloperRequest request) {
        return put(request, AssociateSpaceDeveloperResponse.class,
                builder -> builder.pathSegment("v2",
                        "spaces", request.getId(),
                        "developers", request.getDeveloperId()));
    }

    @Override
    public Publisher<AssociateSpaceManagerResponse> associateManager(AssociateSpaceManagerRequest request) {
        return put(request, AssociateSpaceManagerResponse.class,
                builder -> builder.pathSegment("v2",
                        "spaces", request.getId(),
                        "managers", request.getManagerId()));
    }

    @Override
    public Publisher<AssociateSpaceSecurityGroupResponse> associateSecurityGroup(
            AssociateSpaceSecurityGroupRequest request) {
        return put(request, AssociateSpaceSecurityGroupResponse.class,
                builder -> builder.pathSegment("v2",
                        "spaces", request.getId(),
                        "security_groups", request.getSecurityGroupId()));
    }

    @Override
    public Publisher<CreateSpaceResponse> create(CreateSpaceRequest request) {
        return post(request, CreateSpaceResponse.class,
                builder -> builder.pathSegment("v2", "spaces"));
    }

    @Override
    public Publisher<Void> delete(DeleteSpaceRequest request) {
        return delete(request, builder -> {
            builder.pathSegment("v2", "spaces", request.getId());
            QueryBuilder.augment(builder, request);
        });
    }

    @Override
    public Publisher<GetSpaceResponse> get(GetSpaceRequest request) {
        return get(request, GetSpaceResponse.class,
                builder -> builder.pathSegment("v2", "spaces", request.getId()));
    }

    @Override
    public Publisher<GetSpaceSummaryResponse> getSummary(GetSpaceSummaryRequest request) {
        return get(request, GetSpaceSummaryResponse.class,
                builder -> builder.pathSegment("v2", "spaces", request.getId(), "summary"));
    }

    @Override
    public Publisher<ListSpacesResponse> list(ListSpacesRequest request) {
        return get(request, ListSpacesResponse.class, builder -> {
            builder.pathSegment("v2", "spaces");
            FilterBuilder.augment(builder, request);
            QueryBuilder.augment(builder, request);
        });
    }

    @Override
    public Publisher<ListSpaceApplicationsResponse> listApplications(ListSpaceApplicationsRequest request) {
        return get(request, ListSpaceApplicationsResponse.class, builder -> {
            builder.pathSegment("v2", "spaces", request.getId(), "apps");
            FilterBuilder.augment(builder, request);
            QueryBuilder.augment(builder, request);
        });
    }

}
