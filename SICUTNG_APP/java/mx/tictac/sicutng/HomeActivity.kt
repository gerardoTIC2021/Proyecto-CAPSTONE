    package mx.tictac.sicutng
/*
 * Pantalla de Autenticación de usuarios
 * por: El Profe Tacho
 * Fecha: 10 de julio de 2022
 * 
 * Clase de acceso a las distintas funcionalidades del proyecto.
 */
    import android.content.Context
    import android.content.Intent
    import android.os.Bundle
    import androidx.appcompat.app.AppCompatActivity
    import com.google.firebase.auth.FirebaseAuth
    import kotlinx.android.synthetic.main.activity_home.*
    import mx.tictac.sicutng.control.ControlActivity
    import mx.tictac.sicutng.notification.NotificationActivityTest
    import mx.tictac.sicutng.sensors.SensorsActivity

    class HomeActivity : AppCompatActivity() {

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_home)

            //Setup
            val bundle: Bundle? = intent.extras
            val email: String? = bundle?.getString("email")
            val provider: String? = bundle?.getString("provider")

            setup(email ?:"", provider ?:"")

            // Saving data
            val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
            prefs.putString("email", email)
            prefs.putString("provider", provider)
            prefs.apply()


        }

        private fun setup(email: String, provider: String) {
            title = "Start"
            emailTextView.text = email
            proveedorTextView.text = provider
            
            //Botón de Cerrar Sesión
            logOutButton.setOnClickListener {

                // Remove data
                val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
                prefs.clear()
                prefs.apply()

                FirebaseAuth.getInstance().signOut()
                onBackPressed()
            }
            
            //Botón para acceso a la pantalla de solicitud de atención.
            requestAttentionButton.setOnClickListener {

                startActivity(Intent(this, AlertActivity::class.java))
            }
            
            //Botón de acceso a la pantalla de notificaciones directas al vendedor.
            notificationTestButton.setOnClickListener {
                startActivity(Intent(this, NotificationActivityTest::class.java))
            }

            //Botón para acceso a la pantalla de sensores de temperatura y humedad. 
            sensorsButton.setOnClickListener {
                startActivity(Intent(this, SensorsActivity::class.java))
            }

            //Botón para acceso a la pantalla de control de la electricidad de cada una de las areas de la tienda.
            controlButton.setOnClickListener {
                startActivity(Intent(this, ControlActivity::class.java))
            }

        }
    }
    
    //Tipos de autenticación o de inicio de sesión.
    enum class ProviderType{
        BASIC, GOOGLE
    }
