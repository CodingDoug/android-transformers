/*
 * Copyright 2019 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hyperaware.transformer.plugin

import com.android.build.gradle.AppExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.logging.LogLevel

class MyPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        project.logger.log(LogLevel.INFO, "MyPlugin applied")
        // Check to see if this is an android project
        val ext = project.extensions.findByName("android")
        if (ext != null && ext is AppExtension) {
            project.logger.log(LogLevel.INFO, "Registering transform")
            // Register our class transform
            ext.registerTransform(MyTransform(project))
            // Add an extension for gradle configuration
            project.extensions.create("transform", MyExtension::class.java)
        }
        else {
            throw Exception("${MyPlugin::class.java.name} plugin may only be applied to Android app projects")
        }
    }

}
