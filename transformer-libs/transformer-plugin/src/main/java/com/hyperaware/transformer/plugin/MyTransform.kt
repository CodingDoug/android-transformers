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

import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformInvocation
import com.android.build.gradle.AppExtension
import org.gradle.api.Project
import org.gradle.api.logging.Logging

class MyTransform(private val project: Project) : Transform() {

    private val logger = Logging.getLogger(MyTransform::class.java)

    override fun getName(): String {
        return MyTransform::class.java.simpleName
    }

    // This transform is interested in classes only (and not resources)
    private val typeClasses = setOf(QualifiedContent.DefaultContentType.CLASSES)

    override fun getInputTypes(): Set<QualifiedContent.ContentType> {
        return typeClasses
    }


    // This transform is interested in classes from all parts of the app
    private val scopes = setOf(
        QualifiedContent.Scope.PROJECT,
        QualifiedContent.Scope.SUB_PROJECTS,
        QualifiedContent.Scope.EXTERNAL_LIBRARIES
    )

    override fun getScopes(): MutableSet<in QualifiedContent.Scope> {
        return scopes.toMutableSet()
    }


    // This transform can handle incremental builds
    override fun isIncremental(): Boolean {
        return true
    }


    override fun transform(transformInvocation: TransformInvocation) {
        val start = System.currentTimeMillis()

        // Find the Gradle extension that contains configuration for this Transform
        val ext = project.extensions.findByType(MyExtension::class.java) ?: MyExtension()
        logger.debug("config logVisits ${ext.logVisits}")
        logger.debug("config logInstrumentation ${ext.logInstrumentation}")

        val appExtension = project.extensions.findByName("android") as AppExtension
        val ignores = listOf(
            // Don't instrument the companion library
            Regex("com/hyperaware/transformer/.*")
        )
        val config = TransformConfig(transformInvocation, appExtension.bootClasspath, ignores, ext)

        MyTransformImpl(config).doIt()
        val end = System.currentTimeMillis()
        logger.info("Transform took ${end-start}ms")
    }

}
