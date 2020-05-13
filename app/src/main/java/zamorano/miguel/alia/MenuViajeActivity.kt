package zamorano.miguel.alia

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_menu_viaje.*
import kotlinx.android.synthetic.main.activity_mi_perfil.*

enum class ProviderType {
    BASIC
}

class MenuViajeActivity : AppCompatActivity() {
    var database = FirebaseDatabase.getInstance().reference
    lateinit var usuarioUID: String
    var esConductor: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_viaje)

        // Obtener dato de usuario
        obtenDatosUsuario()

        // Click al boton de menu Autobus
        btnMenuAutobus.setOnClickListener {
            if(esConductor == true) {
                // val intent: Intent = Intent(this, MiPerfilActivity::class.java)
                // startActivity(intent)
            } else {
                val intent: Intent = Intent(this, BusStation::class.java)
                startActivity(intent)
            }
        }

        // Click al boton de Menu de Perfil
        btnMenuPerfil.setOnClickListener {
            val intent: Intent = Intent(this, MiPerfilActivity::class.java)
            startActivity(intent)
        }

    }

    /**
     * Método obtenDatosUsuario funciona para saber si el usuario que tiene la
     * sesión iniciada es conductor o no.
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
                    esConductor = map["conductor"].toString().toBoolean()
                }

            })
    }
}
