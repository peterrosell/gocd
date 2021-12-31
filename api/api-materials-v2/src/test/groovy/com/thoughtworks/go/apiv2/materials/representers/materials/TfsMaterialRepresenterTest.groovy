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
package com.thoughtworks.go.apiv2.materials.representers.materials


import com.thoughtworks.go.config.BasicCruiseConfig
import com.thoughtworks.go.config.PipelineConfig
import com.thoughtworks.go.config.PipelineConfigSaveValidationContext
import com.thoughtworks.go.config.materials.MaterialConfigs
import com.thoughtworks.go.config.materials.tfs.TfsMaterialConfig
import com.thoughtworks.go.helper.MaterialConfigsMother
import com.thoughtworks.go.security.GoCipher
import com.thoughtworks.go.util.command.HgUrlArgument

import static com.thoughtworks.go.helper.MaterialConfigsMother.tfs

class TfsMaterialRepresenterTest implements MaterialRepresenterTrait<TfsMaterialConfig> {
  TfsMaterialConfig existingMaterial() {
    MaterialConfigsMother.tfsMaterialConfig()
  }

  TfsMaterialConfig existingMaterialWithErrors() {
    def tfsConfig = tfs(new GoCipher(), new HgUrlArgument(''), '', '', '', '/some-path/')
    def materialConfigs = new MaterialConfigs(tfsConfig)
    materialConfigs.validateTree(PipelineConfigSaveValidationContext.forChain(true, "group", new BasicCruiseConfig(), new PipelineConfig()))
    return materialConfigs.first() as TfsMaterialConfig
  }

  def materialHash() {
    [
      type       : 'tfs',
      fingerprint: existingMaterial().fingerprint,
      attributes : [
        url               : "http://10.4.4.101:8080/tfs/Sample",
        destination       : "dest-folder",
        filter            : [
          ignore: ['**/*.html', '**/foobar/']
        ],
        invert_filter     : false,
        domain            : "some_domain",
        username          : "loser",
        encrypted_password: new GoCipher().encrypt("passwd"),
        project_path      : "walk_this_path",
        name              : "tfs-material",
        auto_update       : true
      ]
    ]
  }


  def expectedMaterialHashWithErrors() {
    [
      type       : "tfs",
      fingerprint: existingMaterialWithErrors().fingerprint,
      attributes : [
        url          : "",
        destination  : null,
        filter       : null,
        invert_filter: false,
        name         : null,
        auto_update  : true,
        domain       : "",
        username     : "",
        project_path : "/some-path/"
      ],
      errors     :
        [
          url     : ["URL cannot be blank"],
          username: ["Username cannot be blank"]
        ]
    ]
  }
}
