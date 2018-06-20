/*
 * Copyright 2018 ThoughtWorks, Inc.
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

package com.thoughtworks.go.server.service;

import com.thoughtworks.go.config.*;
import com.thoughtworks.go.domain.config.Configuration;
import com.thoughtworks.go.domain.config.ConfigurationProperty;
import com.thoughtworks.go.plugin.access.PluginNotFoundException;
import com.thoughtworks.go.plugin.access.artifact.ArtifactExtension;
import com.thoughtworks.go.plugin.api.response.validation.ValidationError;
import com.thoughtworks.go.plugin.api.response.validation.ValidationResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/*
    handles publish and fetch configuration of external artifacts
*/
@Service
public class ExternalArtifactsService {

    private ArtifactExtension artifactExtension;

    @Autowired
    public ExternalArtifactsService(ArtifactExtension artifactExtension) {
        this.artifactExtension = artifactExtension;
    }

    public void validateExternalArtifactConfig(PluggableArtifactConfig preprocessedPluggableArtifactConfig, ArtifactStore artifactStore, ValidationContext validationContext) {
        if (preprocessedPluggableArtifactConfig.hasValidPluginAndStore(validationContext)) {
            try {
                ValidationResult validationResult = artifactExtension.validatePluggableArtifactConfig(artifactStore.getPluginId(), preprocessedPluggableArtifactConfig.getConfiguration().getConfigurationAsMap(true));
                mapErrorsToConfiguration(validationResult, preprocessedPluggableArtifactConfig.getConfiguration(), preprocessedPluggableArtifactConfig);

            } catch (PluginNotFoundException e) {
                preprocessedPluggableArtifactConfig.addError("pluginId", String.format("Plugin with id `%s` is not found.", artifactStore.getPluginId()));
            }
        }
        else {
            preprocessedPluggableArtifactConfig.addError("pluginId", "Could not determine the plugin to perform the plugin validations. Usually this happens when the plugin is not installed.");;
        }
    }

    public void validateFetchExternalArtifactTask(FetchPluggableArtifactTask preprocessedFetchPluggableArtifactTask, ValidationContext validationContext, PipelineConfig pipelineConfig) {
        PluggableArtifactConfig specifiedExternalArtifact = preprocessedFetchPluggableArtifactTask.getSpecifiedExternalArtifact(validationContext.getCruiseConfig(), pipelineConfig);
        if (specifiedExternalArtifact.hasValidPluginAndStore(validationContext)) {
            try {
                ValidationResult validationResult = artifactExtension.validateFetchArtifactConfig(specifiedExternalArtifact.getArtifactStore().getPluginId(), preprocessedFetchPluggableArtifactTask.getConfiguration().getConfigurationAsMap(true));
                mapErrorsToConfiguration(validationResult, preprocessedFetchPluggableArtifactTask.getConfiguration(), preprocessedFetchPluggableArtifactTask);

            } catch (PluginNotFoundException e) {
                preprocessedFetchPluggableArtifactTask.addError("pluginId", String.format("Plugin with id `%s` is not found.", specifiedExternalArtifact.getArtifactStore().getPluginId()));
            }
        }
        else {
            preprocessedFetchPluggableArtifactTask.addError("pluginId", "Could not determine the plugin to perform the plugin validations. Usually this happens when the plugin is not installed.");
        }
    }

    private void mapErrorsToConfiguration(ValidationResult result, Configuration configuration, Validatable validatableConfig) {
        for (ValidationError validationError : result.getErrors()) {
            ConfigurationProperty property = configuration.getProperty(validationError.getKey());

            if (property == null) {
                validatableConfig.addError(validationError.getKey(), validationError.getMessage());
            } else {
                property.addError(validationError.getKey(), validationError.getMessage());
            }
        }
    }
}
