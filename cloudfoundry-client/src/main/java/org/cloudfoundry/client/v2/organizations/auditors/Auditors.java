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

package org.cloudfoundry.client.v2.organizations.auditors;

import org.reactivestreams.Publisher;

/**
 * Main entry point to the Cloud Foundry Organization Auditors Client API
 */
public interface Auditors {

    /**
     * Makes the <a
     * href="http://apidocs.cloudfoundry.org/214/organizations/associate_auditor_with_the_organization.html">Create
     * Auditor</a> request
     *
     * @param request the List Organizations request
     * @return the response from the List Organizations request
     */
    Publisher<CreateAuditorResponse> create(CreateAuditorRequest request);

}
