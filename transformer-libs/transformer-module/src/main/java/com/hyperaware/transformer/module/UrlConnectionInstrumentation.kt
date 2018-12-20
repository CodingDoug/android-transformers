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

import android.annotation.TargetApi
import android.os.Build
import android.util.Log
import java.io.InputStream
import java.io.OutputStream
import java.lang.RuntimeException
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLConnection
import java.security.Permission
import java.security.Principal
import java.security.cert.Certificate
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLSocketFactory

class UrlConnectionInstrumentation {

    companion object {

        @JvmStatic
        fun openConnection(url: URL): URLConnection {
            Log.d("@@@@@", "Fetching $url via openConnection")
            val conn = url.openConnection()
            return when (conn) {
                is HttpsURLConnection ->
                    InstrumentedHttpsURLConnection(url, conn)
                is HttpURLConnection ->
                    InstrumentedHttpURLConnection(url, conn)
                else ->
                    conn
            }
        }

        // Should also provide these methods to cover all possible uses of
        // java.net.URL to kick off an HTTP request.

//        @JvmStatic
//        fun openStream(url: URL): InputStream
//        @JvmStatic
//        fun openConnection(url: URL, proxy: Proxy): URLConnection
//        @JvmStatic
//        fun getContent(url: URL): IOException
//        @JvmStatic
//        fun getContent(url: URL, classes: Array<Class<Any>>): IOException

    }

}

// The following two classes:
// - InstrumentedHttpURLConnection
// - InstrumentedHttpsURLConnection
//
// Do the following:
// - Decorate a Http(s)URLConnection
// - Delegate all API methods to an implementation class that measures
//   and records the transaction, and further delegates the decorated object
// - Subclass Http(s)URLConnection so they gain private implementation details
//   and be downcast as an opaque replacement for the decorated object

@Suppress("DELEGATED_MEMBER_HIDES_SUPERTYPE_OVERRIDE")
private open class InstrumentedHttpURLConnection(url: URL, urlc: HttpURLConnection)
    : InstrumentedURLConnection by InstrumentedHttpURLConnectionImpl(urlc), HttpURLConnection(url)

@Suppress("DELEGATED_MEMBER_HIDES_SUPERTYPE_OVERRIDE")
private class InstrumentedHttpsURLConnection(url: URL, private val urlc: HttpsURLConnection)
    : InstrumentedURLConnection by InstrumentedHttpsURLConnectionImpl(urlc), HttpsURLConnection(url)


// The following two classes:
// - InstrumentedHttpURLConnectionImpl
// - InstrumentedHttpsURLConnectionImpl
// implement the universal interface for all HTTPS and HTTP requests.
// They override all exposed API methods, measures and records the transaction, and
// delegate to an actual Http(s)URLConnection object that does the real work.

