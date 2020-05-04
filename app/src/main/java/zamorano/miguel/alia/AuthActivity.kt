package zamorano.miguel.alia

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_auth.*


class AuthActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        // Setup
        setup()
    }

    private fun setup() {
        btn_listo.setOnClickListener {
            if (register_correo.text.isNotEmpty()
                && register_contra.text.isNotEmpty()
            ) {

                FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                    register_correo.text.toString(),
                    register_contra.text.toString()
                ).addOnCompleteListener {
                    if (it.isSuccessful) {
                        showHome(it.result?.user?.email ?: "", ProviderType.BASIC)
                    } else {
                        showAlert(it.exception?.message.toString())
                    }
                }
            }
        }
    }

    private fun showAlert(exception: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage(exception)
        builder.setPositiveButton("Acepter", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun showHome(email: String, provider: ProviderType) {
        val menuViajeIntent: Intent = Intent(this, RegisterActivity::class.java).apply {
            putExtra("email", email)
            putExtra("provider", provider.name)
        }
        startActivity(menuViajeIntent)
    }

}
