package mx.tictac.sicutng
/*
 * Pantalla de Splash
 * por: El Profe Tacho
 * Fecha: 10 de julio de 2022
 * 
 * Clase de pantalla de splash para arrancar la aplicación.
 * Referencia: https://www.youtube.com/watch?v=ksaaMt8Lo6U
 */
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class SplashScreenActivity : AppCompatActivity() {

    //Valores de inicio, constante para el tiempo de visualización de la pantalla.
    companion object {
        const val TIME_SPLASH_SCREEN = 500L
    }

    //Método de creación del ciclo de vida de la actividad
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        //Mensaje hacia el método de salida de la pantalla de inicio.
        exitSplashScreen()
    }
    
    //Método que envia de la pantalla de splash screen a la pantalla de autenticación de usuarios.
    private fun exitSplashScreen() {
        val handler = Handler()
        handler.postDelayed({
            startActivity(Intent(this, AuthActivity::class.java))
            overridePendingTransition(android.R.anim.fade_in, androidx.appcompat.R.anim.abc_fade_out)
            this.finish()
        }, TIME_SPLASH_SCREEN)
    }


}
