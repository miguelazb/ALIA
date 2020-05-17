package zamorano.miguel.alia

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.net.toUri
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_viaje_resume.*

class ViajeResume : AppCompatActivity() {
    // Info usuario
    lateinit var nombre: String
    lateinit var colonia: String
    lateinit var ruta: String
    lateinit var foto: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_viaje_resume)

        nombre = intent.getStringExtra("nombre")
        ruta = intent.getStringExtra("ruta")
        colonia = intent.getStringExtra("colonia")
        foto = intent.getStringExtra("foto").toString().toUri()

        eliminarCorchetes(ruta)
        enCamino.text = "$nombre está en camino"
        perfil_nombre.text = nombre
        textViewFullPath.text = "${ruta}, ${colonia}"
        Picasso.get().load(foto).into(fotoUsuario);

        btnCancelar.setOnClickListener {
            val menuPrincipal: Intent = Intent(this, MenuViajeActivity::class.java)
            startActivity(menuPrincipal)
        }
    }

    fun eliminarCorchetes(ruta: String) {
        val filtered = "[]"
        this.ruta = ruta.filterNot { filtered.indexOf(it) > -1 }
    }
}
