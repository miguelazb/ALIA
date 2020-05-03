package zamorano.miguel.alia

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.os.Handler;

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnRegistro: Button = findViewById(R.id.btn_registro)
        val btnInicio: Button = findViewById(R.id.btn_iniciar)

        btnRegistro.setOnClickListener {
            val intent: Intent = Intent(this, registerActivity::class.java);
            startActivity(intent);
        }

        btnInicio.setOnClickListener {
            val intent: Intent = Intent(this, LogInActivity::class.java);
            startActivity(intent);
        }


    }

}
