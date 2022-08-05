package mx.tictac.sicutng.notification
/*
 * por: El Profe Tacho
 * Fecha: 10 de julio de 2022
 * 
 * Clase de datos de la notificación.
 * Referencias:
 * https://es.linkedin.com/learning/desarrollo-android-esencial
 * https://www.youtube.com/watch?v=kZJhB7LlcOU
 */

//Una data class no es más que una clase que sólo contiene estado y no realiza ninguna operación. 
data class NotificationData (
    //Título de la notificación
    val title: String,
    //Mensaje de la notificación
    val message: String
)
