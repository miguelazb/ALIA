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
    // Referencia a la sesión iniciada en firebase
    val currentFirebaseUser = FirebaseAuth.getInstance().currentUser
    val idUsuario = currentFirebaseUser!!.uid

    // Referencia a base de datos Firebase
    val databaseReference = FirebaseDatabase.getInstance().reference

    // Adapter rutas vars
    var listaRutas = ArrayList<ListaRutasConductor>()
    lateinit var adaptador: RutasAdapter

    // Saber si es conductor
    var esConductor: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mi_perfil)
        // Obtiene los datos del usuario
        obtenDatosUsuario()
        // Carga la lista de prueba
        listaRutas()

        // Adaptador de rutas
        adaptador = RutasAdapter(this, listaRutas)
        listaRutasFrecuentes.adapter = adaptador

        // Si se clickea el boton de bus menu
        btnAutoBusMenu.setOnClickListener {
            if(!esConductor) {
                val intent: Intent = Intent(this, BusStation::class.java)
                startActivity(intent)
            } else {
                val intent: Intent = Intent(this, AgregarRutas::class.java)
                startActivity(intent)
            }
        }

        // Si se clickea el boton de agregar rutas
        btnAddRoutes.setOnClickListener {
            val intent: Intent = Intent(this, AgregarRutas::class.java)
            startActivity(intent)
        }

        // Si se clickea el boton del menu principal (corazon)
        btnMenuPrincipal.setOnClickListener {
            val intent: Intent = Intent(this, MenuViajeActivity::class.java)
            startActivity(intent)
        }
    }

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
                    txtViewNombre.text = map["nombre"].toString()
                    txtViewEdad.text = "${map["edad"].toString()} años"
                    txtViewCarrera.text = map["carrera"].toString()
                    txtViewValores.text = map["valores"].toString()
                    ratingBarCalificacion.rating = map["puntuacion"].toString().toFloat()
                    esConductor = map["conductor"].toString().toBoolean()

                    if(!esConductor) {
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
     * listaRutas carga una lista de prueba para corroborar la funcionalidad
     * del listView
     */
    fun listaRutas() {
        databaseReference
            .child("users")
            .child(idUsuario)
            .addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onCancelled(p0: DatabaseError) { TODO("Not yet implemented") }

                override fun onDataChange(p0: DataSnapshot) {
                    var map = p0.value as Map<*, *>
                    val rutasArray: ArrayList<String> = map["rutas"] as ArrayList<String>

                    for (ruta in rutasArray) {
                        var idxSColon = ruta.indexOf(";")
                        if(idxSColon != -1 ){
                            var camion: String = ruta.substring(0,idxSColon)
                            var colonia: String = ruta.substring(idxSColon + 1)
                            listaRutas.add(ListaRutasConductor(camion,colonia))
                        }
                    }
                }

            })
    }
}
