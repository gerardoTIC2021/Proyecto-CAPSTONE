package mx.tictac.sicutng.sensors

/*
 * por: El Profe Tacho
 * Fecha: 10 de julio de 2022
 * 
 * Pantalla de Sensores
 * DHT11 con ESP32 con Base de Datos en Tiempo Real.
 * Referencias:
 * https://www.youtube.com/watch?v=ShsoW5lFflw
 * 
 */


//Bibliotecas
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import mx.tictac.sicutng.sensors.fragments.CollectionAdapter
import mx.tictac.sicutng.R

//Pantalla de vista de sensores de la aplicación
class SensorsActivity : AppCompatActivity() {
    
    //Método de creación de actividad
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        //Vista en xml para actividad de los sensores
        setContentView(R.layout.activity_main)
        
        //Adaptador ede colecciones para el vista en paginación.
        val collectionAdapter = CollectionAdapter(this)
        
        //Definimos la instancia de la vista en paginaciones.
        val viewPager: ViewPager2 = this.findViewById(R.id.viewPager2)
        //Añadimos el adaptador al viewPager.
        viewPager.adapter = collectionAdapter

    }
}
