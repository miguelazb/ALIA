package zamorano.miguel.alia

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_register.*


class RegisterActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    lateinit var register_carrera: String
    lateinit var usuarioUID: String
    val REQUEST_IMAGE_CAPTURE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        spinnerDeclarations()

        userImage.setOnClickListener {
            dispatchTakePictureIntent()
            onActivityResult(0, 0, null)
        }

        btn_listo.setOnClickListener {
            saveOnDatabase()
        }

    }

    override fun onNothingSelected(parent: AdapterView<*>?) { TODO("Not yet implemented") }

    /**
     * Método onItemSelected iguala la variable register_carrera al valor seleccionado
     * en el Spinner del activity.
     * @param parent parent del spinner.
     * @param view vista del spinner.
     * @param position posición del elemento seleccionado
     * @param id id del spinner.
     */
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        register_carrera = if(position != 0) {
            parent?.getItemAtPosition(position).toString()
        } else {
            parent?.getItemAtPosition(1).toString()
        }
    }

    /**
     * Método utilizado para obtener la imágen tomada en la cámara
     */
    override fun onActivityResult(requestCode:Int, resultCode:Int, data:Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            userImage.setImageBitmap(imageBitmap)
        }
    }

    /**
     * Método utilizado para abrir la camara y poder tomar fotos.
     */
    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    /**
     * El método SpinnerDeclarations crea un ArrayAdapter usando el arreglo de Strings
     * en el drawable de Strings para crear el Spinner con las diferentes opciones.
     */
    private fun spinnerDeclarations() {
        val spinner: Spinner = findViewById(R.id.majors_spinner)

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            this,
            R.array.majors_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }
        spinner.onItemSelectedListener = this
    }

    /**
     * Método saveOnDatabase crea un usuario nuevo, obtiene el uid del usuario
     * que se encuentra registrando y lo guarda en Firebase.
     */
    private fun saveOnDatabase() {
        val nombre = register_nombre.text
        val edad = register_edad.text
        val valores = register_valores.text
        val esConductora = esConductora.isChecked

        if(nombre.isNotEmpty() && edad.isNotEmpty() && valores.isNotEmpty()) {
            // Se crea un usuario con los valores obtenidos a lo largo
            // del activity.
            val usuario = User(
                nombre.toString(),
                edad.toString(),
                register_carrera,
                valores.toString(),
                esConductora,
                puntuacion = 0.0F
            )

            // Aquí se obtiene el UID del usuario que se registró en el AuthActivity
            // Para así ligar sus datos a Firebase.
            val currentFirebaseUser = FirebaseAuth.getInstance().currentUser
            usuarioUID = currentFirebaseUser!!.uid

            // Se guarda al usuario en la base de datos de Firebase
            FirebaseDatabase.getInstance().reference
                .child("users")
                .child(usuarioUID)
                .setValue(usuario)

            showMenu()

        } else {
            showAlert()
        }

    }

    /**
     * Método ShowAlert construirá y mostrará un error en caso que existan
     * campos vacíos.
     */
    private fun showAlert() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("No se permiten campos vacíos.")
        builder.setPositiveButton("Acepter", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    /**
     * ShowMenu se encarga de hacer un intent a la pantalla de MenuViaje, esto en caso
     * que no existan errores.
     */
    private fun showMenu() {
        val menuViajeIntent: Intent = Intent(this, MenuViajeActivity::class.java)
        startActivity(menuViajeIntent)
    }
}
