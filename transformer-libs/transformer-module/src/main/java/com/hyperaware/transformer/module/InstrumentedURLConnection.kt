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

package com.hyperaware.transformer.module

import java.io.InputStream
import java.io.OutputStream
import java.net.URL
import java.security.Permission
import java.security.Principal
import java.security.cert.Certificate
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLSocketFactory

/**
 * Combined API of Http and HttpsURLConnection. Used for automatic delegation
 * in Kotlin.
 */

interface InstrumentedURLConnection {

    fun getContentEncoding(): String

    fun getHeaderField(name: String?): String

    fun getReadTimeout(): Int

    fun connect()

    fun getUseCaches(): Boolean

    fun setConnectTimeout(timeout: Int)

    fun getDate(): Long

    fun getExpiration(): Long

    fun getContent(): Any

    fun getContent(classes: Array<out Class<Any>>?): Any

    fun getContentLengthLong(): Long

    fun getHeaderFieldInt(name: String?, Default: Int): Int

    fun setUseCaches(usecaches: Boolean)

    fun getIfModifiedSince(): Long

    fun setIfModifiedSince(ifmodifiedsince: Long)

    fun getDoInput(): Boolean

    fun getLastModified(): Long

    fun setDefaultUseCaches(defaultusecaches: Boolean)

    fun setDoOutput(dooutput: Boolean)

    fun getDefaultUseCaches(): Boolean

    fun getRequestProperties(): MutableMap<String, MutableList<String>>

    fun setReadTimeout(timeout: Int)

    fun getDoOutput(): Boolean

    fun addRequestProperty(key: String?, value: String?)

    fun getConnectTimeout(): Int

    fun setDoInput(doinput: Boolean)

    fun getHeaderFields(): MutableMap<String, MutableList<String>>

    fun getInputStream(): InputStream

    fun getAllowUserInteraction(): Boolean

    fun getURL(): URL

    fun setRequestProperty(key: String?, value: String?)

    fun setAllowUserInteraction(allowuserinteraction: Boolean)

    fun getContentLength(): Int

    fun getContentType(): String

    fun getRequestProperty(key: String?): String

    fun getOutputStream(): OutputStream

    fun getHeaderFieldLong(name: String?, Default: Long): Long

    fun getHeaderField(n: Int): String

    fun usingProxy(): Boolean

    fun getHeaderFieldKey(n: Int): String

    fun setInstanceFollowRedirects(followRedirects: Boolean)

    fun getHeaderFieldDate(name: String?, Default: Long): Long

    fun setChunkedStreamingMode(chunklen: Int)

    fun getPermission(): Permission

    fun getInstanceFollowRedirects(): Boolean

    fun getRequestMethod(): String

    fun getErrorStream(): InputStream

    fun getResponseMessage(): String

    fun setFixedLengthStreamingMode(contentLength: Int)

    fun setFixedLengthStreamingMode(contentLength: Long)

    fun disconnect()

    fun setRequestMethod(method: String?)

    fun getResponseCode(): Int

    // For HTTPS

    fun getLocalPrincipal(): Principal

    fun getHostnameVerifier(): HostnameVerifier

    fun getServerCertificates(): Array<Certificate>

    fun setHostnameVerifier(v: HostnameVerifier?)

    fun setSSLSocketFactory(sf: SSLSocketFactory?)

    fun getPeerPrincipal(): Principal

    fun getCipherSuite(): String

    fun getLocalCertificates(): Array<Certificate>

    fun getSSLSocketFactory(): SSLSocketFactory

}
