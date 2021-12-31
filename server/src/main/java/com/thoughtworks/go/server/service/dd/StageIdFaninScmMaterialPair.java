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
package com.thoughtworks.go.server.service.dd;

import com.thoughtworks.go.domain.StageIdentifier;

public class StageIdFaninScmMaterialPair {
    StageIdentifier stageIdentifier;
    FaninScmMaterial faninScmMaterial;

    public StageIdFaninScmMaterialPair(StageIdentifier stageIdentifier, FaninScmMaterial faninScmMaterial) {
        this.stageIdentifier = stageIdentifier;
        this.faninScmMaterial = faninScmMaterial;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        StageIdFaninScmMaterialPair that = (StageIdFaninScmMaterialPair) o;

        if (faninScmMaterial != null ? !faninScmMaterial.equals(that.faninScmMaterial) : that.faninScmMaterial != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return faninScmMaterial != null ? faninScmMaterial.hashCode() : 0;
    }
}
