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

        // Corremos el método setup()
        setup()
    }

    /**
     * Método setup se encarga de hacer todo el registro de correo y contraseña
     * hacia firebase.
     */
    private fun setup() {
        val correo = register_correo.text
        val contra = register_contra.text

        // Si se clickea el botón listo.
        btn_listo.setOnClickListener {
            if (correo.isNotEmpty()
                && contra.isNotEmpty()
            ) {

                // Se obtiene una instancia de FirebaseAuth y con el correo y contraseña
                // se intenta hacer el registro del usuario.
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                    correo.toString(),
                    contra.toString()
                ).addOnCompleteListener {
                    if (it.isSuccessful) {
                        // Si todo sale bien, se continúa en la siguiente activity.
                        showRegister(it.result?.user?.email ?: "", ProviderType.BASIC)
                    } else {
                        // Si algo sale mal, se mostrará un error.
                        showAlert(it.exception?.message.toString())
                    }
                }
            }
        }
    }

    /**
     * Método ShowAlert construirá y mostrará un error en caso que algo haya salido mal
     * en el momento que se intenta hacer un registro.
     * @exception exception Error ocurrido en el registro.
     */
    private fun showAlert(exception: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage(exception)
        builder.setPositiveButton("Acepter", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    /**
     * Método ShowRegister hará un intent a la activity Register, además de hacer un
     * bundle del email y el provider (de momento parecen no ser utilizados).
     * @param email Correo con el que el usuario se va a registrar.
     * @param provider Provider indica el tipo de registro que está realizando.
     *
     * Los ProviderType pueden ser consultado en MenuViajeActivity
     */
    private fun showRegister(email: String, provider: ProviderType) {
        val menuViajeIntent: Intent = Intent(this, RegisterActivity::class.java).apply {
            putExtra("email", email)
            putExtra("provider", provider.name)
        }
        startActivity(menuViajeIntent)
    }

}
