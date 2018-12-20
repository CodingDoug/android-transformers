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

package com.hyperaware.transformer.plugin.asm

import com.hyperaware.transformer.plugin.InstrumentationConfig
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import java.net.URLClassLoader

class ClassInstrumenter(private val config: InstrumentationConfig) {

    private val cl = URLClassLoader(config.runtimeClasspath.toTypedArray())

    fun instrument(input: ByteArray): ByteArray {
        val cr = ClassReader(input)

        // Custom ClassWriter needs to specify a ClassLoader that knows
        // about all classes in the app.
        val cw = object : ClassWriter(ClassWriter.COMPUTE_MAXS or ClassWriter.COMPUTE_FRAMES) {
            override fun getClassLoader(): ClassLoader = cl
        }

        // Our InstrumentationVisitor wraps the ClassWriter to intercept and
        // change bytecode as class elements are being visited.
        val cv = InstrumentationVisitor(cw, config)
        cr.accept(cv, ClassReader.SKIP_FRAMES)

        return cw.toByteArray()
    }

}
