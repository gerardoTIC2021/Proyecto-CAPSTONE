package mx.tictac.sicutng.control

/*
 * Pantalla de Control de dispositivos de la tienda.
 * por: El Profe Tacho
 * Fecha: 10 de julio de 2022
 * 
 * Clase de alertas o solicitudes de atención de los usuarios al encargado de la tienda(solicitud y atención).
 * Requiere del prototipo de la matrix de leds para desplegar la información.
 */

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_control.*
import mx.tictac.sicutng.R

//Controla Dispositivos de acuerdo a datos de una base de datos en tiempo real de Firebase.
class ControlActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_control)
        //Definición de la base de datos
        val database = Firebase.database
        //Referencia de la base de datos
        val databaseReference = database.reference
        //Variables de estatus de cada uno de los dispositivos o secciones de la tienda.
        var entrada = 0
        var salida = 0
        var pasillo = 0
        var banio = 0
        var cajas = 0
        var almacen = 0
        var lampara = 0
        var ventilador = 0


      // TODO Para cambiar a un Mapa, para ahorrar lineas de código 
      //  var status = mapOf("entrada" to 0,"salida" to 0, "pasillo" to 0, "banio" to 0,"cajas" to 0,
      //      "almacen" to 0, "lampara" to 0, "ventilador" to 0)

        databaseReference.addValueEventListener(object : ValueEventListener {
            @SuppressLint("SetTextI18n")
            override fun onDataChange(dataSnapshot: DataSnapshot) {

             /*   val itr = status.keys.iterator()
                while (itr.hasNext()){
                    val key = itr.next()
                    var value = status[key]
                    println("${key}=$value")
                    value = dataSnapshot.child(key).getValue<Int>()!!
                    if (value == 0) {
                        entradaButton.isChecked = false
                        value = 1
                    } else {
                        entradaButton.isChecked = true
                        entrada = 0
                    }

                }*/
                
                //Cambiar el valor de la sección de entrada dependiendo del valor del Boton Toggle.
                entrada = dataSnapshot.child("entrada").getValue<Int>()!!
                if (entrada == 0) {
                    entradaButton.isChecked = false
                    entrada = 1
                } else {
                    entradaButton.isChecked = true
                    entrada = 0
                }
                
                //Datos y condicional para manejar el valor de la sección de salida dependiendo del valor del Boton Toggle.
                salida = dataSnapshot.child("salida").getValue<Int>()!!
                if (salida == 0) {
                    salidaButton.isChecked = false
                    salida = 1
                } else {
                    salidaButton.isChecked = true
                    salida = 0
                }
                
                //Datos y condicional para manejar el valor de la sección de pasillo dependiendo del valor del Boton Toggle.
                pasillo = dataSnapshot.child("pasillo").getValue<Int>()!!
                if (pasillo == 0) {
                    pasilloButton.isChecked = false
                    pasillo = 1
                } else {
                    pasilloButton.isChecked = true
                    pasillo = 0
                }

                //Datos y condicional para manejar el valor de la sección de baños dependiendo del valor del Boton Toggle.
                banio = dataSnapshot.child("banio").getValue<Int>()!!
                if (banio == 0) {
                    banoButton.isChecked = false
                    banio = 1
                } else {
                    banoButton.isChecked = true
                    banio = 0
                }
                
                //Datos y condicional para manejar el valor de la sección de cobro dependiendo del valor del Boton Toggle.
                cajas = dataSnapshot.child("cajas").getValue<Int>()!!
                if (cajas == 0) {
                    cajasButton.isChecked = false
                    cajas = 1
                } else {
                    cajasButton.isChecked = true
                    cajas = 0
                }

                //Datos y condicional para manejar el valor de la sección de almacen dependiendo del valor del Boton Toggle.
                almacen = dataSnapshot.child("almacen").getValue<Int>()!!
                if (almacen == 0) {
                    almacenButton.isChecked = false
                    almacen = 1
                } else {
                    almacenButton.isChecked = true
                    almacen = 0
                }
                
                //Datos y condicional para manejar el valor de la lampara dependiendo del valor del Boton Toggle.
                lampara = dataSnapshot.child("lampara").getValue<Int>()!!
                if (lampara == 0) {
                    lampButton.isChecked = false
                    lampara = 1
                } else {
                    lampButton.isChecked = true
                    lampara = 0
                }
                
                //Datos y condicional para manejar el valor del ventilador dependiendo del valor del Boton Toggle.
                ventilador = dataSnapshot.child("ventilador").getValue<Int>()!!
                if (ventilador == 0) {
                    airButton.isChecked = false
                    ventilador = 1
                } else {
                    airButton.isChecked = true
                    ventilador = 0
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("TAG", "Failed to read value.", error.toException())
            }
        })

        //Evento del cambio en el Boton de Entrada
        entradaButton.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                databaseReference.child("entrada").setValue(entrada)
            } else {
                databaseReference.child("entrada").setValue(0)
            }
        }
        
        //Evento del cambio en el Boton de Salida
        salidaButton.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                databaseReference.child("salida").setValue(salida)
            } else {
                databaseReference.child("salida").setValue(0)
            }
        }
        
        //Evento del cambio en el Boton de Pasillo
        pasilloButton.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                databaseReference.child("pasillo").setValue(pasillo)
            } else {
                databaseReference.child("pasillo").setValue(0)
            }
        }

        //Evento del cambio en el Boton de Baños
        banoButton.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                databaseReference.child("banio").setValue(banio)
            } else {
                databaseReference.child("banio").setValue(0)
            }
        }

        //Evento del cambio en el Boton de Cajas
        cajasButton.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                databaseReference.child("cajas").setValue(cajas)
            } else {
                databaseReference.child("cajas").setValue(0)
            }
        }

        //Evento del cambio en el Boton de Almacen
        almacenButton.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                databaseReference.child("almacen").setValue(almacen)
            } else {
                databaseReference.child("almacen").setValue(0)
            }
        }

        //Evento del cambio en el Boton de Lampara
        lampButton.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                databaseReference.child("lampara").setValue(lampara)
            } else {
                databaseReference.child("lampara").setValue(0)
            }
        }

        //Evento del cambio en el Boton de Ventilador
        airButton.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                databaseReference.child("ventilador").setValue(ventilador)
            } else {
                databaseReference.child("ventilador").setValue(0)
            }
        }


    }
}
