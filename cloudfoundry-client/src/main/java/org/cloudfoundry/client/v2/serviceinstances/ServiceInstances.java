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

package org.cloudfoundry.client.v2.serviceinstances;

import org.reactivestreams.Publisher;

/**
 * Main entry point to the Cloud Foundry Service Instances Client API
 */
public interface ServiceInstances {

    /**
     * Makes the <a href="http://apidocs.cloudfoundry.org/214/service_instances/list_all_service_instances.html">List
     * Service Instances</a> request
     *
     * @param request the List Service Instances request
     * @return the response from the List Service Instances request
     */
    Publisher<ListServiceInstancesResponse> list(ListServiceInstancesRequest request);

}
