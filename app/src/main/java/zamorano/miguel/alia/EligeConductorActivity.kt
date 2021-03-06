package zamorano.miguel.alia

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_bus_station.*
import kotlinx.android.synthetic.main.activity_elige_conductor.*
import kotlinx.android.synthetic.main.activity_register.*

class EligeConductorActivity : AppCompatActivity() {
    // Referencia a la sesión iniciada en firebase
    val currentFirebaseUser = FirebaseAuth.getInstance().currentUser
    val idUsuario = currentFirebaseUser!!.uid
    // Referencia a base de datos Firebase
    val databaseReference = FirebaseDatabase.getInstance().reference
    // Datos de conductor
    var camionElegido: String? = null
    var coloniaElegida: String? = null
    var conductoras = ArrayList<User>()
    // extras
    lateinit var item: ListaConductorRuta
    // Adaptador
    lateinit var adaptador: ConductoresAdapter
    //
    lateinit var nombre: String
    lateinit var colonia: String
    lateinit var ruta: String
    lateinit var foto: String

    //var conductorasOrdenadas = LinkedHashMap<User, ArrayList<String>>()
    var conductorasOrdenadas = ArrayList<ListaConductorRuta>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_elige_conductor)
        camionElegido = intent.getStringExtra("camion")
        coloniaElegida = intent.getStringExtra("colonia")
        // Obtiene el dato de usuario
        obtenDatosUsuario()
        // obtén lista de conductoras
        obtenListaConductoras()

        listaConductores.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            item = parent.getItemAtPosition(position) as ListaConductorRuta
            nombre = item.nombreConductor
            colonia = item.colonia
            ruta = item.ruta.toString()
            foto = item.url
            Toast.makeText(this, "Seleccionaste ${nombre}", Toast.LENGTH_LONG).show()
        }

        btn_confirmar.setOnClickListener {
            if(item.nombreConductor.isNotEmpty()) {
                val resumenViaje: Intent = Intent(this, ViajeResume::class.java)
                resumenViaje.putExtra("nombre", nombre)
                resumenViaje.putExtra("colonia", colonia)
                resumenViaje.putExtra("ruta", ruta)
                resumenViaje.putExtra("foto", foto)
                startActivity(resumenViaje)
            }
        }

    }

    /**
     * Método obtenDatoUsuario nos da el nombre del usuario existente
     * en la base de datos Firebase.
     */
    /**
     * obtenDatosUsuario trae todos los datos del usuario directamente de
     * la base de datos de Firebase.
     */
    fun obtenDatosUsuario() {
        databaseReference
            .child("users")
            .child(idUsuario)
            .addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onCancelled(p0: DatabaseError) { TODO("Not yet implemented") }

                override fun onDataChange(p0: DataSnapshot) {
                    var map = p0.value as Map<*, *>

                }
            })
    }

    /**
     * obtenDatosUsuario trae todos los datos del usuario directamente de
     * la base de datos de Firebase.
     */
    fun obtenListaConductoras() {
        FirebaseDatabase.getInstance().reference
            .child("users")
            .addValueEventListener(object: ValueEventListener {
                override fun onCancelled(p0: DatabaseError) { TODO("Not yet implemented") }

                override fun onDataChange(p0: DataSnapshot) {
                    val map = p0.value as Map<*, *>
                    for(m in map) {
                        val a = m.value as Map<*,*>
                        val conductor: Boolean = a["conductor"].toString().toBoolean()
                        if(conductor) {
                            val nombre: String = a["nombre"].toString()
                            val edad: String = a["edad"].toString()
                            val carrera: String = a["carrera"].toString()
                            val valores: String = a["valores"].toString()
                            val url: String = a["url"].toString()
                            val puntuacion: Float? = a["puntuacion"].toString().toFloatOrNull()
                            val rutas: ArrayList<String>? = a["rutas"] as ArrayList<String>
                            var conductora: User = User(nombre, edad, carrera, valores, conductor, url, puntuacion, rutas)
                            conductoras.add(conductora)
                        }
                    }

                    var listaAux = ArrayList<User>()
                    listaAux.addAll(conductoras)

                    var rutas = null
                    // se evalúa si coindide tanto la ruta como la colonia a la que se dirige
                    for(conductora in conductoras) {
                        for(ruta in conductora.rutas!!) {
                            // formato camion;colonia
                            var datosRuta = ruta.split(";")
                            var listaRutas: ArrayList<String> = arrayListOf<String>()
                            if(datosRuta.size == 2){
                                if(camionElegido.equals(datosRuta[0], true)
                                    && coloniaElegida.equals(datosRuta[1], true)){
                                    listaRutas.add(datosRuta[0])
                                    conductorasOrdenadas.add(
                                        ListaConductorRuta(
                                        conductora.nombre,
                                        listaRutas,
                                        coloniaElegida!!,
                                        conductora.url
                                    )
                                    )
                                    // conductorasOrdenadas.put(conductora,listaRutas)
                                    listaAux.remove(conductora)
                                    break
                                }
                            }
                        }
                    }
                    // ahora si coincide la colonia
                    for(conductora in listaAux) {
                        // puede haber varias rutas que coincidan en colonia, se guardan en una lista
                        var listaRutas: ArrayList<String> = arrayListOf<String>()
                        var coincideAlgunaRuta: Boolean = false
                        for(ruta in conductora.rutas!!) {
                            // formato camion;colonia
                            var datosRuta = ruta.split(";")
                            if(datosRuta.size == 2
                                && coloniaElegida.equals(datosRuta[1], true)){
                                // si al menos una colonia coincide, se establece en true
                                coincideAlgunaRuta = true
                                // y se añade a la lista de rutas
                                listaRutas.add(datosRuta[0])
                            }
                        }
                        // después de recorrer todas las rutas, si se encontró alguna...
                        if(coincideAlgunaRuta) {
                            // se añade la conductora y la lista de rutas correspondiente
                            conductorasOrdenadas.add(ListaConductorRuta(
                                conductora.nombre,
                                listaRutas,
                                coloniaElegida!!,
                                conductora.url
                            ))
                        }
                    }
                    adaptador = ConductoresAdapter(this@EligeConductorActivity, conductorasOrdenadas)
                    listaConductores.adapter = adaptador
                    if(conductorasOrdenadas.isEmpty()) {
                        btn_confirmar.visibility = View.GONE
                        listaConductores.visibility = View.GONE
                        mensajeColonia.visibility = View.VISIBLE
                    }
                }
            })

    }


}
