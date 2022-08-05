package mx.tictac.sicutng.sensors.fragments

/*
 * por: El Profe Tacho
 * Fecha: 10 de julio de 2022
 * 
 * Fragmento de sensor de temperatura
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

//Comienza la clase del fragmento de temperatura
class TemperatureFragment : Fragment() {

    //Método de creación del fragmento 
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_temperature, container, false)
        //Se instancia la base de datos Firebase
        val database = Firebase.database
        //Se instancia la referencia base de datos Firebase
        val databaseReference = database.reference
        //Se inicializa el valor del estado del aire acondicionado o ventilador
        var fan = 0
        //Elementos gráficos para pintar el fragmento
        val temperatureProgressBar: ProgressBar = view.findViewById(R.id.temperatureProgressBar)
        val temperatureTextView: TextView = view.findViewById(R.id.temperatureTextView)
        val dht11ToggleButton: ToggleButton = view.findViewById(R.id.dht11ToggleButton)
        //Escucha del agregado de valor a la referencia de base de datos
        databaseReference.addValueEventListener(object : ValueEventListener {
            @SuppressLint("SetTextI18n")
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                //Al cambiar el dato cambiará el estado y el valor de la temperatura y el estado
                val value = dataSnapshot.child("temperature").value
                fan = dataSnapshot.child("ventilador").getValue<Int>()!!
                temperatureProgressBar.progress = value.toString()?.toInt() ?: 0
                temperatureTextView.text = "Temperatura: "+value.toString()+" °C"
                //Puede cambiar el estado del botón para mostrar si está encendido o apagado.
                if (fan == 0) {
                    dht11ToggleButton.isChecked = false
                    fan = 1
                } else {
                    dht11ToggleButton.isChecked = true
                    fan = 0
                }

            }
            //Al cancelar la acción mantendrá registro el el Logcat para depuración.
            override fun onCancelled(error: DatabaseError) {
                Log.w("TAG", "Failed to read value.", error.toException())
            }
        })

        //Al cambiar el estado del boton tambien cambiara el estado en la base de datos en tiempo real de firebase.
        dht11ToggleButton.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                databaseReference.child("state").setValue(fan)
            } else {
                databaseReference.child("state").setValue(0)
            }
        }

        return view
    }

}
