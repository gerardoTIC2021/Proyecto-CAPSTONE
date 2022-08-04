package mx.tictac.sicutng.notification

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.iid.FirebaseInstanceIdReceiver
import com.google.firebase.iid.internal.FirebaseInstanceIdInternal
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_notification_test.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import mx.tictac.sicutng.R
import retrofit2.Retrofit

const val TOPIC = "topic-notification"

class NotificationActivityTest : AppCompatActivity() {
    val TAG = "NotificationActivityTest"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification_test)

        FirebaseService.sharedPref = getSharedPreferences("sharedPref", Context.MODE_PRIVATE)

        FirebaseMessaging.getInstance().token.addOnSuccessListener {
            FirebaseService.token = it
            etToken.setText(it)

        }

        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC)

        btnSend.setOnClickListener {
            val title = etTitle.text.toString()
            val message = etMessage.text.toString()
            val recipientToken = etToken.text.toString()

            if(title.isNotEmpty() && message.isNotEmpty() && recipientToken.isNotEmpty()){
                PushNotification(NotificationData(title, message),
                    recipientToken).also {
                    senNotification(it)
                }
            }

        }
    }

    private fun senNotification(notification: PushNotification) = CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = RetrofitInstance.api.postNotification(notification)
            if(response.isSuccessful){
                Log.d(TAG, "Response: ${Gson().toJson(response)}")
            }else{
                Log.e(TAG, response.errorBody().toString())
            }
        }catch (e: Exception){
            Log.e(TAG, e.toString())
        }
    }
}