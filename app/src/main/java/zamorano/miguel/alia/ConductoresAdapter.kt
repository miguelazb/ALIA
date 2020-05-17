package zamorano.miguel.alia

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.core.net.toUri
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.lista_mostrar_conductores.view.*

class ConductoresAdapter: BaseAdapter {
    var context: Context
    var conductores = ArrayList<ListaConductorRuta>()

    constructor(context: Context, conductores: ArrayList<ListaConductorRuta>) {
        this.context = context
        this.conductores = conductores
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var inflador = LayoutInflater.from(context)
        var vista = inflador.inflate(R.layout.lista_mostrar_conductores, null)
        var conductor = conductores[position]

        Picasso.get().load(conductor.url.toUri()).into(vista.imagenConductor)
        vista.nombreCondcutor.text = conductor.nombreConductor

        var textoRutas: String = "Rutas: "
        textoRutas = textoRutas.plus(conductor.ruta[0])
        var i: Int = 1
        while (conductor.ruta.size > i) {
            textoRutas = textoRutas.plus(", ").plus(conductor.ruta[i])
        }
        vista.rutaConductor.text = textoRutas
        vista.coloniaConductor.text = "Colonia: ${conductor.colonia}"

        return vista
    }

    override fun getItem(position: Int): Any {
        return conductores[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return conductores.size
    }
}