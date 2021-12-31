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
package com.thoughtworks.go.validation;

import com.thoughtworks.go.domain.materials.ValidationBean;
import org.junit.jupiter.api.Test;

import static com.thoughtworks.go.validation.PipelineGroupValidator.ERRORR_MESSAGE;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class PipelineGroupValidatorTest {

    @Test
    public void shouldRejectEmptyPipelineGroup() {
        ValidationBean validationBean = Validator.PIPELINEGROUP.validate(null);
        assertThat(validationBean.isValid(), is(true));
    }

    @Test
    public void shouldValidateNormalPipelineGroupName() {
        ValidationBean validationBean = Validator.PIPELINEGROUP.validate("pipelineGroupName");
        assertThat(validationBean.isValid(), is(true));
    }

    @Test
    public void shouldRejectWhiteSpace() {
        ValidationBean validationBean = Validator.PIPELINEGROUP.validate("pipeline GroupName");
        assertThat(validationBean.isValid(), is(false));
        assertThat(validationBean.getError(),
                is(ERRORR_MESSAGE));
    }

    @Test
    public void shouldRejectAmpersand() {
        ValidationBean validationBean = Validator.PIPELINEGROUP.validate("pipeline& GroupName");
        assertThat(validationBean.isValid(), is(false));
        assertThat(validationBean.getError(),
                is(ERRORR_MESSAGE));
    }

}
