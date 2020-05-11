package zamorano.miguel.alia

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_mi_perfil.*
import kotlinx.android.synthetic.main.activity_register.*

class MiPerfilActivity : AppCompatActivity() {
    var database = FirebaseDatabase.getInstance().reference
    lateinit var usuarioUID: String


    var edad: Int = 0
    var nombre: String = ""
    var carrera: String = ""
    var valores: String = ""
    var esConductora: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mi_perfil)
        obtenDatosUsuario()
    }

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

                        Picasso.get().load(map["url"].toString().toUri()).into(imgViewFoto);

                        if(map["conductor"] == false) {
                            verifiedUser.visibility = View.GONE
                            clRutasFrecuentes.visibility = View.GONE
                        } else {
                            verifiedUser.visibility = View.VISIBLE
                            clRutasFrecuentes.visibility = View.VISIBLE
                        }
                    }

                })





    }
}
