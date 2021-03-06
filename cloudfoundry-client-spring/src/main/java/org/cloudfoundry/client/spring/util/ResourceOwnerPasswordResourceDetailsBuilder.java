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

package org.cloudfoundry.client.spring.util;

import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;

/**
 * A builder API for a {@link ResourceOwnerPasswordResourceDetails}
 */
public final class ResourceOwnerPasswordResourceDetailsBuilder {

    private final ResourceOwnerPasswordResourceDetails details = new ResourceOwnerPasswordResourceDetails();

    /**
     * Configure the access token uri
     *
     * @param accessTokenUri the access token uri
     * @return {@code this}
     */
    public ResourceOwnerPasswordResourceDetailsBuilder withAccessTokenUri(String accessTokenUri) {
        this.details.setAccessTokenUri(accessTokenUri);
        return this;
    }

    /**
     * Configure the client id
     *
     * @param clientId the client id
     * @return {@code this}
     */
    public ResourceOwnerPasswordResourceDetailsBuilder withClientId(String clientId) {
        this.details.setClientId(clientId);
        return this;
    }

    /**
     * Configure the client secret
     *
     * @param clientSecret the client secret
     * @return {@code this}
     */
    public ResourceOwnerPasswordResourceDetailsBuilder withClientSecret(String clientSecret) {
        this.details.setClientSecret(clientSecret);
        return this;
    }

    /**
     * Configure the password
     *
     * @param password the password
     * @return @{code this}
     */
    public ResourceOwnerPasswordResourceDetailsBuilder withPassword(String password) {
        this.details.setPassword(password);
        return this;
    }

    /**
     * Configure the username
     *
     * @param username the username
     * @return {@code this}
     */
    public ResourceOwnerPasswordResourceDetailsBuilder withUsername(String username) {
        this.details.setUsername(username);
        return this;
    }

    /**
     * Build the {@link ResourceOwnerPasswordResourceDetails}
     *
     * @return the properly configured {@link ResourceOwnerPasswordResourceDetails}
     */
    public ResourceOwnerPasswordResourceDetails build() {
        return this.details;
    }

}
