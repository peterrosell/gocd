<?xml version="1.0"?>
<!--
  ~ Copyright 2022 ThoughtWorks, Inc.
  ~
  ~ Licensed under the Apache License, Version 2.0 (the "License");
  ~ you may not use this file except in compliance with the License.
  ~ You may obtain a copy of the License at
  ~
  ~     http://www.apache.org/licenses/LICENSE-2.0
  ~
  ~ Unless required by applicable law or agreed to in writing, software
  ~ distributed under the License is distributed on an "AS IS" BASIS,
  ~ WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  ~ See the License for the specific language governing permissions and
  ~ limitations under the License.
  -->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:migratorClass="com.thoughtworks.go.domain.label.PipelineLabel"
                version="1.0">
    <xsl:template match="/cruise/@schemaVersion">
        <xsl:attribute name="schemaVersion">135</xsl:attribute>
    </xsl:template>
    <!-- Copy everything -->
    <xsl:template match="@*|node()">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()"/>
        </xsl:copy>
    </xsl:template>

    <xsl:template name="migratePipelineLabel">
        <xsl:param name="templateValue"/>
        <xsl:value-of select="migratorClass:migratePipelineLabelTemplate($templateValue)"/>
    </xsl:template>

    <xsl:template match="//pipeline/@labeltemplate">
        <xsl:attribute name="{name(.)}">
            <xsl:call-template name="migratePipelineLabel">
                <xsl:with-param name="templateValue" select="."/>
            </xsl:call-template>
        </xsl:attribute>
    </xsl:template>
</xsl:stylesheet>
