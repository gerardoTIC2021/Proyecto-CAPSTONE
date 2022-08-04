package mx.tictac.sicutng.sensors

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import mx.tictac.sicutng.sensors.fragments.CollectionAdapter
import mx.tictac.sicutng.R

class SensorsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val collectionAdapter = CollectionAdapter(this)
        val viewPager: ViewPager2 = this.findViewById(R.id.viewPager2)
        viewPager.adapter = collectionAdapter

    }
}