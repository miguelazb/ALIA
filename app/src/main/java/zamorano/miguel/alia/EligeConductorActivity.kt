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
import kotlinx.android.synthetic.main.activity_elige_conductor.*
import kotlinx.android.synthetic.main.activity_register.*

class EligeConductorActivity : AppCompatActivity() {
    // Referencia a la sesión iniciada en firebase
    val currentFirebaseUser = FirebaseAuth.getInstance().currentUser
    val idUsuario = currentFirebaseUser!!.uid
    // Referencia a base de datos Firebase
    val databaseReference = FirebaseDatabase.getInstance().reference

    var conductoras = ArrayList<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_elige_conductor)
        // Obtiene el dato de usuario
        obtenDatosUsuario()
        // obtén lista de conductoras
        obtenListaConductoras()
        val camion: String? = intent.getStringExtra("camion")
        val colonia: String? = intent.getStringExtra("colonia")


    }

    fun pruebaListaConductoras() {
        var text: String = ""
        for(a in conductoras) {
            text = text.plus(a.nombre).plus("\n");
        }
        textoPrueba.text = text
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
                    pruebaListaConductoras()
                }
            })

    }


}
