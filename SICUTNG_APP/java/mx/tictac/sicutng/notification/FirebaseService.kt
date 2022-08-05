package mx.tictac.sicutng.notification
/*
 * Clase de Servicio de Mensajería de de Firebase
 * por: El Profe Tacho
 * Fecha: 10 de julio de 2022
 * 
 *  Definición de datos estáticos para el uso de notificaciones en firebase, con metodos de Tokens y recepción de notificaciones.
 * Referencias:
 * https://www.youtube.com/watch?v=kZJhB7LlcOU
 */

//Bibliotecas
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import mx.tictac.sicutng.R
import kotlin.random.Random


// Canal de la notificación
private const val CHANNEL_ID = "my_channel"


//Se define la clase de servicio de mensajería de Firebase
class FirebaseService: FirebaseMessagingService() {
    
    // Valores estaticos disponible para cualquier objeto o uso de la clase
    companion object{
        
        //Declaración de preferencia compartida
        var sharedPref: SharedPreferences? = null
        //Declaración de Token usando preferencias compartidas en su obtención y colocacion de datos
        var token: String?
            get() {
                return sharedPref?.getString("token", "")
            }
            set(value) {
                sharedPref?.edit()?.putString("token", value)?.apply()
            }
    }

    //Método para la definición de nuevos tokens
    override fun onNewToken(newToken: String) {
        super.onNewToken(newToken)
        token = newToken
    }

    //Método que se ejecuta al recibir un mensaje remoto
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        
        
        // Definición del intento
        val intent = Intent(this, NotificationActivityTest::class.java)
        
        //Creación del administrador de notificaciones
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        //Definición de un entero aleatorio  para el id de la notificación
        val notificationID = Random.nextInt()

        //Dependiendo de la versión de Android añadir configuraciones
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            createNofiticationChannel(notificationManager)
        }
        
        
        /*
        La actividad que se está iniciando ya se está ejecutando en la tarea actual,
        entonces, en lugar de iniciar una nueva instancia de esa actividad, todas las demás actividades 
        encima se cerrarán y este intent se entregará a (ahora en arriba) la actividad anterior como un nuevo intent.
        */
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        // Una referencia a un token mantenido por el sistema que describe los datos originales utilizados para recuperarlo.
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_MUTABLE)

        //Se construye la notificación a partir del patron de diseño builder.
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(message.data["title"])
            .setContentText(message.data["message"])
            .setSmallIcon(R.drawable.ic_baseline_emoji_people_24)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()
        
        //El administrador de notificaciones hace la notificación
        notificationManager.notify(notificationID, notification)


    }

    //Método que crea el canal para las notificaciones
    private fun createNofiticationChannel(notificationManager: NotificationManager){
        //Nombre del canal
        val channelName = "channelName"
        //Creación del canal de notificaciones
        val channel = NotificationChannel(CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_HIGH).apply {
           description = "MyChannelDescription"
           enableLights(true)
           lightColor = Color.GREEN
        }
        
        //El administrador de notificaciones crea el canal de notificaciones
        notificationManager.createNotificationChannel(channel)

    }

}
