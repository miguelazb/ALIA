package zamorano.miguel.alia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

enum class ProviderType {
    BASIC
}

class MenuViajeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_viaje)
    }
}
