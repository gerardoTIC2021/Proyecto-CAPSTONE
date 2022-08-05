package mx.tictac.sicutng.sensors.fragments

/*
 *
 * por: El Profe Tacho
 * Fecha: 10 de julio de 2022
 * Adaptador de colección para el View Pager.
 * 
 * Referencias:
 * https://www.youtube.com/watch?v=ShsoW5lFflw
 *
 */


//Bibliotecas
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter


//Clase para adaptar la colección de fragementos de pantalla al View Pager.
class CollectionAdapter(fm: FragmentActivity) : FragmentStateAdapter(fm) {
    //Numero de paginas (fragmentos del view pager)
    override fun getItemCount(): Int {
        return 2 //Dos fragmentos 
    }
    
    //Método para la creación de fragmentos para el fragmento de página para temperatura y humedad.
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            //Posición 0 para el fragmento de temperatura
            0 -> TemperatureFragment()
            //Posición 1 para el fragmento de Humedad
            1 -> HumidityFragment()
            // No hay al momento más fragmentos de los arriba descritos.
            else -> throw IllegalStateException("Unexpected position $position")
        }
    }
}
