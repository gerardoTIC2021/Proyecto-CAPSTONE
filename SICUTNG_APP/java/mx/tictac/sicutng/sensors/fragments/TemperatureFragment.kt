package mx.tictac.sicutng.sensors.fragments

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

class TemperatureFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_temperature, container, false)

        val database = Firebase.database
        val databaseReference = database.reference
        var fan = 0

        val temperatureProgressBar: ProgressBar = view.findViewById(R.id.temperatureProgressBar)
        val temperatureTextView: TextView = view.findViewById(R.id.temperatureTextView)
        val dht11ToggleButton: ToggleButton = view.findViewById(R.id.dht11ToggleButton)

        databaseReference.addValueEventListener(object : ValueEventListener {
            @SuppressLint("SetTextI18n")
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val value = dataSnapshot.child("temperature").value
                fan = dataSnapshot.child("ventilador").getValue<Int>()!!
                temperatureProgressBar.progress = value.toString()?.toInt() ?: 0
                temperatureTextView.text = "Temperatura: "+value.toString()+" °C"

                if (fan == 0) {
                    dht11ToggleButton.isChecked = false
                    fan = 1
                } else {
                    dht11ToggleButton.isChecked = true
                    fan = 0
                }

            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("TAG", "Failed to read value.", error.toException())
            }
        })

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