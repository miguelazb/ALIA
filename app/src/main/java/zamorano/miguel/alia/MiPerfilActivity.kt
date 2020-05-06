package zamorano.miguel.alia

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_mi_perfil.*

class MiPerfilActivity : AppCompatActivity() {
    var database = FirebaseDatabase.getInstance().reference
    lateinit var usuarioUID: String

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
                        var map = p0.value as Map<String, Any>
                        txtViewNombre.text = map["nombre"].toString()
                        txtViewEdad.text = "${map["edad"].toString()} años"
                        txtViewCarrera.text = map["carrera"].toString()
                        txtViewValores.text = map["valores"].toString()
                    }

                })





    }
}
