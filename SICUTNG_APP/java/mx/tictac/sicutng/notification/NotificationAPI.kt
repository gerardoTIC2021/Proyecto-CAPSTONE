package mx.tictac.sicutng.notification

/*
 * Inteface (protocolo) para el uso de la interface de programación de aplicaciones (API)
 * Retrofit es un cliente de servidores REST para Android y Java desarrollado por Square, muy simple y muy fácil de aprender.
 * Permite hacer peticiones al servidor tipo: GET, POST, PUT, PATCH, DELETE y HEAD, y gestionar diferentes tipos de parámetros, 
 * paseando automáticamente la respuesta a un tipo de datos.
 * por: El Profe Tacho
 * Fecha: 10 de julio de 2022
 * 
 *  Definición de datos estáticos para el uso de notificaciones en firebase, con metodos de Tokens y recepción de notificaciones.
 * Referencias:
 * https://es.linkedin.com/learning/desarrollo-android-esencial
 * https://www.youtube.com/watch?v=kZJhB7LlcOU
 */

//Bibliotecas
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

//Inicio de la inteface
interface NotificationAPI {

    //Anotaciones de la intefaz para el método de post de Notificación    
    @Headers("Authorization:  key=${Constants.SERVER_KEY}", "Conten-Type:${Constants.CONTENT_TYPE}")
    @POST("fcm/send")
    suspend fun postNotification(
        @Body notification: PushNotification
    ): Response<ResponseBody>
}
