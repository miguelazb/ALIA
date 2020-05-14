package zamorano.miguel.alia

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_mi_perfil.*

class MiPerfilActivity : AppCompatActivity() {
    var database = FirebaseDatabase.getInstance().reference
    lateinit var usuarioUID: String

    var listaRutas = ArrayList<ListaRutasConductor>()
    lateinit var adaptador: RutasAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mi_perfil)
        // Obtiene los datos del usuario
        obtenDatosUsuario()
        // Carga la lista de prueba
        listaDePrueba()

        // Adaptador de rutas
        adaptador = RutasAdapter(this, listaRutas)
        listaRutasFrecuentes.adapter = adaptador

        // Si se clickea el boton de bus menu
        btnAutoBusMenu.setOnClickListener {
            val intent: Intent = Intent(this, BusStation::class.java)
            startActivity(intent)
        }

        // Si se clickea el boton de agregar rutas
        btnAddRoutes.setOnClickListener {
            val intent: Intent = Intent(this, AgregarRutas::class.java)
            startActivity(intent)
        }
    }

    /**
     * obtenDatosUsuario trae todos los datos del usuario directamente de
     * la base de datos de Firebase.
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
                        txtViewNombre.text = map["nombre"].toString()
                        txtViewEdad.text = "${map["edad"].toString()} años"
                        txtViewCarrera.text = map["carrera"].toString()
                        txtViewValores.text = map["valores"].toString()
                        ratingBarCalificacion.rating = map["puntuacion"].toString().toFloat()

                        if(map["conductor"] == false) {
                            verifiedUser.visibility = View.GONE
                            btnAddRoutes.visibility = View.GONE
                            clRutasFrecuentes.visibility = View.GONE
                        } else {
                            verifiedUser.visibility = View.VISIBLE
                            btnAddRoutes.visibility = View.VISIBLE
                            clRutasFrecuentes.visibility = View.VISIBLE
                        }
                        val uri: Uri = Uri.parse(map["url"].toString())

                        Picasso.get().load(uri).into(imgViewFoto);
                    }

                })
    }

    /**
     * listaDePrueba carga una lista de prueba para corroborar la funcionalidad
     * del listView
     */
    fun listaDePrueba() {
        listaRutas.add(ListaRutasConductor("Ruta 5, Calle Zaragoza 611"))
        listaRutas.add(ListaRutasConductor("Ruta 3, Calle Jálisco y Av. Las palmas"))
        listaRutas.add(ListaRutasConductor("Ruta 7, Calle 5 de Febrero"))
    }
}
