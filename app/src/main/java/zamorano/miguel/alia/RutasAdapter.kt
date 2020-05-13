package zamorano.miguel.alia

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import kotlinx.android.synthetic.main.lista_rutas_conductor.view.*

class RutasAdapter: BaseAdapter {
    var context: Context
    var rutas = ArrayList<ListaRutasConductor>()

    constructor(context: Context, rutas: ArrayList<ListaRutasConductor>) {
        this.context = context
        this.rutas = rutas
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var inflador = LayoutInflater.from(context)
        var vista = inflador.inflate(R.layout.lista_rutas_conductor, null)
        var ruta = rutas[position]

        vista.textViewRuta.text = ruta.rutaConductor

        return vista
    }

    override fun getItem(position: Int): Any {
        return rutas[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return rutas.size
    }
}