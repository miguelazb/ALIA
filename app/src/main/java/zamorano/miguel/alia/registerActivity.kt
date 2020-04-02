package zamorano.miguel.alia

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class registerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val boton: Button = findViewById(R.id.btn_listo);

        boton.setOnClickListener({
            val intent: Intent = Intent(this,MenuViajeActivity::class.java);
            startActivity(intent);
        });

    }
}
