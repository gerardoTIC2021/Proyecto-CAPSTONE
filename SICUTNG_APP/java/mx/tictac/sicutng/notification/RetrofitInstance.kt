package mx.tictac.sicutng.notification

/*
 *
 * por: El Profe Tacho
 * Fecha: 10 de julio de 2022
 * 
 * Clase para las instancias de Retrofit 
 * Retrofit es una librería para Android y java compatible con Kotlin para hacer peticiones a un API de forma sencilla.
 * Referencias:
 * https://engineering.creativesociety.mx/retrofit-cliente-http-para-android/
 * https://es.linkedin.com/learning/desarrollo-android-esencial
 * https://www.youtube.com/watch?v=kZJhB7LlcOU
 */


//Bibliotecas
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// Instancias de Retrofit parten de esta clase
class RetrofitInstance {
    // Elementos estáticos de clase que construye la instancia de retrofit y api de retrofit
    companion object{
        //Instancia estatica de retrofit
        private val retrofit by lazy {
            Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        }
        
        // Instancia estática para el API
        val api by lazy {
            retrofit.create(NotificationAPI::class.java)
        }
    }
}
