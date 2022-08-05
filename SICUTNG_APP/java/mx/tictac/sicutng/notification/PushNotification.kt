package mx.tictac.sicutng.notification

/*
 * 
 * por: El Profe Tacho
 * Fecha: 10 de julio de 2022
 * 
 * Definición de los datos de la notificación push.
 * Referencias:
 * https://es.linkedin.com/learning/desarrollo-android-esencial
 * https://www.youtube.com/watch?v=kZJhB7LlcOU
 */

//Bibliotecas
import mx.tictac.sicutng.notification.NotificationData

//Datos de la notificación Push
data class PushNotification (
    // Datos de la notificación
    val data: NotificationData,
    // A quién será enviada la notificación
    val to: String
)
