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

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.Singular;

import java.util.Map;

/**
 * The response payload for the Get Application Environment operation
 */
@Data
public final class GetApplicationEnvironmentResponse {

    private final Map<String, Object> applicationEnvironmentVariables;

    private final Map<String, Object> environmentVariables;

    private final Map<String, Object> runningEnvironmentVariables;

    private final Map<String, Object> stagingEnvironmentVariables;

    @Builder
    GetApplicationEnvironmentResponse(@JsonProperty("application_env_json") @Singular Map<String, Object>
                                              applicationEnvironmentVariables,
                                      @JsonProperty("environment_variables") @Singular Map<String, Object>
                                              environmentVariables,
                                      @JsonProperty("running_env_json") @Singular Map<String, Object>
                                              runningEnvironmentVariables,
                                      @JsonProperty("staging_env_json") @Singular Map<String, Object>
                                              stagingEnvironmentVariables) {
        this.applicationEnvironmentVariables = applicationEnvironmentVariables;
        this.environmentVariables = environmentVariables;
        this.runningEnvironmentVariables = runningEnvironmentVariables;
        this.stagingEnvironmentVariables = stagingEnvironmentVariables;
    }

}
