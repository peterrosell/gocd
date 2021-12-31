/*
 * Copyright 2022 ThoughtWorks, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.thoughtworks.go.config.update;

import com.thoughtworks.go.config.CruiseConfig;
import com.thoughtworks.go.config.commands.EntityConfigUpdateCommand;
import com.thoughtworks.go.config.exceptions.EntityType;
import com.thoughtworks.go.config.remote.ConfigRepoConfig;
import com.thoughtworks.go.config.remote.ConfigReposConfig;
import com.thoughtworks.go.server.domain.Username;
import com.thoughtworks.go.server.service.SecurityService;
import com.thoughtworks.go.server.service.result.HttpLocalizedOperationResult;

import static com.thoughtworks.go.serverhealth.HealthStateType.forbidden;

public class DeleteConfigRepoCommand implements EntityConfigUpdateCommand<ConfigRepoConfig> {
    private ConfigRepoConfig preprocessedConfigRepo;
    private SecurityService securityService;
    private final String repoId;
    private final Username username;
    private final HttpLocalizedOperationResult result;

    public DeleteConfigRepoCommand(SecurityService securityService, String repoId, Username username, HttpLocalizedOperationResult result) {
        this.securityService = securityService;
        this.repoId = repoId;
        this.username = username;
        this.result = result;
    }

    @Override
    public void update(CruiseConfig preprocessedConfig) {
        ConfigReposConfig configRepos = preprocessedConfig.getConfigRepos();
        preprocessedConfigRepo = configRepos.getConfigRepo(repoId);
        configRepos.remove(preprocessedConfigRepo);
    }

    @Override
    public boolean isValid(CruiseConfig preprocessedConfig) {
        return true;
    }

    @Override
    public void clearErrors() {
    }

    @Override
    public ConfigRepoConfig getPreprocessedEntityConfig() {
        return preprocessedConfigRepo;
    }

    @Override
    public boolean canContinue(CruiseConfig cruiseConfig) {
        return doesConfigRepoExist(cruiseConfig);
    }

    private boolean doesConfigRepoExist(CruiseConfig cruiseConfig) {
        return cruiseConfig.getConfigRepos().getConfigRepo(repoId) != null;
    }
}
