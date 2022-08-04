package mx.tictac.sicutng.control

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

class ControlActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_control)
        val database = Firebase.database
        val databaseReference = database.reference
        var entrada = 0
        var salida = 0
        var pasillo = 0
        var banio = 0
        var cajas = 0
        var almacen = 0
        var lampara = 0
        var ventilador = 0


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
                entrada = dataSnapshot.child("entrada").getValue<Int>()!!
                if (entrada == 0) {
                    entradaButton.isChecked = false
                    entrada = 1
                } else {
                    entradaButton.isChecked = true
                    entrada = 0
                }

                salida = dataSnapshot.child("salida").getValue<Int>()!!
                if (salida == 0) {
                    salidaButton.isChecked = false
                    salida = 1
                } else {
                    salidaButton.isChecked = true
                    salida = 0
                }

                pasillo = dataSnapshot.child("pasillo").getValue<Int>()!!
                if (pasillo == 0) {
                    pasilloButton.isChecked = false
                    pasillo = 1
                } else {
                    pasilloButton.isChecked = true
                    pasillo = 0
                }

                banio = dataSnapshot.child("banio").getValue<Int>()!!
                if (banio == 0) {
                    banoButton.isChecked = false
                    banio = 1
                } else {
                    banoButton.isChecked = true
                    banio = 0
                }

                cajas = dataSnapshot.child("cajas").getValue<Int>()!!
                if (cajas == 0) {
                    cajasButton.isChecked = false
                    cajas = 1
                } else {
                    cajasButton.isChecked = true
                    cajas = 0
                }

                almacen = dataSnapshot.child("almacen").getValue<Int>()!!
                if (almacen == 0) {
                    almacenButton.isChecked = false
                    almacen = 1
                } else {
                    almacenButton.isChecked = true
                    almacen = 0
                }

                lampara = dataSnapshot.child("lampara").getValue<Int>()!!
                if (lampara == 0) {
                    lampButton.isChecked = false
                    lampara = 1
                } else {
                    lampButton.isChecked = true
                    lampara = 0
                }

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

        entradaButton.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                databaseReference.child("entrada").setValue(entrada)
            } else {
                databaseReference.child("entrada").setValue(0)
            }
        }

        salidaButton.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                databaseReference.child("salida").setValue(salida)
            } else {
                databaseReference.child("salida").setValue(0)
            }
        }
        pasilloButton.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                databaseReference.child("pasillo").setValue(pasillo)
            } else {
                databaseReference.child("pasillo").setValue(0)
            }
        }

        banoButton.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                databaseReference.child("banio").setValue(banio)
            } else {
                databaseReference.child("banio").setValue(0)
            }
        }

        cajasButton.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                databaseReference.child("cajas").setValue(cajas)
            } else {
                databaseReference.child("cajas").setValue(0)
            }
        }

        almacenButton.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                databaseReference.child("almacen").setValue(almacen)
            } else {
                databaseReference.child("almacen").setValue(0)
            }
        }

        lampButton.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                databaseReference.child("lampara").setValue(lampara)
            } else {
                databaseReference.child("lampara").setValue(0)
            }
        }

        airButton.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                databaseReference.child("ventilador").setValue(ventilador)
            } else {
                databaseReference.child("ventilador").setValue(0)
            }
        }


    }
}