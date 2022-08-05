package mx.tictac.sicutng.sensors.fragments
/*
 * por: El Profe Tacho
 * Fecha: 10 de julio de 2022
 * 
 * Fragmento de sensor de humedad
 * DHT11 con ESP32 con Base de Datos en Tiempo Real.
 * Referencias:
 * https://www.youtube.com/watch?v=ShsoW5lFflw
 * 
 */

//Bibliotecas
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.ToggleButton
import androidx.fragment.app.Fragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import mx.tictac.sicutng.R

//Comienza la clase dle fragmento de humedad
class HumidityFragment : Fragment() {

    //Método de creación del fragmento 
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_humidity, container, false)
        
        //Se instancia la base de datos Firebase
        val database = Firebase.database
        //Se instancia la referencia de la base de datos 
        val databaseReference = database.reference
        //Se inicializa el valor del estado del aire acondicionado o ventilador
        var state = 0

        //Elementos gráficos para pintar el fragmento
        val humidityProgressBar: ProgressBar = view.findViewById(R.id.humidityProgressBar)
        val humidityTextView: TextView = view.findViewById(R.id.humidityTextView)
        val dht11ToggleButton: ToggleButton = view.findViewById(R.id.dht11ToggleButton)
        
        //Escucha del agregado de valor a la referencia de base de datos
        databaseReference.addValueEventListener(object : ValueEventListener {
            @SuppressLint("SetTextI18n")
            
            //Al cambiar el dato cambiará el estado y el valor de la humedad y el estado
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val value = dataSnapshot.child("humidity").value
                state = dataSnapshot.child("state").getValue<Int>()!!

                humidityProgressBar.progress = value.toString()?.toInt() ?: 0
                humidityTextView.text = "Humedad: "+value.toString()+" %"
                
                //Puede cambiar el estado del boton para mostrar si está encendido o apagado.
                if (state == 0) {
                    dht11ToggleButton.isChecked = false
                    state = 1
                } else {
                    dht11ToggleButton.isChecked = true
                    state = 0
                }

            }
            
            //Al cancelar la acción mantendra registro el el Logcat para depuración,
            override fun onCancelled(error: DatabaseError) {
                Log.w("TAG", "Failed to read value.", error.toException())
            }
        })
        
        //Al cambiar el estado del boton tambien cambiara el estado en la base de datos en tiempo real de firebase.
        dht11ToggleButton.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                databaseReference.child("state").setValue(state)
            } else {
                databaseReference.child("state").setValue(0)
            }
        }

        return view
    }

}
