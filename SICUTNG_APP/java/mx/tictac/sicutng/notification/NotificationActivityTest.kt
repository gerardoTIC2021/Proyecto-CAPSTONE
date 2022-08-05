package mx.tictac.sicutng.notification
/*
 * 
 * por: El Profe Tacho
 * Fecha: 10 de julio de 2022
 * 
 * Pantalla de notificaciones para envio a los tenderos especificos.
 * Referencias:
 * https://es.linkedin.com/learning/desarrollo-android-esencial
 * https://www.youtube.com/watch?v=kZJhB7LlcOU
 */

//Bibliotecas
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

//Constante del tópico de la notificación
const val TOPIC = "topic-notification"

//Clase Actividad de prueba de notificaciones 
class NotificationActivityTest : AppCompatActivity() {
   //Etiquea de la actividad
    val TAG = "NotificationActivityTest"

    //Creación de la actividad 
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Vista de la actividad 
        setContentView(R.layout.activity_notification_test)
        
        //Preferencias compartidad para la notificación
        FirebaseService.sharedPref = getSharedPreferences("sharedPref", Context.MODE_PRIVATE)

        //Escuchador de inicio exitoso del token
        FirebaseMessaging.getInstance().token.addOnSuccessListener {
            FirebaseService.token = it
            etToken.setText(it)

        }
        
        //Instancia de Mensajería Firebase que se suscribe al topico.
        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC)

        //Escuchador del botón enviar al dar click coloca los valores de las cajas de texto y si no son vacios envia la notificación push
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

    //Método que envia la notificación push mandando mensajes al LogCat en casos de respuesta exitosa, con errores y con excepciones.
    private fun senNotification(notification: PushNotification) = CoroutineScope(Dispatchers.IO).launch {
        try {
            //Si se envia la notificación inicia la respuesta del servicio--
            val response = RetrofitInstance.api.postNotification(notification)
            if(response.isSuccessful){
                //Valor enviado al registro con la respuesta en formato JSON
                Log.d(TAG, "Response: ${Gson().toJson(response)}")
            }else{
                //Mensaje con el cuerpo de los errores de la respuesta 
                Log.e(TAG, response.errorBody().toString())
            }
        }catch (e: Exception){
            Log.e(TAG, e.toString())
        }
    }
}
