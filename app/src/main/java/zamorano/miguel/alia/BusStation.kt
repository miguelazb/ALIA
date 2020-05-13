package zamorano.miguel.alia

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_bus_station.*

class BusStation : AppCompatActivity() {
    var database = FirebaseDatabase.getInstance().reference
    lateinit var usuarioUID: String

    var listaRutas = ArrayList<ListaRutasConductor>()
    lateinit var adaptador: RutasAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bus_station)
        // Obtiene el dato de usuario
        obtenDatosUsuario()
        // Carga la lista de prueba
        listaDePrueba()

        // Carga el adaptador de Rutas
        adaptador = RutasAdapter(this, listaRutas)
        listaRutasFrecuentes.adapter = adaptador

        // Si se clickea el boton Menu Perfil
        btnMenuPerfil.setOnClickListener {
            val intent: Intent = Intent(this, MiPerfilActivity::class.java)
            startActivity(intent)
        }
    }

    /**
     * Método listaDePrueba carga una lista de prueba para corroborar
     * la funcionalidad de listView
     */
    fun listaDePrueba() {
        listaRutas.add(ListaRutasConductor("Ruta 5, Calle Zaragoza 611"))
        listaRutas.add(ListaRutasConductor("Ruta 3, Calle Jálisco y Av. Las palmas"))
        listaRutas.add(ListaRutasConductor("Ruta 7, Calle 5 de Febrero"))
    }

    /**
     * Método obtenDatoUsuario nos da el nombre del usuario existente
     * en la base de datos Firebase.
     */
    fun obtenDatosUsuario() {
        val currentFirebaseUser = FirebaseAuth.getInstance().currentUser
        usuarioUID = currentFirebaseUser!!.uid

        database.child("users")
            .child(usuarioUID)
            .addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    TODO("Not yet implemented")
                }

                override fun onDataChange(p0: DataSnapshot) {
                    var map = p0.value as Map<*, *>
                    textViewNombre.text = "Bienvenida, ${map["nombre"].toString()}"
                }

            })
    }

}
