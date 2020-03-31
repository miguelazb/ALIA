package zamorano.miguel.alia

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.os.Handler;

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val boton: Button = findViewById(R.id.btn_registrar);

        val handler = Handler()
        handler.postDelayed({
            val intent: Intent = Intent(this,registerActivity::class.java);
            startActivity(intent);
        }, 3000)

        boton.setOnClickListener({
            val intent: Intent = Intent(this,registerActivity::class.java);
            startActivity(intent);
        });
    }

}
