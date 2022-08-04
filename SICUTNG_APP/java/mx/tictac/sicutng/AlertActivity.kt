package mx.tictac.sicutng

import android.content.Context
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import kotlinx.android.synthetic.main.activity_alert.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONException
import org.json.JSONObject
import java.io.*
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL


class AlertActivity : AppCompatActivity() {
    //val ip = "http://172.16.2.45";
    private val ip = "http://192.168.1.64"
    private var email = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alert)
        setup()

        // Saving data
        email =
            getSharedPreferences(getString(R.string.prefs_file), MODE_PRIVATE).getString("email","")
                .toString()

    }

    private fun setup() {
        val policy: ThreadPolicy =
            ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        notifyButton.setOnClickListener {
            val mainExecutor = ContextCompat.getMainExecutor(this)
            mainExecutor.execute{
                try {
                    /*val url = URL("$ip/on")
                    val urlConnection = url.openConnection()
                    val inputStream = BufferedInputStream(urlConnection.getInputStream())

                    //val outputStream = BufferedOutputStream(urlConnection.getOutputStream())

                    val reader = BufferedReader(InputStreamReader(inputStream))
                    val data = reader.readLine()
                    Log.d("data", data)
                    val objOn = JSONObject(data)
                    objOn.put("user", "tachin")
                    Log.d("objOn", objOn.toString())
                    url.openConnection()
                    mainExecutor.execute {
                        run { updateStatusView(objOn.getInt("status")) }
                    }*/
                    rawJSON()

                } catch (e: MalformedURLException) {
                    e.printStackTrace()
                } catch (e: IOException) {
                    e.printStackTrace()
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }

        }

        messButton.setOnClickListener {
            val mainExecutor = ContextCompat.getMainExecutor(this)
            mainExecutor.execute{
                try {
                    val url = URL("$ip/off")
                    val urlConnection = url.openConnection()
                    val inputStream = BufferedInputStream(urlConnection.getInputStream())

                    val reader = BufferedReader(InputStreamReader(inputStream))
                    val data = reader.readLine()
                    val objOff = JSONObject(data)
                    mainExecutor.execute {
                        run { updateStatusView(objOff.getInt("status")) }
                    }

                } catch (e: MalformedURLException) {
                    e.printStackTrace()
                } catch (e: IOException) {
                    e.printStackTrace()
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }

        }
    }

    private fun updateStatusView(flag: Int) {
        if (flag == 1) {
            this.statusDisplay.text = getString(R.string.notification)
        } else if (flag == 0) {
            this.statusDisplay.text = getString(R.string.message)
        }
    }

    fun rawJSON() {

        // Create JSON using JSONObject
        val jsonObject = JSONObject()
        jsonObject.put("status", "1")
        jsonObject.put("user", email)

        // Convert JSONObject to String
        val jsonObjectString = jsonObject.toString()

        GlobalScope.launch(Dispatchers.IO) {
            val url = URL("$ip/on")
            val httpURLConnection = url.openConnection() as HttpURLConnection
            httpURLConnection.requestMethod = "POST"
            httpURLConnection.setRequestProperty("Content-Type", "application/json") // The format of the content we're sending to the server
            httpURLConnection.setRequestProperty("Accept", "application/json") // The format of response we want to get from the server
            httpURLConnection.doInput = true
            httpURLConnection.doOutput = true

            // Send the JSON we created
            val outputStreamWriter = OutputStreamWriter(httpURLConnection.outputStream)
            outputStreamWriter.write(jsonObjectString)
            outputStreamWriter.flush()

            // Check if the connection is successful
            val responseCode = httpURLConnection.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                val response = httpURLConnection.inputStream.bufferedReader()
                    .use { it.readText() }  // defaults to UTF-8
                withContext(Dispatchers.Main) {

                    // Convert raw JSON to pretty JSON using GSON library
                    val gson = GsonBuilder().setPrettyPrinting().create()
                    //val prettyJson = gson.toJson(JsonParser.parseString(response))
                    val prettyJson = gson.toJson(response)
                    Log.d("Pretty Printed JSON:", prettyJson)

                }
            } else {
                Log.e("HTTPURLCONNECTION_ERROR", responseCode.toString())
            }
        }
    }

}