private open class InstrumentedHttpURLConnectionImpl(private val urlConn: HttpURLConnection)
    : InstrumentedURLConnection {

    override fun getContentEncoding(): String {
        return urlConn.contentEncoding
    }

    override fun getHeaderField(name: String?): String {
        return urlConn.getHeaderField(name)
    }

    override fun getReadTimeout(): Int {
        return urlConn.readTimeout
    }

    override fun connect() {
        urlConn.connect()
    }

    override fun getUseCaches(): Boolean {
        return urlConn.useCaches
    }

    override fun setConnectTimeout(timeout: Int) {
        urlConn.connectTimeout = timeout
    }

    override fun getDate(): Long {
        return urlConn.date
    }

    override fun getExpiration(): Long {
        return urlConn.expiration
    }

    override fun getContent(): Any {
        return urlConn.content
    }

    override fun getContent(classes: Array<out Class<Any>>?): Any {
        return urlConn.getContent(classes)
    }

    @TargetApi(Build.VERSION_CODES.N)
    override fun getContentLengthLong(): Long {
        return urlConn.contentLengthLong
    }

    override fun getHeaderFieldInt(name: String?, Default: Int): Int {
        return urlConn.getHeaderFieldInt(name, Default)
    }

    override fun setUseCaches(usecaches: Boolean) {
        urlConn.useCaches = usecaches
    }

    override fun getIfModifiedSince(): Long {
        return urlConn.ifModifiedSince
    }

    override fun setIfModifiedSince(ifmodifiedsince: Long) {
        return setIfModifiedSince(ifmodifiedsince)
    }

    override fun getDoInput(): Boolean {
        return urlConn.doInput
    }

    override fun getLastModified(): Long {
        return urlConn.lastModified
    }

    override fun setDefaultUseCaches(defaultusecaches: Boolean) {
        urlConn.defaultUseCaches = defaultusecaches
    }

    override fun setDoOutput(dooutput: Boolean) {
        urlConn.doOutput = dooutput
    }

    override fun getDefaultUseCaches(): Boolean {
        return urlConn.defaultUseCaches
    }

    override fun getRequestProperties(): MutableMap<String, MutableList<String>> {
        return urlConn.requestProperties
    }

    override fun setReadTimeout(timeout: Int) {
        urlConn.readTimeout = timeout
    }

    override fun getDoOutput(): Boolean {
        return urlConn.doOutput
    }

    override fun addRequestProperty(key: String?, value: String?) {
        urlConn.addRequestProperty(key, value)
    }

    override fun getConnectTimeout(): Int {
        return urlConn.connectTimeout
    }

    override fun setDoInput(doinput: Boolean) {
        urlConn.doInput = doinput
    }

    override fun getHeaderFields(): MutableMap<String, MutableList<String>> {
        return urlConn.headerFields
    }

    override fun getInputStream(): InputStream {
        Log.d("@@@@@", "You're using an instrumented stream.")
        return InstrumentedInputStream(urlConn.inputStream)
    }

    override fun getAllowUserInteraction(): Boolean {
        return urlConn.allowUserInteraction
    }

    override fun getURL(): URL {
        return urlConn.url
    }

    override fun setRequestProperty(key: String?, value: String?) {
        urlConn.setRequestProperty(key, value)
    }

    override fun setAllowUserInteraction(allowuserinteraction: Boolean) {
        urlConn.allowUserInteraction
    }

    override fun getContentLength(): Int {
        return urlConn.contentLength
    }

    override fun getContentType(): String {
        return urlConn.contentType
    }

    override fun getRequestProperty(key: String?): String {
        return urlConn.getRequestProperty(key)
    }

    override fun getOutputStream(): OutputStream {
        return urlConn.outputStream
    }

    @TargetApi(Build.VERSION_CODES.N)
    override fun getHeaderFieldLong(name: String?, Default: Long): Long {
        return urlConn.getHeaderFieldLong(name, Default)
    }

    override fun getHeaderField(n: Int): String {
        return urlConn.getHeaderField(n)
    }

    override fun usingProxy(): Boolean {
        return urlConn.usingProxy()
    }

    override fun getHeaderFieldKey(n: Int): String {
        return urlConn.getHeaderFieldKey(n)
    }

    override fun setInstanceFollowRedirects(followRedirects: Boolean) {
        urlConn.instanceFollowRedirects = followRedirects
    }

    override fun getHeaderFieldDate(name: String?, Default: Long): Long {
        return urlConn.getHeaderFieldDate(name, Default)
    }

    override fun setChunkedStreamingMode(chunklen: Int) {
        urlConn.setChunkedStreamingMode(chunklen)
    }

    override fun getPermission(): Permission {
        return urlConn.permission
    }

    override fun getInstanceFollowRedirects(): Boolean {
        return urlConn.instanceFollowRedirects
    }

    override fun getRequestMethod(): String {
        return urlConn.requestMethod
    }

    override fun getErrorStream(): InputStream {
        return urlConn.errorStream
    }

    override fun getResponseMessage(): String {
        return urlConn.responseMessage
    }

    override fun setFixedLengthStreamingMode(contentLength: Int) {
        return urlConn.setFixedLengthStreamingMode(contentLength)
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    override fun setFixedLengthStreamingMode(contentLength: Long) {
        return urlConn.setFixedLengthStreamingMode(contentLength)
    }

    override fun disconnect() {
        urlConn.disconnect()
    }

    override fun setRequestMethod(method: String?) {
        urlConn.requestMethod = method
    }

    override fun getResponseCode(): Int {
        return urlConn.responseCode
    }

    // Http connections won't ever do anything with HttpsUrlConnection methods

    override fun getLocalPrincipal(): Principal {
        throw newNotAnHttpsException()
    }

    override fun getHostnameVerifier(): HostnameVerifier {
        throw newNotAnHttpsException()
    }

    override fun getServerCertificates(): Array<Certificate> {
        throw newNotAnHttpsException()
    }

    override fun setHostnameVerifier(v: HostnameVerifier?) {
        throw newNotAnHttpsException()
    }

    override fun setSSLSocketFactory(sf: SSLSocketFactory?) {
        throw newNotAnHttpsException()
    }

    override fun getPeerPrincipal(): Principal {
        throw newNotAnHttpsException()
    }

    override fun getCipherSuite(): String {
        throw newNotAnHttpsException()
    }

    override fun getLocalCertificates(): Array<Certificate> {
        throw newNotAnHttpsException()
    }

    override fun getSSLSocketFactory(): SSLSocketFactory {
        throw newNotAnHttpsException()
    }

    private fun newNotAnHttpsException(): RuntimeException {
        throw RuntimeException("This is not an HTTPS connection")
    }

}


// Since HttpsURLConnection just adds a few extra methods to HttpURLConnection,
// we'll just subclass the above implementation and delegate only the added
// methods.  These methods aren't interesting for the purpose of performance
// monitoring, so it's just pure delegation.

private class InstrumentedHttpsURLConnectionImpl(private val urlConn: HttpsURLConnection)
    : InstrumentedHttpURLConnectionImpl(urlConn) {

    override fun getLocalPrincipal(): Principal {
        return urlConn.localPrincipal
    }

    override fun getHostnameVerifier(): HostnameVerifier {
        return urlConn.hostnameVerifier
    }

    override fun getServerCertificates(): Array<Certificate> {
        return urlConn.serverCertificates
    }

    override fun setHostnameVerifier(v: HostnameVerifier?) {
        urlConn.hostnameVerifier = v
    }

    override fun setSSLSocketFactory(sf: SSLSocketFactory?) {
        urlConn.sslSocketFactory = sf
    }

    override fun getPeerPrincipal(): Principal {
        return urlConn.peerPrincipal
    }

    override fun getCipherSuite(): String {
        return urlConn.cipherSuite
    }

    override fun getLocalCertificates(): Array<Certificate> {
        return urlConn.localCertificates
    }

    override fun getSSLSocketFactory(): SSLSocketFactory {
        return urlConn.sslSocketFactory
    }

}


// InstrumentedInputStream decorates an InputStream, and monitors its behavior.

private class InstrumentedInputStream(private val stream: InputStream) : InputStream() {

    override fun read(): Int {
        return stream.read()
    }

    override fun read(b: ByteArray?): Int {
        val bytesRead = super.read(b)
        Log.d("@@@@@", "It just read $bytesRead bytes into an array")
        return bytesRead
    }

    override fun read(b: ByteArray?, off: Int, len: Int): Int {
        return stream.read(b, off, len)
    }

    override fun skip(n: Long): Long {
        return stream.skip(n)
    }

    override fun available(): Int {
        return stream.available()
    }

    override fun reset() {
        stream.reset()
    }

    override fun close() {
        stream.close()
    }

    override fun mark(readlimit: Int) {
        stream.mark(readlimit)
    }

    override fun markSupported(): Boolean {
        return stream.markSupported()
    }

}



// LEGACY
// This wrapper class needs to override ALL exposed API methods and delegate
// to the given HttpsURLConnection given in the constructor.

//private open class InstrumentedHttpsUrlConnection(url: URL, private val urlc: HttpsURLConnection) : HttpsURLConnection(url) {
//    override fun getContentEncoding(): String {
//        return urlc.contentEncoding
//    }
//
//    override fun getHeaderField(name: String?): String {
//        return urlc.getHeaderField(name)
//    }
//
//    override fun getReadTimeout(): Int {
//        return urlc.readTimeout
//    }
//
//    override fun connect() {
//        urlc.connect()
//    }
//
//    override fun getUseCaches(): Boolean {
//        return urlc.useCaches
//    }
//
//    override fun setConnectTimeout(timeout: Int) {
//        urlc.connectTimeout = timeout
//    }
//
//    override fun getDate(): Long {
//        return urlc.date
//    }
//
//    override fun getExpiration(): Long {
//        return urlc.expiration
//    }
//
//    override fun getContent(): Any {
//        return urlc.content
//    }
//
//    override fun getContent(classes: Array<out Class<Any>>?): Any {
//        return urlc.getContent(classes)
//    }
//
//    @TargetApi(Build.VERSION_CODES.N)
//    override fun getContentLengthLong(): Long {
//        return urlc.contentLengthLong
//    }
//
//    override fun getHeaderFieldInt(name: String?, Default: Int): Int {
//        return urlc.getHeaderFieldInt(name, Default)
//    }
//
//    override fun setUseCaches(usecaches: Boolean) {
//        urlc.useCaches = usecaches
//    }
//
//    override fun getIfModifiedSince(): Long {
//        return urlc.ifModifiedSince
//    }
//
//    override fun toString(): String {
//        return urlc.toString()
//    }
//
//    override fun setIfModifiedSince(ifmodifiedsince: Long) {
//        urlc.ifModifiedSince = ifmodifiedsince
//    }
//
//    override fun getDoInput(): Boolean {
//        return urlc.doInput
//    }
//
//    override fun getLastModified(): Long {
//        return urlc.lastModified
//    }
//
//    override fun setDefaultUseCaches(defaultusecaches: Boolean) {
//        urlc.defaultUseCaches = defaultusecaches
//    }
//
//    override fun setDoOutput(dooutput: Boolean) {
//        urlc.doOutput = dooutput
//    }
//
//    override fun getDefaultUseCaches(): Boolean {
//        return urlc.defaultUseCaches
//    }
//
//    override fun getRequestProperties(): MutableMap<String, MutableList<String>> {
//        return urlc.requestProperties
//    }
//
//    override fun setReadTimeout(timeout: Int) {
//        urlc.readTimeout = timeout
//    }
//
//    override fun getDoOutput(): Boolean {
//        return urlc.doOutput
//    }
//
//    override fun addRequestProperty(key: String?, value: String?) {
//        urlc.addRequestProperty(key, value)
//    }
//
//    override fun getConnectTimeout(): Int {
//        return urlc.connectTimeout
//    }
//
//    override fun setDoInput(doinput: Boolean) {
//        urlc.doInput = doinput
//    }
//
//    override fun getHeaderFields(): MutableMap<String, MutableList<String>> {
//        return urlc.headerFields
//    }
//
//    override fun getInputStream(): InputStream {
//        return urlc.inputStream
//    }
//
//    override fun getAllowUserInteraction(): Boolean {
//        return urlc.allowUserInteraction
//    }
//
//    override fun getURL(): URL {
//        return urlc.url
//    }
//
//    override fun setRequestProperty(key: String?, value: String?) {
//        urlc.setRequestProperty(key, value)
//    }
//
//    override fun setAllowUserInteraction(allowuserinteraction: Boolean) {
//        urlc.allowUserInteraction = allowuserinteraction
//    }
//
//    override fun getContentLength(): Int {
//        return urlc.contentLength
//    }
//
//    override fun getContentType(): String {
//        return urlc.contentType
//    }
//
//    override fun getRequestProperty(key: String?): String {
//        return urlc.getRequestProperty(key)
//    }
//
//    override fun getOutputStream(): OutputStream {
//        return urlc.outputStream
//    }
//
//    @TargetApi(Build.VERSION_CODES.N)
//    override fun getHeaderFieldLong(name: String?, Default: Long): Long {
//        return urlc.getHeaderFieldLong(name, Default)
//    }
//
//    override fun getHeaderField(n: Int): String {
//        return urlc.getHeaderField(n)
//    }
//
//    override fun usingProxy(): Boolean {
//        return urlc.usingProxy()
//    }
//
//    override fun getHeaderFieldKey(n: Int): String {
//        return urlc.getHeaderFieldKey(n)
//    }
//
//    override fun setInstanceFollowRedirects(followRedirects: Boolean) {
//        urlc.instanceFollowRedirects = followRedirects
//    }
//
//    override fun getHeaderFieldDate(name: String?, Default: Long): Long {
//        return urlc.getHeaderFieldDate(name, Default)
//    }
//
//    override fun setChunkedStreamingMode(chunklen: Int) {
//        urlc.setChunkedStreamingMode(chunklen)
//    }
//
//    override fun getPermission(): Permission {
//        return urlc.permission
//    }
//
//    override fun getInstanceFollowRedirects(): Boolean {
//        return urlc.instanceFollowRedirects
//    }
//
//    override fun getRequestMethod(): String {
//        return urlc.requestMethod
//    }
//
//    override fun getErrorStream(): InputStream {
//        return urlc.errorStream
//    }
//
//    override fun getResponseMessage(): String {
//        return urlc.responseMessage
//    }
//
//    override fun setFixedLengthStreamingMode(contentLength: Int) {
//        urlc.setFixedLengthStreamingMode(contentLength)
//    }
//
//    @TargetApi(Build.VERSION_CODES.KITKAT)
//    override fun setFixedLengthStreamingMode(contentLength: Long) {
//        urlc.setFixedLengthStreamingMode(contentLength)
//    }
//
//    override fun disconnect() {
//        urlc.disconnect()
//    }
//
//    override fun setRequestMethod(method: String?) {
//        urlc.requestMethod = method
//    }
//
//    override fun getResponseCode(): Int {
//        return urlc.responseCode
//    }
//
//    override fun getLocalPrincipal(): Principal {
//        return urlc.localPrincipal
//    }
//
//    override fun getHostnameVerifier(): HostnameVerifier {
//        return urlc.hostnameVerifier
//    }
//
//    override fun getServerCertificates(): Array<Certificate> {
//        return urlc.serverCertificates
//    }
//
//    override fun setHostnameVerifier(v: HostnameVerifier?) {
//        urlc.hostnameVerifier = v
//    }
//
//    override fun setSSLSocketFactory(sf: SSLSocketFactory?) {
//        urlc.sslSocketFactory = sf
//    }
//
//    override fun getPeerPrincipal(): Principal {
//        return urlc.peerPrincipal
//    }
//
//    override fun getCipherSuite(): String {
//        return urlc.cipherSuite
//    }
//
//    override fun getLocalCertificates(): Array<Certificate> {
//        return urlc.localCertificates
//    }
//
//    override fun getSSLSocketFactory(): SSLSocketFactory {
//        return urlc.sslSocketFactory
//    }
//
//    override fun equals(other: Any?): Boolean {
//        return urlc == other
//    }
//
//    override fun hashCode(): Int {
//        return urlc.hashCode()
//    }
//
//}

// This class should override and delegate all methods just like above.
// Right now it's just doing the minimum to compile.

//private open class InstrumentedHttpUrlConnection(url: URL, private val urlc: HttpURLConnection) : HttpURLConnection(url) {
//    override fun getContentEncoding(): String {
//        return super.getContentEncoding()
//    }
//
//    override fun getHeaderField(name: String?): String {
//        return super.getHeaderField(name)
//    }
//
//    override fun getReadTimeout(): Int {
//        return super.getReadTimeout()
//    }
//
//    override fun connect() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//    }
//
//    override fun getUseCaches(): Boolean {
//        return super.getUseCaches()
//    }
//
//    override fun setConnectTimeout(timeout: Int) {
//        super.setConnectTimeout(timeout)
//    }
//
//    override fun getDate(): Long {
//        return super.getDate()
//    }
//
//    override fun getExpiration(): Long {
//        return super.getExpiration()
//    }
//
//    override fun getContent(): Any {
//        return super.getContent()
//    }
//
//    override fun getContent(classes: Array<out Class<Any>>?): Any {
//        return super.getContent(classes)
//    }
//
//    override fun getContentLengthLong(): Long {
//        return super.getContentLengthLong()
//    }
//
//    override fun getHeaderFieldInt(name: String?, Default: Int): Int {
//        return super.getHeaderFieldInt(name, Default)
//    }
//
//    override fun setUseCaches(usecaches: Boolean) {
//        super.setUseCaches(usecaches)
//    }
//
//    override fun getIfModifiedSince(): Long {
//        return super.getIfModifiedSince()
//    }
//
//    override fun toString(): String {
//        return super.toString()
//    }
//
//    override fun setIfModifiedSince(ifmodifiedsince: Long) {
//        super.setIfModifiedSince(ifmodifiedsince)
//    }
//
//    override fun getDoInput(): Boolean {
//        return super.getDoInput()
//    }
//
//    override fun getLastModified(): Long {
//        return super.getLastModified()
//    }
//
//    override fun setDefaultUseCaches(defaultusecaches: Boolean) {
//        super.setDefaultUseCaches(defaultusecaches)
//    }
//
//    override fun setDoOutput(dooutput: Boolean) {
//        super.setDoOutput(dooutput)
//    }
//
//    override fun getDefaultUseCaches(): Boolean {
//        return super.getDefaultUseCaches()
//    }
//
//    override fun getRequestProperties(): MutableMap<String, MutableList<String>> {
//        return super.getRequestProperties()
//    }
//
//    override fun setReadTimeout(timeout: Int) {
//        super.setReadTimeout(timeout)
//    }
//
//    override fun getDoOutput(): Boolean {
//        return super.getDoOutput()
//    }
//
//    override fun addRequestProperty(key: String?, value: String?) {
//        super.addRequestProperty(key, value)
//    }
//
//    override fun getConnectTimeout(): Int {
//        return super.getConnectTimeout()
//    }
//
//    override fun setDoInput(doinput: Boolean) {
//        super.setDoInput(doinput)
//    }
//
//    override fun getHeaderFields(): MutableMap<String, MutableList<String>> {
//        return super.getHeaderFields()
//    }
//
//    override fun getInputStream(): InputStream {
//        return super.getInputStream()
//    }
//
//    override fun getAllowUserInteraction(): Boolean {
//        return super.getAllowUserInteraction()
//    }
//
//    override fun getURL(): URL {
//        return super.getURL()
//    }
//
//    override fun setRequestProperty(key: String?, value: String?) {
//        super.setRequestProperty(key, value)
//    }
//
//    override fun setAllowUserInteraction(allowuserinteraction: Boolean) {
//        super.setAllowUserInteraction(allowuserinteraction)
//    }
//
//    override fun getContentLength(): Int {
//        return super.getContentLength()
//    }
//
//    override fun getContentType(): String {
//        return super.getContentType()
//    }
//
//    override fun getRequestProperty(key: String?): String {
//        return super.getRequestProperty(key)
//    }
//
//    override fun getOutputStream(): OutputStream {
//        return super.getOutputStream()
//    }
//
//    override fun getHeaderFieldLong(name: String?, Default: Long): Long {
//        return super.getHeaderFieldLong(name, Default)
//    }
//
//    override fun getHeaderField(n: Int): String {
//        return super.getHeaderField(n)
//    }
//
//    override fun usingProxy(): Boolean {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//    }
//
//    override fun getHeaderFieldKey(n: Int): String {
//        return super.getHeaderFieldKey(n)
//    }
//
//    override fun setInstanceFollowRedirects(followRedirects: Boolean) {
//        super.setInstanceFollowRedirects(followRedirects)
//    }
//
//    override fun getHeaderFieldDate(name: String?, Default: Long): Long {
//        return super.getHeaderFieldDate(name, Default)
//    }
//
//    override fun setChunkedStreamingMode(chunklen: Int) {
//        super.setChunkedStreamingMode(chunklen)
//    }
//
//    override fun getPermission(): Permission {
//        return super.getPermission()
//    }
//
//    override fun getInstanceFollowRedirects(): Boolean {
//        return super.getInstanceFollowRedirects()
//    }
//
//    override fun getRequestMethod(): String {
//        return super.getRequestMethod()
//    }
//
//    override fun getErrorStream(): InputStream {
//        return super.getErrorStream()
//    }
//
//    override fun getResponseMessage(): String {
//        return super.getResponseMessage()
//    }
//
//    override fun setFixedLengthStreamingMode(contentLength: Int) {
//        super.setFixedLengthStreamingMode(contentLength)
//    }
//
//    override fun setFixedLengthStreamingMode(contentLength: Long) {
//        super.setFixedLengthStreamingMode(contentLength)
//    }
//
//    override fun disconnect() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//    }
//
//    override fun setRequestMethod(method: String?) {
//        super.setRequestMethod(method)
//    }
//
//    override fun getResponseCode(): Int {
//        return super.getResponseCode()
//    }
//
//    override fun equals(other: Any?): Boolean {
//        return super.equals(other)
//    }
//
//    override fun hashCode(): Int {
//        return super.hashCode()
//    }
//
//}
