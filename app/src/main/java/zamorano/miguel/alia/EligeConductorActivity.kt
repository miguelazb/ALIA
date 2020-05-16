package zamorano.miguel.alia

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class EligeConductorActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_elige_conductor)
        val camion: String? = intent.getStringExtra("camion")
        val colonia: String? = intent.getStringExtra("colonia")


    }
}
