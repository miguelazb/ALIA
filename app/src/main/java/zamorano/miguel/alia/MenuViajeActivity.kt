package zamorano.miguel.alia

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_menu_viaje.*

enum class ProviderType {
    BASIC
}

class MenuViajeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_viaje)

        btnMenuPerfil.setOnClickListener {
            val intent: Intent = Intent(this, MiPerfilActivity::class.java)
            startActivity(intent)
        }
    }
}
