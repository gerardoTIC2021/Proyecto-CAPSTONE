package mx.tictac.sicutng.notification
/*
 * Constantes para notificaciones Fibebase 
 * por: El Profe Tacho
 * Fecha: 10 de julio de 2022
 * 
 * Representación de datos estáticos de las clase para poder acceder a la base de datos de firebase
 *
 */

class Constants {
    companion object {
        //Url para las notificaciones.
        const val BASE_URL = "https://fcm.googleapis.com"
        //Cambiar por tu server key de la aplicacion generada desde consola de firebase.
        const val SERVER_KEY =
            "TYPE_YOUR_SERVER_KEY"
        //Tipos de contenidos de las notificaciones en este caso JSON
        const val CONTENT_TYPE = "application/json"
    }
}
