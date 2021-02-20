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

package com.hyperaware.transformers

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import java.net.URL
import java.nio.charset.Charset
import javax.net.ssl.HttpsURLConnection

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Thread(MyRunnable()).start()
    }

}

class MyRunnable : Runnable {

    override fun run() {
        val url = URL("https://www.google.com")
        val array = ByteArray(15)
        val conn = url.openConnection()

        // When the connection is instrumented, we'll see the decorator class
        // here.
        //
        Log.d("@@@@@", "URLConnection: $conn")

        // The decorated class also returns a decorated InputStream, which logs
        // a message to make itself known here.
        //
        conn.getInputStream().use {
            // Since the decorator subclasses the appropriate expected type, it
            // can still be cast as expected in order to call special methods
            // for HTTPS connections.
            //
            if (conn is HttpsURLConnection) {
                Log.d("@@@@@", conn.cipherSuite.toString())
            }

            it.read(array)
            Log.d("@@@@@", array.toString(Charset.defaultCharset()))
        }
    }

}
